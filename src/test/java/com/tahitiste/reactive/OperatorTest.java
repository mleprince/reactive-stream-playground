package com.tahitiste.reactive;

import com.tahitiste.reactive.publisher.ArrayPublisher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Flow;

@Slf4j
class OperatorTest {

    @Test
    void map() {
        new ArrayPublisher<>(List.of("ho", "hi", "ca", "cu", "pi"))
                .map(value -> value + value)
                .subscribe(new Flow.Subscriber<>() {
                    @Override
                    public void onSubscribe(Flow.Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(String s) {
                        LOGGER.info("{}", s);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Test
    void filter() {

        List<String> cloudTeamMembers = List.of("alex", "manu", "max", "yunus");

        new ArrayPublisher<>(cloudTeamMembers)
                .filter(this::isMeilleurCoderDeFrance)
                .subscribe(new Flow.Subscriber<>() {
                    @Override
                    public void onSubscribe(Flow.Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(String s) {
                        LOGGER.info("{}", s);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private boolean isMeilleurCoderDeFrance(String member) {
        return member.equals("alex");
    }

}
