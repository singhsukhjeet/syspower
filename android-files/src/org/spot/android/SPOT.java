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

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.spot.android.event.handler.EventHandler;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.widget.TextView;

public class SPOT extends Activity {

	private ArrayList<EventHandler> listeners_ = new ArrayList<EventHandler>();

	private static final String TAG = "SPOT";

	private static SPOT instance_;

	private LocationListener ll_ = new MyLocationListener();
	private boolean locationRegistered_ = false;
	private SensorEventListener sel_ = new MySensorListener();
	private boolean sensorRegistered_ = false;
	private PowerManager.WakeLock wl_ = null;

	private static final int START_GPS = 0;
	private static final int STOP_GPS = 1;
	private static final int PERIODIC_UPDATE = 2;
	private static final int START_ACC = 3;
	private static final int STOP_ACC = 4;




	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		instance_ = this;


		for (String clazz: Configuration.Handlers.HANDLERS){
			try{
				listeners_.add((EventHandler)Class.forName(clazz).newInstance());
			}catch (Exception e) {
				Log.e(TAG, "Unable to instantiate EventHandler " + clazz);
				e.printStackTrace();
			}
		}


		//        Intent recIntent = new Intent(this, RecordingService.class);
		//        startService(recIntent);
		//        bindService(recIntent, recordingService, BIND_AUTO_CREATE);

		//        XMLParser p = new XMLParser();

		//parse the configuration file
		//        try {
		//			XMLReader xr = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
		//			xr.setContentHandler(p);
		//			xr.parse(new InputSource(new FileInputStream(new File("/sdcard/spotconf.xml"))));
		//		} catch (SAXException e) {
		//			e.printStackTrace();
		//		} catch (ParserConfigurationException e) {
		//			e.printStackTrace();
		//		} catch (FactoryConfigurationError e) {
		//			e.printStackTrace();
		//		} catch (FileNotFoundException e) {
		//			e.printStackTrace();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}

