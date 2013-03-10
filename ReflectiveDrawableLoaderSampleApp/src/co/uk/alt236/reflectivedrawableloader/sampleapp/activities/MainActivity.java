package co.uk.alt236.reflectivedrawableloader.sampleapp.activities;

import android.app.ListActivity;
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
