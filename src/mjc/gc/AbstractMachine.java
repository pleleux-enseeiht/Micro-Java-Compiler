package mjc.gc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import mjc.compiler.MJCException;
import mjc.tds.*;
import mjc.type.*;

/** 
 * Classe abstraite definissant les differentes methodes pour la generation de code.
 * @author Philippe Leleux, Arthur Manoha, Quentin Glinel-Mortreuil
 */
public abstract class AbstractMachine {

	/**
	 * Ecrit le code genere dans un fichier de nom celui du fichier microjava de depart
	 * et d'extension le suffixe de la machine concrete.
	 * @param fname le nom du fichier microjava
	 * @param code le code genere a ecrire dans le fichier
	 * @throws MJCException une exception du systeme de generation de code si le fichier n'est pas trouve
	 */
	public void writeCode(String fname, String code) throws MJCException {
		try {
			String asmName = fname + "." + getSuffixe();
			System.err.println("Ecriture du code dans " + asmName);
			PrintWriter pw = new PrintWriter(new FileOutputStream(asmName));
			pw.print(code);
			pw.close();
		} catch (FileNotFoundException e) {
			throw new MJCException(e.getMessage());
		}
	}

	/**
	 * Retourne l'extension d'un fichier genere pour la machine TAM.
	 * @return l'extension d'un fichier pour la machine TAM : tam
	 */
	protected abstract String getSuffixe();
	
	/**
	 * genere la table des virtuelles a partir de la table des classes
	 * @param classes la table des classes
	 * @return le code de la table des virtuelles
	 */
	public abstract String genererTDV(TDC classes);
	
	/**
	 * Genere l'appel a la fonction main qui sera place en debut de chaque 
	 * programme apres la TDV. C'est ce code qui affichera que le programme termine et 
	 * la valeur de retour (souvent non significative)
	 * @param classes la table des classes
	 * @return le code de l'appel a la methode main
	 */
	public abstract String genererAppelMain(TDC classes);

	/**
	 * Genere le code pour enlever des mots de la memoire
	 * @param taille le nombre de mots a liberer
	 * @return le code genere de liberation
	 */
	public abstract String genFree(int taille);

	/**
	 * Genere le retour d'une methode et vide la table des symboles locale
	 * @param m la methode dont on genere le retour
	 * @param locales la table des symboles locale
	 * @return le code genere
	 */
	public abstract String genRetour(INFOMETHOD m, TDS locales);

	/**
	 * Genere (ou plutot assemble) le code d'une methode en ajoutant un eventuel retour
	 * @param classe le nom de la classe de la methode
	 * @param methode la methode dont on genere le code
	 * @param code le code du corps de la methode
	 * @return le code genere
	 */
	public abstract String genMethode(String classe, INFOMETHOD methode, String code);
	

	
	/**
	 * Genere (ou plutot assemble) le code d'un constructeur avec appel au constructeur
	 * par defaut pour creer l'objet
	 * @param classe le nom de la classe du constructeur
	 * @param constructeur le nom du constructeur
	 * @param code le code du corps du constructeur
	 * @return le code genere
	 */
	public abstract String genConstructeur(String classe, INFOMETHOD constructeur, String code);
	
	/**
	 * Genere le code du constructeur par defaut
	 * @param c la classe du constructeur
	 * @return le code genere
	 */
	public abstract String genConstructeurDefaut(INFOCLASSE c);
	
	/**
	 * Genere le code appelé en cas d'exception pointeur null
	 * @return le code genere
	 */
	public abstract String genererCodeNPE();
	
	/**
	 * Genere le code d'appel d'une methode
	 * @param classe la classe de la methode
	 * @param m la methode a appeler
	 * @return le code genere
	 */ 
	public abstract String genAppelMeth(INFOCLASSE classe, INFOMETHOD m);
	
	/**
	 * Genere le code pour l'ecriture d'une expression en memoire
	 * @param adresse le code pour le calcul de l'adresse
	 * @param expression le code de l'expression a ecrire en memoire
	 * @param type le type de l'expression
	 * @return le code genere
	 */
	public abstract String genEcritureMem(String adresse, String expression, DTYPE type);
	
	/**
	 * Genere le code pour une lecture en memoire
	 * @param code le code pour le calcul de l'adresse en memoire
	 * @param type le type de l'objet a lire en memoire
	 * @return le code genere
	 */
	public abstract String genLectureMem(String code, DTYPE type);
	
	/**
	 * Appelle la methode pour generer une lecture en memoire si le booleen en parametre
	 * n'est pas une valeur, retourne simplement le code sinon
	 * @param code le code pour le calcul de l'expression ou de l'adresse en memoire
	 * @param type le type de l'objet
	 * @param estValeur vrai si code designe une valeur, faux si c'est une adresse
	 * @return le code genere
	 */
	public abstract String genLectureMem(String code, DTYPE type, boolean estValeur);

	/**
	 * Genere le code pour l'affectation d'une variable
	 * @param variable la variable a affecter
	 * @return le code genere
	 */
	public abstract String genAffect(INFOVARIABLE variable);
	
	/**
	 * Genere le code d'une declaration de variable (sans affectation)
	 * @param variable la variable a declarer
	 * @return le code genere
	 */
	public abstract String genDecl(INFOVARIABLE var);

