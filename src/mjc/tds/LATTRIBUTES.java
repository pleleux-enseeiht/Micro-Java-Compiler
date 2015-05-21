package mjc.tds;

import java.util.ArrayList;

/** 
 * Cette classe implante la liste d'attributs.
 * Elle herite d'une ArrayList d'INFOATTRIBUTE.
 * @author Philippe Leleux, Arthur Manoha, Quentin Glinel-Mortreuil
 */
public class LATTRIBUTES extends ArrayList<INFOATTRIBUTE> {
	
	private static final long serialVersionUID = 1L; // Warning Serialisable..

	/**
	 * Retourne la taille de la liste d'attributs en memoire (c'est a dire la somme des
	 * tailles des types de chaque attribut.
	 * @return la taille de la liste en memoire
	 */
	public int getSize() {
		int res = 0;
		for(INFOATTRIBUTE a : this) {
			res += a.getType().getSize();
		}
		return res;
	}
	
	/**
	 * Retourne le plus grand deplacement parmi les deplacements des attributs de la
	 * liste ou -1 si la liste est vide.
	 * @return le plus grand deplacement parmi les deplacements des attributs de la
	 * liste ou -1 si la liste est vide
	 */
	public int getDep() {
		int retour = -1;
		for(INFOATTRIBUTE a : this) {
			retour = Math.max(retour, a.getDep());
		}
		return retour;
	}

	/**
	 * Retourne la taille de la liste
	 * @return la taille de la liste
	 */
	public int getNbAttributes() {
		int nbAttributes = 0;
		for (INFOATTRIBUTE a : this) {
			nbAttributes++;
		}
		return nbAttributes;
	}

	/**
	 * Recherche un attribut dans la liste grace a son nom et renvoie l'INFOATTRIBUTE
	 * correspondant, ou null si il n'est pas present.
	 * @param name le nom de l'attribut a chercher
	 * @return l'INFOATTRIBUTE cherché si il est present, null sinon
	 */
	public INFOATTRIBUTE search(String name){
		for(INFOATTRIBUTE a : this){
			if (name.equals(a.getName())){
				return a;
			}
		}
		return null;
	}
	
	/**
	 * Compare la liste courante avec une autre liste en comparant les attributs un a un
	 * @param la la liste d'attributs a comparer
	 * @return vrai si les attributs etaient de meme type un a un, faux sinon
	 */
	public boolean compareTo(LATTRIBUTES la) {
		boolean res = true;
		for (INFOATTRIBUTE at : this) {
			for (INFOATTRIBUTE a : la) {
				res &= at.compareTo(a);
			}
		}
		return res;
	}
	
	/** 
	 * Retourne la représentation de la liste d'attributs sous forme de string :
	 * affichage des attributs séparés par des virgules
	 * @return la chaine representant la liste d'attributs
	 */
	public String toString() {
		boolean beginning = true;
		String message  = "";
		if (this.getNbAttributes() != 0) {
			//Evite simplement d'avoir un retour a la ligne de trop si la liste est vide
			message += "\n";
		}
		for (INFOATTRIBUTE a : this) {
			if (beginning) {
				beginning = false;	
			} else {
				message += ", ";
			}
			message += a.getType() + " ";
			message += a.getName();
		}
		return message;
	}
}
