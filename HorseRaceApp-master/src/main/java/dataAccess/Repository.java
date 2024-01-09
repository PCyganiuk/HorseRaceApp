package dataAccess;

import java.util.List;

public abstract class Repository<TEntity extends Entity> {
    public abstract List<TEntity> getAll();
    abstract TEntity getById(int id);
    abstract void insert(TEntity entity);
    abstract void update(TEntity entity);
    abstract void removeById(int id);
}
