package mjc.tds;

import java.util.ArrayList;

/** 
 * Cette classe implante la liste de methodes.
 * Elle herite d'une ArrayList d'INFOMETHOD.
 * @author Philippe Leleux, Arthur Manoha, Quentin Glinel-Mortreuil
 */
public class LMETHODS extends ArrayList<INFOMETHOD> {
	
	private static final long serialVersionUID = 1L; // Warning Serialisable..

	/**
	 * Retourne le deplacement d'une methode dans la liste, c'est a dire son classement.
	 * On ne peut pas simplement utiliser indexOf de ArrayList car la fonction de
	 * comparaison est spéciale.
	 * @param m la methode dont on souhaite le deplacement
	 * @return le deplacement de la methode recherchee
	 */
	public int getDep(INFOMETHOD m) {
		int res = 0;
		boolean trouve = false;
		for(INFOMETHOD i : this) {
			if (m.compareTo(i)) {
				trouve = true; 
			}
			if (!trouve) {
				res ++;
			}
		}
		return res;
	}

	/**
	 * Recherche une methode dans la liste
	 * On ne peut pas simplement utiliser indexOf de ArrayList car la fonction de
	 * comparaison est spéciale.
	 * @param m la methode que l'on recherche
	 * @return l'info de la methode recherchee si elle est trouvee, null sinon
	 */
	public INFOMETHOD search(INFOMETHOD m){
		for(INFOMETHOD i : this){
			if (m.compareTo(i)){
				return i;
			}
		}
		return null;
	}

	/**
	 * Remplace une methode dans la liste.
	 * @param old la methode a remplacer
	 * @param newOne la nouvelle methode
	 */
	public void replace(INFOMETHOD old, INFOMETHOD newOne) {
		int index = this.indexOf(old);
		this.remove(old);
		this.add(index,newOne);
	}
	
	/** 
	 * Retourne la représentation de la liste de methodes sous forme de string :
	 * affichage d'une methode et de son deplacement par ligne
	 * @return la chaine representant la methode
	 */
	public String toString() {
		String message = "";
		for(INFOMETHOD m : this) {
			message += m +" deplacement : " + this.getDep(m) + "\n";
		}
		return message;
	}
}
