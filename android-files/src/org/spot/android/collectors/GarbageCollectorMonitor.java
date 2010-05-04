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
package org.spot.android.collectors;

import java.lang.ref.WeakReference;

import android.os.SystemClock;
import android.util.Log;

public class GarbageCollectorMonitor {
	
	private WeakReference<GcWatcher> gcWatcher_ = new WeakReference<GcWatcher>(new GcWatcher());
	private long lastGcTime;
	private static final String TAG = "GarbageCollectorMonitor";
	
	private class GcWatcher{
		@Override
		protected void finalize() throws Throwable{
			Log.d(TAG, "GC Running...");
			lastGcTime = SystemClock.uptimeMillis();
			gcWatcher_ = new WeakReference<GcWatcher>(new GcWatcher());
		}
		
		//TODO:Implement recorder queue so recoding passes won't cause gc to run forever
	}

}
