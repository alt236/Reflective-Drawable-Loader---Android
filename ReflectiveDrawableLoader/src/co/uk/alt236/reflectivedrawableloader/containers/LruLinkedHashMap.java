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
