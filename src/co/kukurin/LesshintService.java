package co.kukurin;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import javax.annotation.Nullable;

/**
 * Unused.
 * Service class to keep plugin configuration persistent across IntelliJ invocations.
 */
@State(name = "LesshintService")
public class LesshintService implements PersistentStateComponent<LesshintService> {

  private static final Logger log = Logger.getInstance(LesshintService.class);

  public LesshintService() {
  }

  static LesshintService getInstance(Project project) {
    return ServiceManager.getService(project, LesshintService.class);
  }

  @Nullable
  @Override
  public LesshintService getState() {
    return this;
  }

  @Override
  public void loadState(LesshintService state) {
    XmlSerializerUtil.copyBean(state, this);
  }

}
