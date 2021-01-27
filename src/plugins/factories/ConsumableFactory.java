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
		consumablesFarmer.add(new Consumables(10, 2, 8, "Bouteille d'eau"));
		consumablesFarmer.add(new Consumables(50, 5, 15, "Coca-Cola"));
		consumablesFarmer.add(new Consumables(100, 10, 14, "P'tit LU"));
		consumablesFarmer.add(new Consumables(500, 30, 20, "Engrais"));
		consumablesFarmer.add(new Consumables(1000, 20, 100, "Glyphosate"));
		consumablesFarmer.add(new Consumables(3000, 50, 500, "SuperFarmer"));
		return consumablesFarmer;
	}

	@Override
	public List<Consumables> createBrewerConsumables() {
		// TODO Auto-generated method stub
		return null;
	}

}
