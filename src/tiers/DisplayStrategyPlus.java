package tiers;

import appli.IDisplayStrategy;
import data.Personne;

public class DisplayStrategyPlus implements IDisplayStrategy {

	@Override
	public void affiche(Personne p) {
		System.out.println("PLUS" + p);

	}

}
