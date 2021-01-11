package appli.plateform;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import appli.interfaces.IAppClicker;


public class Loader {

	// TODO reflechir à l'intêret d'avoir une classe application si on a qu'une
	// seule application --> faire une application différente entre le fermier et le
	// brasseur (avec plugins chargé différents)

	// TODO : renommer la liste des descripteurs
	

	private Map<String,DescripteurPlugin> plugins = new HashMap<String,DescripteurPlugin>();

	private String path;

	// CONSTRUCTOR
	public Loader() throws Exception {
		this.path = new File("").getAbsoluteFile().getAbsolutePath().concat(File.separator + "ressource");
		try {
			this.getPlugins(path);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
			throws Exception {
		Loader loader = new Loader();
		
		IAppClicker app = (IAppClicker) loader.getConfiguredPlugin("application.interfaces.IAppClicker");
		// FIXME add manually plugin for test
//		AppClicker app = new AppClicker();
		app.run();
	}

	private Map<String,DescripteurPlugin> getPlugins(String path) throws IOException, ParseException {
		FileReader pluginFileReader = new FileReader(path + "/plugins.json");
		// TODO : ajouter la doc pour importer GSON

		Gson gson = new Gson();
		JsonReader jsonReader = gson.newJsonReader(pluginFileReader);
		JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);
		Set<Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
		for (Map.Entry<String, JsonElement> entry : entrySet) {
			createDescripteurPlugin(entry.getKey(), entry.getValue().getAsJsonObject());
		}

		return plugins;

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
			plugins.put(name, descripteur);
		}
	}
//
//	/**
//	 * Fonction temporaire permettant l'instanciation des plugins
//	 * 
//	 * @param descripteur
//	 * @param plugins     listes des plugins dependants listé dans le descripteur
//	 * @return
//	 */
//	public Object instanciatePlugin(DescripteurPlugin descripteur, List<Object> plugins) {
//
//		try {
//			if (descripteur.getDependencies().size() == 0) {
//				Object plugin = Class.forName(descripteur.getClassname()).newInstance();
//				return plugin;
//			} else {
//				Class<?>[] dependenciesNames = new Class<?>[descripteur.getDependencies().size()];
//
//				try {
//					for (int i = 0; i < descripteur.getDependencies().size(); i++) {
//						dependenciesNames[i] = Class.forName(descripteur.getDependencies().get(i));
//					}
//
//					Object plugin = Class.forName(descripteur.getClassname()).getDeclaredConstructor(dependenciesNames)
//							.newInstance(plugins.toArray());
//					return plugin;
//
//				} catch (ClassNotFoundException | SecurityException e) {
//					e.printStackTrace();
//					return null;
//				}
//			}
//
//		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException
//				| SecurityException | InvocationTargetException e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}
	
	/**
	 * @return the descriptionDisplayDisponibles
	 */
	public Map<String, DescripteurPlugin> getDescriptionDisplayDisponibles() {
		return plugins;
	}

	/**
	 * @param descriptionDisplayDisponibles the descriptionDisplayDisponibles to set
	 */
	public void setDescriptionDisplayDisponibles(Map<String, DescripteurPlugin> descriptionDisplayDisponibles) {
		this.plugins = descriptionDisplayDisponibles;
	}
	
	/**
	 * Instanciate a plugin using it's interface name (uses the config.json file to
	 * detect the right plugin and its the dependencies)
	 * 
	 * @param interfaceName name of the interface, like
	 *                      "application.interfaces.IApplication"
	 * 
	 * @return Object
	 */
	public Object getConfiguredPlugin(String interfaceName) throws Exception {
//		File configJsonFile = new File(path + File.separator + "config.json");

//		if (!configJsonFile.exists()) {
//			throw new FileNotFoundException();
//		}

//		JsonObject configJson = Json.parse(new FileReader(configJsonFile)).asObject();

//		if (!configJson.contains(interfaceName)) {
//			throw new Exception(interfaceName + " : Interface does not exist");
//		}
		FileReader pluginFileReader = new FileReader(path + "/configuration.json");

		Gson gson = new Gson();
		JsonReader jsonReader = gson.newJsonReader(pluginFileReader);
		JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);

