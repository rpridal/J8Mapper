package cz.rpridal.j8mapper.manipulator;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ObjectCache {
	private static Map<Object, Object> objectCache = Collections.synchronizedMap(new IdentityHashMap<>());
	private final static Logger LOGGER = Logger.getLogger(ObjectCache.class.getName());

	public static Object getTargetObject(Object source) {
		if (!objectCache.containsKey(source)) {
			LOGGER.fine("Source object is not in cache! Source object:" + source);
		}
		return objectCache.get(source);
	}

	public synchronized static <SourceType, TargetType> void saveObjects(SourceType source, TargetType target) {
		if (objectCache.containsKey(source)) {
			LOGGER.fine("Object is already in cache! Source object" + source + "; Target object:" + target);
		}
		objectCache.put(source, target);
	}
}
