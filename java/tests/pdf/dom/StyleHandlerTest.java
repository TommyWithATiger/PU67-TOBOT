package pdf.dom;

import static org.junit.Assert.*;

import base.BaseTest;
import org.apache.xerces.dom.DocumentImpl;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class StyleHandlerTest extends BaseTest {

  private Node node;
  private StyleHandler styleHandler;

  @Before
  public void setUp() {
    Document document = new DocumentImpl();
    node = document.createElement("div");
    ((Element) node).setAttribute("style", "width:1.7pt;height:10pt;display:absolute;");
    styleHandler = new StyleHandler(node);
  }

  @Test
  public void testHasValues() {
    assertTrue(styleHandler.hasStyleTag("width"));
    assertTrue(styleHandler.hasStyleTag("display"));
    assertTrue(styleHandler.hasStyleTag("height"));
    assertFalse(styleHandler.hasStyleTag("tag"));
  }

  @Test
  public void testGetFloatValues() {
    assertEquals(1.7, styleHandler.getFloatValue("width"), 0.01);
    assertEquals(10, styleHandler.getFloatValue("height"), 0.01);
  }

  @Test
  public void testGetTextValues() {
    assertEquals("absolute", styleHandler.getStyleTag("display"));
    assertEquals("10pt", styleHandler.getStyleTag("height"));
  }

}