package pdf.dom.images;

import static pdf.dom.images.ImageSaver.saveBase64Image;

import api.exceptions.APIBadRequestException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ImageExtractor {

  public static void extractImages(Node containerNode) {
    for (int childIndex = 0; childIndex < containerNode.getChildNodes().getLength(); childIndex++) {
      Node node = containerNode.getChildNodes().item(childIndex);
      if (node.getNodeName().equals("img")) {
        // If there is no source we cannot handle it
        if (((Element) node).getAttribute("src") == null) {
          continue;
        }
        // All images produced by DOM are base 64 encoded
        String urlCode = saveBase64Image(((Element) node).getAttribute("src"));
        if (urlCode == null) {
          throw new APIBadRequestException("Could not handle the PDF");
        }
        ((Element) node).setAttribute("src", urlCode);
      }
    }
  }
}
