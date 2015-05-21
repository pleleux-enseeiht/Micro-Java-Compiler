package mjc.tds;

import mjc.type.DTYPE;

/** 
 * Cette classe implante l'info pour les methodes de classe.
 * Chacune de ces methodes a un nom, une liste d'attributs (les parametres), un type de
 * retour et le nom de la classe a laquelle elle appartient.
 * @author Philippe Leleux, Arthur Manoha, Quentin Glinel-Mortreuil
 */
public class INFOMETHOD{

	private String name;			// nom de la methode
	private LATTRIBUTES attributes;		// liste des parametres de la methodes
	private DTYPE returnType;		// type de retour de la methode
	private String classe;			// nom de la classe de la methode
	
	/**
	 * Constructeur creant une methode partir de son nom, sa liste de parametres, le nom
	 * de sa classe et son type de retour.
	 * @param name le nom de la methode
	 * @param attributes la liste des parametres de la methode
	 * @param classe le nom de la classe de la methode
	 * @param returnType le type de retour de la methode
	 */	
	public INFOMETHOD(String name, LATTRIBUTES attributes, String classe , DTYPE returnType) {
		this.name = name;
		this.attributes = attributes;
		this.classe = classe;
		this.returnType = returnType;
	}

	/**
	 * Retourne le nom de la methode
	 * @return le nom de la methode
	 */
	public String getName() {
		return this.name.trim();
	}
	
	/**
	 * Retourne la liste des parametres de la methode
	 * @return la liste des parametres de la methode
	 */
	public LATTRIBUTES getAttributes() {
		return this.attributes;
	}

	/**
	 * Retourne le nom de la classe de la methode
	 * @return le nom de la classe de la methode
	 */
	public String getClasse() {
		return this.classe;
	}
	
	/**
	 * Retourne le type de retour de la methode
	 * @return le type de retour de la methode
	 */
	public DTYPE getReturnType() {
		return this.returnType;
	}

	/**
	 * Retourne le nombre de parametres de la methode
	 * @return le nombre de parametres de la methode
	 */
	public int getNbAttributes () {
		int res = this.attributes.getSize();
		if(!this.name.equals("main")) {
			res++;
		}
		return res;
	}

	/**
	 * Compare la methode courante et une autre methode.
	 * Elles sont comparables si elles ont le même nom et des parametres de meme type.
	 * @param m la methode a comparer
	 * @return vrai si les methodes sont comparables, faux sinon
	 */	
	public boolean compareTo(INFOMETHOD m) {
		return this.name.equals(m.getName());
	}
	
	/** 
	 * Retourne la représentation de la methode sous forme de string :
	 * pour un constructeur :
	 * 	"Constructeur " nom de la methode " (" parametres de la methode ")"
	 * sinon :
	 * 	"Methode " type de retour " " nom de la methode " (" parametres de la
	 * methode ")"
	 * @return la chaine representant l'attribut
	 */
	public String toString() {
		String message = "";
		if (this.returnType == null) {
			message += "Constructeur ";
			message += this.name;
		} else {
			message += "Methode ";
			message += this.returnType.getName() + " ";
			message += this.name;
		}
		message += " (";
		message += this.attributes;
		message += ")";
		return message;
	}

}
