package synthesis.callback.wrapper;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import synthesis.callback.Callback;
import synthesis.callback.core.Wrapper;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by ben on 6/14/2015.
 * @param <T> The type stored by this variable.
 */
public class Variable<T> {
    private List<Callback<? super T,?>> updateCallbacks;
    private List<Callback<? super T,?>> removals;

    public Variable() {
        updateCallbacks = new LinkedList<>();
        removals = new LinkedList<>();
    }

    /**
     * Update the value of this variable.
     * @param value The new value.
     */
    public synchronized <TP extends T> void update(TP value) {
        updateCallbacks.removeAll(removals);
        removals.clear();
        for(Callback<? super T,?> updateCallback : updateCallbacks) {
            updateCallback.call(value);
        }
    }

    /**
     * Set the callback handler for this variable.
     * @param callback The handler.
     */
    public synchronized <O> void onUpdate(Callback<? super T,O> callback) {
        updateCallbacks.add(callback);
    }

    private synchronized <O> void remove(Callback<? super T,O> callback) {
        removals.add(callback);
    }

    /**
     * The called variable recieves no data, updates are piped to the returned Variable of this function.
     * @return The piped to variable.
     */
    public Variable<T> absorb() {
        Variable<T> absorbingVariable = new Variable<>();
        updateCallbacks.clear();
        updateCallbacks.add(new Callback<T,T>() {
            @Override
            public void call(T input) {
                absorbingVariable.update(input);
            }

            @Override
            public <T1> void onReturn(Callback<T, T1> callback) {
                throw new IllegalAccessError("As this callback links from the original variable, the onReturn method should not be called.");
            }
        });
        return absorbingVariable;
    }

    /**
     * The returned variable provides only the first output of the called variable.
     * @return The variable providing only the first result.
     */
    public Variable<T> first() {
        Variable<T> first = new Variable<>();
        updateCallbacks.add(new Callback<T,T>() {
            @Override
            public void call(T input) {
                first.update(input);
                remove(this);
            }

            @Override
            public <T1> void onReturn(Callback<T, T1> callback) {
                throw new IllegalAccessError("As this callback links from the original variable, the onReturn method should not be called.");
            }
        });
        return first;
    }

    public Variable<T> conditional(Callback<T,Boolean> decision) {
        Variable<T> conditional = new Variable<>();
        onUpdate(new Callback<T, T>() {
            private Wrapper<T,Boolean> decisionWrapper = new Wrapper(decision);

            @Override
            public void call(T input) {
                if(decisionWrapper.call(input)) {
                    conditional.update(input);
                }
            }

            @Override
            public <T1> void onReturn(Callback<T, T1> callback) {
                return;
            }
        });
        return conditional;
    }

    /**
     * Provides every update to this variable. (Note that a copy of the called Variable does the same).
     * @return A variable that gives every update.
     */
    public Variable<T> all() {
        Variable all = new Variable<T>();
        updateCallbacks.add(new Callback<T,T>() {
            @Override
            public void call(T input) {
                all.update(input);
            }

            @Override
            public <T1> void onReturn(Callback<T, T1> callback) {
                throw new IllegalAccessError("As this callback links from the original variable, the onReturn method should not be called.");
            }
        });
        return all;
    }

    /**
     * Combines calls from each variable.
     * @param other
     * @param <U>
     * @return
     */
    public <U> Variable<Map<Type,Object>> and(Variable<U> other) {
        return null;
    }

    /**
     * Aggregates amount calls from this variable.
     * @param amount
     * @return
     */
    public Variable<List<T>> aggregate(int amount) {
        Variable<List<T>> first = new Variable<>();
        updateCallbacks.add(new Callback<T,T>() {
            List<T> aggregated = new LinkedList<>();
            int count = 0;

            @Override
            public void call(T input) {
                aggregated.add(input);
                count++;
                if(count == amount) {
                    first.update(aggregated);
                    remove(this);
                }
            }

            @Override
            public <T1> void onReturn(Callback<T, T1> callback) {
                throw new IllegalAccessError("As this callback links from the original variable, the onReturn method should not be called.");
            }
        });
        return first;
    }

    static class Void {
        public static final Void Instance = new Void();
    }
}
