package cz.rpridal.j8mapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CollectionBuilder {

	public static <DataType> Collection<DataType> getInstance(
			Class<? extends Collection<? extends DataType>> collectionClass, Class<DataType> targetClass) {
		if (Set.class.isAssignableFrom(collectionClass)) {
			return new HashSet<>();
		} else if (List.class.isAssignableFrom(collectionClass)) {
			return new LinkedList<>();
		}
		return null;
	}
}
