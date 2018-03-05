package co.kukurin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Output of a valid call to Lesshint.
 */
public class LesshintOutput {

  public static final String ERROR_PREFIX = "Error";
  private static final LesshintOutput DEFAULT_INSTANCE = new LesshintOutput(
      Collections.emptyList());

  static final class OutputComponent {
    private final int row;
    private final int col;
    private final String error;

    public OutputComponent(int row, int col, String error) {
      this.row = row;
      this.col = col;
      this.error = error;
    }

    public int getRow() {
      return row;
    }

    public int getCol() {
      return col;
    }

    public String getError() {
      return error;
    }
  }

  private final List<OutputComponent> outputComponents;

  LesshintOutput(List<OutputComponent> outputComponents) {
    this.outputComponents = outputComponents;
  }

  public List<OutputComponent> getOutputComponents() {
    return outputComponents;
  }

  static LesshintOutput empty() {
    return DEFAULT_INSTANCE;
  }

  static LesshintOutput fromString(String linterOutput) {
    List<OutputComponent> outputComponents = new ArrayList<>();

    // fmt: "Error: file.less: line 1, column 1, parse error: Name of the error"
    for (String line : linterOutput.split("\n")) {
      String[] components = line.split(":");

      if (!components[0].equals(ERROR_PREFIX)) {
        continue;
      }

      String[] info = components[2].split(",", 3);

      // convert to 0-indexed values.
      // fmt: "line 1", "column 1"
      int row = Integer.parseInt(info[0].trim().split(" ")[1]) - 1;
      int col = Integer.parseInt(info[1].trim().split(" ")[1]) - 1;
      String error = components[3].trim();

      outputComponents.add(new OutputComponent(row, col, error));
    }

    return new LesshintOutput(outputComponents);
  }
}
