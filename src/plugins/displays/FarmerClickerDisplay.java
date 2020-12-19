package plugins.displays;

import java.util.List;

import appli.interfaces.IDisplay1;
import appli.models.Consumables;
import appli.models.Products;

public class Display1 implements IDisplay1 {

	@Override
	public void displayProducts(List<Products> products) {
		if (products != null && !products.isEmpty()) {
			for (Products product : products) {
				System.out.println(product.toString());
			}
		}
	}

	@Override
	public void displayConsumables(List<Consumables> consumables) {
		if (consumables != null && !consumables.isEmpty()) {
			for (Consumables consumable : consumables) {
				System.out.println(consumable.toString());
			}
		}
	}

}
