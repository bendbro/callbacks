package synthesis.callback.examples;

import synthesis.callback.Callback;
import synthesis.callback.wrapper.Function;
import synthesis.callback.wrapper.Variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ben on 6/23/2015.
 */
public class SimpleTest {
    static Function<Object,Void> printFunction = new Function<>(new Callback<Object,Void>() {
        @Override
        public void call(Object input) {
            System.out.println("Print function " + input);
        }

        @Override
        public <T> void onReturn(Callback<Void, T> callback) {
            return;
        }
    });

    static int scale = 3;
    static Function<List<Integer>,List<Integer>> scaleFunction = new Function<>(new Callback<List<Integer>,List<Integer>>() {
        Callback<List<Integer>,?> returnCallback;

        @Override
        public void call(List<Integer> input) {
            List<Integer> newinput = new ArrayList<>();
            for(Integer i : input) {
                newinput.add(i * scale);
            }
            returnCallback.call(Collections.unmodifiableList(newinput));
        }

        @Override
        public <T> void onReturn(Callback<List<Integer>, T> callback) {
            returnCallback = callback;
        }
    });

    static Function<List<Integer>,Void> exitFunction = new Function<>(new Callback<List<Integer>, Void>() {
        @Override
        public void call(List<Integer> input) {
            System.exit(0);
        }

        @Override
        public <T> void onReturn(Callback<Void, T> callback) {
            return;
        }
    });

    public static void main(String[] args) {
        Variable<Integer> source = new Variable<>();

        printFunction.pass(source.all());
        Variable<List<Integer>> aggregated = source.aggregate(5);
        printFunction.pass(aggregated);
        printFunction.pass(scaleFunction.pass(aggregated));
        exitFunction.pass(source.aggregate(10));
        printFunction.pass(source.first());
        printFunction.pass(source.conditional(new Callback<Integer,Boolean>() {

            private Callback<Boolean,?> returnCallback;

            @Override
            public void call(Integer input) {
                returnCallback.call(input %2 == 0);
            }

            @Override
            public <T> void onReturn(Callback<Boolean, T> callback) {
                returnCallback = callback;
            }
        }));

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while(true) {
                    i++;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {

                    }
                    source.update(i);
                }
            }
        }).start();
    }
}
