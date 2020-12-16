package appli.models;

import appli.interfaces.IDisplayStrategy;

public class DisplayStrategyPlus implements IDisplayStrategy {

	@Override
	public void affiche(Personne p) {
		System.out.println("PLUS" + p);

	}

}
