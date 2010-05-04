 /**************************************************************************
 * Copyright 2010 Chris Thompson                                           *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/
package org.spot.android.recorders;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.spot.android.event.PowerEvent;
import org.spot.android.event.SpotEvent;

import android.util.Log;

public class FileRecorder extends RecordingStrategy {

	private static final String TAG = "FileRecorder";
	
	
	
	@Override
	public void record(SpotEvent app) {
		File out = new File("/sdcard/spot_output.csv");
		PowerEvent pe = (PowerEvent) app;
		if (!out.exists()){
			try {
				out.createNewFile();
				writeHeader(out);
			} catch (IOException e) {
				Log.e(TAG, "Could not create output file, aborting.");
				e.printStackTrace();
				return;
			}
		}
		
		FileWriter fw;
		try {
			fw = new FileWriter(out, true);
		} catch (IOException e) {
			Log.e(TAG, "Could not get FileWriter to output file, aborting.");
			e.printStackTrace();
			return;
		}
		
		BufferedWriter output = new BufferedWriter(fw);
		
		try{
			output.write(System.currentTimeMillis() + "," + pe.getName() + "," + pe.getValues()[0] + "," + pe.getCpuTime() + "," + pe.getGpsTime() + "\n");
			output.flush();
		}catch(IOException e){
			Log.e(TAG, "Could not write value " + pe.getName() + " to file, aborting.");
			e.printStackTrace();
			return;
		}
	}
	
	private void writeHeader(File out){
		
		FileWriter fw;
		try {
			fw = new FileWriter(out, true);
		} catch (IOException e) {
			Log.e(TAG, "Could not get FileWriter to output file, aborting.");
			e.printStackTrace();
			return;
		}
		
		BufferedWriter output = new BufferedWriter(fw);
		try {
			output.write("Time,Label,Power,CPUTime,GPSTime\n");
			output.flush();
		} catch (IOException e) {
			Log.e(TAG, "Could not write file header");
			e.printStackTrace();
		}
		
	}

}
