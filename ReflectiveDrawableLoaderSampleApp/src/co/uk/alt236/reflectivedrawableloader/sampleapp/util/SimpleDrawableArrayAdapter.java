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
import java.util.Random;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.alt236.reflectivedrawableloader.ReflectiveDrawableLoader;
import co.uk.alt236.reflectivedrawableloader.sampleapp.R;

public class SimpleDrawableArrayAdapter extends ArrayAdapter<String>{
    final static int mLayout = R.layout.list_item_icon_check;
    final static int mMissingIconId = R.drawable.ic_missing_icon;
    
    final Context mContext;
    final List<String> mItemList;
    final ReflectiveDrawableLoader mReflectiveLoader;
    final Random mRandom;
    
    public SimpleDrawableArrayAdapter(Context context, List<String> itemList) {
        super(context, mLayout);
        mContext = context;
        mItemList = itemList;
        mReflectiveLoader = ReflectiveDrawableLoader.getInstance(context);
        mRandom = new Random();
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
    public String getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    private class Wrapper {

        private TextView name = null;
        private ImageView image1 = null;

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

        public TextView getName() {
            if (name == null) {
                name = (TextView) row.findViewById(R.id.name);
            }
            return(name);
        }

        public void populateFrom(final String iconName) {
            if (iconName != null) {
        	String fullName = ReflectiveDrawableLoader.ICON_PREFIX_MENU.concat(mReflectiveLoader.formatKey(
        		iconName, 
        		IconArray.FAMILY_ARRAY[mRandom.nextInt(IconArray.FAMILY_ARRAY.length)]));
        	
        	//
        	// Note that it is simpler to call mReflectiveLoader.getMenuDrawable(),
        	// passing the iconName and family instead of doing the concatenation above and
        	// calling mReflectiveLoader.getDrawable()
        	//
        	
                getImage1().setImageResource(
                	mReflectiveLoader.getDrawableId(
                		fullName,
                		mMissingIconId));
                
                getName().setText(fullName);
            }
        }
    }
}
