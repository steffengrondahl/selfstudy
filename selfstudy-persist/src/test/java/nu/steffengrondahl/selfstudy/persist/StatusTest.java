package nu.steffengrondahl.selfstudy.persist;

import nu.steffengrondahl.selfstudy.persist.QuerySpecificationFactory;
import nu.steffengrondahl.selfstudy.persist.StatusEntityDAO;
import nu.steffengrondahl.selfstudy.persist.domain.StatusEntity;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by Steffen on 29-10-2016.
 */
public class StatusTest {

    private static StatusEntityDAO dao = new StatusEntityDAO();

    @Test
    public void testStatus() {
        List<StatusEntity> statusList = dao.query(QuerySpecificationFactory.queryAll());

        int i = 1;
        Assert.assertTrue(statusList.size() == 3);
        for (StatusEntity se : statusList) {
            Assert.assertTrue(se.getId().intValue() == i++);
            System.out.printf("Status: %s. %s%n",se.getId(), se.getName());
        }
    }

}
