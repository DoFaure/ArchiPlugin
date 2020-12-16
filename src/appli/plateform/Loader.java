package appli.plateform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import appli.interfaces.IApp;
import plugins.application.App;

public class Loader {

	private ArrayList<DescripteurPlugin> descriptionDisplayDisponibles = new ArrayList<DescripteurPlugin>();

	private String path;

	// CONSTRUCTOR
	public Loader() {
		this.path = new File("").getAbsoluteFile().getAbsolutePath().concat(File.separator + "ressource");
		try {
			getPlugins(this.path);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		Loader loader = new Loader();

		// FIXME add manually plugin for test

		IApp app = new App();

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

	private ArrayList<DescripteurPlugin> getPlugins(String path) throws IOException, ParseException {
		FileReader pluginFileReader = new FileReader(path + "/plugins.json");
		// TODO : ajouter la doc pour importer GSON

		Gson gson = new Gson();
		JsonReader jsonReader = gson.newJsonReader(pluginFileReader);
		JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);
		Set<Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
		for (Map.Entry<String, JsonElement> entry : entrySet) {
			createDescripteurPlugin(entry.getKey(), entry.getValue().getAsJsonObject());
		}

		return descriptionDisplayDisponibles;

	}

	private void createDescripteurPlugin(String interfaceName, JsonObject pluginAsObject) {
		Set<Entry<String, JsonElement>> entrySet = pluginAsObject.entrySet();

		for (Map.Entry<String, JsonElement> entry : entrySet) {
			String name = entry.getKey();
			String classname = entry.getValue().getAsJsonObject().get("class").toString();
			String description = entry.getValue().getAsJsonObject().get("description").toString();
			System.out.println("name : " + name + ", classname : " + classname + ", description : " + description);
			List<String> dependencies = new ArrayList();
			if (entry.getValue().getAsJsonObject().get("dependencies").getAsJsonArray().size() > 0) {
				entry.getValue().getAsJsonObject().get("dependencies")
				.getAsJsonArray()
				.forEach(item -> {
					dependencies.add(item.getAsJsonObject().get("interface").toString());
					
				});
			}
			System.out.println("dependencies : " + dependencies);

			DescripteurPlugin descripteur = new DescripteurPlugin(name, classname, description, dependencies);
			descriptionDisplayDisponibles.add(descripteur);
		}
	}

}
