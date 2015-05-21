package mjc.tds;

import mjc.type.*;

/** 
 * Cette classe implante l'info pour les variables que l'on stockera dans la table des
 * symboles.
 * Chacune de ces variables a un type et un deplacement.
 * @author Philippe Leleux, Arthur Manoha, Quentin Glinel-Mortreuil
 */
public class INFOVARIABLE {

	private DTYPE type;	// type de la variable
	private int dep;	// deplacement de la variable
	
	/**
	 * Constructeur creant une variable a partir de type et son deplacement.
	 * @param type le type de la variable
	 * @param dep le deplacement de la variable
	 */
	public INFOVARIABLE (DTYPE type, int dep) {
		this.type = type;
		this.dep = dep;
	}
	
	/**
	 * Retourne le type de la variable
	 * @return le type de la variable
	 */
	public DTYPE getType() {
		return this.type;
	}
	
	/**
	 * Retourne le deplacement de la variable
	 * @return le deplacement de la variable
	 */
	public int getDep() {
		return this.dep;
	}
	
	/** 
	 * Retourne la représentation de la variable sous forme de string :
	 * "Variable de type : " type de la variable " et de dep : " le deplacement de la
	 * variable
	 * @return la chaine representant la variable
	 */
	public String toString() {
		String message = "";
		message += "Variable de type : " + this.type;
		message += " et de dep : " + this.dep;
		return message;
	}
	
}
