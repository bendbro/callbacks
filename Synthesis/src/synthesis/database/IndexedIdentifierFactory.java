package synthesis.database;

import synthesis.database.framework.IIdentifierFactory;

/**
 * Created by ben on 6/8/2015.
 */
public class IndexedIdentifierFactory implements IIdentifierFactory<Integer> {
    private int index;

    public IndexedIdentifierFactory() {
        index = 0;
    }

    @Override
    public Integer create() {
        return index++;
    }
}
