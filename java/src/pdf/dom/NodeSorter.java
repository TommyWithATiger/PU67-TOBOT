package pdf.dom;

import java.util.Comparator;
import org.w3c.dom.Node;

public class NodeSorter implements Comparator<Node> {

  /**
   * Compares two nodes based on their position in the given document.
   *
   * @param node1 The first node
   * @param node2 The other node
   * @return An integer indicating which of the two nodes should be sorted first.
   */
  @Override
  public int compare(Node node1, Node node2) {
    StyleHandler styleHandlerNode1 = new StyleHandler(node1);
    StyleHandler styleHandlerNode2 = new StyleHandler(node2);

    float topNode1 = styleHandlerNode1.getFloatValue("top");
    float topNode2 = styleHandlerNode2.getFloatValue("top");

    // If the nodes are very close in height at the top we sort them by how far from the left
    // they are. As in-line math formulas will give small differences in the top attribute.
    if (Math.abs(topNode1 - topNode2) < 5) {
      float leftNode1 = styleHandlerNode1.getFloatValue("left");
      float leftNode2 = styleHandlerNode2.getFloatValue("left");
      return roundAwayFromZero(leftNode1 - leftNode2);
    }

    return roundAwayFromZero(topNode1 - topNode2);
  }

  /**
   * Round the given float value away from zero. Meaning negative numbers are rounded down and
   * possible numbers are rounded up
   *
   * @param value The value to round
   * @return The rounded value as an Integer
   */
  private int roundAwayFromZero(float value) {
    if (value < 0) {
      return (int) Math.floor(value);
    }
    return (int) Math.ceil(value);
  }

}
