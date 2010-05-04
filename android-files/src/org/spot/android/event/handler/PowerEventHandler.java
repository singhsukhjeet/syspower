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
package org.spot.android.event.handler;

import java.util.ArrayList;

import org.spot.android.Configuration;
import org.spot.android.collectors.PowerCollector;
import org.spot.android.recorders.RecordingStrategy;

import android.util.Log;

public class PowerEventHandler implements EventHandler {

	private static final String TAG = "PowerEventHandler";

	ArrayList<PowerCollector> pia_ = new ArrayList<PowerCollector>();

	public PowerEventHandler(){
		for (String clazz: Configuration.Collectors.COLLECTORS){
			try{
				pia_.add((PowerCollector)Class.forName(clazz).newInstance());
			}catch (Exception e) {
				Log.e(TAG, "Unable to instantiate PowerCollector " + clazz);
				e.printStackTrace();
			}
		}

		for (String cls:Configuration.Recorders.RECORDERS){
			
			RecordingStrategy rs;
			try{
				rs = (RecordingStrategy)Class.forName(cls).newInstance();
			}catch(Exception e){
				Log.e(TAG, "Could not instantiate " + cls);
				e.printStackTrace();
				continue;
			}
			for (PowerCollector pc: pia_){
				pc.addRecorder(rs);
			}
		}

	}

	@Override
	public void handleEvent(String label) {
		for (PowerCollector c: pia_){
			c.processApplicationUsage(label);
		}
	}

}
