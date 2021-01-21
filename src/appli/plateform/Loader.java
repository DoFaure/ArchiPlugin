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

	private Map<String,DescripteurPlugin> plugins = new HashMap<String,DescripteurPlugin>();

	private String path;

	// CONSTRUCTOR
	public Loader() throws Exception {
		this.path = new File("").getAbsoluteFile().getAbsolutePath().concat(File.separator + "ressource");
		try {
			this.getPluginsToMap(path);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
			throws Exception {
		Loader loader = new Loader();
		
		IAppClicker app = (IAppClicker) loader.getConfiguredPlugin("application.interfaces.IAppClicker");
		app.run();
	}
	
	public static void loadPlugin() {
		
	}

	/**
	 * Parse plugins.json file to push all the plugins into the map "plugins"
	 * @param path
	 * @return Map<String, DescripteurPlugin> name of the plugin, descriptor of the plugin @DescripteurPlugin
	 * @throws IOException
	 * @throws ParseException
	 */
	private Map<String,DescripteurPlugin> getPluginsToMap(String path) throws IOException, ParseException {
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

	/**
	 * Put a plugin into the map by given interfaceName and plugin as JsonObject
	 * @param interfaceName
	 * @param pluginAsObject
	 */
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
	 * Function that give a configured plugin by calling successively methods 
	 * @param interfaceName
	 * @return Object
	 * @throws Exception
	 */
	public Object getConfiguredPlugin(String interfaceName) throws Exception {

		FileReader pluginFileReader = new FileReader(path + "/configuration.json");

		Gson gson = new Gson();
		JsonReader jsonReader = gson.newJsonReader(pluginFileReader);
		JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);

		JsonObject interfaceJson = jsonObject.get(interfaceName).getAsJsonObject();

		return this.getPluginFromJson(interfaceName, interfaceJson);
	}
	
	/**
	 * Return a plugin give from interface's name by calling getPluginWithDependencies or withoutDependencies
	 * @param interfaceName
	 * @param interfaceJson
	 * @return Object 	a plugin
	 * @throws Exception
	 */
	private Object getPluginFromJson(String interfaceName, JsonObject interfaceJson) throws Exception {
		String pluginName = interfaceJson.get("name").getAsString();

		DescripteurPlugin pluginDescriptor = this.plugins.get(pluginName);

		//Without dependencies
		if (pluginDescriptor.getDependencies().size() == 0) {
			return this.getPluginWithoutDependencies(pluginDescriptor);
		}

		JsonArray dependenciesJson = interfaceJson.get("dependencies").getAsJsonArray();
		ArrayList<Object> dependencies = new ArrayList<Object>();

		for (JsonElement dependencyJson : dependenciesJson) {
			JsonObject dependencyJsonObject = dependencyJson.getAsJsonObject();

			dependencies.add(this.getPluginFromJson(interfaceName, dependencyJsonObject));
		}

		return this.getPluginWithDependencies(pluginDescriptor, dependencies);
	}
	
	/**
	 * Instanciate plugin with it dependencies
	 * @param pluginDescriptor
	 * @param dependencies
	 * @return Object
	 */
	public Object getPluginWithDependencies(DescripteurPlugin pluginDescriptor, List<Object> dependencies) {

		// If call error and plugin has no dependencies
		if (dependencies.size() == 0) {
			return this.getPluginWithoutDependencies(pluginDescriptor);
		}
		
		Class<?>[] dependenciesNames = new Class<?>[pluginDescriptor.getDependencies().size()];

		try {
			for (int i = 0; i < pluginDescriptor.getDependencies().size(); i++) {
				dependenciesNames[i] = Class.forName(pluginDescriptor.getDependencies().get(i));
			}
		} catch (ClassNotFoundException | SecurityException e) {
			e.printStackTrace();
			return null;
		}

		try {
			Object plugin = Class.forName(pluginDescriptor.getClassname()).getDeclaredConstructor(dependenciesNames)
					.newInstance(dependencies.toArray());
			return plugin;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException
				| SecurityException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Instantiate plugin without dependencies
	 * @param pluginDescriptor
	 * @return Object
	 */
	public Object getPluginWithoutDependencies(DescripteurPlugin pluginDescriptor) {

		try {
			Object plugin = Class.forName(pluginDescriptor.getClassname()).getDeclaredConstructor().newInstance();
			return plugin;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException | SecurityException  e) {
			e.printStackTrace();
			return null;
		}
	}

}
