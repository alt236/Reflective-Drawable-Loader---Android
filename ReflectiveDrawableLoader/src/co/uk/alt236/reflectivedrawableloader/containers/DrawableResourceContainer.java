package co.uk.alt236.reflectivedrawableloader.containers;

import android.graphics.PorterDuff.Mode;
import android.widget.ImageView;

public class DrawableResourceContainer {
    private final int mResourceId;
    private final Integer mColourFilterColour;
    private final String mDrawableName;
    
    public DrawableResourceContainer(int resourceId, Integer colourFilterColour) {
	this(null, resourceId, colourFilterColour);
    }
    
    public DrawableResourceContainer(String drawableName, int resourceId, Integer colourFilterColour) {
	this.mResourceId = resourceId;
	this.mColourFilterColour = colourFilterColour;
	this.mDrawableName = drawableName;
    }

    public Integer getColourFilterColour() {
	return mColourFilterColour;
    }

    public String getDrawableName() {
        return mDrawableName;
    }

    public int getResourceId() {
	return mResourceId;
    }

    public boolean hasColourFilter() {
	return !(mColourFilterColour == null);
    }

    public void setDrawableWithPorterDuffMultiply(ImageView iv) {
	iv.setImageResource(mResourceId);
	if (hasColourFilter()) {
	    iv.setColorFilter(mColourFilterColour, Mode.MULTIPLY);
	} else {
	    iv.setColorFilter(null);
	}
    }

}
