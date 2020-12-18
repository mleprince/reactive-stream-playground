package com.tahitiste.reactive;

import com.tahitiste.reactive.publisher.ColdIntervalPublisher;
import com.tahitiste.reactive.publisher.HotIntervalPublisher;
import com.tahitiste.reactive.publisher.ComposablePublisher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.Flow;

@Slf4j
class IntervalPublisherTest {

    @Test
    void coldInterval() throws InterruptedException {
        new ColdIntervalPublisher(Duration.ofSeconds(1)).subscribe(
                new Flow.Subscriber<>() {
                    @Override
                    public void onSubscribe(Flow.Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        LOGGER.info("tick received");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        LOGGER.error("error", throwable);
                    }

                    @Override
                    public void onComplete() {
                        LOGGER.info("complete !");
                    }
                }
        );

        // we need to sleep the main thread because the publisher use another thread
        Thread.sleep(10000);
    }


    @Test
    void hotInterval() throws InterruptedException {

        ComposablePublisher<Long> interval = new HotIntervalPublisher(Duration.ofSeconds(1));

        interval.subscribe(new Flow.Subscriber<>() {

            private final Logger LOGGER = LoggerFactory.getLogger("first");

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Long value) {
                LOGGER.info("{}", value);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });

        Thread.sleep(3500);

        interval.subscribe(new Flow.Subscriber<>() {

            private final Logger LOGGER = LoggerFactory.getLogger("second");

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Long value) {
                LOGGER.info("{}", value);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });

        Thread.sleep(5000);
    }


}
