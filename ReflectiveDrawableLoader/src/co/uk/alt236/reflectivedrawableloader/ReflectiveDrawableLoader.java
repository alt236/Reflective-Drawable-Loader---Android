package co.uk.alt236.reflectivedrawableloader;

import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.graphics.Color;
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
    private static final boolean LOG_REFLECTION_ERRORS = true;

    private final AtomicBoolean mAddDrawableNameToContainer;
    
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
	mReflectionUtils = null;
	mCache = null;
	mAddDrawableNameToContainer = null;
    }

    private ReflectiveDrawableLoader(Context context) {
	mReflectionUtils = new ReflectionUtils(context.getApplicationContext().getPackageName());
	mCache = new LruLinkedHashMap<String, Integer>(CACHE_SIZE, 0.75f);
	mAddDrawableNameToContainer = new AtomicBoolean(false);
    }

    public String formatKey(String name, String family){
	if(family != null && family.length() > 0){
	    return family.concat("_").concat(name);
	} else {
	    return name;
	}
    }

    public synchronized void setAddDrawableNameToContainer(boolean value){
	mAddDrawableNameToContainer.set(value);
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

    //

    public int getDialogDrawable(String drawableName, String family, int fallbackDrawableId) {
	return getDrawableId(ICON_PREFIX_DIALOG + formatKey(drawableName, family), fallbackDrawableId);
    }

    public int getDrawable(String name, int fallbackDrawableId){
	return getDrawable(name, null, fallbackDrawableId);
    }

    public int getDrawable(String drawableName, String family, int fallbackDrawableId){
	return getDrawableId(formatKey(drawableName, family), fallbackDrawableId);
    }

    private synchronized int getDrawableId(String drawableName, int fallbackDrawableId){
	Integer result = null;

	result = mCache.get(drawableName);

	if(result == null){
	    result  = mReflectionUtils.reflectDrawable(drawableName, fallbackDrawableId, LOG_REFLECTION_ERRORS);

	    if(result != null && result != fallbackDrawableId){
		mCache.put(drawableName, result);
	    }
	} 

	return result;
    }

    private DrawableResourceContainer getDrawableContainer(String drawableName, String color, int fallbackDrawableId){
	int res = getDrawableId(drawableName, fallbackDrawableId);
	if(res == fallbackDrawableId){
	    return new DrawableResourceContainer((mAddDrawableNameToContainer.get() ? drawableName: null), res, null);
	} else {
	    return new DrawableResourceContainer((mAddDrawableNameToContainer.get() ? drawableName: null), res, tryColor(color));
	}
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