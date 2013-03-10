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

import java.util.LinkedHashMap;
import java.util.Map;

public class LruLinkedHashMap<K,V> extends LinkedHashMap<K,V>{

    private final int mMaxEntries;
    /**
     * 
     */
    private static final long serialVersionUID = -1575270872785732340L;

    public LruLinkedHashMap(int maxEntries, float loadFactor){
	super(maxEntries + 1, loadFactor, true);
	this.mMaxEntries = maxEntries + 1;
	
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean removeEldestEntry(Map.Entry eldest) {
        return size() > mMaxEntries;
    }
}
