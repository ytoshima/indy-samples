package name.yt.switchpoint;
import java.lang.invoke.*;
public class AmethystConstantCallSite extends MutableCallSite {
  private String name;
  public AmethystConstantCallSite(MethodType type, String name) {
    super(type);
    setName(name);
  }
  public void setName(String name) { this.name = name; }
  public String name() {
    return this.name;
  }
}
