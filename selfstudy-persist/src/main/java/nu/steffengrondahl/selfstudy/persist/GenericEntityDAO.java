package nu.steffengrondahl.selfstudy.persist;

import java.util.List;

/**
 * Created by Steffen on 29-10-2016.
 */
public interface GenericEntityDAO<T> {

    Integer add(T t);

    void update(T t);

    void delete(T t);

    T find(Integer key, boolean decorate);

    List<T> query(QuerySpecification specification);

}
