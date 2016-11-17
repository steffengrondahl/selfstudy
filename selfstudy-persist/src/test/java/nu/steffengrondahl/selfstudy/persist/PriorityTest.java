package nu.steffengrondahl.selfstudy.persist;

import nu.steffengrondahl.selfstudy.persist.PriorityEntityDAO;
import nu.steffengrondahl.selfstudy.persist.QuerySpecificationFactory;
import nu.steffengrondahl.selfstudy.persist.domain.PriorityEntity;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * Created by Steffen on 29-10-2016.
 */
public class PriorityTest {

    private static PriorityEntityDAO dao = new PriorityEntityDAO();

    @Ignore
    @Test
    public void testPriority() {
        List<PriorityEntity> priorityList = dao.query(QuerySpecificationFactory.queryAll());

        int i = 1;
        Assert.assertTrue(priorityList.size() == 5);
        for (PriorityEntity pe : priorityList) {
            Assert.assertTrue(pe.getId().intValue() == i++);
            System.out.printf("Priority: %s. %s%n", pe.getId(), pe.getName());
        }

    }
}
