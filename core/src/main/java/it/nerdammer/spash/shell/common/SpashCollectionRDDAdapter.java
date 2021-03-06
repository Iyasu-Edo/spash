package it.nerdammer.spash.shell.common;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

/**
 * Adapts a {@code org.apache.spark.api.java.JavaRDD} to a {@code SpashCollection}.
 *
 * @author Nicola Ferraro
 */
public class SpashCollectionRDDAdapter<T> implements SpashCollection<T> {

    private JavaRDD<T> target;

    public SpashCollectionRDDAdapter(JavaRDD<T> target) {
        this.target = target;
    }

    @Override
    public void mkString(PrintWriter writer) {
        Iterator<T> it = target.toLocalIterator();
        while(it.hasNext()) {
            T el = it.next();
            writer.println(el != null ? el.toString() : "");
        }
    }

    @Override
    public <R> SpashCollection<R> map(final SerializableFunction<T, R> f) {
        return new SpashCollectionRDDAdapter<>(target.map(f));
    }

    @Override
    public SpashCollection<T> union(SpashCollection<T> coll) {
        return new SpashCollectionUnionAdapter<>(this, coll);
    }

    @Override
    public SpashCollection<T> filter(SerializableFunction<T, Boolean> condition) {
        return new SpashCollectionRDDAdapter<>(target.filter(condition));
    }

    @Override
    public JavaRDD<T> toRDD(JavaSparkContext sc) {
        return this.target;
    }

    @Override
    public List<T> collect() {
        return this.target.collect();
    }
}
