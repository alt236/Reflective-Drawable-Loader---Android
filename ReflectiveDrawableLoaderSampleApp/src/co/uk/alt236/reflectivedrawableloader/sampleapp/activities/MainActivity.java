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
package co.uk.alt236.reflectivedrawableloader.sampleapp.activities;

import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import co.uk.alt236.reflectivedrawableloader.sampleapp.R;
import co.uk.alt236.reflectivedrawableloader.sampleapp.util.ColorisedDrawableArrayAdapter;
import co.uk.alt236.reflectivedrawableloader.sampleapp.util.IconArray;
import co.uk.alt236.reflectivedrawableloader.sampleapp.util.SimpleDrawableArrayAdapter;

public class MainActivity extends ListActivity{
    private static final int RESULT_SET_SIZE = 500;
    
    ListAdapter mAdapter;
    
    public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_icon_display);
	getListView().setBackgroundColor(Color.parseColor("#c0c0c0"));
	onRandomiseClick(null);
    }
    
    
    public void onRandomiseClick(View v){
	mAdapter = new SimpleDrawableArrayAdapter(this, IconArray.getSimpleDrawableList(RESULT_SET_SIZE));
	getListView().setAdapter(mAdapter);
    }
    
    public void onColoriseClick(View v){
	mAdapter = new ColorisedDrawableArrayAdapter(this, IconArray.getColorisedDrawableList(this, RESULT_SET_SIZE));
	getListView().setAdapter(mAdapter);
    }
    
}
