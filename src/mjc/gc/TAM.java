package mjc.gc;

import mjc.type.*;
import mjc.tds.*;
import java.util.Map;

/** 
 * Cette classe implante les differentes methodes de Abstract Machine permettant la
 * generation de code avec la machine TAM.
 * @author Philippe Leleux, Arthur Manoha, Quentin Glinel-Mortreuil
 */
public class TAM extends AbstractMachine {

	// Les trois valeurs suivantes ne sont la que pour la clarte du code
	private final String FAUX = "0";	// valeur du booleen faux en TAM
	private final String VRAI = "1";	// valeur du booleen vrai en TAM
	private final String NULL = "0";	// valeur de l'objet null en TAM
	private static int cpt = 0; 		// Pour la génération d'étiquettes

	/**
	 * Retourne l'extension d'un fichier genere pour la machine TAM.
	 * @return l'extension d'un fichier pour la machine TAM : tam
	 */
	public String getSuffixe() {
		return "tam";
	}

	/** 
	 * Génère une étiquette generique unique grace au compteur de la classe : 
	 * 	_x_[valeur du compteur]
	 * @return l'etiquette generee
	 */
	private static String genEtiq() {
		return "_X_" + cpt++;
	}

	/** Génère l'etiquette d'une méthode a partir de la methode et du nom de la classe :
	 * 	[classe de la methode]_[nom de la methode]_[type parametre1_..._type parametre n]
	 * @param classe la classe de la methode
	 * @param m la methode
	 * @return l'etiquette generee de la methode
	 */
	private String genEtiqMeth(String classe, INFOMETHOD m) {
		String retour = classe + "_";
		retour += m.getName();
		for(INFOATTRIBUTE a : m.getAttributes()) {
			retour += "_" + a.getType().getName();
		}

		return retour;
	}

	/** 
	 * Genere l'etiquette d'une methode a partir du nom de la classe, du nom de la
	 * methode et de la liste des parametres de la methodes :
	 * 	[classe de la methode]_[nom de la methode]_[type parametre1_..._type parametre n]
	 * @param classe le nom de la classe de la methode
	 * @param ident le nom de la methode
	 * @param attributes la liste des parametres de la methode
	 * @return l'etiquette generee de la methode
	 */
	private String genEtiqMeth(String classe, String ident, LATTRIBUTES attributes) {
		String retour = classe + "_";
		retour += ident;
		for(INFOATTRIBUTE a : attributes) {
			retour += "_" + a.getType().getName();
		}

		return retour;
	}
	
	/**
	 * genere la table des virtuelles a partir de la table des classes :
	 * enchainements de "LOADA " Etiquette de la methode
	 * @param classes la table des classes
	 * @return le code de la table des virtuelles
	 */
	@Override
	public String genererTDV(TDC classes) {
		String retour = "; Table des virtuelles : tableau d'indirection des méthodes des classes\n";
		/**
		 * On va parcourir la liste des classes par ordre de deplacement croissant
		 * et le deplacement correspondra a l'adresse dans la pile.
		 * On crée donc la liste des classes rangée par ordre de déplacement avec la
		 * methode sortByDep de la table des classes.
		 */
		Map<Integer, INFOCLASSE> tdc = classes.sortByDep();		
		for(INFOCLASSE c : tdc.values()) {
				retour += "\t; INFOCLASSE " + c.getName() + "\n";
				for(INFOMETHOD m : c.getMethods()) {
					String name = m.getName();
					// On ne prend pas en compte constructeurs et main
					if(!(name.equals(c.getName()) || name.equals("main"))) {
						retour += "\tLOADA " + genEtiqMeth(m.getClasse(), m) + "\n";
					}
				}
			}
		return retour + "\n\n\n";
	}

