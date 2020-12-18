package com.tahitiste.reactive.operator;

import com.tahitiste.reactive.publisher.ComposablePublisher;

import java.util.concurrent.Flow;
import java.util.function.Function;

public class MapOperator<T, U> extends ComposablePublisher<U> {

    private final Function<T, U> mapper;
    private final ComposablePublisher<T> inner;

    public MapOperator(Function<T, U> mapper, ComposablePublisher<T> inner) {
        this.mapper = mapper;
        this.inner = inner;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super U> subscriber) {
        inner.subscribe(new Flow.Subscriber<T>() {
            @Override
            public void onSubscribe(Flow.Subscription s) {
                subscriber.onSubscribe(s);
            }

            @Override
            public void onNext(T t) {
                subscriber.onNext(mapper.apply(t));
            }

            @Override
            public void onError(Throwable t) {
                subscriber.onError(t);
            }

            @Override
            public void onComplete() {
                subscriber.onComplete();
            }
        });
    }
}
