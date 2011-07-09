/*
 * Copyright (C) 2010 Adam NybŠck
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

import android.graphics.Point;

/**
 * Methods for converting between the physics world coordinates and the screen coordinates.
 * Note! setScreenSize has to be called before the other methods are used.
 */
public final class SizeUtil {
	private static int sScreenWidth = 320;
	private static int sScreenHeight = 480;
	private static int sHalfWidth = sScreenWidth / 2;
	private static int sHalfHeight = sScreenHeight / 2;
	private static float sScale = 16;
	
	public static void setScreenSize(int width, int height) {
		sScreenWidth = width;
		sScreenHeight = height;
		
		sHalfWidth = width / 2;
		sHalfHeight = height / 2;
		
		// Scale to maximum height/width depending on the ratio
    	if (width * 3 > height * 2) {
    		sScale = (float) height / Physics.GAME_HEIGHT;
    	} else {
    		sScale = (float) width / Physics.GAME_WIDTH;
    	}
	}
	
	public static int getScreenWidth() {
		return sScreenWidth;
	}
	
	public static int getScreenHeight() {
		return sScreenHeight;
	}
	
	public static void toScreen(float worldX, float worldY, Point screen) {
		screen.x = sHalfWidth + (int) (worldX * sScale);
		screen.y = sHalfHeight - (int) (worldY * sScale);
	}
	
	public static int toScreen(float worldLen) {
		return (int) (worldLen * sScale);
	}

	public static float fromScreen(int screenLen) {
		return screenLen / sScale;
	}
}
