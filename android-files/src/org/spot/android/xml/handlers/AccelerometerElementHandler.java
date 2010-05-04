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

package org.spot.android.xml.handlers;

import org.spot.android.Configuration;
import org.spot.android.Configuration.Accelerometer.Delay;
import org.xml.sax.Attributes;

public class AccelerometerElementHandler extends ElementHandler {

	
	@Override
	public void handleAttributes(Attributes attrs){
		Delay temp;
		String val = attrs.getValue("delay");
		if(val.equalsIgnoreCase("ui"))
			temp = Delay.UI;
		else if(val.equalsIgnoreCase("normal"))
			temp = Delay.Normal;
		else if(val.equalsIgnoreCase("game"))
			temp = Delay.Game;
		else if(val.equalsIgnoreCase("fastest"))
			temp = Delay.Fastest;
		else
			temp = Delay.Normal;
		
		
		Configuration.Accelerometer.RATE = temp;
	}
}
