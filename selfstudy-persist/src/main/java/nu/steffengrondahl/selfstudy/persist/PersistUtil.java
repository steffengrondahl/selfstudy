package nu.steffengrondahl.selfstudy.persist;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Simple util for creating and closing javax.persistence.EntityManagerFactory
 *
 * Created by Steffen on 29-10-2016.
 */
public class PersistUtil {
    private static final EntityManagerFactory entityManagerFactory = buildEntityManagerFactory();

    private static EntityManagerFactory buildEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("nu.steffengrondahl.project.persist");
    }

    static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public static void shutdown() {
        entityManagerFactory.close();
    }
}
