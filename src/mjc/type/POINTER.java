package mjc.type;

import mjc.tds.*;

/** 
 * Cette classe implante le type booleen heritant de DTYPE.
 * @author Philippe Leleux, Arthur Manoha, Quentin Glinel-Mortreuil
 */
public class POINTER extends DTYPE{
	
	private INFOCLASSE classe;	// classe de l'objet pointé

	/** 
	 * Constructeur creant un dtype de nom "pointeur".
	 * @param c la classe de l'objet pointé
	 */
	public POINTER(INFOCLASSE c) {
		super("pointeur", 1);
		this.classe = c;
	}

	/** 
	 * Retourne la classe de l'objet pointé
	 * @return la classe de l'objet pointé
	 */
	public INFOCLASSE getClasse() {
		return this.classe;
	}
	
	/**
	 * Compare le type courant avec un autre type.
	 * Un pointeur n'est compatible qu'avec un objet NULL ou un autre pointeur vers
	 * le meme type.
	 * @param type le type a comparer
	 * @return true si les types sont compatibles, false sinon
	 */
	@Override
	public boolean compareTo(DTYPE type) {
		boolean res = false;
		if(type instanceof NULL) {
			res = true;
		} else if (type instanceof POINTER) {
			res =  this.classe.compareTo(((POINTER) type).getClasse());
		}
		return res;
	}


	/** 
	 * Retourne la représentation du pointeur sous forme de string :
	 * "pointeur (" taille de la classe pointee " vers type = " nom de la classe pointee
	 * @return la chaine representant le pointeur
	 */
	public String toString(){
		return super.toString() + " vers type = " + this.classe.getName();
	}
}
