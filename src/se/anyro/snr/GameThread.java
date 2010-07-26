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
import se.anyro.snr.bodies.Bridge;
import se.anyro.snr.bodies.Circle;
import se.anyro.snr.bodies.Goal;
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
	private ArrayList<Wall> mWalls= new ArrayList<Wall>();
	private Wall mSwipee;
	
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
    private Point mTouchEnd = new Point();
    private Object mTouchLock = new Object();
    
	private float mLeftLimit = -10;
	private float mRightLimit = 10;
	private float mTopLimit = 15;
	private float mBottomLimit = -15;
	
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

    	public Body ball, collider;

    	public Collision(Body ball, Body collider) {
    		this.ball = ball;
    		this.collider = collider;
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
			mWalls.add((Wall) body);
	}

	public void addTouchEvent(MotionEvent event) {
		synchronized (mTouchLock) {
			// Make sure we don't overwrite a touch start
			// before it's consumed by this thread
			if (mTouchEvent == mTouchStart)
				return;

			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				mTouchEvent = mTouchMove;
				break;
			case MotionEvent.ACTION_DOWN:
				mTouchEvent = mTouchStart;
				break;
			case MotionEvent.ACTION_UP:
				mTouchEvent = mTouchEnd;
				break;
			}
			mTouchEvent.x = (int) event.getX();
			mTouchEvent.y = (int) event.getY();
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
				if (mTouchEvent == mTouchMove) {
					touchMove();
				} else if (mTouchEvent == mTouchStart) {
					touchStart();
				} else {
					touchEnd();
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
		for (Wall wall : mWalls) {
			if (wall.contains(x, y)) {
				mSwipee = wall;
				mSwipee.onTouchStart();
				switch (mSwipee.getOrientation()) {
				case HORIZONTAL:
					calculateHorizontalSwipeLimits();
					break;
				case VERTICAL:
					calculateVerticalSwipeLimits();
					break;
				}
				return;
			}
		}
	}
	
	private void calculateHorizontalSwipeLimits() {
		float halfWidth = mSwipee.getWidth() / 2f;
		mLeftLimit = -10 + halfWidth;
		mRightLimit = 10 - halfWidth;
		
		Rect swipeeBounds = mSwipee.getScreenBounds();
		
		for (Wall wall : mWalls) {
			if (wall == mSwipee)
				continue;

			Rect bounds = wall.getScreenBounds();
			
			// Check vertical intersection
			if (bounds.top < swipeeBounds.bottom && bounds.bottom > swipeeBounds.top) {
				if (bounds.left > swipeeBounds.right) {
					// Calculate new right limit
					int diff = bounds.left - swipeeBounds.right;
					float newRightLimit = mSwipee.getPosition().x + SizeUtil.fromScreen(diff - 1);
					if (newRightLimit < mRightLimit)
						mRightLimit = newRightLimit;
				} else if (bounds.right < swipeeBounds.left) {
					// Calculate new left limit
					int diff = swipeeBounds.left - bounds.right;
					float newLeftLimit = mSwipee.getPosition().x - SizeUtil.fromScreen(diff - 1);
					if (newLeftLimit > mLeftLimit)
						mLeftLimit = newLeftLimit;
				}
			}
		}
	}

	private void calculateVerticalSwipeLimits() {
		float halfHeight = mSwipee.getHeight() / 2f;
		mTopLimit = 15 - halfHeight;
		mBottomLimit = -15 + halfHeight;
		
		Rect swipeeBounds = mSwipee.getScreenBounds();
		
		for (Wall wall : mWalls) {
			if (wall == mSwipee)
				continue;

			Rect bounds = wall.getScreenBounds();
			
			// Check Horizontal intersection
			if (bounds.left < swipeeBounds.right && bounds.right > swipeeBounds.left) {
				if (bounds.top > swipeeBounds.bottom) {
					// Calculate new bottom limit
					int diff = bounds.top - swipeeBounds.bottom;
					float newBottomLimit = mSwipee.getPosition().y - SizeUtil.fromScreen(diff - 1);
					if (newBottomLimit > mBottomLimit)
						mBottomLimit = newBottomLimit;
				} else if (bounds.bottom < swipeeBounds.top) {
					// Calculate new top limit
					int diff = swipeeBounds.top - bounds.bottom;
					float newtopLimit = mSwipee.getPosition().y + SizeUtil.fromScreen(diff - 1);
					if (newtopLimit < mTopLimit)
						mTopLimit = newtopLimit;
				}
			}
		}
	}

	private void touchMove() {
		
		if (mSwipee == null)
			return;
			
		switch (mSwipee.getOrientation()) {
		case HORIZONTAL:
			moveHorizontal();
			break;
		case VERTICAL:
			moveVertical();
			break;
		}
	}

	private void moveHorizontal() {
		// User moved finger to a new position
		// Calculate how far the finger moved
		float diffX = SizeUtil.fromScreen(mTouchMove.x - mTouchStart.x);
		
		// Move one of the walls
		Vector2 pos = mSwipee.getPosition();
		pos.x += diffX;
		if (pos.x < mLeftLimit)
			pos.x = mLeftLimit;
		else if (pos.x > mRightLimit)
			pos.x = mRightLimit;
		mSwipee.setPosition(pos);
		
		// Remember the new position
		mTouchStart.x = mTouchMove.x;
	}
	
	private void moveVertical() {
		// User moved finger to a new position
		// Calculate how far the finger moved
		float diffY = SizeUtil.fromScreen(mTouchMove.y - mTouchStart.y);
		
		// Move one of the walls
		Vector2 pos = mSwipee.getPosition();
		pos.y -= diffY;
		if (pos.y > mTopLimit)
			pos.y = mTopLimit;
		else if (pos.y < mBottomLimit)
			pos.y = mBottomLimit;
		mSwipee.setPosition(pos);
		
		// Remember the new position
		mTouchStart.y = mTouchMove.y;
	}
	
	private void touchEnd() {
		if (mSwipee == null)
			return;
		
		mSwipee.onTouchEnd();
		
		mSwipee = null;
	}
	
	private void updatePhysics() {
		
		mPhysics.update();
		
		if (mCollision != null) {
			
			if (mCollision.collider instanceof Circle) {
				mCollision.ball.setPosition(mCollision.collider.getPosition());
			}
			
			if (mCollision.collider instanceof Bridge) {
				Bridge bridge = (Bridge) mCollision.collider;
				bridge.collision(mCollision.ball);
			} else if (mCollision.collider instanceof Goal) {
				mState = State.WIN;
				if (mLevel < Level.count()) {
					++mLevel; 
					mHandler.sendEmptyMessage(WIN);
				} else {
					mLevel = 1;
					mHandler.sendEmptyMessage(COMPLETED);
				}
				mCollision = null;
			} else {
				mState = State.GAME_OVER;
				mHandler.sendEmptyMessage(GAME_OVER);
				mCollision = null;
			}
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
		if (body1 == null)
			return;
		
		Body body2 = (Body) contact.getFixtureB().getBody().getUserData();
		if (body2 == null)
			return;
		
		if (body1.isCollider()) {
			mCollision = new Collision(body2, body1);
		} else if (body2.isCollider()) {
			mCollision = new Collision(body1, body2);
		}
	}

	// Collision ended
	@Override
	public void endContact(Contact contact) {
		Body body1 = (Body) contact.getFixtureA().getBody().getUserData();
		if (body1 == null)
			return;
		
		Body body2 = (Body) contact.getFixtureB().getBody().getUserData();
		if (body2 == null)
			return;
		
		if (body1.isCollider() || body2.isCollider()) {
			mCollision = null;
		}
	}
}
