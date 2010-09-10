package se.anyro.snr;

import se.anyro.snr.bodies.Ball;
import se.anyro.snr.bodies.EvilWall;
import se.anyro.snr.bodies.Goal;
import se.anyro.snr.bodies.Hole;
import se.anyro.snr.bodies.Laser;
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
				gameThread.add(new Wall(1.5f, 6, 17, 3, "<< Swipe me! <<"));
		        gameThread.add(new Wall(-1.5f, -6, 17, 3, ">> Swipe me! >>"));
		        		        
		        gameThread.add(new Hole(-8, 3));
		        gameThread.add(new Hole(0, 0));
		        gameThread.add(new Hole(8, -3));
		        
		        gameThread.add(new Goal(-8, 13));

		        gameThread.add(new Ball(0, -13));
			}
		},
		new Level() {
			@Override
			public void setup(GameThread gameThread) {
				gameThread.add(new EvilWall(0, 7, 13, 3));
		        
		        gameThread.add(new SquareHole(-6.5f, -3, 7, 3));
		        gameThread.add(new SquareHole(6.5f, -3, 7, 3));
		        
		        gameThread.add(new Goal(0, 13));

		        gameThread.add(new Ball(8, -13));
			}
		},
		new Level() {
			@Override
			public void setup(GameThread gameThread) {
				
				gameThread.add(new SquareHole(6, 10, 7, 3));
				gameThread.add(new SquareHole(-6, 3, 7, 3));
				gameThread.add(new SquareHole(6, -3, 7, 3));
				gameThread.add(new SquareHole(-6, -10, 7, 3));

		        gameThread.add(new Wall(0, 11.6f, 3.2f, 6.8f));
		        gameThread.add(new Wall(0, 3.8f, 3.2f, 6.8f));
		        gameThread.add(new Wall(0, -3.8f, 3.2f, 6.8f));
		        gameThread.add(new Wall(0, -11.6f, 3.2f, 6.8f));
		        
		        gameThread.add(new Goal(8, 13));

		        gameThread.add(new Ball(-8, -13));
			}
		},
		new Level() {
			@Override
			public void setup(GameThread gameThread) {
		        gameThread.add(new Laser(7));
		        gameThread.add(new Laser(0));
		        gameThread.add(new Laser(-7));

				gameThread.add(new Wall(0f, 12, 3, 6));

		        gameThread.add(new Goal(8, 13));

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
		        gameThread.add(new EvilWall(-1.5f, -8, 17, 2));
		        
		        gameThread.add(new Goal(-8, 13));

		        gameThread.add(new Ball(0, -13));
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
		        gameThread.add(new Hole(3.5f, -7.6f));
		        gameThread.add(new Hole(3.5f, -10));
		        gameThread.add(new Hole(5.7f, -10));
		        gameThread.add(new Hole(7.9f, -10));
		        
		        gameThread.add(new SquareHole(7, -3, 2, 5));
		        
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
		        gameThread.add(new SquareHole(-5, -3, 5, 5));
		        gameThread.add(new Hole(-4, -13));
		        
		        gameThread.add(new Goal(8, -13));
		        gameThread.add(new Ball(0, 0));
			}
		}
	};
}
