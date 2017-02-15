import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Ignore;

@Ignore
public class BaseTest extends TestCase {
    @BeforeClass
    public static void setUpBaseClass(){
        System.setProperty("javax.xml.accessExternalDTD", "all");
    }
}
