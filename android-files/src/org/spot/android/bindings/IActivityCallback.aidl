package org.spot.android.bindings;

interface IActivityCallback{

	void accelerometerChanged(in float x, in float y, in float z);
	
	void gpsChanged(in double lat, in double lon);
}