package synthesis.callback.core;

import synthesis.callback.Callback;

/**
 * Created by ben on 6/17/2015.
 */
public class CallbackUtility {
    /**
     * A callback that does nothing.
     */
    public static Callback<?,?> NullCallbackInstance = new Callback<Object, Object>() {
        @Override
        public void call(Object input) {
            return;
        }

        @Override
        public <T> void onReturn(Callback<Object, T> callback) {
            return;
        }
    };

    /**
     * Wrap the provided callback in an async callback.
     * @param callback The callback.
     * @return
     */
    public static <I,O> Callback<I,O> makeAsyncSequential(Callback<I,O> callback) {
        CallbackRunnable<I,O> callbackRunnable = new CallbackRunnable<>(callback);
        Thread thread = new Thread(callbackRunnable);
        thread.start();
        return new Callback<I,O>() {
            @Override
            public void call(I input) {
                callbackRunnable.call(input);
            }

            @Override
            public <T> void onReturn(Callback<O, T> returnCallback) {
                callback.onReturn(returnCallback);
            }
        };
    }

}