		((TextView)findViewById(R.id.acc_active)).setText("False");
		((TextView)findViewById(R.id.gps_active)).setText("False");

		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				handler_.sendMessage(Message.obtain(handler_, PERIODIC_UPDATE));
			}

		}, 10000, 10000);
		((TextView)findViewById(R.id.acc_service_value)).setText("No");
		((TextView)findViewById(R.id.gps_service_value)).setText("No");
		//		Timer t2 = new Timer();
		//		t2.scheduleAtFixedRate(new TimerTask(){
		//
		//			@Override
		//			public void run() {
		//				Log.i(TAG, "Random toggle...");
		//
		//				Random rnd = new Random();
		//				if (rnd.nextBoolean()){
		//					//toggle gps
		//					Log.i(TAG, "Toggling gps");
		//					if (locationRegistered_){
		//						handler_.sendMessage(Message.obtain(handler_, STOP_GPS));
		//					}else{
		//						handler_.sendMessage(Message.obtain(handler_, START_GPS));
		//					}
		//				}else{
		//					Log.i(TAG, "GPS not toggled");
		//				}
		//
		//				if (rnd.nextBoolean()){
		//					//toggle accel
		//					Log.i(TAG, "Toggling accel");
		//					if (sensorRegistered_){
		//						handler_.sendMessage(Message.obtain(handler_, STOP_ACC));
		//					}else{
		//						handler_.sendMessage(Message.obtain(handler_, START_ACC));
		//					}
		//
		//				}else{
		//					Log.i(TAG, "Accel not toggled");
		//				}
		//
		//
		//			}
		//
		//		}, 5000, 20000);
		Timer t2 = new Timer();
		t2.schedule(new TimerTask(){

			@Override
			public void run(){
				handler_.sendMessage(Message.obtain(handler_, START_GPS));
				new Timer().schedule(new TimerTask(){

					@Override
					public void run() {
						handler_.sendMessage(Message.obtain(handler_, STOP_GPS));
					}
					
				}, 300000);
				
//				new Timer().schedule(new TimerTask(){
//					
//					@Override
//					public void run(){
//						handler_.sendMessage(Message.obtain(handler_, START_ACC));
//						new Timer().schedule(new TimerTask(){
//							
//							@Override
//							public void run(){
//								handler_.sendMessage(Message.obtain(handler_, STOP_ACC));
//							}
//						}, 300000);
//					}
//				}, 150000);
			}

		}, 120000);



	}

	@Override
	protected void onResume(){
		super.onResume();
		if (wl_ == null){
			wl_ = ((PowerManager)getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
		}
		wl_.acquire();
	}

	@Override
	protected void onPause(){
		super.onPause();
		wl_.release();
	}
	public static ServiceConnection recordingService = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {


		}

		public void onServiceDisconnected(ComponentName className) {


		}
	};

	public static ServiceConnection gpsService = new ServiceConnection(){
		public void onServiceConnected(ComponentName className, IBinder service) {


		}

		public void onServiceDisconnected(ComponentName className) {


		}
	};

	public static ServiceConnection accService = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
		}

	};

	private void notifyListeners(String event){
		for(EventHandler pc: listeners_){
			pc.handleEvent(event);
		}
	}

	public static SPOT getInstance(){
		return instance_;
	}

	private void startSensors(){

	}

	private class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location arg0) {
			((TextView)findViewById(R.id.gps_value)).setText(arg0.getLongitude() + ", " +arg0.getLatitude());
		}

		@Override
		public void onProviderDisabled(String arg0) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status,
				Bundle extras) {
		}

	}

	private class MySensorListener implements SensorEventListener{
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			((TextView)findViewById(R.id.acc_value))
			.setText("X: " + event.values[0] + "; Y: " 
					+ event.values[1] + "; Z: " + event.values[2]);
		}
	}

	private Handler handler_ = new Handler(){
		@Override
		public void handleMessage(Message msg){
			SensorManager manager;
			Sensor acc;
			switch(msg.what){

			case START_GPS:
				Log.i(TAG, "Requesting location updates");
				LocationManager lm = ((LocationManager)getSystemService(Context.LOCATION_SERVICE));
				lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, Configuration.GPS.FREQUENCY, 
						Configuration.GPS.MIN_DISTANCE, ll_);
				notifyListeners("GPS Started");
				Log.i(TAG, "Requesting location updates");
				locationRegistered_ = true;
				((TextView)findViewById(R.id.gps_active)).setText("True");
				break;
			case STOP_GPS:
				((LocationManager)getSystemService(Context.LOCATION_SERVICE)).removeUpdates(ll_);
				notifyListeners("GPS Stopped");
				locationRegistered_ = false;
				((TextView)findViewById(R.id.gps_active)).setText("False");
				break;
			case PERIODIC_UPDATE:
				notifyListeners("Periodic Update");
				break;
			case START_ACC:
				manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
				acc = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
				int delay;
				if (Configuration.Accelerometer.RATE == Configuration.Accelerometer.Delay.UI)
					delay = SensorManager.SENSOR_DELAY_UI;
				else if (Configuration.Accelerometer.RATE == Configuration.Accelerometer.Delay.Game)
					delay = SensorManager.SENSOR_DELAY_GAME;
				else if (Configuration.Accelerometer.RATE == Configuration.Accelerometer.Delay.Fastest)
					delay = SensorManager.SENSOR_DELAY_FASTEST;
				else
					delay = SensorManager.SENSOR_DELAY_NORMAL;

				manager.registerListener(sel_, acc, delay);
				notifyListeners("Accelerometer Started");
				sensorRegistered_ = true;
				((TextView)findViewById(R.id.acc_active)).setText("True");
				break;
			case STOP_ACC:
				manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
				acc = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
				manager.unregisterListener(sel_);
				notifyListeners("Accelerometer Stopped");
				sensorRegistered_ = false;
				((TextView)findViewById(R.id.acc_active)).setText("False");
				break;

			}
		}

	};
}