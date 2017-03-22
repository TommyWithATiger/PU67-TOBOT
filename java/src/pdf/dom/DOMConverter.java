package pdf.dom;

import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;

public class DOMConverter {

  /**
   * Transforms a DOM node to a html string
   *
   * @param node The node to transform
   * @return A string of the DOM document as HTML
   * @throws TransformerException If there is any transformation error
   */
  public static String DOMToString(Node node) throws TransformerException {
    TransformerFactory transFactory = TransformerFactory.newInstance();
    Transformer transformer = transFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.METHOD, "html");
    StringWriter buffer = new StringWriter();
    transformer.transform(new DOMSource(node),
        new StreamResult(buffer));
    return buffer.toString();
  }
}
