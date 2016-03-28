package it.nerdammer.spash.shell.common;

import ch.lambdaj.function.convert.Converter;
import org.apache.spark.api.java.function.Function;

import java.io.Serializable;

/**
 * A {@code Function} that is also {@code Serializable}.
 *
 * @author Nicola Ferraro
 */
public abstract class SerializableFunction<T, R> implements Function<T, R>, Converter<T, R>, Serializable {

    @Override
    public R call(T t) throws Exception {
        return apply(t);
    }

    @Override
    public R convert(T t) {
        return apply(t);
    }

    public abstract R apply(T v1);

}
