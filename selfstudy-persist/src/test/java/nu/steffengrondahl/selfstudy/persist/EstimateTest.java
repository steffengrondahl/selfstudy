package nu.steffengrondahl.selfstudy.persist;

import nu.steffengrondahl.selfstudy.persist.EstimateEntityDAO;
import nu.steffengrondahl.selfstudy.persist.QuerySpecificationFactory;
import nu.steffengrondahl.selfstudy.persist.domain.EstimateEntity;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * Created by Steffen on 29-10-2016.
 */
public class EstimateTest {

    private static EstimateEntityDAO dao = new EstimateEntityDAO();

    @Ignore
    @Test
    public void testEstimate() {
        List<EstimateEntity> estimateList = dao.query(QuerySpecificationFactory.queryAll());

        int i = 1;
        Assert.assertTrue(estimateList.size() == 4);
        for (EstimateEntity ee : estimateList) {
            Assert.assertTrue(ee.getId() == i++);
            System.out.printf("Estimate: %s. %s%n", ee.getId(), ee.getName());
        }

    }

}
