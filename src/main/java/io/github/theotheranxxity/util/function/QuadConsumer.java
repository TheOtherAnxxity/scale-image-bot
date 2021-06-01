package io.github.theotheranxxity.util.function;

import java.util.Objects;

/**
 * Represents an operation that accepts two input arguments and returns no
 * result. This is the four-arity specialization of {@link java.util.function.Consumer}.
 * Unlike most other functional interfaces, {@code QuadConsumer} is expected
 * to operate via side-effects.
 *
 * <p>This is a functional interface whose functional method is {@link #accept(Object, Object, Object, Object)}.
 *
 * @param <P> the type of the first argument to the operation
 * @param <Q> the type of the second argument to the operation
 * @param <R> the type of the third argument to the operation
 * @param <S> the type of the fourth argument to the operation
 *
 * @see TriConsumer
 */
@FunctionalInterface
public interface QuadConsumer<P, Q, R, S> {

    void accept(P p, Q q, R r, S s);

    default QuadConsumer<P, Q, R, S> andThen(QuadConsumer<? super P, ? super Q, ? super R, ? super S> after) {
        Objects.requireNonNull(after);

        return (p, q, r, s) -> {
            accept(p, q, r, s);
            after.accept(p, q, r, s);
        };
    }

}
