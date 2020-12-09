import java.lang.reflect.*;


public class Conversion {

	
	public static Object Conversion (Object source, Class<?> target) throws InstantiationException, IllegalAccessException {
		Object obj = target.newInstance(); //On créer un objet de la classe Target
	
		ArrayList<E> sourceAttribute= ((Object) source).getGenericComponentType();
		
		//On vérifie dans un premier si l'objet est de la même classe que Target avec Member
		if(source.getClass() instanceof target.getDeclaringClass()) {
			
			//Si oui on retourne Object
			return source;

			//Sinon On parcours l'object Source
		}else {
			for(Ob)
		
			//On récupére chaque attribut un par un et on les converties en attrbut de object target
			
			return obj;
		}
	
		
	}
}
