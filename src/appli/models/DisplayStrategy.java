package appli.models;

import appli.interfaces.IDisplayStrategy;

public class DisplayStrategy implements IDisplayStrategy {

	@Override
	public void affiche(Personne p) {
		System.out.println(p);
		
	}
	
}
