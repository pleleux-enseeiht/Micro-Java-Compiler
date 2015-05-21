package mjc.type;

/** 
 * Cette classe abstraite implante les types
 * Un type a une taille en mémoire et un nom
 * @author Philippe Leleux, Arthur Manoha, Quentin Glinel-Mortreuil
 */
public abstract class DTYPE {

	private int size;	// taille du type en memoire (ex : un entier -> 1)
	private String name;	// nom du type

	/**
	 * Constructeur par défaut de type
	 */
	public DTYPE() {
	}

	/**
	 * Constructeur creant un DTYPE partir de sa taille et son nom
	 * @param name le nom du type
	 * @param size la taille du type en memoire
	 */
	public DTYPE(String name, int size) {
		this.name = name;
		this.size = size;
	}

	/** 
	 * Retourne la taille du type
	 * @return la taille du type
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * Retourne le nom du type
	 * @return le nom du type
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Compare le type courant avec un autre type.
	 * Deux types sont compatibles s'ils ont le même nom ou si l'un d'eux est un
	 * operateur polymorphe.
	 * @param type le type a comparer
	 * @return true si les types sont compatibles, false sinon
	 */
	public boolean compareTo(DTYPE type) {
		return (name.equals("operateur polymorphe"))||(type.name.equals("operateur polymorphe"))||this.name.equals(type.getName());
	}

	/** 
	 * Retourne la représentation du type sous forme de string :
	 * nom du type "(" taille du type ")"
	 * @return la chaine representant le type
	 */
	public String toString() {
		return this.name + "(" + this.size + ")";
	}
}
