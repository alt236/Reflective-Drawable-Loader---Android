package co.uk.alt236.reflectivedrawableloader.sampleapp.util;

import java.util.Random;

public class RandomColorGenerator {
    private final Random mRandom;
    
    public RandomColorGenerator(){
	mRandom = new Random();
    }
    
    public String getRandomHexColor()  
    {  
	
	int red = mRandom.nextInt(255);
	int green = mRandom.nextInt(255);
	int blue = mRandom.nextInt(255);

	return "#" + Integer.toHexString(red) + Integer.toHexString(green) + Integer.toHexString(blue);
    }  
}
