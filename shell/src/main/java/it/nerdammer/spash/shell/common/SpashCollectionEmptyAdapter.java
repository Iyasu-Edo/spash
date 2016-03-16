package it.nerdammer.spash.shell.common;

import java.io.PrintWriter;
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
}
