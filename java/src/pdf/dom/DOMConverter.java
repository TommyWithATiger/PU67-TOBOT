package pdf.dom;

import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

public class DOMConverter {

  /**
   * Transforms a DOM document to a html string
   *
   * @param DOM The DOM document to transform
   * @return A string of the DOM document as HTML
   * @throws TransformerException If there is any transformation error
   */
  public static String DOMToString(Document DOM) throws TransformerException {
    TransformerFactory transFactory = TransformerFactory.newInstance();
    Transformer transformer = transFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.METHOD, "html");
    StringWriter buffer = new StringWriter();
    transformer.transform(new DOMSource(DOM),
        new StreamResult(buffer));
    return buffer.toString();
  }
}
