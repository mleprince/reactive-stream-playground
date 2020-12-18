package com.tahitiste.reactive;

import com.tahitiste.reactive.publisher.ArrayPublisher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Flow;

@Slf4j
class ArrayPublisherTest {

    private static final List<String> POKEMON_LIST = List.of("Pikachu", "Salameche", "Bulbizarre", "Carapuce");

    @Test
    void subscribeAll() {

        new ArrayPublisher<>(POKEMON_LIST).subscribe(
                new Flow.Subscriber<>() {

                    @Override
                    public void onSubscribe(Flow.Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(String nextPokemon) {
                        LOGGER.info("onNext : {}", nextPokemon);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        // do nothing
                    }

                    @Override
                    public void onComplete() {
                        LOGGER.info("Complete !");
                    }
                });
    }

    @Test
    void subscribeAndConsumeStepByStep() {

        new ArrayPublisher<>(POKEMON_LIST).subscribe(
                new Flow.Subscriber<>() {

                    private boolean terminated = false;

                    private Flow.Subscription pokemonSubscription;

                    @Override
                    public void onSubscribe(Flow.Subscription subscription) {
                        pokemonSubscription = subscription;
                        pokemonSubscription.request(1);
                    }

                    @Override
                    public void onNext(String nextPokemon) {
                        LOGGER.info("{}", nextPokemon);
                        // do something

                        // ask for the next one
                        if (!terminated) {
                            pokemonSubscription.request(1);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        // do nothing
                    }

                    @Override
                    public void onComplete() {
                        LOGGER.info("Complete !");
                        this.terminated = true;
                    }
                }
        );
    }
}
