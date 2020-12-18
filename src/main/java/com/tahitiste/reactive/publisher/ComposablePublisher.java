package com.tahitiste.reactive.publisher;

import com.tahitiste.reactive.operator.FilterOperator;
import com.tahitiste.reactive.operator.MapOperator;

import java.util.concurrent.Flow;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class ComposablePublisher<U> implements Flow.Publisher<U> {

    public <T> ComposablePublisher<T> map(Function<U, T> mapper) {
        return new MapOperator<>(mapper, this);
    }

    public ComposablePublisher<U> filter(Predicate<U> predicate) {
        return new FilterOperator<>(predicate, this);
    }

}
