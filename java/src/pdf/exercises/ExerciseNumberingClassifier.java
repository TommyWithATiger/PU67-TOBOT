package pdf.exercises;

import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.w3c.dom.Node;

/**
 * Checks a set of possible numbering formats finding the one with the most occurrences in the
 * child nodes of the given container node.
 */
public class ExerciseNumberingClassifier {

  private HashMap<String, Integer> numberingFormats = new HashMap<>();
  private static List<String> possibleNumberingFormats = Lists
      .newArrayList("[a-z]\\)", "[A-Z]\\)", "[a-z]\\.", "[A-Z]\\.", "[0-9]\\.", "[0-9]\\)",
          "M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|VI{0,3}|I{2,3})", "([0-9]+\\.){1,2}[0-9]+",
          "m{0,4}(cm|cd|d?c{0,3})(xc|xl|l?x{0,3})(ix|ix|vi{0,3}|i{2,3})");

  public ExerciseNumberingClassifier(Node container) {
    possibleNumberingFormats.forEach(numberFormat -> numberingFormats.put(numberFormat, 0));
    classify(container);
  }

  /**
   * Counts the occurrences of each numbering format
   *
   * @param container The container to search
   */
  private void classify(Node container) {
    for (int nodeIndex = 0; nodeIndex < container.getChildNodes().getLength(); nodeIndex++) {
      String nodeContent = container.getChildNodes().item(nodeIndex).getTextContent();
      numberingFormats.keySet().stream().filter(nodeContent::matches).forEach(numberingFormat ->
          numberingFormats.put(numberingFormat, numberingFormats.get(numberingFormat) + 1));
    }
  }

  /**
   * @return The map of the numbering formats and their occurrences
   */
  public HashMap<String, Integer> getNumberingFormats() {
    return numberingFormats;
  }

  /**
   * @return The regex of the numbering format with the highest occurrence
   */
  public String getBestNumberingFormat() {
    return numberingFormats.entrySet().stream().max(Comparator.comparingInt(Entry::getValue))
        .map(Entry::getKey).get();
  }

}
