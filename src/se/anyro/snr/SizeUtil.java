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
public class SizeUtil {
	private static int halfWidth = 160;
	private static int halfHeight = 240;
	private static float scale = 16;
	
	public static void setScreenSize(int width, int height) {
		halfWidth = width / 2;
		halfHeight = height / 2;
		
		// Scale to maximum height/width depending on the ratio
    	if (width * 3 > height * 2) {
    		scale = (float) height / 30;
    	} else {
    		scale = (float) width / 20f;
    	}
	}
	
	public static int getScreenWidth() {
		return halfWidth * 2;
	}
	
	public static int getScreenHeight() {
		return halfHeight * 2;
	}
	
	public static void toScreen(float worldX, float worldY, Point screen) {
		screen.x = halfWidth + (int) (worldX * scale);
		screen.y = halfHeight - (int) (worldY * scale);
	}
	
	public static int toScreen(float worldLen) {
		return (int) (worldLen * scale);
	}

	public static float fromScreen(int screenLen) {
		return screenLen / scale;
	}
}
