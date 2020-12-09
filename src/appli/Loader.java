package appli;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Loader {
	
	
	public static IDisplayStrategy getDisplay() {
		Properties prop = new Properties();
		try {
			prop.load(new FileReader("config.txt"));
			String classname = prop.getProperty("display");
			return (IDisplayStrategy) Class.forName(classname).newInstance();
		} catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}

//	public static IDisplayStrategy getDisplayFor(ArrayList<DescripteurPlugin> descriptionDisplayDisponibles) {
//		
//		return null;
//	}
	
}