	/**
	 * Genere l'appel a la fonction main qui sera place en debut de chaque 
	 * programme apres la TDV. C'est ce code qui affichera que le programme termine et 
	 * la valeur de retour (souvent non significative) :
	 * 	";Appel de la fonction main
	 * 	CALL (LB) [nom de la classe contenant main]_main
	 * 	LOADL "FIN DU PROGRAMME !\n"
	 * 	SUBR SOut
	 *	LOADL "\n"
	 *	SUBR SOut
	 *	HALT
	 * @param classes la table des classes
	 * @return le code de l'appel a la methode main
	 */	
	@Override
	public String genererAppelMain(TDC classes) {
		String retour = "";
		int count = 0;
		// recherche de la (ou des) methode(s) void main()
		for(INFOCLASSE c : classes.values()) {
			for(INFOMETHOD m : c.getMethods()) {
				if (m.getName().equals("main") && (m.getAttributes().size() == 0) && (m.getReturnType() instanceof VIDE)) {
					count++;
					// Appel de la methode main
					retour = "\tCALL (LB) " + genEtiqMeth(c.getName(), m) + "\n"; 
				}
			}
		}
		// si la methode void main() existe et est unique, on genere le code
		if (!retour.equals("") && count==1) {
			retour = "\t; Appel de la fonction main\n" + retour;
			retour += "\t LOADL \"FIN DU PROGRAMME !\\n\"\n";
			retour += "\tSUBR SOut\n";
			retour += "\tLOADL \"VALEUR DE RETOUR : \"\n";
			retour += "\tSUBR SOut\n";
			retour += "\tSUBR IOut\n";
			// Flush du buffer
			retour += "\tLOADL \"\\n\"\n";
			retour += "\tSUBR SOut\n";
			retour += "\tHALT";
		} //sinon on affiche l'erreur
		  else if(count>1)
			retour = "Plusieurs definitions de main";
		else 
			retour = "Pas de main valide";
		return retour;  
	}

	/**
	 * Genere le code pour enlever des mots de la memoire :
	 *	;Liberation de [taille a liberer] mot
	 * 	POP 0 [taille a liberer]
	 * @param taille le nombre de mots a liberer
	 * @return le code genere de liberation
	 */
	@Override
	public String genFree(int taille) {
		String retour = "";
		if (taille != 0) {
			retour +=  "\t;Liberation de " + taille + " mot" + (taille > 1 ? "s" : "");
			retour += "\n\tPOP (0) " + taille +"\n";
		}
		return retour;
	}
	
	/**
	 * Genere le retour d'une methode et vide la table des symboles locale
	 * 	;Nettoyage des locales : [taille de la table des symboles locale]
	 * 	;Il faut conserver la valuer a retourner !
	 * 	POP ( [taille du type du retour] ) [taille de la table des symboles locale]
	 * 	;Retour de la fonction : [taille de la liste de parametres] parametres
	 * 	RETURN ( [taille du type du retour] ) [taille de la liste des parametres]
	 * @param m la methode dont on genere le retour
	 * @param locales la table des symboles locale
	 * @return le code genere
	 */
	@Override
	public String genRetour(INFOMETHOD m, TDS locales) {
		DTYPE type = m.getReturnType();
		int taille_attributes = m.getNbAttributes();
		int taille_locales = locales.getSize();

		String retour = "";
		
		if (taille_locales > 1) {
			// Si taille_locales = 1 : C'est la valeur à retourner
			retour += "\t; Nettoyage des locales : " + taille_locales + "\n";			
			retour += "\t; Il faut conserver la valeur à retourner !\n";
			retour += "\tPOP (" + type.getSize() + ") " + taille_locales + "\n";
		}
		retour += "\t; Retour de la fonction : " + taille_attributes + " parametres\n";
		retour += "\tRETURN (" + type.getSize() + ") " + taille_attributes  + "\n\n";

		return retour;
	}

	/**
	 * Genere (ou plutot assemble) le code d'une methode en ajoutant un eventuel retour :
	 * 	;Methode [nom de la methode] de la classe [nom de la classe}
	 * 	[code du corps de la methode]
	 * 	RETURN (1) [taille de la liste de parametres + 1 (taille du this)]
	 * @param classe le nom de la classe de la methode
	 * @param methode le nom de la methode
	 * @param code le code du corps de la methode
	 * @return le code genere
	 */
	@Override
	public String genMethode(String classe, INFOMETHOD methode, String code) {
		String etiq = genEtiqMeth(classe, methode);
		String retour =  "; Méthode "+methode.getName() + " de la classe " + classe + "\n";
		retour += etiq + "\n";
		retour += code;
		if(methode.getReturnType().compareTo(new VIDE())) {
			retour += "\tRETURN (1) " + (methode.getAttributes().getSize() + 1) + "\n\n"; 
		}
		return retour;
	}
	
