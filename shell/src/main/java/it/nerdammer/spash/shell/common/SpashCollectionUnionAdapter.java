package it.nerdammer.spash.shell.common;

import java.io.PrintWriter;
import java.util.function.Function;
import java.util.stream.Stream;

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
    public <R> SpashCollection<R> map(Function<T, R> f) {
        return new SpashCollectionUnionAdapter<>(one.map(f), two.map(f));
    }

    @Override
    public SpashCollection<T> union(SpashCollection<T> coll) {
        return new SpashCollectionUnionAdapter<>(this, coll);
    }
}
