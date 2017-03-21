package pdf.dom;

import org.w3c.dom.Node;

public class GraphicFixer {

  /**
   * Fixes a graphical bug where a node starting with a dash [-] would have the rest of the text on
   * a new line, rather than behind the text. This is fixed by increasing the width of the node by a
   * minuscule amount.
   *
   * @param node The node which should be fixed
   */
  public static void fixDashNode(Node node) {
    StyleHandler styleHandler = new StyleHandler(node);
    styleHandler
        .setStyleTag("width", styleHandler.getFloatValue("width") + 0.1 + "pt");
    styleHandler.updateNodeStyle();
  }

}
