/*******************************************************************************
 * Copyright 2013 alex
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
package co.uk.alt236.reflectivedrawableloader.sampleapp.util;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.alt236.reflectivedrawableloader.ReflectiveDrawableLoader;
import co.uk.alt236.reflectivedrawableloader.containers.DrawableResourceContainer;
import co.uk.alt236.reflectivedrawableloader.sampleapp.R;

public class ColorisedDrawableArrayAdapter extends ArrayAdapter<DrawableResourceContainer>{
    final static int mLayout = R.layout.list_item_icon_check_colorised;
    final static int mMissingIconId = R.drawable.ic_missing_icon;
    
    final Context mContext;
    final List<DrawableResourceContainer> mItemList;
    final ReflectiveDrawableLoader mReflectiveLoader;
    final int mColourTransparent;
    
    public ColorisedDrawableArrayAdapter(Context context, List<DrawableResourceContainer> itemList) {
        super(context, mLayout);
        mReflectiveLoader = ReflectiveDrawableLoader.getInstance(context);
        mContext = context;
        mItemList = itemList;
        mColourTransparent = context.getResources().getColor(android.R.color.transparent);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        Wrapper wrapper = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(mLayout, null);
            wrapper = new Wrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (Wrapper) row.getTag();
        }

        wrapper.populateFrom(getItem(position));

        return (row);
    }

    @Override
    public DrawableResourceContainer getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    private class Wrapper {

        private TextView name = null;
        private ImageView image1 = null;
        private ImageView image2 = null;
        private ImageView image3 = null;
        private ImageView image4 = null;

        private View row = null;


        public Wrapper(View row) {
            this.row = row;
        }

        public ImageView getImage1() {
            if (image1 == null) {
                image1 = (ImageView) row.findViewById(R.id.image1);
            }
            return(image1);
        }

        public ImageView getImage2() {
            if (image2 == null) {
                image2 = (ImageView) row.findViewById(R.id.image2);
            }
            return(image2);
        }

        public ImageView getImage3() {
            if (image3 == null) {
                image3 = (ImageView) row.findViewById(R.id.image3);
            }
            return(image3);
        }

        public ImageView getImage4() {
            if (image4 == null) {
                image4 = (ImageView) row.findViewById(R.id.image4);
            }
            return(image4);
        }

        public TextView getName() {
            if (name == null) {
                name = (TextView) row.findViewById(R.id.name);
            }
            return(name);
        }

        public void populateFrom(DrawableResourceContainer container) {
            if (container != null) {        	
                getName().setText(container.getDrawableName());
                
                // This is the original Drawable
                getImage1().setImageResource(container.getResourceId());
                
                // This is the colour we are using:
                if(container.getColourFilterColour() == null){
                    getImage2().setBackgroundColor(mColourTransparent);
                } else {
                    getImage2().setBackgroundColor(container.getColourFilterColour());
                }
                
                // This is applied using PorterDuff Mode Multiply
                // It is the equivalent of:
                // getImage3().setImageResource(container.getResourceId());
                // getImage3().setColorFilter(container.getColourFilterColour(), Mode.MULTIPLY);
                container.setDrawableWithPorterDuffMultiply(getImage3());
                
                // This is applied using a custom matrix which will replace
                // other colours with the one set as the colorfilter.
                container.setDrawableWithColorOverrideMatrix(getImage4());
            }
        }

    }
}
