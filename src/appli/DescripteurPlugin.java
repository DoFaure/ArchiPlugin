package appli;

public class DescripteurPlugin {
	
	String classname = "";
	String resume = "";
	
	public DescripteurPlugin(String classname, String resume) {
		this.classname = classname;
		this.resume = resume;
	}
	
	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}
}