		JsonObject interfaceJson = jsonObject.get(interfaceName).getAsJsonObject();

//		if (!this.validatePluginConfiguration(interfaceJson)) {
//			throw new Exception(interfaceName + " : Not a valid plugin object");
//		}

		return this.getPluginFromInterfaceJson(interfaceName, interfaceJson);
	}
	
	private Object getPluginFromInterfaceJson(String interfaceName, JsonObject interfaceJson) throws Exception {
		String pluginName = interfaceJson.get("name").getAsString();

		DescripteurPlugin pluginDescriptor = this.plugins.get(pluginName);

//		if (pluginDescriptor == null) {
//			throw new Exception(interfaceName + " : '" + pluginName + "' does not exist (is it defined in 'resources"
//					+ File.separator + "available-plugins.json' ?)");
//		}

		// without dependencies
		if (pluginDescriptor.getDependencies().size() == 0) {
			return this.getPlugin(pluginDescriptor);
		}

		JsonArray dependenciesJson = interfaceJson.get("dependencies").getAsJsonArray();
		ArrayList<Object> dependencies = new ArrayList<Object>();

		for (JsonElement dependencyJson : dependenciesJson) {
			JsonObject dependencyJsonObject = dependencyJson.getAsJsonObject();

//			if (!this.validatePluginConfiguration(dependencyJsonObject)) {
//				throw new Exception(interfaceName + " : Not a valid plugin dependency");
//			}

			dependencies.add(this.getPluginFromInterfaceJson(interfaceName, dependencyJsonObject));
		}

		return this.getPlugin(pluginDescriptor, dependencies);
	}
	
	/**
	 * Instanciate a plugin using a plugin descriptor that have dependencies
	 * 
	 * @param pluginDescriptor the plugin that you want to load
	 * @param dependencies     the plugin's instantiated dependencies (put in the
	 *                         right order, from the available plugins file
	 *                         'available-plugins.json')
	 * @return Object
	 */
	public Object getPlugin(DescripteurPlugin pluginDescriptor, List<Object> dependencies) {
//		if (pluginDescriptor.getStatus().equals("loader")) {
//			return pluginDescriptor.getLoadedPlugin();
//		}

		if (dependencies.size() == 0) {
			return this.getPlugin(pluginDescriptor);
		}

		// Get the interface name of the dependencies to instantiate the plugin
		Class<?>[] dependenciesNames = new Class<?>[pluginDescriptor.getDependencies().size()];

		try {
			for (int i = 0; i < pluginDescriptor.getDependencies().size(); i++) {
				dependenciesNames[i] = Class.forName(pluginDescriptor.getDependencies().get(i));
			}
		} catch (ClassNotFoundException | SecurityException e) {
			e.printStackTrace();
//			pluginDescriptor.setStatus("failed");
			return null;
		}

		try {
			Object plugin = Class.forName(pluginDescriptor.getClassname()).getDeclaredConstructor(dependenciesNames)
					.newInstance(dependencies.toArray());
//			pluginDescriptor.setLoadedPlugin(plugin);
//			pluginDescriptor.setStatus("loaded");
			return plugin;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException
				| SecurityException | InvocationTargetException e) {
			e.printStackTrace();
//			pluginDescriptor.setStatus("failed");
			return null;
		}
	}
	
	/**
	 * Instanciate a plugin using a plugin descriptor that doesn't have dependencies
	 * 
	 * @param pluginDescriptor the plugin that you want to load
	 * 
	 * @return Object
	 */
	public Object getPlugin(DescripteurPlugin pluginDescriptor) {
//		if (pluginDescriptor.getStatus().equals("loaded")) {
//			return pluginDescriptor.getLoadedPlugin();
//		}

		try {
			Object plugin = Class.forName(pluginDescriptor.getClassname()).getDeclaredConstructor().newInstance();
//			pluginDescriptor.setLoadedPlugin(plugin);
//			pluginDescriptor.setStatus("loaded");
			return plugin;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException
				| SecurityException | InvocationTargetException e) {
			e.printStackTrace();
//			pluginDescriptor.setStatus("failed");
			return null;
		}
	}

}
