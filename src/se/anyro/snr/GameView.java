/*
 * Copyright (C) 2010 Adam Nyb�ck
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

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	private GameThread mGameThread;
	
	private boolean mStarted = false;
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setKeepScreenOn(true);
        
		// register our interest in hearing about changes to our surface
		getHolder().addCallback(this);
	}

	public void setGameThread(GameThread gameThread) {
		mGameThread = gameThread;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		SizeUtil.setScreenSize(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mStarted) {
			mGameThread.resume(holder);
			return;
		}
        mStarted = true;
		
		Rect frame = holder.getSurfaceFrame();
		SizeUtil.setScreenSize(frame.width(), frame.height());
				
        mGameThread.start(holder, frame.width(), frame.height());
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        mGameThread.pause();
	}
}
