package pdf.dom.images;

import api.exceptions.APIBadRequestException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ImageExtractor {

  /**
   * Extracts images from the given container node, giving them to an ImageSaver for saving on an
   * external image host.
   *
   * @param containerNode The node to extract images from
   */
  public static void extractImages(Node containerNode) {
    List<ImageSaver> imageSavers = new ArrayList<>();

    UncaughtExceptionHandler exceptionHandler = (t, e) -> {
      throw new APIBadRequestException("Could no handle the images in the PDF");
    };

    for (int childIndex = 0; childIndex < containerNode.getChildNodes().getLength(); childIndex++) {
      Node node = containerNode.getChildNodes().item(childIndex);
      if (node.getNodeName().equals("img")) {
        // If there is no source we cannot handle it
        if (((Element) node).getAttribute("src") == null) {
          continue;
        }

        // Use threads to process all the images at the same time
        ImageSaver imageSaver = new ImageSaver(((Element) node).getAttribute("src"), node);
        imageSaver.setUncaughtExceptionHandler(exceptionHandler);
        imageSavers.add(imageSaver);
        imageSaver.start();
      }
    }

    while (imageSavers.size() > 0) {
      // Sleep for a small amount of time to not use a lot of CPU
      try {
        Thread.sleep(50);
      } catch (InterruptedException ignored) {
      }
      imageSavers = imageSavers.stream().filter(ImageSaver::isAlive).collect(Collectors.toList());
    }
  }
}
