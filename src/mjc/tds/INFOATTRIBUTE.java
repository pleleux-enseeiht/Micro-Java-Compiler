package mjc.tds;

import mjc.type.*;

/** 
 * Cette classe implante l'info pour les attributs des classes et les parametres 
 * pour l'appel de methodes. Que ce soit un attribut ou un parametre, cela revient au meme
 * et nous parlerons par la suite seulement d'"attribut".
 * Chacun de ces attributs a un nom, un type et un deplacement (correspondant a son
 * classement dans la liste d'attributs + 1 (pour la table des virtuelles).
 * @author Philippe Leleux, Arthur Manoha, Quentin Glinel-Mortreuil
 */
public class INFOATTRIBUTE{

	private String name;	// nom de l'attribut
	private DTYPE type;	// type de l'attribut
	private int dep;	// deplacement de l'attribut

	/**
	 * Constructeur creant un attribut partir de son nom, son type et son deplacement.
	 * @param name le nom de l'attribut
	 * @param type le type de l'attribut
	 * @param dep le deplacement de l'attribut
	 */	
	public INFOATTRIBUTE(String name, DTYPE type, int dep) {
		this.name = name;
		this.type = type;
		this.dep = dep;
	}

	/**
	 * Retourne le nom de l'attribut
	 * @return le nom de l'attribut
	 */
	public String getName() {
		return this.name.trim();
	}

	/**
	 * Retourne le type de l'attribut
	 * @return le type de l'attribut
	 */
	public DTYPE getType() {
		return this.type;
	}
	
	/**
	 * Retourne le deplacement de l'attribut
	 * @return le deplacement de l'attribut
	 */
	public int getDep() {
		return this.dep;
	}
	
	/**
	 * Compare si l'attribut courant est de meme type qu'un autre attribut
	 * @param l'attribut a comparer
	 * @return vrai si les attributs sont de meme type, faux sinon
	 */
	public boolean compareTo(INFOATTRIBUTE a) {
		return this.type.compareTo(a.getType());
	}
	
	/** 
	 * Retourne la représentation de l'attribut sous forme de string :
	 * "ATTRIBUT " nom de l'attribut " de type : " type de l'attribut " et de dep : "
	 * deplacement de l'attribut
	 * @return la chaine representant l'attribut
	 */
	public String toString() {
		String message = "";
		message += "ATTRIBUT " + this.name;
		message += " de type : " + this.type.getName();
		message += " et de dep : " + this.dep + "\n";
		return message;
	}

}
