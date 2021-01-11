package plugins.factories;

import java.util.ArrayList;
import java.util.List;

import appli.interfaces.IConsumableFactory;
import appli.models.Consumables;

public class ConsumableFactory implements IConsumableFactory {

	@Override
	public List<Consumables> createFarmerConsumables() {
		List<Consumables> consumablesFarmer = new ArrayList<Consumables>();
		// Prix, duree, augmentation, nom
		consumablesFarmer.add(new Consumables(1, 10, 10, "Test"));
		consumablesFarmer.add(new Consumables(25, 100, 2, "Bouteille d'eau"));
		consumablesFarmer.add(new Consumables(2, 100, 3, "Coca-Cola"));
		consumablesFarmer.add(new Consumables(100, 100, 5, "Engrais"));
		consumablesFarmer.add(new Consumables(500, 100, 25, "Glyphosate"));
		consumablesFarmer.add(new Consumables(1000, 100, 100, "SuperFarmer"));
		return consumablesFarmer;
	}

	@Override
	public List<Consumables> createBrewerConsumables() {
		// TODO Auto-generated method stub
		return null;
	}

}