	/**
	 * Genere (ou plutot assemble) le code d'un constructeur avec appel au constructeur
	 * par defaut pour creer l'objet :
	 * 	;Constructeur de la classe [nom de la classe]
	 * 	[etiquette du constructeur]
	 * 	;Appel au constructeur par défaut
	 * 	CALL (LB) [nom de la classe]_defaut
	 * 	[code du corps du constructeur]
	 * 	(LOAD (1) 3[LB] ; on rempile l'@ du pointeur à retourner)?ssi le code est vide
	 *	RETURN (1) [taille de la liste de parametres]
	 * @param classe le nom de la classe du constructeur
	 * @param constructeur le nom du constructeur
	 * @param code le code du corps du constructeur
	 * @return le code genere
	 */
	@Override
	public String genConstructeur(String classe, INFOMETHOD constructeur, String code) {
		String etiq = genEtiqMeth(classe, constructeur);
		String retour = "; Constructeur de la classe " + classe + "\n";
		retour += etiq + "\n";
		// On appelle le constructeur par défaut
		retour += "\t; Appel au constructeur par défaut\n";
		retour += "\tCALL (LB) " + classe + "_defaut\n";
		retour += code;
		if (!code.equals("")) { // (Toute) petite optimisation :D
			retour += "\tLOAD (1) 3[LB] ; On rempile l'@ du pointeur à retourner\n";
		}
		retour += "\tRETURN (1) " + constructeur.getAttributes().size() + "\n\n";
		return retour;
	}
	
	/**
	 * Genere le code du constructeur par defaut :
	 * 	; Constructeur par défaut de la classe [nom de la classe]
	 *	[nom de la classe]_defaut
	 *	; Allocation de l'espace mémoire
	 *	LOADL [taille de l'objet en memoire] ;Taille de l'objet + TDV
	 *	SUBR MAlloc ; Allocation
	 *	LOADL [deplacement de la classe] ;@TDV de la classe
	 *	LOAD (1) -2[ST] ;On récupère l'@ fournie par le malloc
	 *	STOREI (1) ;Et on affecte la valeur de la TDV dans l'objet crée
	 *	;Initialisation des attributs
	 * 	(;Initialisation de [nom de l'attribut] (type [type de l'attribut])
	 * 	 LOADL [valeur par defaut de l'attribut] ;Valeur par defaut: [valeur par defaut]
	 * 	 ; On récupère l'@ de this
	 * 	 LOAD (1) 3[LB]
	 *	 LOADL [deplacement de l'attribut]
	 *	 SUBR IAdd
	 * 	 STOREI (1) ;Et on sauve le tout !)*
	 *	RETURN (1) 0
	 * @param c la classe du constructeur
	 * @return le code genere
	 */
	@Override
	public String genConstructeurDefaut(INFOCLASSE c) {
		String retour = "\t; Constructeur par défaut de la classe " + c.getName() + "\n";
		retour += c.getName() + "_defaut\n";
		int taille = c.getAttributes().getSize();
		taille++; // Pour la TDV en début d'objet.
		int indice_TDV = c.getDep();
		retour += "\t; Allocation de l'espace mémoire\n";
		retour += "\tLOADL " + taille + " ;Taille de l'objet + TDV\n";
		retour += "\tSUBR MAlloc ; Allocation \n";
		retour += "\tLOADL " + indice_TDV + " ; @TDV de la classe\n";
		retour += "\tLOAD (1) -2[ST] ; On récupère l'@ fournie par le malloc\n";
		retour += "\tSTOREI (1) ; Et on affecte la valeur de la TDV dans l'objet crée\n";
		retour += "\t; Initialisation des attributs\n";
		for(INFOATTRIBUTE p : c.getAttributes()) {
			DTYPE t = p.getType();
			// On initialise chacun des attributs de type entier à 42
			if(t instanceof ENTIER) {
				retour += "\t\t; Initialisation de " + p.getName() + " (type entier) \n";
				retour += "\tLOADL 42 ; Valeur par défaut: 42\n";
			} else if (t instanceof BOOLEEN) { // Les booleens sont faux par defaut
				retour += "\t\t; Initialisation de " + p.getName() + " (type booléen) \n";
				retour += "\tLOADL " + FAUX + " ; Valeur par défaut: FAUX\n";
			} else { // Les pointeurs sont initialisés à NULL
				retour += "\t\t; Initialisation de " + p.getName() + " (pointeur) \n";
				retour += "\tLOADL " + NULL +" ; Valeur par défaut: NULL\n";
			}
			retour += "\t; On récupère l'@ de this\n";
			retour += "\tLOAD (1) 3[LB]\n";
			retour += "\tLOADL " + p.getDep() + "\n";
			retour += "\tSUBR IAdd\n";
			retour += "\tSTOREI (1) ; Et on sauve le tout !\n";
		}
		retour += "\tRETURN (1) 0\n\n";
		return retour;
	}

