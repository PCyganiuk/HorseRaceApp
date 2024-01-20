package dataAccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class Repository<TEntity extends Entity> {
    protected Connection connection;

    public Repository(Connection connection) {
        this.connection = connection;
    }

    public abstract List<TEntity> getAll() throws SQLException;
    abstract TEntity getById(int id) throws SQLException;
    abstract void insert(TEntity entity) throws SQLException;
    abstract void update(TEntity entity) throws SQLException;
    abstract void removeById(int id) throws SQLException;
}
