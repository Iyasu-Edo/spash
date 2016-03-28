package it.nerdammer.spash.shell.common;

import ch.lambdaj.Lambda;
import ch.lambdaj.function.convert.Converter;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.io.PrintWriter;
import java.util.List;

/**
 * Adapts a Java {@code Iterable} to a SpashCollection.
 *
 * @author Nicola Ferraro
 */
public class SpashCollectionListAdapter<T> implements SpashCollection<T> {

    /**
     * The iterable object.
     */
    private List<T> target;

    public SpashCollectionListAdapter(List<T> target) {
        this.target = target;
    }

    @Override
    public void mkString(PrintWriter writer) {
        for(T el : target) {
            writer.println(el != null ? el.toString() : "");
        }
    }

    @Override
    public <R> SpashCollection<R> map(final SerializableFunction<T, R> f) {
        return new SpashCollectionListAdapter<>(Lambda.convert(target, new Converter<T, R>() {
            @Override
            public R convert(T t) {
                return f.apply(t);
            }
        }));
    }

    @Override
    public SpashCollection<T> union(SpashCollection<T> coll) {
        return new SpashCollectionUnionAdapter<>(this, coll);
    }

    @Override
    public SpashCollection<T> filter(final SerializableFunction<T, Boolean> condition) {
        return new SpashCollectionListAdapter<>(Lambda.filter(new BaseMatcher<T>() {
            @Override
            public boolean matches(Object o) {
                return condition.apply((T) o);
            }
            @Override
            public void describeTo(Description description) {
            }
        }, target));
    }

    @Override
    public JavaRDD<T> toRDD(JavaSparkContext sc) {
        return sc.parallelize(this.target);
    }

    @Override
    public List<T> collect() {
        return this.target;
    }
}
