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

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

/**
 * Accessing the x, y, z values of the accelerometer in a static way
 */
public final class Accelerometer {
    
	private static SensorManager sSensorManager;
    private static Sensor sAccSensor;
    private static boolean sStarted = false;
    
    // Slightly off initial values for use with the emulator
    private static float x = 0.01f;
    private static float y = 9.81f;
    private static float z = 0.01f;
    
    private static SensorEventListener listener0 = new SensorEventListener() {
    	
    	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    	public void onSensorChanged(SensorEvent event) {
    		x = event.values[0];
    		y = event.values[1];
    		z = event.values[2];
    	}
    };
    
    private static SensorEventListener listener90 = new SensorEventListener() {
        
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent event) {
            x = -event.values[1];
            y = event.values[0];
            z = event.values[2];
        }
    };
    
    private static SensorEventListener listener180 = new SensorEventListener() {
        
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent event) {
            x = -event.values[0];
            y = -event.values[1];
            z = event.values[2];
        }
    };
    
    private static SensorEventListener listener270 = new SensorEventListener() {
        
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent event) {
            x = event.values[1];
            y = -event.values[0];
            z = event.values[2];
        }
    };
    
    private static SensorEventListener listener = listener0;
    
	private Accelerometer() {
	}

    public static float getX() {
    	return x;
    }
    
    public static float getY() {
    	return y;
    }
    
    public static float getZ() {
    	return z;
    }
    
	public static void start(Context context) {
		if (sStarted) {
			return;
		}
		if (sAccSensor == null) {
		    
            sSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            List<Sensor> accSensors = sSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
            if (accSensors.size() == 0) {
                throw new IllegalStateException("No accelerometer available");
            }
            sAccSensor = accSensors.get(0);
            
	        // Adjust rotation for devices who's natural orientation isn't portrait
            // Using Display.getRotation() which is available since Froyo
	        if (Integer.parseInt(Build.VERSION.SDK) >= Build.VERSION_CODES.FROYO) {
	            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	            Display display = windowManager.getDefaultDisplay();
                switch (display.getRotation()) {
                case Surface.ROTATION_0:
                    listener = listener0;
                    break;
                case Surface.ROTATION_90:
                    listener = listener90;
                    break;
                case Surface.ROTATION_180:
                    listener = listener180;
                    break;
                case Surface.ROTATION_270:
                    listener = listener270;
                    break;
                }
	        }
		}
    	sSensorManager.registerListener(listener, sAccSensor, SensorManager.SENSOR_DELAY_GAME);
		sStarted = true;
	}
	
	public static void stop() {
		if (!sStarted) {
			return;
		}
		sStarted = false;
		
		sSensorManager.unregisterListener(listener);
	}
}