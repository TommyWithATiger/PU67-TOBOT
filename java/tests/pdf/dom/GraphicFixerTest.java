package pdf.dom;

import static org.junit.Assert.*;

import base.BaseTest;
import org.apache.xerces.dom.DocumentImpl;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class GraphicFixerTest extends BaseTest {

  private Node node;

  @Before
  public void setUp() throws Exception {
    Document document = new DocumentImpl();
    node = document.createElement("div");
    ((Element) node).setAttribute("style", "width:1pt;");
  }

  @Test
  public void testFixDashNode() {
    GraphicFixer.fixDashNode(node);
    assertEquals("width:1.1pt;", ((Element) node).getAttribute("style"));
  }

}