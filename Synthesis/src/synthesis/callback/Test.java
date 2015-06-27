package synthesis.callback;

import synthesis.callback.wrapper.Function;
import synthesis.callback.wrapper.Variable;

import java.util.List;

/**
 * Created by ben on 6/14/2015.
 */
public class Test {
    public static void main(String[] args) {
        ExternalFunction spaceBarPressed = new ExternalFunction(null);
        ExternalFunction gameTick = new ExternalFunction(null);
        Function<Void,Egg> layEgg = new Function<>(null);
        Function<Egg,CrackedEgg> crackEgg = new Function<>(null);
        Function<List<CrackedEgg>,Void> updateScreen = new Function<>(null);

        Variable<Egg> layedEgg = layEgg.pass(spaceBarPressed.pass());
        Variable<CrackedEgg> crackedEgg = crackEgg.pass(layedEgg);
        updateScreen.pass(crackedEgg.aggregate(10));
    }

    class Hen {

    }

    class Egg {

    }

    class CrackedEgg {

    }

    class Cracker {

    }

    class Screen {

    }

    static class ExternalFunction extends Function<Void,Void> {
        public ExternalFunction(Callback<Void, Void> callback) {
            super(callback);
        }

        public Variable<Void> pass() {
            return null;
        }
    }
}
