package it.nerdammer.spash.shell.common;

import java.io.PrintWriter;
import java.util.function.Function;

/**
 * An abstract collection. It can be implemented either by a standard java collection or a distributed collection.
 *
 * @author Nicola Ferraro
 */
public interface SpashCollection<T> {

    /**
     * Prints the whole collection to the given writer.
     *
     * @param writer the writer on which the collection will be printed on.
     */
    void mkString(PrintWriter writer);

    /**
     * Tranforms every element of the collection into another element.
     *
     * @param f the transformation function
     * @param <R> the new type
     * @return the transformed collection
     */
    <R> SpashCollection<R> map(Function<T, R> f);

    /**
     * Merges this collection to the one provided as input.
     *
     * @param coll the collection to merge
     * @return the union of the two collections
     */
    SpashCollection<T> union(SpashCollection<T> coll);

}
