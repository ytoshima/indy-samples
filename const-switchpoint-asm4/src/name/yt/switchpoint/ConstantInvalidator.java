package name.yt.switchpoint;
import java.lang.invoke.*;
public class ConstantInvalidator {
  SwitchPoint lastInstance;
  SwitchPoint getData() { lastInstance = new SwitchPoint(); return lastInstance; }
  void invalidate() { SwitchPoint.invalidateAll(new SwitchPoint[] {lastInstance}); }
}
