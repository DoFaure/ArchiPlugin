package appli;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import data.Personne;

public class Loader {

	IDisplayStrategy display;
	private ArrayList<DescripteurPlugin> descriptionDisplayDisponibles;

	final static String CHEMIN = "H:/Logiciel_Developement/Workspace_Java/ArchiPlugin/src/config.txt";

	
	// CONSTRUCTOR
	public Loader() {
		descriptionDisplayDisponibles = Loader.getDescriptionDisplay();
	}
	
	public static void main(String[] args) {
		Loader loader = new Loader();
		loader.run();
	}

	
	// 
	private void run() {
		Personne p = new Personne();
		p.setAge(p.getAge() + 1);

		affiche(p);
	}

	private void affiche(Personne p) {
		if (display == null) {
			display = Loader.getDisplayFor(descriptionDisplayDisponibles.get(0));
		}
		display.affiche(p);
	}

	public static IDisplayStrategy getDisplay() {
		Properties prop = new Properties();
		try {
			// FIXME : ATTENTION A BIEN CHANGER LE CHEMIN
			prop.load(new FileReader(CHEMIN));
			String classname = prop.getProperty("display");
			return (IDisplayStrategy) Class.forName(classname).newInstance();
		} catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ArrayList<DescripteurPlugin> getDescriptionDisplay() {

		Properties prop = new Properties();
		ArrayList<DescripteurPlugin> descripteurs = new ArrayList<DescripteurPlugin>();
		try {
			prop.load(new FileReader(CHEMIN));

			String display = prop.getProperty("display");

			String[] allDisplayer = display.split(";");
			for (String descripteur : allDisplayer) {
				String[] descripteurFields = descripteur.split(",");
				descripteurs.add(new DescripteurPlugin(descripteurFields[0], descripteurFields[1]));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			return descripteurs;
		}

	}

	public static IDisplayStrategy getDisplayFor(DescripteurPlugin descripteurPlugin) {
		try {
			return (IDisplayStrategy) Class.forName(descripteurPlugin.getClassname()).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
