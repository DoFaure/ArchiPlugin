package appli.interfaces;

import appli.plateform.Loader;

public interface IApp {
	
	public void run();
	
	public void setLoader(Loader loader);
	
	public Loader getLoader();
	
}