	/**
	 * Genere le code appelé en cas d'exception pointeur null
	 * 	;En cas d'exception
	 *	_NPE_
	 * 	LOADL "Null Pointer Exception"
	 * 	SUBR SOut
	 * 	HALT
	 * @return le code genere
	 */
	@Override
	public String genererCodeNPE() {
		String retour = "\t\n; En cas d'exception \n";
		retour += "_NPE_\n";
		retour += "\tLOADL \"Null Pointer Exception\"\n";
		retour += "\tSUBR SOut\n";
		retour += "\tHALT\n\n";
		return retour;
	}

	/**
	 * Genere le code d'appel d'une methode :
	 *	;Appel de la méthode [nom de la classe]::[nom de la methode]
	 *	int deplacement = classe.getMethods().getDep(m);
	 *	int taille_attributes = m.getAttributes().getSize();
	 *	LOADL 0 ;Pour le CALLI
	 *	// Le pointeur sur lequel on applique la méthode est empilé
	 *	LOAD (1) [- taille des attributs - 2] [ST] ;On récupère la valeur du pointeur
	 *	LOADI (1) ;On récupère l'@ de la TDV
	 *	LOADL [deplacement de la liste de methodes de la classe] ;Id de la méthode dans la TDV
	 *	SUBR IAdd
	 *	;On obtient l'adresse réelle de la méthode
	 *	LOADI (1) ;Que l'on va chercher et mettre en tête de pile
	 *	CALLI ;Et on appelle la méthode.. OUF
	 * @param classe la classe de la methode
	 * @param m la methode a appeler
	 * @return le code genere
	 */ 
	@Override
	public String genAppelMeth(INFOCLASSE classe, INFOMETHOD m) {
		
		String retour = "\t; Appel de la méthode " + classe.getName() + "::" + m.getName() + "\n";
		int deplacement = classe.getMethods().getDep(m);
		int taille_attributes = m.getAttributes().getSize();
		
		retour += "\tLOADL 0 ; Pour le CALLI (cf doc de tam)\n";
		// Le pointeur sur lequel on applique la méthode est empilé
		retour += "\tLOAD (1) " + -(taille_attributes + 2) + "[ST] ; On récupère la valeur du pointeur\n";
		retour += "\tLOADI (1) ; On récupère l'@ de la TDV\n";
		if (deplacement > 0) {
			retour += "\tLOADL " + deplacement + " ; Id de la méthode dans la TDV\n";
			retour += "\tSUBR IAdd\n";
		}
		retour += "\t; On obtient l'adresse réelle de la méthode\n";
		retour += "\tLOADI (1) ; Que l'on va chercher et mettre en tête de pile\n";
		retour += "\tCALLI ; Et on appelle la méthode.. OUF\n";
		return retour;
	}