	/**
	 * Genere le code d'appel a un constructeur
	 * @param type le type de l'objer cree par le constructeur
	 * @param m le constructeur
	 * @param code le code des parametres de la methode
	 * @return le code genere
	 */
	public abstract String genAppelConstructeur(DTYPE type, INFOMETHOD m, String code);
	
	/**
	 * Genere la verification d'exception pointeur null
	 * @return le code genere;
	 */
	public abstract String genVerifNPE();
	
	/**
	 * Genere l'adresse d'une variable
	 * @param variable la variable dont on souhaite l'adresse
	 * @return le code genere
	 */
	public abstract String genAdresseVariable(INFOVARIABLE var);

	/**
	 * Genere l'adresse d'un parametre
	 * @param attribute parametre dont on souhaite l'adresse
	 * @return le code genere
	 */
	public abstract String genAdresseParam(INFOATTRIBUTE attribute);

	/**
	 * Genere l'adresse d'un attribut
	 * @param attribute l'attribut dont on souhaite l'adresse
	 * @return le code genere
	 */
	public abstract String genAdresseAttribut(INFOATTRIBUTE attribut);
	
	/**
	 * Genere l'acces a une variable
	 * @param variable la variable a laquelle on accède
	 * @return le code genere
	 */
	public abstract String genAccesVariable(INFOVARIABLE var);

	/**
	 * Genere l'acces a un parametre
	 * @param attribute le parametre auquel on accède
	 * @return le code genere
	 */
	public abstract String genAccesParam(INFOATTRIBUTE attribute);

	/**
	 * Genere l'acces a un attribut
	 * @param attribute l'attribut auquel on accède
	 * @return le code genere
	 */
	public abstract String genAccesAttribut(INFOATTRIBUTE attribut);
	
	/**
	 * Genere le code pour empiler l'adresse de l'objet courant this
	 * @param la methode dans laquelle on empile this
	 * @return le code genere
	 */
	public abstract String genEmpilerThis(INFOMETHOD m);

	/**
	 * Genere le code necessaire a une instruction si alors sinon
	 * @param condition la condition de l'instruction si
	 * @param alors le code du bloc alors
	 * @param sinon le code du bloc sinon
	 * @return le code genere
	 */
	public abstract String genSi(String condition, String alors, String sinon);
	
	/**
	 * Genere le code necessaire a une instruction tant que
	 * @param condition la condition de l'instruction tant que
	 * @param boucle le code de la boucle
	 * @return le code genere
	 */
	public abstract String genTq(String condition, String boucle) ;

	/**
	 * Generer le code du pointeur null
	 * @return le code genere
	 */
	public abstract String genNull();
	
	/**
	 * Generer le code d'un boolen faux
	 * @return le code genere
	 */
	public abstract String genFaux();
	
	/**
	 * Generer le code d'un boolen vrai
	 * @return le code genere
	 */
	public abstract String genVrai();

	/**
	 * Generer le code d'un entier avec la valeur en parametre
	 * @return le code genere
	 */
	public abstract String genEntier(String valeur);
	
	/**
	 * Generer le code pour une addition en considerant les membres gauche et droit empiles
	 * @return le code genere
	 */
	public abstract String genAddition();

	/**
	 * Generer le code pour une soustraction en considerant les membres gauche et
	 * droit empiles
	 * @return le code genere
	 */
	public abstract String genMoins();

	/**
	 * Generer le code pour un operateur modulo en considerant les membres gauche et
	 * droit empiles
	 * @return le code genere
	 */
	public abstract String genMod();

	/**
	 * Generer le code pour une multiplication en considerant les membres gauche et
	 * droit empiles
	 * @return le code genere
	 */
	public abstract String genMult();

	/**
	 * Generer le code pour une division en considerant les membres gauche et droit empiles
	 * @return le code genere
	 */
	public abstract String genDiv();

	/**
	 * Generer le code pour un test de superiorite stricte en considerant les membres
	 * gauche et droit empiles
	 * @return le code genere
	 */
	public abstract String genSup();
	
	/**
	 * Generer le code pour un test de superiorite en considerant les membres gauche et
	 * droit empiles
	 * @return le code genere
	 */
	public abstract String genSupEg();

	/**
	 * Generer le code pour un test d'egalite en considerant les membres gauche et
	 * droit empiles
	 * @return le code genere
	 */
	public abstract String genEg();
	
	/**
	 * Generer le code pour un test de non egalite en considerant les membres gauche et
	 * droit empiles
	 * @return le code genere
	 */
	public abstract String genNeq();

	/**
	 * Generer le code pour un test d'inferiorite strict en considerant les membres
	 * gauche et droit empiles
	 * @return le code genere
	 */
	public abstract String genInf();

	/**
	 * Generer le code pour un test d'inferiorite en considerant les membres gauche et
	 * droit empiles
	 * @return le code genere
	 */
	public abstract String genInfEg();
	
	/**
	 * Generer le code pour un operateur ET en considerant les membres gauche et
	 * droit empiles
	 * @return le code genere
	 */
	public abstract String genOu();
	
	/**
	 * Generer le code pour un operateur ET en considerant les membres gauche et
	 * droit empiles
	 * @return le code genere
	 */
	public abstract String genEt();

	/**
	 * Generer le code pour l'inversement d'un booleen en considerant le membre droit
	 * empile
	 * @return le code genere
	 */
	public abstract String genNon();

	/**
	 * Generer le code pour un operateur unaire neg en considerant le membre droit
	 * @return le code genere
	 */
	public abstract String genOpp();
}
