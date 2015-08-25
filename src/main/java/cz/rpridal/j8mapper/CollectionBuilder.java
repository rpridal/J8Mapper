package cz.rpridal.j8mapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CollectionBuilder {

	public static <D> Collection<D> getInstance(Class<? extends Collection<?>> collectionClass, Class<D> targetClass) {
		if (Set.class.isAssignableFrom(collectionClass)) {
			return new HashSet<>();
		} else if (List.class.isAssignableFrom(collectionClass)) {
			return new LinkedList<>();
		}
		return null;
	}
}