	/**
	 * genere le code pour l'ecriture d'une expression en memoire :
	 *	;Ecriture mémoire\
	 * 	;-> Calul de l'expression
	 *	[code de l'expression]
	 *	;-> Calcul de l'adresse
	 *	[code de l'adresse]
	 *	STOREI ([taille du type en memoire])
	 * @param adresse le code pour le calcul de l'adresse
	 * @param expression le code de l'expression a ecrire en memoire
	 * @param type le type de l'expression
	 * @return le code genere
	 */	
	@Override
	public String genEcritureMem(String adresse, String expression, DTYPE type) {
		String retour = "\t; Ecriture mémoire\n\t; -> Calul de l'expression\n";
		retour += expression;
		retour += "\t; -> Calcul de l'adresse\n";
		retour += adresse;
		retour += "\tSTOREI (" + type.getSize() + ")\n";
		return retour;
	}
	
	/**
	 * Genere le code pour une lecture en memoire :
	 * 	;Lecture mémoire
	 *	[code pour le calcul de l'adresse en memoire]
	 *	LOADI ([taille du type en memoire])
	 * @param code le code pour le calcul de l'adresse en memoire
	 * @param type le type de l'objet a lire en memoire
	 * @return le code genere
	 */
	@Override
	public String genLectureMem(String code, DTYPE type) {
		String retour = "\t; Lecture mémoire\n";
		retour += code;
		retour += "\tLOADI (" + type.getSize() + ")\n";
		return retour;
	}
	
	/**
	 * Appelle la methode pour generer une lecture en memoire si le booleen en parametre
	 * n'est pas une valeur, retourne simplement le code sinon
	 * @param code le code pour le calcul de l'expression ou de l'adresse en memoire
	 * @param type le type de l'objet
	 * @param estValeur vrai si code designe une valeur, faux si c'est une adresse
	 * @return le code genere
	 */
	@Override
	public String genLectureMem(String code, DTYPE type, boolean estValeur) {
		String retour = code;
		if(!estValeur) {
			retour =  genLectureMem(code, type);
		}
		return retour;
	}
	

	/**
	 * Genere le code pour l'affectation d'une variable :
	 * 	STORE ([taille du type de la variable en memoire]) [deplacement de la variable] [LB]
	 * @param variable la variable a affecter
	 * @return le code genere
	 */
	@Override
	public String genAffect(INFOVARIABLE variable) {
		return "\tSTORE (" + variable.getType().getSize() + ") "+
		variable.getDep() + "[LB]\n";
	}

	/**
	 * Genere le code d'une declaration de variable :
	 * 	;declaration d'un [nom du type de la variable]
	 * 	LOADL [valeur par defaut de la variable]
	 * @param variable la variable a declarer
	 * @return le code genere
	 */
	@Override
	public String genDecl(INFOVARIABLE variable) {
		String retour = "";
		DTYPE t = variable.getType();
		if(t instanceof POINTER) {
			retour += "\t;Declaration d'un pointeur\n";
			retour += "\tLOADL " + NULL + "\n";
		} else if(t instanceof ENTIER) {
			retour += "\t;Declaration d'un entier\n";
			retour += "\tLOADL 42\n";
		} else if(t instanceof BOOLEEN){
			retour += "\t;Declaration d'un booléen\n";
			retour += "\tLOADL " + FAUX + "\n";
		}

		return retour;
	}
	
	/**
	 * Genere le code d'appel a un constructeur :
	 *	;Appel au constructeur :
	 *	[code des parametres pour le constructeur]
	 *	CALL (LB) [etiquette du constructeur] ; Appel du constructeur\n";
	 * @param type le type de l'objer cree par le constructeur
	 * @param m le constructeur
	 * @param code le code des parametres de la methode
	 * @return le code genere
	 */
	@Override
	public String genAppelConstructeur(DTYPE type, INFOMETHOD m, String code) {
		String retour = "\t;Appel au constructeur :\n";
		INFOCLASSE classe = ((POINTER)type).getClasse();
		// On charge les paramètres du constructeur
		retour += code;
		// Et on génère l'appel :)
		String etiq = "";
		if(m == null) {
			// C'est l'appel au constucteur par défaut
			etiq = classe.getName() + "_defaut";
		} else {
			etiq = genEtiqMeth(classe.getName(), classe.getName(), m.getAttributes());
		}
		retour += "\tCALL (LB) " + etiq + " ; Appel du constructeur\n";
		return retour;
	}
	
