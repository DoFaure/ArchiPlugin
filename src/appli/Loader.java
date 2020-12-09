package appli;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;



public class Loader {
	
	final static String CHEMIN = "C:/Users/arthu/eclipse-workspace/ArchiPlugin/src/config.txt";
	
	public static IDisplayStrategy getDisplay() {
		Properties prop = new Properties();
		try {
			//FIXME : ATTENTION A BIEN CHANGER LE CHEMIN
			prop.load(new FileReader(CHEMIN));
			String classname = prop.getProperty("display");
			return (IDisplayStrategy) Class.forName(classname).newInstance();
		} catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}

	public static ArrayList<DescripteurPlugin> getDescriptionDisplay(){
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
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
