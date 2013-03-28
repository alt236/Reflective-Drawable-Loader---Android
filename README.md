Reflective Drawable Loader
-----------
Were you ever in a situation where you had to access Drawables based on their names (for example if the Drawable names are stored in a DB) and you had to write long lookup tables converting the names into R.drawable.ids? And maintaining them?

This library is offering a way around it by using reflection to access the Drawable directly by name. You only need to include them as normal into your Res folder tree. It has been benchmarked at up to 5x faster than the platform's `Resources.getIdentifier()` method.

It is using LRU caching to mitigate the reflection time overhead.

It also includes a few convenience functions to help change Drawable colour based on a hex colour value. 

Basic Usage
-----------
To use:

1. Download a copy of the ReflectiveDrawableLoader library and reference it in your project.
2. Get an Instance of the ReflectiveDrawableLoader by calling `ReflectiveDrawableLoader.getInstance(Context);`
3. Start getting your Drawable ids by calling  any of the getDrawable functions.
 
<b>Drawable families</b>

The library is using a concept of icon families to distinguish between different variations of similar icons.
For example, icons in Android can come in a Holo Light and Holo Dark variant.

So, if you call `getDrawableId("submarine", "yellow", R.drawable.ic_list_fallback)` then the library will try to look for an icon called `yellow_submarine` and return its Id if it exists, or `R.drawable.ic_list_fallback` otherwise.

Similarly, if you call `getDrawableId("submarine", null, R.drawable.ic_list_fallback)` or  `getDrawableId("submarine", R.drawable.ic_list_fallback)` then the library will try to look for an icon called `submarine` and return its Id if it exists, or `R.drawable.ic_list_fallback` otherwise. 

Of course, nothing stops you from calling  `getDrawableId("yellow_submarine", null, R.drawable.ic_list_fallback)` to get the `yellow_submarine` icon as well.

<b>Convenience Functions and Drawable naming conventions</b>

The convenience functions in the library assume that Drawables are named using the convention described here [Icon Design Guidelines](http://developer.android.com/guide/practices/ui_guidelines/icon_design.html).

So for example,

If you call `getListDrawableId("submarine", "yellow", R.drawable.ic_list_fallback);` then the library will try to look for an icon called `ic_list_yellow_submarine` and return its Id if it exists, or `R.drawable.ic_list_fallback` otherwise.

Colorising Drawables
-----------

If you ask for a Colorised Icon by calling `getColorisedListDrawable("table", "furniture", "#c0c0c0", R.drawable.ic_list_fallback);` instead of an Id you will get a DrawableResourceContainer object which will contain the Id of the Drawable to use, the colour to use as and Integer and a couple convenience functions to use to colourise the Drawable. If there was an error parsing the colour, the Integer will be null.

For sample code on how this works and the different between the two convenience color filtering functions have a look at ColorisedDrawableArrayAdapter.java in the Sample App project.

Also, the convenience functions do not apply the colorFilter to the Drawable directly, but to the ImageView holding it.

Feel free to expand the DrawableResourceContainer to add other colour filters or behaviours.

Jarification
-----------
Type `ant jar` at the root of the Library Project to produce a Jar file.

ProGuard
--------

ProGuard users must ensure that the R class, its inner drawable class and all fields are not obfuscated for the runtime reflection to work. Add the following to your your proguard-project.txt file:

    -keepattributes InnerClasses
    
    -keep class **.R
    -keep class **.R$* {
        <fields>;
    }

Changelog
-----------
* v0.0.1 First public release
* v0.0.2 Bugfixes, added caching of the resource classes in ReflectionUtils.

Permission Explanation
-----------
* No permissions required
	
Sample App Screenshots
-----------
![screenshot1](https://github.com/alt236/Reflective-Drawable-Loader---Android/raw/master/screenshots/screenshot_1.png)
![screenshot2](https://github.com/alt236/Reflective-Drawable-Loader---Android/raw/master/screenshots/screenshot_2.png)

Links
-----------
* Github: [https://github.com/alt236/Reflective-Drawable-Loader---Android]()

Credits
-----------
Author: [Alexandros Schillings](https://github.com/alt236).

Based on code by [Jeff Gilfelt](https://github.com/jgilfelt), who showed me that contrary to my academic reservations, reflectively loading icons is not that bad :)

All logos are the property of their respective owners.

The icons used for the example app were downloaded from here: [Android Design](http://developer.android.com/design/downloads/index.htm)

The code in this project is licensed under the Apache Software License 2.0.

Copyright (c) 2013 Alexandros Schillings.
