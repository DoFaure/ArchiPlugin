package appli.plateform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import appli.interfaces.IApp;
import plugins.application.App;

public class Loader {

	private static ArrayList<DescripteurPlugin> descriptionDisplayDisponibles;

	private String path;

	// CONSTRUCTOR
	public Loader() {
		this.path = new File("").getAbsoluteFile().getAbsolutePath().concat(File.separator + "ressource");

	}

	public static void main(String[] args) throws FileNotFoundException {
		Loader loader = new Loader();

		// FIXME add manually plugin for test

		IApp app = new App();

		getPlugins(loader.path);
		// app.run();

	}

//	public static ArrayList<DescripteurPlugin> getPlugin() {
//
//		Properties prop = new Properties();
//		ArrayList<DescripteurPlugin> descripteurs = new ArrayList<DescripteurPlugin>();
//		try {
//			prop.load(new FileReader(CHEMIN));
//
//			String display = prop.getProperty("display");
//
//			String[] allDisplayer = display.split(";");
//			for (String descripteur : allDisplayer) {
//				String[] descripteurFields = descripteur.split(",");
//				descripteurs.add(new DescripteurPlugin(descripteurFields[0], descripteurFields[1], descripteurFields[2]));
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			return descripteurs;
//		}
//
//	}

	 public static ArrayList<DescripteurPlugin> getPlugins(String path) throws FileNotFoundException {
		FileReader jsonPluginReader = new FileReader(path + "/plugins.json");

		// TODO : ajouter la doc pour importer GSON
		Gson gson = new Gson();
		JsonReader jsonReader = gson.newJsonReader(jsonPluginReader);
		JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);
		Set<Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
		for (Map.Entry<String, JsonElement> entry : entrySet) {
				System.out.println(entry);
		}

		return descriptionDisplayDisponibles;

	}

}
