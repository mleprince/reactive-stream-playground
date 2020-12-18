package com.tahitiste.reactive.publisher;

import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Flow;

@AllArgsConstructor
public class ArrayPublisher<U> extends ComposablePublisher<U> {

    private final List<U> list;

    @Override
    public void subscribe(Flow.Subscriber<? super U> subscriber) {
        subscriber.onSubscribe(
                new ArraySubscription<U>(subscriber, list)
        );
    }

    private static class ArraySubscription<U> implements Flow.Subscription {

        private final Flow.Subscriber<? super U> subscriber;
        private final Iterator<U> iterator;

        private boolean canceled = false;

        private ArraySubscription(Flow.Subscriber<? super U> subscriber, List<U> list) {
            this.subscriber = subscriber;
            this.iterator = list.iterator();
        }

        @Override
        public void request(long numberOfRequests) {

            if (canceled) {
                return;
            }

            for (int i = 0; i < numberOfRequests; i++) {

                if (iterator.hasNext()) {
                    subscriber.onNext(iterator.next());
                } else {
                    subscriber.onComplete();
                    break;
                }
            }

        }

        @Override
        public void cancel() {
            this.canceled = true;
        }
    }
}
