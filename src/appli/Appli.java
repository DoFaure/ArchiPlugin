package appli;

import java.util.ArrayList;

import data.Personne;
import tiers.DisplayStrategy;
import tiers.DisplayStrategyPlus;

public class Appli {
	
	IDisplayStrategy display;
	private ArrayList<DescripteurPlugin> descriptionDisplayDisponibles;
	
	public static void main(String[] args) {
		Appli appli = new Appli();
		appli.run();
		appli.setDisplay(new DisplayStrategy());
		appli.run();
	}
	
	public Appli() {
		display = new DisplayStrategyPlus();
//		descriptionDisplayDisponibles = Loader.getDescriptionDisplay();

	}
	
	public void setDisplay(IDisplayStrategy display) {
		this.display = display;
	}
	
	private void run() {
		Personne p = new Personne();
		p.setAge(p.getAge() + 1);
		
		affiche(p);
	}

	private void affiche(Personne p) {
//		if(display == null) {
//			display = Loader.getDisplayFor(descriptionDisplayDisponibles);
//		}
		display.affiche(p);
	}
	
}
