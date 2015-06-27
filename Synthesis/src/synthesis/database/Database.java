package synthesis.database;

import synthesis.database.framework.IDatabase;
import synthesis.database.framework.IIdentifierFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by ben on 5/17/2015.
 */
public class Database<Identifier> implements IDatabase<Identifier> {
    private IIdentifierFactory<Identifier> identifierFactory;
    private Map<Identifier, Object> entities;
    private Map<Identifier, Collection<Identifier>> members;
    private Map<Identifier, Collection<Identifier>> memberships;
    private Map<Object, Collection<Identifier>> keys;
    private Map<Identifier, Collection<Object>> keyReference;

    public Database(IIdentifierFactory<Identifier> identifierFactory) {
        this.identifierFactory = identifierFactory;
        entities = new HashMap<>();
        members = new HashMap<>();
        memberships = new HashMap<>();
        keys = new HashMap<>();
        keyReference = new HashMap<>();
    }

    @Override
    public Identifier addEntity(Object entity) {
        Identifier identifier = identifierFactory.create();
        entities.put(identifier, entity);
        return identifier;
    }

    @Override
    public <T> T getEntity(Identifier identifier) {
        return (T) entities.get(identifier);
    }

    @Override
    public <T> T deleteEntity(Identifier identifier) {
        T entity = (T) entities.remove(identifier);
        if(keyReference.containsKey(identifier)) {
            for(Object keyEntity : keyReference.remove(identifier)) {
                keys.get(keyEntity).remove(identifier);
            }
        }
        return entity;
    }

    @Override
    public Collection<Identifier> getEntities() {
        return entities.keySet();
    }

    @Override
    public void setEntity(Identifier identifier, Object entity) {
        entities.put(identifier, entity);
    }

    @Override
    public Identifier addKey(Object keyEntity) {
        Identifier identifier = addEntity(keyEntity);

        if(!keys.containsKey(keyEntity)) {
            keys.put(keyEntity, new HashSet<>());
        }
        keys.get(keyEntity).add(identifier);

        if(!keyReference.containsKey(identifier)) {
            keyReference.put(identifier, new HashSet<>());
        }
        keyReference.get(identifier).add(keyEntity);

        return identifier;
    }

    @Override
    public void removeKey(Identifier identifier) {
        Object keyEntity = getEntity(identifier);
        keys.get(keyEntity).remove(identifier);
        keyReference.get(identifier).remove(keyEntity);
    }

    @Override
    public Collection<Object> getKeys() {
        return keys.keySet();
    }

    @Override
    public Collection<Identifier> getIdentifiers(Object keyEntity) {
        return keys.get(keyEntity);
    }

    @Override
    public Collection<Identifier> getMembers(Identifier identifier) {
        return members.get(identifier);
    }

    @Override
    public Collection<Identifier> getMemberships(Identifier identifier) {
        return memberships.get(identifier);
    }
}
