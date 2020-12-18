package com.tahitiste.reactive.operator;

import com.tahitiste.reactive.publisher.ComposablePublisher;

import java.util.concurrent.Flow;
import java.util.function.Predicate;

public class FilterOperator<T> extends ComposablePublisher<T> {

    private final Predicate<T> filter;
    private final ComposablePublisher<T> inner;

    public FilterOperator(Predicate<T> filter, ComposablePublisher<T> inner) {
        this.filter = filter;
        this.inner = inner;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        inner.subscribe(new Flow.Subscriber<T>() {
            @Override
            public void onSubscribe(Flow.Subscription s) {
                subscriber.onSubscribe(s);
            }

            @Override
            public void onNext(T t) {
                if (filter.test(t)) {
                    subscriber.onNext(t);
                }
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

