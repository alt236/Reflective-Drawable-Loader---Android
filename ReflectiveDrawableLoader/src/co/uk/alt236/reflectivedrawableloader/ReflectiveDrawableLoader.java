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

import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import co.uk.alt236.reflectivedrawableloader.containers.DrawableResourceContainer;
import co.uk.alt236.reflectivedrawableloader.containers.LruLinkedHashMap;

public final class ReflectiveDrawableLoader {
	public static final String ICON_PREFIX_BASE = "ic_";
	public static final String ICON_PREFIX_LAUNCHER = ICON_PREFIX_BASE + "launcher_";
	public static final String ICON_PREFIX_MENU = ICON_PREFIX_BASE + "menu_";
	public static final String ICON_PREFIX_STATUS_BAR = ICON_PREFIX_BASE + "stat_notify_";
	public static final String ICON_PREFIX_TAB = ICON_PREFIX_BASE + "tab_";
	public static final String ICON_PREFIX_DIALOG = ICON_PREFIX_BASE + "dialog_";
	public static final String ICON_PREFIX_LIST = ICON_PREFIX_BASE + "list_";

	private static final int CACHE_SIZE = 100;
	private static final boolean TIME_LOGGING_ENABLED = false;

	private static ReflectiveDrawableLoader instance = null;

	private final String TAG = getClass().getName();
	private final AtomicBoolean mAddDrawableNameToContainer;
	private final AtomicBoolean mLogErrors;
	private final ReflectionUtils mReflectionUtils;
	private final LruLinkedHashMap<String, Integer> mCache;
	private final LruLinkedHashMap<String, Object> mCacheMisses;

	private ReflectiveDrawableLoader() {
		// We should never be here...
		mReflectionUtils = null;
		mCache = null;
		mAddDrawableNameToContainer = null;
		mLogErrors = null;

		Log.e(TAG, "ReflectiveDrawableLoader() The default Constructor was called! This should never happen...");
		throw new IllegalStateException("The default Constructor was called! This should never happen...");
	}

	private ReflectiveDrawableLoader(Context context) {
		mReflectionUtils = new ReflectionUtils(context.getApplicationContext().getPackageName());
		mCache = new LruLinkedHashMap<String, Integer>(CACHE_SIZE, 0.75f);
		mCacheMisses = new LruLinkedHashMap<String, Object>(CACHE_SIZE, 0.75f);
		mAddDrawableNameToContainer = new AtomicBoolean(false);
		mLogErrors = new AtomicBoolean(false);
	}

	private DrawableResourceContainer fetchDrawableContainer(String drawableName, String color, int fallbackDrawableId){
		int res = fetchDrawableId(drawableName, fallbackDrawableId);
		return new DrawableResourceContainer((mAddDrawableNameToContainer.get() ? drawableName: null), res, tryColor(color));
	}

	private synchronized int fetchDrawableId(String drawableName, int fallbackDrawableId){
		Integer result = null;
		long startTime;

		if(TIME_LOGGING_ENABLED){
			startTime = System.nanoTime();
		}

		// Check if its in the known "cache miss" list
		if(mCacheMisses.containsKey(drawableName)){
			result = fallbackDrawableId;
		} else {
			result = mCache.get(drawableName);
	
			if(result == null){
				result  = mReflectionUtils.reflectDrawable(drawableName, fallbackDrawableId, mLogErrors.get());
	
				if(result != null && result != fallbackDrawableId){
					mCache.put(drawableName, result);
				} else {
					// We do not have this drawable, add it in the "miss" cache.
					mCacheMisses.put(drawableName, null);
				}
			} 
		}

		if(TIME_LOGGING_ENABLED){
			long endTime = System.nanoTime();
			Log.d(TAG, "fetchDrawableId() - Fetched '"  + drawableName + "' in " + (endTime - startTime) + "ns");
		}

		return result;
	}

	/**
	 * This is a convenience function which can be used to quickly fetch Dialog Drawables without 
	 * having to mess around with String concatenation in your code.
	 * The Drawable filename in the Res folder needs to be prefixed with {@value #ICON_PREFIX_DIALOG}.
	 * 
	 * @param drawableName The name of the Drawable to fetch.
	 * @param family The family (if any) of the variable to fetch. Set to null if no family is needed.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return The Id of the Drawable to display.
	 */
	public int getDialogDrawable(String drawableName, String family, int fallbackDrawableId) {
		return getDrawableId(ICON_PREFIX_DIALOG + formatKey(drawableName, family), fallbackDrawableId);
	}

