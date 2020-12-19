package appli.plateform;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import appli.interfaces.IFarmerClickerDisplay;
import plugins.application.AppClicker;

public class Loader {

	// TODO reflechir à l'intêret d'avoir une classe application si on a qu'une
	// seule application --> faire une application différente entre le fermier et le
	// brasseur (avec plugins chargé différents)

	// TODO : renommer la liste des descripteurs
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

	public static void main(String[] args)
			throws IOException, ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException {
//		Loader loader = new Loader();

		// FIXME add manually plugin for test
//		List<Object> pluginDependencies = new ArrayList<Object>();
//
//		pluginDependencies.add(loader.instanciatePlugin(loader.descriptionDisplayDisponibles.get(1), null));
//		pluginDependencies.add(loader.instanciatePlugin(loader.descriptionDisplayDisponibles.get(2), null));
////		pluginDependencies.add(loader.instanciatePlugin(loader.descriptionDisplayDisponibles.get(3), null));
//		IFarmerClickerDisplay farmerDisplay =  (IFarmerClickerDisplay) loader.instanciatePlugin(loader.descriptionDisplayDisponibles.get(0), pluginDependencies);
//		AppClicker app = new AppClicker(farmerDisplay);
		AppClicker app = new AppClicker();

//		app.setLoader(loader);
		app.run();
	}

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
			String classname = entry.getValue().getAsJsonObject().get("class").getAsString();
			String description = entry.getValue().getAsJsonObject().get("description").getAsString();
			System.out.println("name : " + name + ", classname : " + classname + ", description : " + description);
			List<String> dependencies = new ArrayList<String>();
			if (entry.getValue().getAsJsonObject().get("dependencies").getAsJsonArray().size() > 0) {
				entry.getValue().getAsJsonObject().get("dependencies").getAsJsonArray().forEach(item -> {
					dependencies.add(item.getAsJsonObject().get("interface").getAsString());

				});
			}
			System.out.println("dependencies : " + dependencies);

			DescripteurPlugin descripteur = new DescripteurPlugin(name, classname, description, dependencies);
			descriptionDisplayDisponibles.add(descripteur);
		}
	}

	/**
	 * Fonction temporaire permettant l'instanciation des plugins
	 * 
	 * @param descripteur
	 * @param plugins     listes des plugins dependants listé dans le descripteur
	 * @return
	 */
	public Object instanciatePlugin(DescripteurPlugin descripteur, List<Object> plugins) {

		try {
			if (descripteur.getDependencies().size() == 0) {
				Object plugin = Class.forName(descripteur.getClassname()).newInstance();
				return plugin;
			} else {
				Class<?>[] dependenciesNames = new Class<?>[descripteur.getDependencies().size()];

				try {
					for (int i = 0; i < descripteur.getDependencies().size(); i++) {
						dependenciesNames[i] = Class.forName(descripteur.getDependencies().get(i));
					}

					Object plugin = Class.forName(descripteur.getClassname()).getDeclaredConstructor(dependenciesNames)
							.newInstance(plugins.toArray());
					return plugin;

				} catch (ClassNotFoundException | SecurityException e) {
					e.printStackTrace();
					return null;
				}
			}

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException
				| SecurityException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @return the descriptionDisplayDisponibles
	 */
	public ArrayList<DescripteurPlugin> getDescriptionDisplayDisponibles() {
		return descriptionDisplayDisponibles;
	}

	/**
	 * @param descriptionDisplayDisponibles the descriptionDisplayDisponibles to set
	 */
	public void setDescriptionDisplayDisponibles(ArrayList<DescripteurPlugin> descriptionDisplayDisponibles) {
		this.descriptionDisplayDisponibles = descriptionDisplayDisponibles;
	}

}
