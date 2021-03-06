package nu.steffengrondahl.selfstudy.persist;

import java.util.List;

/**
 * Generic data accessor object for entities in nu.steffengrondahl.selfstudy.persist.domain package
 *
 * Created by Steffen on 29-10-2016.
 */
public interface GenericEntityDAO<T> {

    Integer add(T t);

    Integer update(T t);

    void delete(T t);

    T find(Integer key, boolean decorate);

    List<T> query(QuerySpecification specification);

}
