package mjc.type;

/** 
 * Cette classe implante le type vide heritant de DTYPE
 * Ce type ne sert en realiter qu'a exprimer qu'une methode retourne void
 * @author Philippe Leleux, Arthur Manoha, Quentin Glinel-Mortreuil
 */
public class VIDE extends DTYPE {

	/** 
	 * Constructeur creant un dtype de nom "void"
	 */
	public VIDE() {
		super("void", 0);
	}
	
}
