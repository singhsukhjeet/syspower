 /**************************************************************************
 * Copyright 2009 Chris Thompson                                           *
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
package org.spot.android;

public class Configuration {
	
	public static class GPS{
		
		public static long FREQUENCY = 500;
		public static float MIN_DISTANCE = 0F;//0f;
		public static float MIN_PRECISION = 20;
		public static boolean USE_SERVICE = false;
		
	}
	
	public static class Accelerometer{
		public enum Delay{
			UI, Normal, Game, Fastest
		}
		public static Delay RATE = Delay.Fastest;
		public static boolean USE_SERVICE = false;
	}
	
	public static class Collectors{
		public static String[] COLLECTORS = new String[] { "org.spot.android.collectors.AndroidPowerCollector" };
	}
	
	public static class Handlers{
		public static String[] HANDLERS = new String[] { "org.spot.android.event.handler.PowerEventHandler" };
	}
	
	public static class Recorders{
		public static String[] RECORDERS = new String[] { "org.spot.android.recorders.FileRecorder" };
	}

}
