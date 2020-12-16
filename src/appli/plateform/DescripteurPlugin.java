package appli.plateform;

import java.util.List;

public class DescripteurPlugin {
	
	String name;
	String classname;
	String description;
	List<String> dependencies;
	Object plugin; 
	
	public DescripteurPlugin(String name, String classname, String description, List<String> dependencies) {
		this.name = name;
		this.classname = classname;
		this.description = description;
		this.dependencies = dependencies;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the classname
	 */
	public String getClassname() {
		return classname;
	}

	/**
	 * @param classname the classname to set
	 */
	public void setClassname(String classname) {
		this.classname = classname;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the dependencies
	 */
	public List<String> getDependencies() {
		return dependencies;
	}

	/**
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}
	
	public Object getPlugin() {
		return this.plugin;
	}
	
	public void setPlugin(Object plugin) {
		this.plugin = plugin;
	}


	
	
	


}
