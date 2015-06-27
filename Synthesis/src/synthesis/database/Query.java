package synthesis.database;

import synthesis.database.framework.IDatabase;

import java.util.*;

/**
 * Created by ben on 6/10/2015.
 */
public class Query<Identifier> {
    private IDatabase<Identifier> database;
    private Set<Identifier> query;

    public Query(IDatabase<Identifier> database) {
        this.database = database;
        query = new HashSet<>();
        query.addAll(database.getEntities());
    }

    public IDatabase<Identifier> getDatabase() {
        return database;
    }

    public Query<Identifier> fork() {
        Query newQuery = new Query(database);
        newQuery.query.clear();
        newQuery.query.addAll(query);
        return newQuery;
    }

    public Map<Set<Identifier>, Object> meaning() {
        Map<Set<Identifier>, Object> meaning = new HashMap<>();
        for(Identifier identifier : query) {

        }
        return meaning;
    }

    public Collection<Object> values() {
        List<Object> values = new LinkedList<>();
        for(Identifier identifier : query) {
            values.add(database.getEntity(identifier));
        }
        return values;
    }

    public void matching(Object key) {
        query.retainAll(database.getIdentifiers(key));
    }

    public void and(Query other) {
        query.retainAll(other.query);
    }

    public void or(Query other) {
        query.addAll(other.query);
    }

    public void members() {
        Set<Identifier> newQuery = new HashSet<>();
        for(Identifier identifier : query) {
            newQuery.addAll(database.getMembers(identifier));
        }
        query = newQuery;
    }

    public void memberships() {
        Set<Identifier> newQuery = new HashSet<>();
        for(Identifier identifier : query) {
            newQuery.addAll(database.getMemberships(identifier));
        }
        query = newQuery;
    }
}
