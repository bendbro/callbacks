package synthesis.callback.wrapper;

import synthesis.callback.Callback;

/**
 * Created by ben on 6/14/2015.
 * @param <I> The minimum bound of object required for the callback.
 * @param <O> The return type of the callback.
 */
public class Function<I,O> {
    private Callback<I,O> callback;

    public Function(Callback<I,O> callback) {
        this.callback = callback;
    }

    //TODO: difference between stateless and stateful callbacks?
    public <IP extends I> Variable<O> pass(Variable<IP> parameterVariable) {
        parameterVariable.onUpdate(callback);
        Variable<O> returnVariable = new Variable<>();
        callback.onReturn(new Callback<O, Void>() {
            @Override
            public void call(O input) {
                returnVariable.update(input);
            }

            @Override
            public <T> void onReturn(Callback<Void, T> callback) {
                return;
            }
        });
        return returnVariable;
    }
}
