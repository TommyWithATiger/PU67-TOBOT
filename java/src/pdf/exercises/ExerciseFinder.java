package pdf.exercises;

import static pdf.dom.ElementCreator.makeExercise;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import pdf.dom.StyleHandler;

public class ExerciseFinder {

  /**
   * Finds exercises in the given container, and split based on these
   *
   * @param document The document the container node is contained in
   * @param container The container node to find exercises in
   */
  public static void findExercises(Document document, Node container) {
    String regex = new ExerciseNumberingClassifier(container).getBestNumberingFormat();

    int startIndex = -1;
    for (int childIndex = 0; childIndex < container.getChildNodes().getLength(); childIndex++) {
      Node childNode = container.getChildNodes().item(childIndex);
      if (childNode.getTextContent().matches(regex)) {

        // Check if we are on the same line as the last node, as there really aren't two exercises
        // on the same line in most documents
        if (childIndex != 0 && isOnSameLine(childNode,
            container.getChildNodes().item(childIndex - 1))) {
          continue;
        }

        // If we have started an exercise already we will end the current one first
        if (startIndex != -1) {
          makeExercise(document, container, startIndex, childIndex - 1);
          childIndex = startIndex;
          startIndex = -1;
          continue;
        }

        // Start exercise
        startIndex = childIndex;
      }
    }

    // Check if we have started an exercise when we reach the end of the document
    if (startIndex != -1) {
      makeExercise(document, container, startIndex, container.getChildNodes().getLength() - 1);
    }
  }

  /**
   * Checks if the two nodes are on the same line in the given document. That is they are placed the
   * same distance from the top of the document
   *
   * @param node1 The first node
   * @param node2 the second node
   * @return A boolean indicating if the nodes are on the same line
   */
  private static boolean isOnSameLine(Node node1, Node node2) {
    StyleHandler styleHandlerFirstNode = new StyleHandler(node1);
    StyleHandler styleHandlerSecondNode = new StyleHandler(node2);

    return styleHandlerFirstNode.getFloatValue("top") == styleHandlerSecondNode
        .getFloatValue("top");
  }

}
