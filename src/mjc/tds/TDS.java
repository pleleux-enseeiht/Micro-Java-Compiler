package mjc.tds;

import java.util.HashMap;
import java.util.Set;

/** 
 * Cette classe implante la table des symboles qui contiendra les variables d'un niveau de
 * bloc.
 * Elle herite d'une HashMap avec pour clé la classe String et contenant des INFOVARIABLE.
 * @author Philippe Leleux, Arthur Manoha, Quentin Glinel-Mortreuil
 */
public class TDS extends HashMap<String, INFOVARIABLE> {

	private static final long serialVersionUID = 1L;	// Warning Serialisable..
	public TDS parent;	// la table parente (du niveau de bloc precedent)

	/**
	 * Constructeur par defaut de la table des symboles
	 */
	public TDS() {
	}
	
	/**
	 * Constructeur creant une table des symbole a partir de sa parente
	 * @param p la table parente
	 */
	public TDS(TDS p) {
		super();
		this.parent = p;
	}
	
	/**
	 * Ajoute les attributs d'une liste dans la table des symboles pour qu'ils soient
	 * consideres comme des variables.
	 * @param la la liste d'attributs a ajouter
	 */
	public void put(LATTRIBUTES la) {
		for(INFOATTRIBUTE a : la) {
			put(a.getName(), new INFOVARIABLE(a.getType(), 0));
		}
	}

	/**
	 * Retourne dans la table courante la variable dont le nom est passe en parametre
	 * @param n le nom de la variable a retourner
	 * @return la variable de nom n si elle existe, null sinon
	 */
	public INFOVARIABLE searchLocally(String name) {
		return get(name);
	}

	/**
	 * Retourne la variable dont le nom est passe en parametre en la cherchant dans la
	 * table courante puis recursivement dans les parentes.
	 * @param n le nom de la variable a retourner
	 * @return la variable de nom n si elle existe, null sinon
	 */
	public INFOVARIABLE searchGlobally(String name) {
		INFOVARIABLE i = searchLocally(name);
		if (i == null) {
			if (parent != null) {
				return parent.searchGlobally(name);
			}
		}
		return i;
	}

	/**
	 * Retourne la taille de la table en memoire, c'est-a-dire la somme des tailles des
	 * types des variables de la table.
	 * @return la taille de la table en memoire
	 */
	public int getSize() {
		int retour = 0;
		// la methode values retourne un set Iterable contenant les valeurs de la table
		for(INFOVARIABLE v : this.values()) {
			retour += v.getType().getSize();
		}	
		return retour;
	}
	
	/** 
	 * Retourne la représentation de la table des symboles sous forme de string :
	 * "Table des symboles" affichage des noms et variables les uns sous les autres
	 * @return la chaine representant la table des symboles
	 */
	public String toString() {
		String message = "Table des Symboles :\n";
		// la methode keySet retourne un set Iterable contenant les clés de la table
		Set <String> keys = this.keySet();
		for(String k : keys) {
			message += k + " : ";
			message += this.get(k) + "\n";
		}
		return message;
	}
}
