package appli.interfaces;

import java.io.IOException;

public interface IGameManager {
	public void saveGame(int nbWheat, int nbWheatByClick, int nbCo2);
	public int[] loadGame() throws IOException;
}
