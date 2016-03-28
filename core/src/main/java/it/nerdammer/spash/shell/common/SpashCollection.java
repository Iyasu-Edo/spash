package it.nerdammer.spash.shell.common;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

/**
 * An abstract collection. It can be implemented either by a standard java collection or a distributed collection.
 *
 * @author Nicola Ferraro
 */
public interface SpashCollection<T> extends Serializable {

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
    <R> SpashCollection<R> map(SerializableFunction<T, R> f);

    /**
     * Merges this collection to the one provided as input.
     *
     * @param coll the collection to merge
     * @return the union of the two collections
     */
    SpashCollection<T> union(SpashCollection<T> coll);

    /**
     * Filters a collection retaining only elements respecting the given condition.
     *
     * @param condition the condition that must evaluate to true for retained elements
     * @return te resulting collection
     */
    SpashCollection<T> filter(SerializableFunction<T, Boolean> condition);

    /**
     * Converts this collection to a Spark {@code JavaRDD}.
     *
     * @param sc the current Spark context
     * @return a RDD corresponding to this collection
     */
    JavaRDD<T> toRDD(JavaSparkContext sc);

    /**
     * Collects the whole collection into a {@code List}.
     *
     * @return the elements of the collection
     */
    List<T> collect();

}
