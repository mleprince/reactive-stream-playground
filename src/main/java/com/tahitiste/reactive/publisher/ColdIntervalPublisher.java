package com.tahitiste.reactive.publisher;

import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class ColdIntervalPublisher extends ComposablePublisher<Long> {

    private final Duration interval;

    @Override
    public void subscribe(Flow.Subscriber<? super Long> subscriber) {
        subscriber.onSubscribe(new ColdIntervalSubscription(interval, subscriber));
    }

    private static class ColdIntervalSubscription implements Flow.Subscription {


        private final Flow.Subscriber<? super Long> subscriber;
        private final ScheduledExecutorService executorService;

        private long tick = 0;
        private long numberOfRequestReceived = 0;

        public ColdIntervalSubscription(
                Duration duration,
                Flow.Subscriber<? super Long> subscriber
        ) {

            this.subscriber = subscriber;

            this.executorService = Executors.newSingleThreadScheduledExecutor();

            this.executorService.scheduleAtFixedRate(this::tick, duration.toNanos(), duration.toNanos(), TimeUnit.NANOSECONDS);
        }

        private void tick() {
            if (numberOfRequestReceived > 0) {
                subscriber.onNext(tick++);
            } else {
                subscriber.onError(new Throwable("Could not produce tick due to lack of request"));
            }
        }

        @Override
        public void request(long n) {
            numberOfRequestReceived += n;
        }

        @Override
        public void cancel() {
            this.executorService.shutdown();
        }
    }
}
