package co.uk.alt236.reflectivedrawableloader;

import java.lang.reflect.Field;

import android.util.Log;

public class ReflectionUtils {
    public final String TAG = getClass().getName();
    private final String RESOURCE_LOCATION_DRAWABLES = ".R.drawable";
    
    private final String mPackageName;

    public ReflectionUtils(String appPackageName) {
	Log.d(TAG, "New ReflectionUtils() for '" + appPackageName + "'");
	this.mPackageName = appPackageName;
    }

    protected Class<?> getResourceClass(final String suffix) {
	try {
	    Class<?> rClassBase = Class.forName(mPackageName + ".R");
	    Class<?>[] subClassTable = rClassBase.getDeclaredClasses();

	    for (Class<?> subClass : subClassTable) {
		if (subClass.getCanonicalName().endsWith(suffix)) {
		    return subClass;
		}
	    }

	} catch (ClassNotFoundException e) {
	    Log.e(TAG, "getResourceClass() ClassNotFoundException: " + e.getMessage(), e);
	}

	Log.e(TAG, "getResourceClass() Unable to find Sublass: " + suffix);

	return null;
    }

    public void logSubClasses(String baseClass) {
	Log.d(TAG, "logSubClasses() Getting subclasses for '" + baseClass + "' ============= ");

	try {
	    Class<?> rClass = Class.forName(baseClass);
	    Class<?>[] subClassTable = rClass.getDeclaredClasses();
	    for (Class<?> subclass : subClassTable) {
		Log.d(TAG, "logSubClasses() Class: " + subclass.getCanonicalName());
	    }

	} catch (Exception e) {
	    Log.e(TAG, "logSubClasses() Error: " + e.getMessage(), e);
	}
    }

    protected int reflectDrawable(String fieldName, int defaultValue, boolean reportFailure) {
	return reflectResource(RESOURCE_LOCATION_DRAWABLES, fieldName, defaultValue, reportFailure);
    }

    protected int reflectResource(String resourceLocation, String fieldName, int defaultValue,
	    boolean reportFailure) {
	int error = 0;
	try {
	    Field field = getResourceClass(resourceLocation).getField(fieldName);
	    return field.getInt(null);
	} catch (NoSuchFieldException e) {
	    error = 1;
	} catch (IllegalAccessException e) {
	    error = 2;
	} catch (NullPointerException e) {
	    error = 3;
	}

	if (reportFailure) {
	    Log.d(TAG, "reflectResource() Resource '" + fieldName + "' not available! (" + error +")");
	}

	return defaultValue;
    }

    public void logFields(String resourceLocation) {
	Log.d(TAG, "logFields() Getting Fields for '" + resourceLocation + "' ============= ");

	try {
	    Field[] fields = getResourceClass(resourceLocation).getFields();
	    for (Field field : fields) {
		Log.d(TAG, "logFields() Field: '" + field.getName() + "'");
	    }
	} catch (NullPointerException e) {}
    }
}
