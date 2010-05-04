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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.util.Log;

public class HunchTester extends PreferenceActivity {

	private static final String BATTERY_STATS_IMPL_CLASS = "com.android.internal.os.BatteryStatsImpl";
	private static final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";
	private static final String M_BATTERY_INFO_CLASS = "com.android.internal.app.IBatteryStats";


	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.power_usage_summary);

		try {
			Context foreignContext = createPackageContext("com.android.settings", Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);


			Class<?> c = foreignContext.getClassLoader().loadClass("com.android.settings.fuelgauge.PowerUsageSummary");
			Object inst = c.newInstance();
			Log.i("HunchTester", c.getName());
//			for(Method m:c.getDeclaredMethods()){
//				Log.i("HunchTester", m.getName());
//				for(Class cl: m.getParameterTypes()){
//					Log.i("HunchTester",cl.getName());
//				}
//			}

			simulateOnCreate(inst);
			Method reloadStats = c.getDeclaredMethod("refreshStats", (Class[])null);
			reloadStats.setAccessible(true);
			reloadStats.invoke(inst);
			reloadStats.setAccessible(false);

			List sippers;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			e.getCause().printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//		try {

		//			
		//			
		//			
		//		} catch (IllegalAccessException e) {
		//			e.printStackTrace();
		//		} catch (InstantiationException e) {
		//			e.printStackTrace();
		//		} catch (ClassNotFoundException e) {
		//			e.printStackTrace();
		//		} catch (SecurityException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (NoSuchMethodException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (IllegalArgumentException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (InvocationTargetException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}

	}


	/**
	 * This method simulates the onCreate call that would normally be called during
	 * the first stages of the activity lifecycle. Since we aren't starting it normally,
	 * we have to simulate that call and initialize any necessary data members
	 * @param obj
	 */
	private void simulateOnCreate(Object obj){
		
		try{
			//Set the mBatteryInfo field on the object
			Field battInfo = obj.getClass().getDeclaredField("mBatteryInfo");
			
			battInfo.setAccessible(true);
			battInfo.set(obj, Class.forName("com.android.internal.app.IBatteryStats$Stub").getDeclaredMethod("asInterface", 
					IBinder.class).invoke(null, Class.forName("android.os.ServiceManager")
							.getMethod("getService", String.class).invoke(null, "batteryinfo")));
			battInfo.setAccessible(false);
			
			//Set the mPowerProfile field
			Field powerProfile = obj.getClass().getDeclaredField("mPowerProfile");
			
			powerProfile.setAccessible(true);
			powerProfile.set(obj, 
					Class.forName(POWER_PROFILE_CLASS).getConstructor(Context.class).newInstance(this));
			powerProfile.setAccessible(false);
			
			//Set the mAppListGroup field
			Field appList = obj.getClass().getDeclaredField("mAppListGroup");
			
//			Field prefManager = Class.forName("android.preference.PreferenceActivity").getDeclaredField("mPreferenceManager");
//			prefManager.setAccessible(true);
//			Method prefManagerCreate =  Class.forName("android.preference.PreferenceActivity").getDeclaredMethod("onCreatePreferenceManager", (Class[])null);
//			prefManagerCreate.setAccessible(true);
//			prefManager.set(obj, prefManagerCreate.invoke(obj));
//			prefManagerCreate.setAccessible(false);
//			prefManager.setAccessible(false);
			
			PreferenceGroup pg = (PreferenceGroup)findPreference("app_list");
			appList.setAccessible(true);
			appList.set(obj, pg);
			appList.setAccessible(false);
			
		}catch(NoSuchFieldException ex){
			ex.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
