package pdf.dom;

import java.util.HashMap;
import java.util.Map;
import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.NodeImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * A class for handling the style of a node, as this is given to us as a single string and we want
 * to split the style into the different tags and their attributes
 */
public class StyleHandler {

  private Map<String, String> styleMap = new HashMap<>();
  private Node node;

  public StyleHandler(Node node) {
    this.node = node;
    // If there is no style item set for the given node we cannot do anything
    // Else we split using normal CSS rules
    if (node.getAttributes().getNamedItem("style") != null) {
      String style = node.getAttributes().getNamedItem("style").getTextContent();
      String[] styleTags = style.split(";");
      for (String styleTag : styleTags) {
        String[] splitStyle = styleTag.split(":");
        if (splitStyle.length != 2) {
          continue;
        }
        styleMap.put(splitStyle[0], splitStyle[1]);
      }
    }
  }

  /**
   * Checks if the given style tag is set
   *
   * @param styleTag The style tag to check
   * @return A boolean indicating if the style tag is set
   */
  public boolean hasStyleTag(String styleTag) {
    return styleMap.containsKey(styleTag);
  }

  /**
   * Returns the value of the given style tag if it exists, else an empty string
   *
   * @param styleTag The style tag to get the value of
   * @return The value of the style tag
   */
  public String getStyleTag(String styleTag) {
    if (styleMap.containsKey(styleTag)) {
      return styleMap.get(styleTag);
    }
    return "";
  }

  /**
   * Sets the given style tag to the given value.
   *
   * @param styleTag The style tag to set the value of
   * @param value The value to set
   */
  public void setStyleTag(String styleTag, String value) {
    styleMap.put(styleTag, value);
  }

  /**
   * Updates the style of the node to be the current state of this object
   */
  public void updateNodeStyle() {
    styleMap.entrySet()
        .stream()
        .map(entry -> entry.getKey() + ":" + entry.getValue() + ";")
        .reduce((total, entry) -> total += entry)
        .ifPresent(value -> node.getAttributes().getNamedItem("style").setNodeValue(value));
  }

  /**
   * Gets the float value saved in the given style tag
   *
   * @param styleTag The style tag to get the float value of
   * @return The float value of the style tag
   */
  public float getFloatValue(String styleTag) {
    return Float.valueOf(styleMap.get(styleTag).replaceAll("[^0-9\\.]", ""));
  }

}
