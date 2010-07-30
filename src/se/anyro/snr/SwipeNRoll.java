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

import android.app.Activity;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SwipeNRoll extends Activity {
	
	// Main content views for the title page and the game
	private ViewGroup mTitleViewGroup;
	private ViewGroup mGameViewGroup;

	// Views contained in the game view group
	private GameView mGameView;
	private ViewGroup mMessageGroup;
	private TextView mBigText;
	private TextView mSmallText;

	private GameThread mGameThread;
	
	private boolean mStarted = false;

	// I know this is ugly. If you know how to fix it let me know.
	public static Resources resources;
	
	// Handler for receiving messages from the game thread in order to
	// display messages on top of the game view.
	private Handler mHandler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// Display messages on top of the game view
			switch (msg.what) {
			case GameThread.START:
				hideText();
				break;
			case GameThread.WIN:
				showText(R.string.well_done, R.string.next_level);
				break;
			case GameThread.COMPLETED:
				showText(R.string.congratulations, R.string.last_game);
				break;
			case GameThread.GAME_OVER:
				showText(R.string.game_over, R.string.restart_game);
				break;
			case GameThread.PAUSED:
				showText(R.string.paused, R.string.resume_game);
			}
			return true;
		}
	});

	private void hideText() {
		mMessageGroup.setVisibility(View.INVISIBLE);
	}
	
	private void showText(int bigTextId, int smallTextId) {
		mBigText.setText(bigTextId);
		mSmallText.setText(smallTextId);
		mMessageGroup.setVisibility(View.VISIBLE);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resources = getResources();
        
        makeFullScreen();
        
        mGameThread = new GameThread(mHandler);
        
        mTitleViewGroup = (ViewGroup) View.inflate(this, R.layout.title, null);
        mGameViewGroup = (ViewGroup) View.inflate(this, R.layout.game, null);

        mGameView = (GameView) mGameViewGroup.findViewById(R.id.game_view);
        mGameView.setGameThread(mGameThread);
        
        mMessageGroup = (ViewGroup) mGameViewGroup.findViewById(R.id.message_group);
        mMessageGroup.setVisibility(View.INVISIBLE);
        mBigText = (TextView) mMessageGroup.findViewById(R.id.big_text);
        mSmallText = (TextView) mMessageGroup.findViewById(R.id.small_text);

        setContentView(mTitleViewGroup);
    }

	private void makeFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	mGameThread.reset();
    }

    @Override
    protected void onResume() {
    	super.onResume();

    	mGameThread.resume(null);
    	
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Accelerometer.start(sensorManager);
        
//        Debug.startMethodTracing("balldemo");
    };
    
    @Override
    protected void onPause() {
//    	Debug.stopMethodTracing();
    	super.onPause();
    	mGameThread.pause();
    	Accelerometer.stop();
    }

    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	// Handle first touch where we switch from title view to game view
		if (mStarted == false && event.getAction() == MotionEvent.ACTION_DOWN) {
			mStarted = true;
			setContentView(mGameViewGroup);
		} else {
			mGameThread.addTouchEvent(event);
		}
		return true;
	}
}