	/**
	 * This is a convenience function which can be used to quickly fetch Dialog Drawables without 
	 * having to mess around with String concatenation in your code.
	 * The Drawable filename in the Res folder needs to be prefixed with {@value #ICON_PREFIX_DIALOG}.
	 * 
	 * @param drawableName The name of the Drawable to fetch.
	 * @param family The family (if any) of the variable to fetch. Set to null if no family is needed.
	 * @param colorString The colour used for the colour filter. It has to be in "#FFFFFF" format.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return A {@link DrawableResourceContainer} with the requested Drawable data.
	 */
	public DrawableResourceContainer getDialogDrawableContainer(String drawableName, String family, String colorString, int fallbackDrawableId) {
		return fetchDrawableContainer(ICON_PREFIX_DIALOG + formatKey(drawableName, family), colorString, fallbackDrawableId);
	}

	/**
	 * This function will return {@link DrawableResourceContainer} containing the requested Drawable information
	 * This function makes no assumptions regarding a Drawable's prefix, so you will need input its full name. 
	 * It is functionally identical to calling getDrawableContainer(drawableName, family, colorString, fallbackDrawableId) 
	 * with the family set to null.
	 * 
	 * @param drawableName The name of the Drawable to fetch.
	 * @param colorString The colour used for the colour filter. It has to be in "#FFFFFF" format.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return A {@link DrawableResourceContainer} with the requested Drawable data.
	 */
	public DrawableResourceContainer getDrawableContainer(String drawableName, String colorString, int fallbackDrawableId){
		return getDrawableContainer(drawableName, null, colorString, fallbackDrawableId);
	}

	/**
	 * This function will return {@link DrawableResourceContainer} containing the requested Drawable information
	 * <b>This function makes no assumptions regarding a Drawable's prefix, so you will need input its full name.</b> 
	 * 
	 * @param drawableName The name of the Drawable to fetch.
	 * @param family The family (if any) of the variable to fetch. Set to null if no family is needed.
	 * @param colorString The colour used for the colour filter. It has to be in "#FFFFFF" format.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return A {@link DrawableResourceContainer} with the requested Drawable data.
	 */
	public DrawableResourceContainer getDrawableContainer(String drawableName, String family, String colorString, int fallbackDrawableId){
		return fetchDrawableContainer(formatKey(drawableName, family), colorString, fallbackDrawableId);
	}

	/**
	 * Attempts to retrieve the Id of the requested Drawable.
	 *
	 * @param drawableName The name of the Drawable to fetch.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return The Id of the Drawable to display.
	 */
	public int getDrawableId(String drawableName, int fallbackDrawableId){
		return getDrawableId(drawableName, null, fallbackDrawableId);
	}

	/**
	 * Attempts to retrieve the Id of the requested Drawable.
	 *
	 * @param drawableName The name of the Drawable to fetch.
	 * @param family The family (if any) of the variable to fetch. Set to null if no family is needed.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return The Id of the Drawable to display.
	 */
	public int getDrawableId(String drawableName, String family, int fallbackDrawableId){
		return fetchDrawableId(formatKey(drawableName, family), fallbackDrawableId);
	}

	/**
	 * This is a convenience function which can be used to quickly fetch Launcher Drawables without 
	 * having to mess around with String concatenation in your code.
	 * The Drawable filename in the Res folder needs to be prefixed with {@value #ICON_PREFIX_LAUNCHER}.
	 * 
	 * @param drawableName The name of the Drawable to fetch.
	 * @param family The family (if any) of the variable to fetch. Set to null if no family is needed.
	 * @param colorString The colour used for the colour filter. It has to be in "#FFFFFF" format.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return A {@link DrawableResourceContainer} with the requested Drawable data.
	 */
	public DrawableResourceContainer getLauncherDrawableContainer(String drawableName, String family, String colorString, int fallbackDrawableId) {
		return fetchDrawableContainer(ICON_PREFIX_LAUNCHER + formatKey(drawableName, family), colorString, fallbackDrawableId);
	}

