import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by developer4 on 2018- 六月 - 04
 */
@RunWith(Arquillian.class)
public class DataCollectionServiceImplTest {

  @Deployment
  public static JavaArchive createDeployment() {
    return ShrinkWrap.create(JavaArchive.class)
        .addClass(
            com.shellshellfish.aaas.finance.trade.pay.service.impl.DataCollectionServiceImpl.class)
        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }

}
