package com.example.rxjava;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;

public class App {
    public static void main( String[] args ) throws InterruptedException {
        Observable<String> create = Observable.create(emitter -> {
           try {
               emitter.onNext("message1");
               emitter.onNext("message2");
               emitter.onNext("message3");
               emitter.onComplete();
           } catch (Exception e) {
               emitter.onError(e);
           }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(String s) {
                System.out.println("Received " + s);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("Completed");
            }
        };
        create.subscribe(observer);

        // cold observable
        Observable<String> just = Observable.just("just message1", "just message2", "just message3");
        just.subscribe(System.out::println, Throwable::printStackTrace, () -> System.out.println("Completed"));

        // create from a list
        List<String> usrs = Arrays.asList("usr1", "usr2");
        Observable<String> uObservable = Observable.fromIterable(usrs);
        uObservable.subscribe(System.out::println);

        // convert cold observable to hot observable
        ConnectableObservable<String> hot = just.publish();
        hot.subscribe(msg -> System.out.println("HOt1 " + msg));
        hot.subscribe(msg -> System.out.println("HOt2 " + msg));
        hot.connect();

        Observable.interval(200, TimeUnit.MILLISECONDS).subscribe(System.out::println);
        Thread.sleep(2000); // main thread will sleep even before this code runs. halt the main thread for 2 seconds

        Observable.range(0, 10).subscribe(System.out::println);

        Observable.empty(); // will emit nothing but calls onComplete
        Observable.never(); // will emit nothing and keeps the observer waiting forever
        // stop the main thread for above 2 observables

        Completable.fromRunnable(() -> System.out.println("before complete")).subscribe(() -> System.out.println("Done!"));
    }
}
