package it.nerdammer.spash.shell.common;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.rdd.EmptyRDD;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * An empty SpashCollection.
 *
 * @author Nicola Ferraro
 */
public class SpashCollectionEmptyAdapter<T> implements SpashCollection<T> {

    public SpashCollectionEmptyAdapter() {
    }

    @Override
    public void mkString(PrintWriter writer) {
    }

    @Override
    public <R> SpashCollection<R> map(Function<T, R> f) {
        return new SpashCollectionEmptyAdapter<>();
    }

    @Override
    public SpashCollection<T> union(SpashCollection<T> coll) {
        return coll;
    }

    @Override
    public SpashCollection<T> filter(Function<T, Boolean> condition) {
        return this;
    }

    @Override
    public JavaRDD<T> toRDD(JavaSparkContext sc) {
        return sc.emptyRDD();
    }

    @Override
    public List<T> collect() {
        return Collections.emptyList();
    }
}
