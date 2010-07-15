package se.anyro.snr;

import se.anyro.snr.bodies.Ball;
import se.anyro.snr.bodies.Goal;
import se.anyro.snr.bodies.Hole;
import se.anyro.snr.bodies.Wall;

public abstract class Level {

	private Level() {		
	}
	
	abstract void setup(GameThread gameThread);
	
	public static int count() {
		return levels.length;
	}
	
	public static void setupLevel(GameThread gameThread, int index) {
		levels[index - 1].setup(gameThread);
	}

	private static Level[] levels = new Level[] {
		new Level() {
			@Override
			public void setup(GameThread gameThread) {
				gameThread.add(new Wall(-2f, 5, 16, 2));
		        gameThread.add(new Wall(2f, -5, 16, 2));
		        
		        gameThread.add(new Goal(-8, 13));

		        gameThread.add(new Ball(0, -13));
			}
		},
		new Level() {
			@Override
			public void setup(GameThread gameThread) {
				gameThread.add(new Hole(0, 13.5f));
		        gameThread.add(new Hole(-8, 6.5f));
		        gameThread.add(new Hole(-4, 3));
		        gameThread.add(new Hole(8, -2.5f));
		        gameThread.add(new Hole(-8, -5.5f));
		        
		        gameThread.add(new Wall(1.5f, 9, 17, 2));
		        gameThread.add(new Wall(-1.5f, 0, 17, 2));
		        gameThread.add(new Wall(-1.5f, -8, 17, 2));
		        
		        gameThread.add(new Goal(-8, 13));

		        gameThread.add(new Ball(0, -13));
			}
		}
	};
}
