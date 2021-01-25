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
		consumablesFarmer.add(new Consumables(10, 2, 2, "Bouteille d'eau"));
		consumablesFarmer.add(new Consumables(50, 3, 3, "Coca-Cola"));
		consumablesFarmer.add(new Consumables(100, 5, 5, "P'tit LU"));
		consumablesFarmer.add(new Consumables(500, 8, 20, "Engrais"));
		consumablesFarmer.add(new Consumables(1000, 10, 100, "Glyphosate"));
		consumablesFarmer.add(new Consumables(3000, 5, 500, "SuperFarmer"));
		return consumablesFarmer;
	}

	@Override
	public List<Consumables> createBrewerConsumables() {
		// TODO Auto-generated method stub
		return null;
	}

}
