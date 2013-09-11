/*******************************************************************************
 * Copyright 2013 Alexandros Schillings
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package co.uk.alt236.reflectivedrawableloader;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

class ReflectionUtils {
	private final String TAG = getClass().getName();
	protected final static String RESOURCE_LOCATION_DRAWABLES = ".R.drawable";

	private final String mPackageName;
	private final Map<String, Class<?>> mClassCache;

	protected ReflectionUtils(String appPackageName) {
		Log.d(TAG, "New ReflectionUtils() for '" + appPackageName + "'");
		mPackageName = appPackageName;
		mClassCache = new HashMap<String, Class<?>>();
	}

	private Class<?> getResourceClass(final String suffix) {
		if(mClassCache.containsKey(suffix)){
			return mClassCache.get(suffix);
		} else {
			try {
				final Class<?> rClassBase = Class.forName(mPackageName + ".R");
				final Class<?>[] subClassTable = rClassBase.getDeclaredClasses();

				for (Class<?> subClass : subClassTable) {
					if (subClass.getCanonicalName().endsWith(suffix)) {
						mClassCache.put(suffix, subClass);
						return subClass;
					}
				}

			} catch (ClassNotFoundException e) {
				Log.e(TAG, "getResourceClass() ClassNotFoundException: " + e.getMessage(), e);
			}

			Log.e(TAG, "getResourceClass() Unable to find Sublass: " + suffix);

			return null;
		}
	}

	public void logFields(String resourceLocation) {
		Log.d(TAG, "logFields() Getting Fields for '" + resourceLocation + "' ============= ");

		try {
			final Field[] fields = getResourceClass(resourceLocation).getFields();
			for (Field field : fields) {
				Log.d(TAG, "logFields() Field: '" + field.getName() + "'");
			}
		} catch (NullPointerException e) {}
	}

	public void logSubClasses(String baseClass) {
		Log.d(TAG, "logSubClasses() Getting subclasses for '" + baseClass + "' ============= ");

		try {
			final Class<?> rClass = Class.forName(baseClass);
			final Class<?>[] subClassTable = rClass.getDeclaredClasses();
			
			for (final Class<?> subclass : subClassTable) {
				Log.d(TAG, "logSubClasses() Class: " + subclass.getCanonicalName());
			}

		} catch (Exception e) {
			Log.e(TAG, "logSubClasses() Error: " + e.getMessage(), e);
		}
	}

	protected int reflectDrawable(String fieldName, int defaultValue, boolean reportFailure) {
		return reflectResource(RESOURCE_LOCATION_DRAWABLES, fieldName, defaultValue, reportFailure);
	}

	private int reflectResource(String resourceLocation, String fieldName, int defaultValue, boolean reportFailure) {
		int error = 0;
		try {
			final Field field = getResourceClass(resourceLocation).getField(fieldName);
			return field.getInt(null);
		} catch (NoSuchFieldException e) {
			error = 1;
		} catch (IllegalAccessException e) {
			error = 2;
		} catch (NullPointerException e) {
			error = 3;
		}

		if (reportFailure) {
			Log.w(TAG, "reflectResource() Resource '" + fieldName + "' not available! (" + error +")");
		}

		return defaultValue;
	}
}
