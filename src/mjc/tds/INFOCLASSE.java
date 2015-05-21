package mjc.tds;

/** 
 * Cette classe implante l'info pour les classes que l'on stockera dans la table des
 * classes.
 * Chacune de ces classes a un nom, une liste de methodes, une liste d'attributs, une
 * classe parente (qui peut etre nulle) et un deplacement.
 * @author Philippe Leleux, Arthur Manoha, Quentin Glinel-Mortreuil
 */
public class INFOCLASSE {
	
	private String name;			// nom de la classe
	private LMETHODS methods;		// liste des methodes de la classe
	private LATTRIBUTES attributes;		// liste des attributs de la classe
	private INFOCLASSE parent;		// classe parente
	private int dep;			// deplacement de la classe
	
	/**
	 * Constructeur creant une classe a partir de son nom, ses listes de methodes et
	 * d'attributs, sa classe parente et son deplacement.
	 * @param name le nom de la classe
	 * @param methods la liste des methodes de la classe
	 * @param attributes la liste des attributs de la classe
	 * @param parent la classe parente de la classe
	 * @param le deplacement de la classe
	 */
	public INFOCLASSE (String name, LMETHODS methods, LATTRIBUTES attributes, INFOCLASSE parent, int dep) {
		this.name = name;
		this.methods = methods;
		this.attributes = attributes;
		this.parent = parent;
		this.dep = dep;
	}

	/**
	 * Constructeur creant une classe a partir de son nom et son deplacement.
	 * @param name le nom de la classe
	 * @param dep le deplacement de la classe
	 */	
	public INFOCLASSE(String name, int dep) {
		this.name = name;
		this.methods = new LMETHODS();
		this.attributes = new LATTRIBUTES();
		this.dep = dep;
	}
	
	/**
	 * Retourne le nom de la classe
	 * @return le nom de classe
	 */
	public String getName() {
		return this.name.trim();
	}

	/**
	 * Retourne la classe parente de la classe
	 * @return la classe parente
	 */	
	public INFOCLASSE getParent() {
		return this.parent;
	}
	
	/**
	 * Change la valeur de la classe parente de la classe.
	 * Les methodes et attributs de la classe parente sont automatiquement ajoutes a
	 * ceux de la classe en appelant inheritMethods et inheritAttributes (sauf les
	 * constructeurs et classe main).
	 * @param parent la nouvelle classe parente
	 */
	public void setParent(INFOCLASSE parent) {
		this.parent = parent;
		this.inheritMethods(parent);
		this.inheritAttributes(parent);
	}
	
	/**
	 * Retourne la liste des attributs de la classe
	 * @return la liste des attributs de la classe
	 */
	public LATTRIBUTES getAttributes() {
		return this.attributes;
	}
	
	/**
	 * Retourne la liste des methodes de la classe
	 * @return la liste des methodes de la classe
	 */
	public LMETHODS getMethods() {
		return this.methods;
	}
	
	/**
	 * Retourne le deplacement de la classe
	 * @return le deplacement de la classe
	 */
	public int getDep() {
		return this.dep;
	}
	
	/**
	 * Ajoute les methode de la classe parente dans la liste des methodes de la classe
	 * @param parent la classe parente
	 */
	private void inheritMethods(INFOCLASSE parent) {
		for(INFOMETHOD m : parent.getMethods()) {
			String name = m.getName();
			// On hérite pas des constructeurs ni de main
			if(!name.equals(parent.getName()) && !name.equals("main")) {
				this.methods.add(m);
			}
		}
	}

	/**
	 * Ajoute les attributs de la classe parente dans la liste des attributs de la classe
	 * @param parent la classe parente
	 */
	private void inheritAttributes(INFOCLASSE parent) {
		for(INFOATTRIBUTE a : parent.getAttributes()) {
			this.attributes.add(a);
		}
	}
	
	/**
	 * Compare si la classe courante avec une autre classe grace au nom et en prenant
	 * en compte une eventuelle relation d'heritage.
	 * @param c la classe a comparer
	 * @return vrai si les classe sont compatibles
	 */
	public boolean compareTo(INFOCLASSE c) {
		// comparaison avec la classe c
		boolean strict_cmp = this.name.equals(c.getName());
		// comparaison avec tous les ancetres de classe c
		boolean inherit_cmp = false;
		/** 
		 * On effectue le test en remontant tous les parents de la classe c et on
		 * aura vrai si le test est bon au moins pour une grace au ||
		 */
		INFOCLASSE caux = c.getParent();
		while (caux != null) {
			inherit_cmp = inherit_cmp || this.name.equals(caux.getName());
			caux = caux.getParent();
		}
		// test bon si la classe ou un parent est compatible
		return strict_cmp || inherit_cmp;
	}
	
	/**
	 * Retourne le nombre de methodes (hors constructeurs) de la classe
	 * @return le nombre de methodes de la classe
	 */
	public int countMethods() {
		int retour = 0;
		for(INFOMETHOD m : this.methods) {
			//on exclue les constructeurs
			if (!m.getName().equals(this.name)) {
				retour++;
			}
		}
		return retour;
	}
	
	/** 
	 * Retourne la représentation de la classe sous forme de string :
	 * "Classe " nom de la classe "\nAttributs : " attributs de la classe
	 * "\nMethodes : " methodes de la classe "\nDeplacement : " deplacement de la classe
	 * @return la chaine representant la classe
	 */
	public String toString() {
		String message = "Classe " + this.name + "\n";
		message += "\tAttributs :";
		message += attributes + "\n";
		message += "\tMethodes :\n";
		message += methods;
		message += "\tDeplacement : " + this.dep + "\n\n";
		return message;
	}
}
