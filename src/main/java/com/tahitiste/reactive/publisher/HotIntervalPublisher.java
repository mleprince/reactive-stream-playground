package com.tahitiste.reactive.publisher;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class HotIntervalPublisher extends ComposablePublisher<Long> {

    private final List<HotIntervalSubscription> subscriptionList;

    private final AtomicLong tick = new AtomicLong();

    public HotIntervalPublisher(Duration interval) {

        this.subscriptionList = new ArrayList<>();

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::tick, interval.toNanos(), interval.toNanos(), TimeUnit.NANOSECONDS);
    }

    private void tick() {
        long value = tick.getAndIncrement();

        Iterator<HotIntervalSubscription> subscriptionIterator = subscriptionList.iterator();

        while (subscriptionIterator.hasNext()) {
            HotIntervalSubscription hotIntervalSubscription = subscriptionIterator.next();
            if(!hotIntervalSubscription.canceled) {
                if(hotIntervalSubscription.numberOfRequestReceived >0) {
                    hotIntervalSubscription.subscriber.onNext(value);
                } else {
                    hotIntervalSubscription.subscriber.onError(new Throwable("Could not produce tick due to lack of request"));
                }
            } else {
                subscriptionIterator.remove();
            }
        }
    }

    @Override
    public void subscribe(Flow.Subscriber<? super Long> newSubscriber) {

        HotIntervalSubscription hotIntervalSubscription = new HotIntervalSubscription(newSubscriber);

        subscriptionList.add(hotIntervalSubscription);

        newSubscriber.onSubscribe(hotIntervalSubscription);
    }

    private static class HotIntervalSubscription implements Flow.Subscription {

        private final Flow.Subscriber<? super Long> subscriber;

        private long numberOfRequestReceived = 0;
        private boolean canceled = false;

        public HotIntervalSubscription(Flow.Subscriber<? super Long> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            if (!this.canceled) {
                numberOfRequestReceived += n;
            }
        }

        @Override
        public void cancel() {
            this.canceled = true;
        }
    }
}
