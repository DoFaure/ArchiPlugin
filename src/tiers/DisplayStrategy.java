package tiers;

import appli.IDisplayStrategy;
import data.Personne;

public class DisplayStrategy implements IDisplayStrategy {

	@Override
	public void affiche(Personne p) {
		System.out.println(p);
		
	}
	
}
