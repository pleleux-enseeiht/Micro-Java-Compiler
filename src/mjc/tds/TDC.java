package mjc.tds;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/** 
 * Cette classe implante la table des classes qui contiendra les classes du fichier.
 * Elle herite d'une HashMap avec pour clé la classe String et contenant des INFOCLASSE.
 * @author Philippe Leleux, Arthur Manoha, Quentin Glinel-Mortreuil
 */
public class TDC extends HashMap<String, INFOCLASSE> {
	
	private static final long serialVersionUID = 1L;	// Warning Serialisable..

	/**
	 * Ajoute une INFOCLASSE dans la HashMap a partir de l'INFOCLASSE seule
	 * @param c la classe a ajouter
	 */
	public void put(INFOCLASSE c) {
		put(c.getName(), c);
	}
	
	/**
	 * Range les classes par ordre de deplacement dans une Map de clé le déplacement
	 * @return la Map contenant les classes rangées par ordre de deplacement
	 */
	public Map<Integer, INFOCLASSE> sortByDep() {
		Map<Integer, INFOCLASSE> tdc = new TreeMap<Integer, INFOCLASSE>();
		int dep;
		for(INFOCLASSE c : this.values()) {
			dep = c.getDep();
			tdc.put(dep, c);
		}
		return tdc;
	}

	/** 
	 * Retourne la représentation de la table des classes sous forme de string :
	 * "Table des classes" affichage des classes les unes sous les autres
	 * @return la chaine representant la table des classes
	 */
	public String toString() {
		String retour = "Table des classes\n";
		for(INFOCLASSE c : this.values()) {
			retour += c;
		}
		return retour;
	}
}