	/**
	 * Genere la verification d'exception pointeur null :
	 * 	;Vérification d'une NPE
	 * 	LOAD (1) -1[ST]
	 * 	JUMPIF ([code de null]) _NPE_
	 * @return le code genere;
	 */
	@Override
	public String genVerifNPE() {
		String retour = "\t; Vérification d'une NPE\n";
		retour += "\tLOAD (1) -1[ST]\n";
		retour += "\tJUMPIF (" + NULL + ") _NPE_\n";
		return retour;
	}
	
	/**
	 * Genere l'adresse d'un attribut :
	 * 	;@Attribut : [nom de l'attribut]
	 * 	LOADL [deplacement de l'attribut]
	 * 	SUBR IAdd
	 * @param attribute l'attribut dont on souhaite l'adresse
	 * @return le code genere
	 */
	@Override
	public String genAdresseAttribut(INFOATTRIBUTE attribute) {
		// Suppose que le pointeur est empilé en tête
		String retour = "\t; @Attribut : " + attribute.getName() + "\n";
		retour += "\tLOADL " + attribute.getDep() + "\n";
		retour += "\tSUBR IAdd\n"; // Ou l'inverse ?
		return retour;
	}

	/**
	 * Genere l'adresse d'un parametre :
	 * 	;@Parametre : [nom du parametre]
	 * 	LOADA [- deplacement du parametre -1] [LB]
	 * @param attribute parametre dont on souhaite l'adresse
	 * @return le code genere
	 */
	@Override
	public String genAdresseParam(INFOATTRIBUTE attribute) {
		String retour = "\t; @Parametre : " + attribute.getName() + "\n";
		retour += "\tLOADA " + - (attribute.getDep() + 1) + " [LB]\n";
		return retour;
	}

	/**
	 * Genere l'adresse d'une variable :
	 * 	;@Variable
	 * 	LOADA [deplacement de la variable] [LB]
	 * @param variable la variable dont on souhaite l'adresse
	 * @return le code genere
	 */
	@Override
	public String genAdresseVariable(INFOVARIABLE variable) {
		String retour = "\t; @Variable\n";
		retour += "\tLOADA " + variable.getDep() + "[LB]\n";
		return retour;
	}
	
	/**
	 * Genere l'acces a une variable :
	 * 	;Valeur variable
	 * 	LOAD ([taille du type de la variable en memoire]) [deplacement de la variable] [LB]
	 * @param variable la variable a laquelle on accède
	 * @return le code genere
	 */
	@Override
	public String genAccesVariable(INFOVARIABLE variable) {
		String retour = "\t; Valeur variable\n";
		retour += "\tLOAD (" + variable.getType().getSize() +") "
				+ variable.getDep() + "[LB]\n";
		return retour;
		
	}

	/**
	 * Genere l'acces a un parametre :
	 * 	;Valeur param [nom du parametre]
	 * 	LOAD ([taille du type de la variable en memoire]) [- deplacement du parametre - 1] [LB]
	 * @param attribute le parametre auquel on accède
	 * @return le code genere
	 */
	@Override
	public String genAccesParam(INFOATTRIBUTE attribute) {
		String retour = "\t; Valeur param : " + attribute.getName() + "\n";
		retour += "\tLOAD (" + attribute.getType().getSize() + ") "
					+ -(attribute.getDep() + 1) + "[LB]\n";
		return retour;
	}

	/**
	 * Genere l'acces a un attribut :
	 * 	;Valeur attribut [nom de l'attribut]
	 * 	LOADL deplacement de l'attribut
	 * 	SUBR IAdd
	 * 	LOADI ([taille du type de l'attribut en memoire])
	 * @param attribute l'attribut auquel on accède
	 * @return le code genere
	 */
	@Override
	public String genAccesAttribut(INFOATTRIBUTE attribut) {
		// Suppose que this est empilé en tête
		String retour = "\t; Valeur attribut : " + attribut.getName() + "\n";
		retour += "\tLOADL " + attribut.getDep() + "\n";
		retour += "\tSUBR IAdd \n";
		retour += "\tLOADI (" + attribut.getType().getSize() +")\n";
		return retour;
	}
	
