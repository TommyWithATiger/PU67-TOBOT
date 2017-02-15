import junit.framework.TestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

@Ignore
public class BaseTest{

    @BeforeClass
    public static void setUpBaseClass(){
        System.setProperty("javax.xml.accessExternalDTD", "all");
    }
}
