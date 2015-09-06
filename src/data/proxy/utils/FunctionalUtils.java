package data.proxy.utils;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Provides certain functional utility methods.
 */
public class FunctionalUtils {
    
    /**
     * Filters a collection by a predicate and adds filtered items to output collection.
     * 
     * @param allItems input collection containing all items
     * @param predicate predicate that will be used as filter function
     * @param filteredCollection collection into which the filtered items will be inserted
     * @return filtered collection
     */
    public static <T> Collection<T> filteredAddAll(Collection<T> allItems, Predicate<T> predicate,
            Collection<T> filteredCollection) {
        for (T item : allItems) {
            if (predicate.test(item)) {
                filteredCollection.add(item);
            }
        }
        return filteredCollection;
    }
}
