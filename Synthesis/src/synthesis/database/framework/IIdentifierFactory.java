package synthesis.database.framework;

/**
 * Created by ben on 6/2/2015.
 */
public interface IIdentifierFactory<Identifier> {
    public Identifier create();

    /**
     * on dispose that recycles the identifier back into the system.
     * could be useful for transient data types going through callbacks, recycle when they are finished.
     */
}
