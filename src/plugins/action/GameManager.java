package plugins.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import appli.interfaces.IGameManager;

public class GameManager implements IGameManager {
	
	private String path = new File("").getAbsoluteFile().getAbsolutePath().concat(File.separator + "save");
	
	public void saveGame(int nbWheat, int nbWheatByClick, int nbCo2) {
		try {
			FileWriter myWriter = new FileWriter(path + "/save.txt");
			myWriter.write(nbWheat + ":" + nbWheatByClick + ":" + nbCo2);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	public int[] loadGame() throws IOException {
		int[] array = new int[3];
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + "/save.txt"));
			
			String line = br.readLine();
			if(line != null){
                String[] splited = line.split(":");
                for(int i = 0; i < splited.length; i++)
                	array[i] = Integer.parseInt(splited[i]);
			} 
				
			br.close();
		} catch (IOException e) {
			throw new IOException(e);
		}
		return array;
	}
}
