package it.nerdammer.spash.shell.common;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.PrintWriter;
import java.util.List;

/**
 * A SpashCollection that is the union of two {@code SpashCollection}s.
 *
 * @author Nicola Ferraro
 */
public class SpashCollectionUnionAdapter<T> implements SpashCollection<T> {

    /**
     * The first collection.
     */
    private SpashCollection<T> one;

    /**
     * The second collection.
     */
    private SpashCollection<T> two;

    public SpashCollectionUnionAdapter(SpashCollection<T> one, SpashCollection<T> two) {
        this.one = one;
        this.two = two;
    }

    @Override
    public void mkString(PrintWriter writer) {
        one.mkString(writer);
        two.mkString(writer);
    }

    @Override
    public <R> SpashCollection<R> map(SerializableFunction<T, R> f) {
        return new SpashCollectionUnionAdapter<>(one.map(f), two.map(f));
    }

    @Override
    public SpashCollection<T> union(SpashCollection<T> coll) {
        return new SpashCollectionUnionAdapter<>(this, coll);
    }

    @Override
    public SpashCollection<T> filter(SerializableFunction<T, Boolean> condition) {
        return new SpashCollectionUnionAdapter<>(one.filter(condition), two.filter(condition));
    }

    @Override
    public JavaRDD<T> toRDD(JavaSparkContext sc) {
        return this.one.toRDD(sc).union(this.two.toRDD(sc));
    }

    @Override
    public List<T> collect() {
        List<T> res = one.collect();
        res.addAll(two.collect());
        return res;
    }
}
