package plugins.displays;

import java.util.List;

import appli.interfaces.IDisplay1;
import appli.models.Products;

public class Display1 implements IDisplay1 {

	@Override
	public void displayProducts(List<Products> products) {
		for(Products product : products ) {
			System.out.println(product);
		}
	}
	
	
}
