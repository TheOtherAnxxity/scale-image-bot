package io.github.theotheranxxity.util.function;

import java.util.Objects;

/**
 * Copy of {@link org.apache.logging.log4j.util.TriConsumer} to avoid dependency on Log4J, along with some useful additions.
 *
 * @param <M> the type of the first argument to the operation
 * @param <N> the type of the second argument to the operation
 * @param <O> the type of the third argument to the operation
 *
 * @see BiConsumer
 */
@FunctionalInterface
public interface TriConsumer<M, N, O> {

    void accept(M m, N n, O o);

    default TriConsumer<M, N, O> andThen(TriConsumer<? super M, ? super N, ? super O> after) {
        Objects.requireNonNull(after);

        return (m, n, o) -> {
            accept(m, n, o);
            after.accept(m, n, o);
        };
    }

}

