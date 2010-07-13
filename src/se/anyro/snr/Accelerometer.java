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

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Simple class for accessing the x, y, z values of the accelerometer in a static way
 */
public final class Accelerometer {
    
	private static SensorManager sensorManager;
    private static Sensor sensor;
    private static boolean started = false;
    
    // Slightly off initial values for use with the emulator
    private static float x = 0.01f;
    private static float y = 9.81f;
    private static float z = 0.01f;
    
    private static SensorEventListener listener = new SensorEventListener() {
    	
    	public void onAccuracyChanged(Sensor sensor, int accuracy) {
    		// Part of the interface but not used here
    	}

    	public void onSensorChanged(SensorEvent event) {
    		x = event.values[0];
    		y = event.values[1];
    		z = event.values[2];
    	}
    };
    
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
    
	public static void start(SensorManager sensorManager) {
		if (started) {
			return;
		}
		if (sensor == null) {
	        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
	        if (sensors.size() == 0)
	        	throw new IllegalStateException("No accelerometer available");

	        sensor = sensors.get(0);
	        Accelerometer.sensorManager = sensorManager;
		}
    	sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);
		started = true;
	}
	
	public static void stop() {
		if (!started) {
			return;
		}
		started = false;
		
		sensorManager.unregisterListener(listener);
	}
}