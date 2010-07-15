/*
 * Copyright (C) 2010 Adam Nybäck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.anyro.snr;

import java.util.ArrayList;

import se.anyro.snr.bodies.Body;
import se.anyro.snr.bodies.Goal;
import se.anyro.snr.bodies.Hole;
import se.anyro.snr.bodies.Wall;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;

public class GameThread implements Runnable, ContactListener {
	
	public static final int START = 0;
	public static final int WIN = 1;
	public static final int GAME_OVER = 2;
	public static final int COMPLETED = 3;
	public static final int PAUSED = 4;
	
	private GradientDrawable mBackground;
	private ArrayList<Body> mBodies = new ArrayList<Body>();
	private ArrayList<Body> mWalls= new ArrayList<Body>();
	private Body mSwipee;
	
	private SurfaceHolder mSurfaceHolder;
	private Handler mHandler;
	
	private Physics mPhysics;
	private Collision mCollision;

	private Thread mThread;
	// mThreadSuspended is increased each time we pause and decreased when we resume
    private volatile int mThreadSuspended = 1;
    private volatile Point mTouchEvent = null;
    private Point mTouchStart = new Point();
    private Point mTouchMove = new Point();
    private Object mTouchLock = new Object();
	
	private State mState = State.INIT;
	private int mLevel = 1;

	private enum State {
    	INIT,
    	RUNNING,
    	PAUSED,
    	WIN,
    	GAME_OVER,
    }
	
    private class Collision {

    	public Body ball, hole;

    	public Collision(Body ball, Body hole) {
    		this.ball = ball;
    		this.hole = hole;
    	}
    }
    
    public GameThread(Handler handler) {
    	mHandler = handler;
		
		mPhysics = new Physics(this);
    }
    
	public void start(SurfaceHolder surfaceHolder, int width, int height) {
    	mSurfaceHolder = surfaceHolder;
		if (mThread == null) {
			Level.setupLevel(this, mLevel);
			createBackground(width, height);
			mThread = new Thread(this, "GameThread");
			mThread.start();
		} else {
            synchronized(this) {
            	--mThreadSuspended;
            	notify();
            }
		}
	}

	private void createBackground(int width, int height) {
		// Adjust width/height to game ratio to avoid invisible walls
    	Rect bounds = new Rect(0, 0, width, height);
    	if (width * 3 > height * 2) {
    		bounds.left = (width - height * 2 / 3) / 2;
    		bounds.right -= bounds.left;
    	} else if (width * 3 < height * 2) {
    		bounds.top = (height - width * 3 / 2) / 2;
			bounds.bottom -= bounds.top;
    	}

		mBackground = new GradientDrawable(Orientation.TL_BR, new int[] {0xff777766, 0xff556666, 0xff777766});
		mBackground.setShape(GradientDrawable.RECTANGLE);
		mBackground.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		mBackground.setSize(width, height);
		mBackground.setBounds(bounds);
	}

	public void pause() {
//        synchronized(this) {
        	++mThreadSuspended;
//        }
	}

	public void resume(SurfaceHolder holder) {
		if (holder != null) {
			mSurfaceHolder = holder;
		}
        synchronized(this) {
        	--mThreadSuspended;
        	if (mThreadSuspended == 0)
        		notify();
        }
    }

	public void reset() {
		for (Body body : mBodies)
			body.destroy();
		mBodies.clear();
		mWalls.clear();
	}
    
	public void add(Body body) {
		mBodies.add(body);
		
		if (body instanceof Wall)
			mWalls.add(body);
	}

	public void addTouchEvent(MotionEvent event) {
		synchronized (mTouchLock) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mTouchStart.x = (int) event.getX();
				mTouchStart.y = (int) event.getY();
				mTouchEvent = mTouchStart;
				break;
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_UP:
				// Make sure we don't overwrite a touch start
				// before it's consumed by this thread
				if (mTouchEvent == mTouchStart)
					return;
				mTouchMove.x = (int) event.getX();
				mTouchMove.y = (int) event.getY();
				mTouchEvent = mTouchMove;
			}
		}
	}
	
	private void resetGame() {
		reset();
		Level.setupLevel(this, mLevel);
	}

	@Override
	public void run() {
		mState = State.RUNNING;
		while (true) {
			synchronized (this) {
				while (mThreadSuspended > 0) {
			    	if (mState == State.RUNNING) {
			    		mState = State.PAUSED;
			    		mHandler.sendEmptyMessage(PAUSED);
			    	}
					try {
						wait();
					} catch (InterruptedException e) {
					}
				}
				Canvas canvas = mSurfaceHolder.lockCanvas();
				if (canvas != null) {
					try {
						updateInput();
						if (mState == State.RUNNING) {
							updatePhysics();
						}
						// updateAnimations();
						// updateSound();
						updateGraphics(canvas);
					} finally {
						// do this in a finally so that if an exception is thrown
						// during the above, we don't leave the Surface in an
						// inconsistent state
						if (canvas != null) {
							mSurfaceHolder.unlockCanvasAndPost(canvas);
						}
					}
				}
			}
        }
	}

	/**
	 * Take care of the touch events received from the main thread
	 */
	private void updateInput() {
		if (mTouchEvent != null) {
			synchronized (mTouchLock) {
				if (mTouchEvent == mTouchStart) {
					touchStart();
				} else {
					touchMove();
				}
				mTouchEvent = null;
			}
		}
	}

	private void touchStart() {
		
		mSwipee = null;
		
		// Resume/restart game if not already running
		if (mState != State.RUNNING) {
			if (mState != State.PAUSED)
				resetGame();
			mState = State.RUNNING;
			mHandler.sendEmptyMessage(START);
			return;
		}

		int x = mTouchStart.x;
		int y = mTouchStart.y;
		
		// Check if a swipeable body is being touched
		for (Body wall : mWalls) {
			if (wall.contains(x, y)) {
				mSwipee = wall;
				return;
			}
		}
	}
	
	private void touchMove() {
		
		if (mSwipee == null)
			return;
			
		// User moved finger to a new position or stopped touching the screen
		// Calculate how far the finger moved
		float diffX = SizeUtil.fromScreen(mTouchMove.x - mTouchStart.x);
		
		// Move one of the walls
		Vector2 pos = mSwipee.getPosition();
		pos.x += diffX;
		if (pos.x < -1.5f)
			pos.x = -1.5f;
		else if (pos.x > 1.5f)
			pos.x = 1.5f;
		mSwipee.setPosition(pos);
		
		// Remember the new position
		mTouchStart.x = mTouchMove.x;
		mTouchStart.y = mTouchMove.y;
	}

	private void updatePhysics() {
		
		mPhysics.update();
		
		if (mCollision != null) {
			
			// Make ball go inte the hole
			mCollision.ball.setPosition(mCollision.hole.getPosition());
			
			if (mCollision.hole instanceof Goal) {
				mState = State.WIN;
				if (mLevel < Level.count()) {
					++mLevel; 
					mHandler.sendEmptyMessage(WIN);
				} else {
					mLevel = 1;
					mHandler.sendEmptyMessage(COMPLETED);
				}
			} else {
				mState = State.GAME_OVER;
				mHandler.sendEmptyMessage(GAME_OVER);
			}
			mCollision = null;
		}
	}

	private void updateGraphics(Canvas canvas) {
		
		// Draw background
		mBackground.draw(canvas);
		
		// Draw the bodies
		for (Body body : mBodies) {
			body.draw(canvas);
		}
	}

	// Collision between two bodies started
	@Override
	public void beginContact(Contact contact) {
		Body body1 = (Body) contact.getFixtureA().getBody().getUserData();
		Body body2 = (Body) contact.getFixtureB().getBody().getUserData();

		// For now we only care about collision with holes and the goal
		if (body1 instanceof Hole || body1 instanceof Goal) {
			mCollision = new Collision(body2, body1);
		} else if (body2 instanceof Hole || body2 instanceof Goal) {
			mCollision = new Collision(body1, body2);
		}
	}

	// Collision ended
	@Override
	public void endContact(Contact contact) {
	}
}