	/**
	 * Genere le code pour empiler l'adresse de l'objet courant this :
	 * 	LOAD(1) [deplacement necessaire pour recuperer l'adresse] [registre de ref]
	 * @param la methode dans laquelle on empile this
	 * @return le code genere
	 */
	@Override
	public String genEmpilerThis(INFOMETHOD m) {
		// Cas nominal : nombre de paramètres + 1 : le this
		int dep = -(m.getAttributes().getSize() + 1);
		if(m.getClasse().equals(m.getName())) {
			// C'est un constructeur
			//On doit aller chercher @this après LB
			dep = 3;
		}
		String retour = "\t; On empile this\n";
		// FIXME si on est dans le main, c'est par rapport a l'autre registre qu'il faut se deplacer (ex : fact est appele depuis main)
		if (m.getName().equals("fact")) {
			retour += "\tLOAD (1) " + dep + "[LB]\n";
		} else {
			retour += "\tLOAD (1) " + dep + "[ST]\n";
		}
		return retour;
	}
	
	/**
	 * Genere le code necessaire a une instruction si alors sinon :
	 * 	; Test condition (SI)
	 * 	JUMPIF ([code pour le booleen faux]) [etiquette generee du bloc sinon]
	 * 	;BLOC ALORS
	 *	[code du bloc alors]
	 *	;BLOC SINON
	 *	[etiquette generee du bloc sinon]
	 *	[code du bloc sinon]
	 *	;FIN SI
	 * @param condition la condition de l'instruction si
	 * @param alors le code du bloc alors
	 * @param sinon le code du bloc sinon
	 * @return le code genere
	 */
	@Override
	public String genSi(String condition, String alors, String sinon) {
		String etiq_sinon = genEtiq();

		String retour = condition;
		retour += "\t; Test condition (SI)\n";
		retour += "\tJUMPIF (" + FAUX + ") ";

		if(sinon.equals("")) { 		// Dans le cas d'un si/alors sans sinon
			retour += "\t; BLOC ALORS\n";
			retour += alors;
		} else {
			retour += etiq_sinon + "\n";
			retour += "\t; BLOC ALORS\n";
			retour += alors;
			retour += "\t; BLOC SINON\n";
			retour += etiq_sinon +"\n" + sinon;
		}

		retour += "\t; FIN SI\n";

		return retour;
	}	
	
	/**
	 * Genere le code necessaire a une instruction tant que :
	 * 	[etiquette du bloc de la boucle]
	 * 	; Test condition (TANT QUE)
	 *	JUMPIF ([code pour le booleen faux]) [etiquette de fin de boucle]
	 * 	[code du bloc de la boucle]
	 * 	;Fin Tant que
	 * 	JUMP [etiquette du bloc de la boucle]
	 * 	[etiquette de fin de boucle]
	 * @param condition la condition de l'instruction tant que
	 * @param boucle le code de la boucle
	 * @return le code genere
	 */
	@Override
	public String genTq(String condition, String boucle) {
		String etiq_bcle = genEtiq(); String etiq_fin = genEtiq();
		String retour = etiq_bcle;
		retour += "\t; Test condition (TANT QUE)\n";
		retour += "\tJUMPIF (" + FAUX + ") " + etiq_fin + "\n";
		retour += boucle + "\n";
		retour += "\t; Fin Tant que\n";
		retour += "\tJUMP " + etiq_bcle + "\n";
		retour += etiq_fin + "\n";
		return retour;
	}
	
	/**
	 * Generer le code du pointeur null :
	 * 	LOADL [code du pointeur null]
	 * @return le code genere
	 */
	@Override
	public String genNull() {
		return "\tLOADL " + NULL + "\n";
	}

	/**
	 * Generer le code d'un boolen faux :
	 * 	LOADL [code du booleen faux]
	 * @return le code genere
	 */
	@Override
	public String genFaux() {
		return "\tLOADL " + FAUX + "\n";
	}
	
	/**
	 * Generer le code d'un boolen vrai :
	 * 	LOADL [code du booleen vrai]
	 * @return le code genere
	 */
	@Override
	public String genVrai() {
		return "\tLOADL " + VRAI + "\n";
	}
	
