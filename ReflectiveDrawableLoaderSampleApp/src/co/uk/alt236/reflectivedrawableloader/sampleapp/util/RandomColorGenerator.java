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

	return "#" + getColourHex(red) + getColourHex(green) + getColourHex(blue);
    }  
    
    
    private String getColourHex(int colour){
	// Hack to make sure that the resulting value is always two
	// characters long; It will only work for values less than 255;
	return Integer.toHexString(0x100 | colour).substring(1);
    }
}

