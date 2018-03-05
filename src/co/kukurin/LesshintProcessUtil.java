package co.kukurin;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.io.StreamUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Utils to call the Lesshint system process.
 */
public class LesshintProcessUtil {

  private static final Logger log = Logger.getInstance(LesshintProcessUtil.class);
  private static final String NO_OUTPUT = "";

  private LesshintProcessUtil() {}

  public static String lint(String basePath, String lintFilePath)
      throws ExecutionException, IOException, InterruptedException {
    Process process = new GeneralCommandLine()
        .withExePath("lesshint")
        .withEnvironment(System.getenv())
        .withCharset(StandardCharsets.UTF_8)
        .withWorkDirectory(basePath)
        .withParameters(lintFilePath)
        .createProcess();

    String output = StreamUtil.readText(process.getInputStream(), StandardCharsets.UTF_8);
    int exitCode = process.waitFor();
    if (exitCode != 0 && exitCode != 1) {
      log.warn(String.format("Lesshint process exited with unexpected exit code: %d", exitCode));
      return NO_OUTPUT;
    }

    log.info(String.format("Returning response of: %s", output));
    return output;
  }

}
