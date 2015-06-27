package synthesis.database.framework;

import java.util.Collection;

/**
 * Created by ben on 6/2/2015.
 */
public interface IDatabase<Identifier> {
    /**
     * Add an entity to the database.
     * @param entity The entity.
     * @return The assigned identifier of the entity.
     */
    public Identifier addEntity(Object entity);

    /**
     * Get an entity from the database by it's identifier.
     * @param identifier The identifier.
     * @param <T> The type of the entity.
     * @return The entity.
     */
    public <T> T getEntity(Identifier identifier);

    /**
     * Delete an entity from the database via it's identifier.
     * If an identifier is referenced by a key entity, that reference is also deleted.
     * @param identifier The identifier.
     * @param <T> The type of the entity.
     * @return The deleted entity.
     */
    public <T> T deleteEntity(Identifier identifier);

    /**
     * Set an identifier to refer to a different entity.
     * @param identifier The identifier.
     * @param entity The entity.
     * @param <T>
     * @return
     */
    public void setEntity(Identifier identifier, Object entity);

    /**
     * Get all the entities in the database.
     * @return The entities.
     */
    public Collection<Identifier> getEntities();

    /**
     * Add an identifier to be referenced by a key entity.
     * @param keyEntity The key entity.
     * @returnThe identifier.
     */
    public Identifier addKey(Object keyEntity);

    /**
     * Remove an identifier referencing a key entity.
     * @param identifier The identifier.
     */
    public void removeKey(Identifier identifier);

    /**
     * List the keys in the database.
     * @return The keys.
     */
    public Collection<Object> getKeys();

    /**
     * Get the identifiers associated with a key entity.
     * @param keyEntity The key entity.
     * @return The identifiers.
     */
    public Collection<Identifier> getIdentifiers(Object keyEntity);

    /**
     * Get the members of this identifiers group.
     * @param identifier The identifier.
     * @return The members' identifiers.
     */
    public Collection<Identifier> getMembers(Identifier identifier);

    /**
     * Get the groups this identifier is a part of.
     * @param identifier The identifier.
     * @return The groups' identifiers.
     */
    public Collection<Identifier> getMemberships(Identifier identifier);
}
