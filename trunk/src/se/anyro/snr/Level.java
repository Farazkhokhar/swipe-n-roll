package se.anyro.snr;

import se.anyro.snr.bodies.Ball;
import se.anyro.snr.bodies.Goal;
import se.anyro.snr.bodies.Hole;
import se.anyro.snr.bodies.SquareHole;
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
//		        gameThread.add(new EvilWall(-6f, -9, 6, 2));
		        		        
		        gameThread.add(new Hole(0, 0));
		        
		        gameThread.add(new Goal(-8, 13));

		        gameThread.add(new Ball(0, -13));
			}
		},
		new Level() {
			@Override
			public void setup(GameThread gameThread) {
				gameThread.add(new Wall(-2f, 5, 16, 2));
		        
		        gameThread.add(new SquareHole(-6.5f, -3, 7, 3));
//		        gameThread.add(new Bridge(-1.5f, -3, 3, 3.5f));
		        gameThread.add(new SquareHole(6, -3, 8, 3));
		        
		        gameThread.add(new Goal(-8, 13));

		        gameThread.add(new Ball(8, -13));
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
		},
		new Level() {
			@Override
			public void setup(GameThread gameThread) {
		        gameThread.add(new Hole(3.6f, 10));
		        gameThread.add(new Hole(5.8f, 10));
		        gameThread.add(new Hole(8, 10));
				gameThread.add(new Hole(-8, 3));
		        gameThread.add(new Hole(-5.8f, 3));
		        gameThread.add(new Hole(-3.6f, 3));
		        gameThread.add(new Hole(3.6f, -3));
		        gameThread.add(new Hole(5.8f, -3));
		        gameThread.add(new Hole(8, -3));
		        gameThread.add(new Hole(-3.6f, -10));
		        gameThread.add(new Hole(-5.8f, -10));
		        gameThread.add(new Hole(-8, -10));
		        
		        gameThread.add(new Wall(0, 11.3f, 3.2f, 6.8f));
		        gameThread.add(new Wall(0, 3.7f, 3.2f, 6.8f));
		        gameThread.add(new Wall(0, -3.7f, 3.2f, 6.8f));
		        gameThread.add(new Wall(0, -11.3f, 3.2f, 6.8f));
		        
		        gameThread.add(new Goal(8, 13));

		        gameThread.add(new Ball(-8, -13));
			}
		},
		new Level() {
			@Override
			public void setup(GameThread gameThread) {

		        gameThread.add(new Wall(0, 9.5f, 3, 11));
		        gameThread.add(new Wall(-6, 2, 8, 3));
		        gameThread.add(new Wall(6, 2, 8, 3));
		        gameThread.add(new Wall(0, -5.5f, 3, 11));
		        
				// Lower right
		        gameThread.add(new Hole(3.5f, -7.8f));
		        gameThread.add(new Hole(3.5f, -10));
		        gameThread.add(new Hole(5.7f, -10));
		        gameThread.add(new Hole(7.9f, -10));
		        
		        gameThread.add(new Hole(7, -1.5f));
		        gameThread.add(new Hole(7, -3.7f));
		        gameThread.add(new Hole(7, -5.9f));
		        
		        // Upper right
		        gameThread.add(new Hole(8, 7));
		        gameThread.add(new Hole(8, 13));
		        gameThread.add(new Hole(3.5f, 8));
		        gameThread.add(new Hole(3.5f, 13));
		        
		        // Upper left
		        gameThread.add(new Hole(-8, 7));
		        gameThread.add(new Hole(-8, 13));
		        gameThread.add(new Hole(-3.5f, 8));
		        gameThread.add(new Hole(-3.5f, 13));
		        
		        // Lower left
		        gameThread.add(new Hole(-7f, -1.5f));
		        gameThread.add(new Hole(-7f, -3.7f));
		        gameThread.add(new Hole(-7f, -5.9f));
		        gameThread.add(new Hole(-7f, -8.1f));
		        
		        gameThread.add(new Hole(-3.5f, -1.5f));
		        gameThread.add(new Hole(-3.5f, -3.7f));
		        
		        gameThread.add(new Hole(-4, -10.8f));
		        gameThread.add(new Hole(-4, -13));
		        
		        gameThread.add(new Goal(8, -13));
		        gameThread.add(new Ball(0, 0));
			}
		}
	};
}
