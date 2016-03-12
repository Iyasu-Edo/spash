package it.nerdammer.spash.shell.common;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
        target.forEach(el -> {
            writer.print(el != null ? el.toString() : "");
            writer.print("\r\n");
        });
    }

    @Override
    public <R> SpashCollection<R> map(Function<T, R> f) {
        return new SpashCollectionStreamAdapter<>(target.map(f));
    }
}
