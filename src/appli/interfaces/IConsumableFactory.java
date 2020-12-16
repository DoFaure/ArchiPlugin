package appli.interfaces;

import java.util.List;

import appli.models.Consumables;

public interface IConsumableFactory {

	public List<Consumables> createFarmerConsumables();
	public List<Consumables> createBrewerConsumables(); 
}
