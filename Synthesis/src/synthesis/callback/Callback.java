package synthesis.callback;

import synthesis.callback.core.CallbackRunnable;

/**
 * Created by ben on 5/17/2015.
 * An thing that given an input provides something back.
 * The affect of external or internal state on the callback is programmer defined.
 */
public interface Callback<I,O> {
    /**
     * Call this function with the given input.
     * @param input The input.
     */
    public void call(I input);

    /**
     * On a return from this function, execute the given callback.
     * @param callback The callback to execute.
     * @param <T> The output type of the callback.
     */
    public <T> void onReturn(Callback<O,T> callback);
}
