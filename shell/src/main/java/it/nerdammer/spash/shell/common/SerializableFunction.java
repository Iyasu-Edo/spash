package it.nerdammer.spash.shell.common;

import java.io.Serializable;
import java.util.function.Function;

/**
 * A {@code Function} that is also {@code Serializable}.
 *
 * @author Nicola Ferraro
 */
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {
}