	/**
	 * Generer le code d'un entier avec la valeur en parametre :
	 * 	LOADL [code de la valeur]
	 * @return le code genere
	 */
	@Override
	public String genEntier(String valeur) {
		return "\tLOADL " + valeur + "\n";
	}
	
	/**
	 * Generer le code pour une addition en considerant les membres gauche et droit empiles :
	 * 	SUBR IADD
	 * @return le code genere
	 */
	@Override
	public String genAddition() {
		return "\tSUBR IADD\n";
	}

	/**
	 * Generer le code pour une soustraction en considerant les membres gauche et
	 * droit empiles :
	 * 	SUBR ISub
	 * @return le code genere
	 */
	@Override
	public String genMoins() {
		return "\tSUBR ISub\n";
	}

	/**
	 * Generer le code pour un operateur modulo en considerant les membres gauche et
	 * droit empiles :
	 * 	SUBR IMod
	 * @return le code genere
	 */
	@Override
	public String genMod() {
		return "\tSUBR IMod\n";
	}

	/**
	 * Generer le code pour une division en considerant les membres gauche et droit empiles :
	 * 	SUBR IDiv
	 * @return le code genere
	 */
	@Override
	public String genDiv() {
		return "\tSUBR IDiv\n";
	}

	/**
	 * Generer le code pour une multiplication en considerant les membres gauche et
	 * droit empiles :
	 * 	SUBR IMul
	 * @return le code genere
	 */
	@Override
	public String genMult() {
		return "\tSUBR IMul\n";
	}

	/**
	 * Generer le code pour un test de superiorite stricte en considerant les membres
	 * gauche et droit empiles :
	 * 	SUBR ISub
	 * @return le code genere
	 */
	@Override
	public String genSup() {
		return "\tSUBR IGtr\n";
	}

	/**
	 * Generer le code pour un test de superiorite en considerant les membres gauche et
	 * droit empiles :
	 * 	SUBR ISub
	 * @return le code genere
	 */
	@Override
	public String genSupEg() {
		return "\tSUBR IGeq\n";
	}

	/**
	 * Generer le code pour un test d'egalite en considerant les membres gauche et
	 * droit empiles :
	 * 	SUBR IEq
	 * @return le code genere
	 */
	@Override
	public String genEg() {
		return "\tSUBR IEq\n";
	}

	/**
	 * Generer le code pour un test de non egalite en considerant les membres gauche et
	 * droit empiles :
	 * 	SUBR INeqn
	 * @return le code genere
	 */
	@Override
	public String genNeq() {
		return "\tSUBR INeqn";
	}

	/**
	 * Generer le code pour un test d'inferiorite strict en considerant les membres
	 * gauche et droit empiles :
	 * 	SUBR ILss
	 * @return le code genere
	 */
	@Override
	public String genInf() {
		return "\tSUBR ILss\n";
	}

	/**
	 * Generer le code pour un test d'inferiorite en considerant les membres gauche et
	 * droit empiles :
	 * 	SUBR ILeq
	 * @return le code genere
	 */
	@Override
	public String genInfEg() {
		return "\tSUBR ILeq\n";
	}

	/**
	 * Generer le code pour un operateur ET en considerant les membres gauche et
	 * droit empiles :
	 * 	SUBR ISub
	 * @return le code genere
	 */
	@Override
	public String genOu() {
		return "\tSUBR BOr\n";
	}

	/**
	 * Generer le code pour un operateur ET en considerant les membres gauche et
	 * droit empiles :
	 * 	SUBR BAnd
	 * @return le code genere
	 */
	@Override
	public String genEt() {
		return "\tSUBR BAnd\n";
	}

	/**
	 * Generer le code pour l'inversement d'un booleen en considerant le membre droit
	 * empile :
	 * 	SUBR BNeg
	 * @return le code genere
	 */
	@Override
	public String genNon() {
		return "\tSUBR BNeg\n";
	}

	/**
	 * Generer le code pour un operateur unaire neg en considerant le membre droit :
	 * 	SUBR INeg
	 * @return le code genere
	 */
	@Override
	public String genOpp() {
		return "\tSUBR INeg\n";
	}	
}
