package pdf.dom;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import pdf.PDFParser;

public class ElementCreator {

  /**
   * Creates a node for the given document
   *
   * @param document The document
   * @param type The node type, e.g. div, p
   * @param classes The classes to set on the node
   * @return The node created
   */
  public static Node createElement(Document document, String type, String classes) {
    Element node = document.createElement(type);
    node.setAttribute("class", classes);
    node.setAttribute("style", "");
    return node;
  }

  /**
   * Creates an exercise node with the children between the given start index and end index in the
   * container node, and inserts the exercise node at the start index.
   *
   * @param document The document
   * @param containerNode The container node with all the parts of the exercise
   * @param startIndexChildren The index for the first child of the exercise
   * @param endIndexChildren The index for the last child of the exercise
   */
  public static void makeExercise(Document document, Node containerNode, int startIndexChildren,
      int endIndexChildren) {
    Node exerciseContainer = createElement(document, "div", "exercise");

    StyleHandler exerciseContainerStyleHandler = new StyleHandler(exerciseContainer);

    // Retrieve all the child nodes to a separate list for easier handling
    List<Node> exerciseChildren = new ArrayList<>();
    for (int childIndex = startIndexChildren; childIndex <= endIndexChildren; childIndex++) {
      exerciseChildren.add(containerNode.getChildNodes().item(childIndex));
    }

    // Find and set how far down the exercise container should be in the document
    StyleHandler firstChildStyleHandler = new StyleHandler(exerciseChildren.get(0));
    float topValueFirstChild = firstChildStyleHandler.getFloatValue("top");

    exerciseContainerStyleHandler.setStyleTag("top", topValueFirstChild + "pt");

    // When added to the exercise the children will have their top value relative to the exercise
    // container, that is we must reduce their top argument
    exerciseChildren.forEach(child -> {
      StyleHandler childStyleHandler = new StyleHandler(child);
      float topValueChild = childStyleHandler.getFloatValue("top");
      childStyleHandler.setStyleTag("top", topValueChild - topValueFirstChild + "pt");

      // Add the children to the exercise container and remove them from their original parent node
      childStyleHandler.updateNodeStyle();
      child.getParentNode().removeChild(child);
      exerciseContainer.appendChild(child);
    });

    // Compute how high the exercise container is
    exerciseChildren.stream().mapToDouble(child -> {
      StyleHandler childStyleHandler = new StyleHandler(child);
      float topValueChild = childStyleHandler.getFloatValue("top");
      float heightValueChild = 0;
      if (childStyleHandler.hasStyleTag("height")) {
        heightValueChild = childStyleHandler.getFloatValue("height");
      } else if (childStyleHandler.hasStyleTag("line-height")) {
        heightValueChild = childStyleHandler.getFloatValue("line-height");
      }
      return topValueChild + heightValueChild;
    }).max().ifPresent(
        maxValue -> exerciseContainerStyleHandler.setStyleTag("height", maxValue + "pt"));

    exerciseContainerStyleHandler.updateNodeStyle();

    containerNode
        .insertBefore(exerciseContainer, containerNode.getChildNodes().item(startIndexChildren));
  }
}
