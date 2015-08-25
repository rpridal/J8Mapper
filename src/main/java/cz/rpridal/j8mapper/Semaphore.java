package cz.rpridal.j8mapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Semaphore<K> {
	private Map<K, Integer> semaphore = Collections.synchronizedMap(new HashMap<>());
	private final int boundary;
	
	public Semaphore(int boundary) {
		super();
		this.boundary = boundary;
	}

	public void lock(K key) {
		if (semaphore.containsKey(key)) {
			semaphore.put(key, semaphore.get(key) + 1);
		} else {
			semaphore.put(key, 1);
		}
	}

	public void unlock(K key) {
		if (semaphore.containsKey(key)) {
			Integer count = semaphore.get(key);
			if (count == 1) {
				semaphore.remove(key);
			} else {
				semaphore.put(key, count - 1);
			}
		} 
	}

	public boolean isLocked(K key){
		if(!semaphore.containsKey(key)){
			return false;
		}
		return semaphore.get(key) >= boundary;
	}
}
