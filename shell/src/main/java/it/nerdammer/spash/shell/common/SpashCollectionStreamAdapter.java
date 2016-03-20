package it.nerdammer.spash.shell.common;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.PrintWriter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Adapts a Java {@code Iterable} to a SpashCollection.
 *
 * @author Nicola Ferraro
 */
public class SpashCollectionStreamAdapter<T> implements SpashCollection<T> {

    /**
     * The iterable object.
     */
    private Stream<T> target;

    public SpashCollectionStreamAdapter(Stream<T> target) {
        this.target = target;
    }

    @Override
    public void mkString(PrintWriter writer) {
        target.forEach(el -> writer.println(el != null ? el.toString() : ""));
    }

    @Override
    public <R> SpashCollection<R> map(Function<T, R> f) {
        return new SpashCollectionStreamAdapter<>(target.map(f));
    }

    @Override
    public SpashCollection<T> union(SpashCollection<T> coll) {
        return new SpashCollectionUnionAdapter<>(this, coll);
    }

    @Override
    public SpashCollection<T> filter(Function<T, Boolean> condition) {
        return new SpashCollectionStreamAdapter<>(target.filter(e -> condition.apply(e)));
    }

    @Override
    public JavaRDD<T> toRDD(JavaSparkContext sc) {
        return sc.parallelize(this.target.collect(Collectors.toList()));
    }

    @Override
    public List<T> collect() {
        return this.target.collect(Collectors.toList());
    }
}
