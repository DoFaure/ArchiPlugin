package appli.models;

import java.util.Timer;
import java.util.TimerTask;

import plugins.displays.FarmerClickerDisplay;

public class Consumables {
	private int price;
	private int duration;
	private int wheatAugmentation;
	private String label;
	private Timer timer;

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return the wheatAugmentation
	 */
	public float getWheatAugmentation() {
		return wheatAugmentation;
	}

	/**
	 * @param wheatAugmentation the wheatAugmentation to set
	 */
	public void setWheatAugmentation(int wheatAugmentation) {
		this.wheatAugmentation = wheatAugmentation;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	public Consumables(int price, int duration, int wheatAugmentation, String label) {
		super();
		this.price = price;
		this.duration = duration;
		this.wheatAugmentation = wheatAugmentation;
		this.label = label;
	}

	/**
	 * @return the timer
	 */
	public Timer getTimer() {
		return timer;
	}

	/**
	 * @param timer the timer to set
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	@Override
	public String toString() {
		return "Consumables [price=" + price + ", duration=" + duration + ", wheatAugmentation=" + wheatAugmentation
				+ ", label=" + label + "";
	}

	public void startTimer() {
		this.timer = new Timer();
		timer.schedule(new TimerTask() {
            long t0 = System.currentTimeMillis();
            @Override
            public void run() {
                if (System.currentTimeMillis() - t0 <= duration * 1000) {
                 	FarmerClickerDisplay.nbWheat += wheatAugmentation; 
                 	FarmerClickerDisplay.updateComponents();         	
                } else {
                    cancel();
                }
                  	
            }
        },0, 1000);
	}

}
