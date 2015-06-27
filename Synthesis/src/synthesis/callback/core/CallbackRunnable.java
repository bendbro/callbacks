package synthesis.callback.core;

import synthesis.callback.Callback;

import java.util.LinkedList;

/**
 * Created by ben on 5/17/2015.
 */
public class CallbackRunnable<I,O> implements Runnable {
    private Callback<I,O> callback;
    private LinkedList<I> calls;

    public CallbackRunnable(Callback<I,O> aCallback) {
        callback = aCallback;
        calls = new LinkedList<>();
    }

    @Override
    public void run() {
        while(true) {
            I data = null;
            synchronized (calls) {
                if (calls.size() == 0) {
                    try {
                        calls.wait();
                    } catch (InterruptedException e) {
                        throw new IllegalStateException("Call waiter interrupted.");
                    }
                }
                data = calls.removeLast();
            }
            callback.call(data);
        }
    }

    public void call(I data) {
        synchronized (calls) {
            calls.addFirst(data);
            calls.notify();
        }
    }
}
