package nu.steffengrondahl.selfstudy.rest.model;

import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
public interface GenericDAO<T> {

    public T read(Integer key);

    public List<T> readAll();

    public List<T> readSelected(Integer minPriority, Integer maxPriority, Integer minStatus, Integer maxStatus);

    public void create(T t);

    public void update(T t);

    public void delete(T t);
}
