package synthesis.callback.core;

import synthesis.callback.Callback;

/**
 * Created by ben on 6/23/2015.
 */
public class Wrapper<I,O> {
    private O result;
    private Object waiter;
    private Callback<I,O> callback;

    public Wrapper(Callback<I,O> callback) {
        result = null;
        waiter = new Object();
        this.callback = callback;
        callback.onReturn(new Callback<O,Void>() {
            @Override
            public void call(O input) {
                ready(input);
            }

            @Override
            public <T> void onReturn(Callback<Void, T> callback) {
                return;
            }
        });
    }

    public O call(I data) {
        synchronized (waiter) {
            callback.call(data);
            if(result == null) {
                try {
                    System.out.println("Waiting");
                    waiter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            O retresult = result;
            result = null;
            return retresult;
        }
    }

    public void ready(O result) {
        this.result = result;
        waiter.notify();
    }
}