	/**
	 * This is a convenience function which can be used to quickly fetch Launcher Drawables without 
	 * having to mess around with String concatenation in your code.
	 * The Drawable filename in the Res folder needs to be prefixed with {@value #ICON_PREFIX_LAUNCHER}.
	 * 
	 * @param drawableName The name of the Drawable to fetch.
	 * @param family The family (if any) of the variable to fetch. Set to null if no family is needed.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return The Id of the Drawable to display.
	 */
	public int getLauncherDrawableId(String drawableName, String family, int fallbackDrawableId) {
		return fetchDrawableId(ICON_PREFIX_LAUNCHER + formatKey(drawableName, family), fallbackDrawableId);
	}

	/**
	 * This is a convenience function which can be used to quickly fetch List Drawables without 
	 * having to mess around with String concatenation in your code.
	 * The Drawable filename in the Res folder needs to be prefixed with {@value #ICON_PREFIX_LIST}.
	 * 
	 * @param drawableName The name of the Drawable to fetch.
	 * @param family The family (if any) of the variable to fetch. Set to null if no family is needed.
	 * @param colorString The colour used for the colour filter. It has to be in "#FFFFFF" format.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return A {@link DrawableResourceContainer} with the requested Drawable data.
	 */
	public DrawableResourceContainer getListDrawableContainer(String drawableName, String family, String colorString, int fallbackDrawableId) {
		return fetchDrawableContainer(ICON_PREFIX_LIST + formatKey(drawableName, family), colorString, fallbackDrawableId);
	}


	//

	/**
	 * This is a convenience function which can be used to quickly fetch List Drawables without 
	 * having to mess around with String concatenation in your code.
	 * The Drawable filename in the Res folder needs to be prefixed with {@value #ICON_PREFIX_LIST}.
	 * 
	 * @param drawableName The name of the Drawable to fetch.
	 * @param family The family (if any) of the variable to fetch. Set to null if no family is needed.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return The Id of the Drawable to display.
	 */
	public int getListDrawableId(String drawableName, String family, int fallbackDrawableId) {
		return fetchDrawableId(ICON_PREFIX_LIST + formatKey(drawableName, family), fallbackDrawableId);
	}

	/**
	 * This is a convenience function which can be used to quickly fetch Menu Drawables without 
	 * having to mess around with String concatenation in your code.
	 * The Drawable filename in the Res folder needs to be prefixed with {@value #ICON_PREFIX_MENU}.
	 * 
	 * @param drawableName The name of the Drawable to fetch.
	 * @param family The family (if any) of the variable to fetch. Set to null if no family is needed.
	 * @param colorString The colour used for the colour filter. It has to be in "#FFFFFF" format.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return A {@link DrawableResourceContainer} with the requested Drawable data.
	 */
	public DrawableResourceContainer getMenuDrawableContainer(String drawableName, String family, String colorString, int fallbackDrawableId) {
		return fetchDrawableContainer(ICON_PREFIX_MENU + formatKey(drawableName, family), colorString, fallbackDrawableId);
	}

	/**
	 * This is a convenience function which can be used to quickly fetch Menu Drawables without 
	 * having to mess around with String concatenation in your code.
	 * The Drawable filename in the Res folder needs to be prefixed with {@value #ICON_PREFIX_MENU}.
	 * 
	 * @param drawableName The name of the Drawable to fetch.
	 * @param family The family (if any) of the variable to fetch. Set to null if no family is needed.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return The Id of the Drawable to display.
	 */
	public int getMenuDrawableId(String drawableName, String family, int fallbackDrawableId) {
		return fetchDrawableId(ICON_PREFIX_MENU + formatKey(drawableName, family), fallbackDrawableId);
	}

	/**
	 * This is a convenience function which can be used to quickly fetch Status Bar Drawables without 
	 * having to mess around with String concatenation in your code.
	 * The Drawable filename in the Res folder needs to be prefixed with {@value #ICON_PREFIX_STATUS_BAR}.
	 * 
	 * @param drawableName The name of the Drawable to fetch.
	 * @param family The family (if any) of the variable to fetch. Set to null if no family is needed.
	 * @param colorString The colour used for the colour filter. It has to be in "#FFFFFF" format.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return A {@link DrawableResourceContainer} with the requested Drawable data.
	 */
	public DrawableResourceContainer getStatusBarDrawableContainer(String drawableName, String family, String colorString, int fallbackDrawableId) {
		return fetchDrawableContainer(ICON_PREFIX_STATUS_BAR + formatKey(drawableName, family), colorString, fallbackDrawableId);
	}

