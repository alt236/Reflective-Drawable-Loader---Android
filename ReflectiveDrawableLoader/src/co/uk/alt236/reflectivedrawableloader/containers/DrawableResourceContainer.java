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
package co.uk.alt236.reflectivedrawableloader.containers;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff.Mode;
import android.widget.ImageView;

public class DrawableResourceContainer {
	private final int mResourceId;
	private final Integer mColourFilterColour;
	private final String mDrawableName;
	private ColorFilter mColourFilter;

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

	private ColorFilter getOverridingColorFilter(int color){
		if(mColourFilter == null){
			int red = (color & 0xFF0000) / 0xFFFF;
			int green = (color & 0xFF00) / 0xFF;
			int blue = color & 0xFF;

			float[] matrix = { 0, 0, 0, 0, red
					, 0, 0, 0, 0, green
					, 0, 0, 0, 0, blue
					, 0, 0, 0, 1, 0 };

			mColourFilter =  new ColorMatrixColorFilter(matrix);
		}
		return mColourFilter;
	}

	public int getResourceId() {
		return mResourceId;
	}

	public boolean hasColourFilter() {
		return !(mColourFilterColour == null);
	}

	public void setDrawableWithColorOverrideMatrix(ImageView iv) {
		iv.setImageResource(mResourceId);
		if (hasColourFilter()) {
			iv.setColorFilter(getOverridingColorFilter(mColourFilterColour));
		} else {
			iv.setColorFilter(null);
		}
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
