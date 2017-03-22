package pdf;


import static pdf.dom.ElementCreator.createElement;
import static pdf.exercises.ExerciseFinder.findExercises;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import pdf.dom.DOMConverter;
import pdf.dom.GraphicFixer;
import pdf.dom.NodeSorter;
import pdf.dom.StyleHandler;


public class PDFParser {
  /**
   * Converts the PDDocument given in to HTML
   *
   * @param pdf The PDF to convert
   * @return A w3c DOM object of HTML derived from the PDDocument
   * @throws IOException The parser may create an IOException
   * @throws ParserConfigurationException The parser may be miss configured
   */
  private static Document PDFToHTML(PDDocument pdf)
      throws IOException, ParserConfigurationException {
    PDFDomTree parser = new PDFDomTree();
    return parser.createDOM(pdf);
  }

  /**
   * Returns HTML split on exercises from the given PDF
   *
   * @param pdf The PDF to convert and find exercises in
   * @return The HTML of the PDF with marked exercises
   * @throws IOException Parse error
   * @throws ParserConfigurationException Parser setup error
   * @throws TransformerException Transformation to HTML error
   */
  public static String getPrettyHTML(PDDocument pdf)
      throws IOException, ParserConfigurationException, TransformerException {
    Document document = PDFToHTML(pdf);
    document.getChildNodes();
    Node body = document.getElementsByTagName("body").item(0);

    int pages = pdf.getNumberOfPages();

    Node container = createElement(document, "div", "container");

    // Small general changes to the global style for better formatting
    Node style = document.getElementsByTagName("style").item(0);
    style.setTextContent(style.getTextContent()
        + "\n.container{width:595pt;}\n.exercise{position:absolute;width:595pt}\n.orange{border:2px solid orange;}\n.blue{border:2px solid blue;}");

    float minPageSize = Float.MAX_VALUE;
    float currentPageAdds = 0;
    List<Node> contentNodes = new ArrayList<>();

    // Remove pages, we want a continuous block of content
    for (int pageNumber = 0; pageNumber < pages; pageNumber++) {
      Node page = body.getFirstChild();
      minPageSize = Float.min(minPageSize, new StyleHandler(page).getFloatValue("height"));
      body.removeChild(page);

      // Remove all children from the page and move them relatively to the amount of space above the page.
      while (page.hasChildNodes()) {
        Node node = page.getFirstChild();
        StyleHandler styleHandler = new StyleHandler(node);
        String topStyleString = styleHandler.getStyleTag("top");
        float topValue = Float.valueOf(topStyleString.substring(0, topStyleString.length() - 2));
        styleHandler.setStyleTag("top", topValue + currentPageAdds + "pt");
        styleHandler.updateNodeStyle();
        page.removeChild(node);
        contentNodes.add(node);
      }

      currentPageAdds += new StyleHandler(page).getFloatValue("height");
    }

    // Sort the nodes by their position
    contentNodes.sort(new NodeSorter());

    for (Node node : contentNodes) {
      // Remove all r class nodes (boxes) that are higher than the shortest page
      if (((Element) node).getAttribute("class").equals("r")
          && new StyleHandler(node).getFloatValue("height") >= minPageSize) {
        continue;
      }

      // Fix a graphical bug with nodes with text content starting with a dash
      if (!node.getTextContent().isEmpty() && node.getTextContent().charAt(0) == '-') {
        GraphicFixer.fixDashNode(node);
      }

      container.appendChild(node);
    }

    // Find exercises
    findExercises(document, container);

    // Convert the container node to a HTML string
    return DOMConverter.DOMToString(container);
  }


}
