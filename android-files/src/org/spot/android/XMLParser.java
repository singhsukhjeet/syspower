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

package org.spot.android;

import java.util.Stack;

import org.spot.android.xml.handlers.AccelerometerElementHandler;
import org.spot.android.xml.handlers.ElementHandler;
import org.spot.android.xml.handlers.GPSElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser extends DefaultHandler {

	private Stack<ElementHandler> handlers_ = new Stack<ElementHandler>();

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		//TODO: There has to be a better way to do this
		if(localName.equalsIgnoreCase("gps")){
			ElementHandler h = new GPSElementHandler();
			h.handleAttributes(atts);
			handlers_.push(h);
		}else if (localName.equalsIgnoreCase("accelerometer")){
			ElementHandler h = new AccelerometerElementHandler();
			h.handleAttributes(atts);
			handlers_.push(h);
		}

	}

	@Override
	public void characters(char ch[], int start, int length) {
		handlers_.peek().handleInnerText(ch);
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
	throws SAXException {
		handlers_.pop().handleEndOfElement();
	}

}
