package co.kukurin;

import com.intellij.execution.ExecutionException;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Calls Lesshint process, creates annotations.
 */
public class LesshintExternalAnnotator extends ExternalAnnotator<PsiFile, LesshintOutput> {

  private static final Logger log = Logger.getInstance(LesshintExternalAnnotator.class);
  public static final int NEWLINE_LENGTH = System.lineSeparator().length();

  @Nullable
  @Override
  public PsiFile collectInformation(@NotNull PsiFile file) {
    return file;
  }

  @Nullable
  @Override
  public LesshintOutput doAnnotate(PsiFile file) {
    String fileType = file.getName().substring(file.getName().lastIndexOf('.'));
    String basePath = file.getContainingDirectory().getVirtualFile().getPath();

    if (!fileType.equals(".css") && !fileType.equals(".less")) {
      return LesshintOutput.empty();
    }

    try {
      // Preferred way to solve this problem:
      // https://intellij-support.jetbrains.com/hc/en-us/community/posts/
      // 115000337510-Only-trigger-externalAnnotator-when-the-file-system-is-in-sync
      String documentText = file.getText();
      Path path = Files.createTempFile("linter-file", fileType);
      Files.write(path, documentText.getBytes(StandardCharsets.UTF_8));
      String linterOutput = LesshintProcessUtil.lint(basePath, path.toString());
      Files.delete(path);
      return LesshintOutput.fromString(linterOutput);
    } catch (InterruptedException | IOException e) {
      log.error("Error creating temporary file.", e);
    } catch (ExecutionException e) {
      log.error("Error executing lesshint process.", e);
    }

    return LesshintOutput.empty();
  }

  @Override
  public void apply(
      @NotNull PsiFile file, LesshintOutput lesshintOutput, @NotNull AnnotationHolder holder) {
    String[] fileByLines = file.getText().split(System.lineSeparator());
    int[] offsets = getOffsets(fileByLines);

    lesshintOutput.getOutputComponents().forEach(annotation -> {
      int offset = offsets[annotation.getRow()] + annotation.getCol();
      TextRange range = new TextRange(offset, offset + 1);
      holder.createErrorAnnotation(range, annotation.getError());
    });
  }

  private static int[] getOffsets(String[] fileByLines) {
    int[] lengths = new int[fileByLines.length];

    for (int i = 1; i < lengths.length; i++) {
      lengths[i] = lengths[i - 1] + fileByLines[i - 1].length() + NEWLINE_LENGTH;
    }

    return lengths;
  }
}