	/**
	 * This is a convenience function which can be used to quickly fetch Status Bar Drawables without 
	 * having to mess around with String concatenation in your code.
	 * The Drawable filename in the Res folder needs to be prefixed with {@value #ICON_PREFIX_STATUS_BAR}.
	 * 
	 * @param drawableName The name of the Drawable to fetch.
	 * @param family The family (if any) of the variable to fetch. Set to null if no family is needed.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return The Id of the Drawable to display.
	 */
	public int getStatusBarDrawableId(String drawableName, String family, int fallbackDrawableId) {
		return fetchDrawableId(ICON_PREFIX_STATUS_BAR + formatKey(drawableName, family), fallbackDrawableId);
	}

	/**
	 * This is a convenience function which can be used to quickly fetch Tab Drawables without 
	 * having to mess around with String concatenation in your code.
	 * The Drawable filename in the Res folder needs to be prefixed with {@value #ICON_PREFIX_TAB}.
	 * 
	 * @param drawableName The name of the Drawable to fetch.
	 * @param family The family (if any) of the variable to fetch. Set to null if no family is needed.
	 * @param colorString The colour used for the colour filter. It has to be in "#FFFFFF" format.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return A {@link DrawableResourceContainer} with the requested Drawable data.
	 */
	public DrawableResourceContainer getTabDrawableContainer(String drawableName, String family, String colorString, int fallbackDrawableId) {
		return fetchDrawableContainer(ICON_PREFIX_TAB + formatKey(drawableName, family), colorString, fallbackDrawableId);
	}

	/**
	 * This is a convenience function which can be used to quickly fetch Tab Drawables without 
	 * having to mess around with String concatenation in your code.
	 * The Drawable filename in the Res folder needs to be prefixed with {@value #ICON_PREFIX_TAB}.
	 * 
	 * @param drawableName The name of the Drawable to fetch.
	 * @param family The family (if any) of the variable to fetch. Set to null if no family is needed.
	 * @param fallbackDrawableId The id of the Drawable to use if the requested one does not exist.
	 * @return The Id of the Drawable to display.
	 */
	public int getTabDrawableId(String drawableName, String family, int fallbackDrawableId) {
		return fetchDrawableId(ICON_PREFIX_TAB + formatKey(drawableName, family), fallbackDrawableId);
	}

	/**
	 *  This function will print a list of all drawables this library can see into logcat
	 *  Only useful for debugging.
	 */
	public void printDrawablesToLogCat(){
		mReflectionUtils.logFields(ReflectionUtils.RESOURCE_LOCATION_DRAWABLES);
	}


	/**
	 * Enables or disables the addition of the requested Drawable name in the resulting {@link DrawableResourceContainer}
	 * when requesting a Colorised Drawable.
	 * 
	 * @param enable - True to enable, false to disable. False by default;
	 */
	public synchronized void setAddDrawableNameToContainer(boolean enable){
		mAddDrawableNameToContainer.set(enable);
	}

	/**
	 * Enables or disables the logging of errors in LogCat during operation.
	 * The errors will be logged as warning.
	 * Types of errors logged:
	 * - Reflection Errors
	 * - Color parsing errors
	 * 
	 * @param enable - True to enable, false to disable. False by default;
	 */
	public synchronized void setLogErrors(boolean enable){
		mLogErrors.set(enable);
	}

	private Integer tryColor(String colorString){
		if(colorString == null || colorString.length() < 1){
			return null;
		}

		try{
			return Color.parseColor(colorString);
		} catch (IllegalArgumentException e){
			if(mLogErrors.get()){
				Log.w(TAG, "tryColor() - IllegalArgumentException while trying to parse color '" + colorString + "'");
			}
			return null;
		}
	}

	public static String formatKey(String name, String family){
		if(family != null && family.length() > 0){
			return family.concat("_").concat(name);
		} else {
			return name;
		}
	}

	public static String formatKey(String prefix, String name, String family){
		if(family != null && family.length() > 0){
			return prefix.concat(family).concat(formatKey(name, family));
		} else {
			return prefix.concat(name);
		}
	}

	/**
	 * Returns an instance of the ReflectiveDrawableLoader
	 * 
	 * @param context A standard Android context. It cannot be null
	 * @return The instance of the ReflectiveDrawableLoader
	 */
	public static ReflectiveDrawableLoader getInstance(Context context) {
		if (instance == null) {
			synchronized (ReflectiveDrawableLoader .class){
				if (instance == null) {
					instance = new ReflectiveDrawableLoader (context);
				}
			}
		}
		return instance;
	}

}
