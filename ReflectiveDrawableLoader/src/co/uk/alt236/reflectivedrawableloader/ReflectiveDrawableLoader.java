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

    private final AtomicBoolean mAddDrawableNameToContainer;
    private final AtomicBoolean mLogReflectionErrors;
    
    private static ReflectiveDrawableLoader instance = null;
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
    
    private final ReflectionUtils mReflectionUtils;
    private final LruLinkedHashMap<String, Integer> mCache;
    public final String TAG = getClass().getName();

    private ReflectiveDrawableLoader() {
	// We should never be here...
	mReflectionUtils = null;
	mCache = null;
	mAddDrawableNameToContainer = null;
	mLogReflectionErrors = null;
	
	Log.e(TAG, "ReflectiveDrawableLoader() - The default Constructor was called! This should never happen...");
	throw new IllegalStateException("The default Constructor was called! This should never happen...");
    }

    private ReflectiveDrawableLoader(Context context) {
	mReflectionUtils = new ReflectionUtils(context.getApplicationContext().getPackageName());
	mCache = new LruLinkedHashMap<String, Integer>(CACHE_SIZE, 0.75f);
	mAddDrawableNameToContainer = new AtomicBoolean(false);
	mLogReflectionErrors = new AtomicBoolean(false);
    }

    public String formatKey(String name, String family){
	if(family != null && family.length() > 0){
	    return family.concat("_").concat(name);
	} else {
	    return name;
	}
    }

    public DrawableResourceContainer getColorisedDialogDrawable(String drawableName, String family, String color, int fallbackDrawableId) {
	return getDrawableContainer(ICON_PREFIX_DIALOG + formatKey(drawableName, family), color, fallbackDrawableId);
    }
    
    public DrawableResourceContainer getColorisedDrawable(String name, String color, int fallbackDrawableId){
	return getColorisedDrawable(name, null, color, fallbackDrawableId);
    }
    
    public DrawableResourceContainer getColorisedDrawable(String drawableName, String family, String color, int fallbackDrawableId){
	return getDrawableContainer(formatKey(drawableName, family), color, fallbackDrawableId);
    }

    public DrawableResourceContainer getColorisedLauncherDrawable(String drawableName, String family, String color, int fallbackDrawableId) {
	return getDrawableContainer(ICON_PREFIX_LAUNCHER + formatKey(drawableName, family), color, fallbackDrawableId);
    }

    public DrawableResourceContainer getColorisedListDrawable(String drawableName, String family, String color, int fallbackDrawableId) {
	return getDrawableContainer(ICON_PREFIX_LIST + formatKey(drawableName, family), color, fallbackDrawableId);
    }

    public DrawableResourceContainer getColorisedMenuDrawable(String drawableName, String family, String color, int fallbackDrawableId) {
	return getDrawableContainer(ICON_PREFIX_MENU + formatKey(drawableName, family), color, fallbackDrawableId);
    }

    public DrawableResourceContainer getColorisedStatusBarDrawable(String drawableName, String family, String color, int fallbackDrawableId) {
	return getDrawableContainer(ICON_PREFIX_STATUS_BAR + formatKey(drawableName, family), color, fallbackDrawableId);
    }

    public DrawableResourceContainer getColorisedTabDrawable(String drawableName, String family, String color, int fallbackDrawableId) {
	return getDrawableContainer(ICON_PREFIX_TAB + formatKey(drawableName, family), color, fallbackDrawableId);
    }

    public int getDialogDrawable(String drawableName, String family, int fallbackDrawableId) {
	return getDrawableId(ICON_PREFIX_DIALOG + formatKey(drawableName, family), fallbackDrawableId);
    }

    public int getDrawable(String name, int fallbackDrawableId){
	return getDrawable(name, null, fallbackDrawableId);
    }

    //

    public int getDrawable(String drawableName, String family, int fallbackDrawableId){
	return getDrawableId(formatKey(drawableName, family), fallbackDrawableId);
    }

    private DrawableResourceContainer getDrawableContainer(String drawableName, String color, int fallbackDrawableId){
	int res = getDrawableId(drawableName, fallbackDrawableId);
	if(res == fallbackDrawableId){
	    return new DrawableResourceContainer((mAddDrawableNameToContainer.get() ? drawableName: null), res, null);
	} else {
	    return new DrawableResourceContainer((mAddDrawableNameToContainer.get() ? drawableName: null), res, tryColor(color));
	}
    }

    private synchronized int getDrawableId(String drawableName, int fallbackDrawableId){
	Integer result = null;

	result = mCache.get(drawableName);

	if(result == null){
	    result  = mReflectionUtils.reflectDrawable(drawableName, fallbackDrawableId, mLogReflectionErrors.get());

	    if(result != null && result != fallbackDrawableId){
		mCache.put(drawableName, result);
	    }
	} 

	return result;
    }

    public int getLauncherDrawable(String drawableName, String family, int fallbackDrawableId) {
	return getDrawableId(ICON_PREFIX_LAUNCHER + formatKey(drawableName, family), fallbackDrawableId);
    }

    public int getListDrawable(String drawableName, String family, int fallbackDrawableId) {
	return getDrawableId(ICON_PREFIX_LIST + formatKey(drawableName, family), fallbackDrawableId);
    }

    public int getMenuDrawable(String drawableName, String family, int fallbackDrawableId) {
	return getDrawableId(ICON_PREFIX_MENU + formatKey(drawableName, family), fallbackDrawableId);
    }

    public ReflectionUtils getReflectionUtils(){
	return mReflectionUtils;
    }

    public int getStatusBarDrawable(String drawableName, String family, int fallbackDrawableId) {
	return getDrawableId(ICON_PREFIX_STATUS_BAR + formatKey(drawableName, family), fallbackDrawableId);
    }



    public int getTabDrawable(String drawableName, String family, int fallbackDrawableId) {
	return getDrawableId(ICON_PREFIX_TAB + formatKey(drawableName, family), fallbackDrawableId);
    }


    public synchronized void setAddDrawableNameToContainer(boolean enable){
	mAddDrawableNameToContainer.set(enable);
    }

    public synchronized void setLogReflectionErrors(boolean enable){
	mLogReflectionErrors.set(enable);
    }

    private Integer tryColor(String colorString){
	if(colorString == null || colorString.length() < 1){
	    return null;
	}

	try{
	    return Color.parseColor(colorString);
	} catch (IllegalArgumentException e){
	    return null;
	}
    }

}
