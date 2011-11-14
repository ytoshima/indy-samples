package name.yt.switchpoint;
import java.util.concurrent.ConcurrentHashMap;

public class MyThreadContext implements java.io.Serializable {
  private ConcurrentHashMap<String,IAmethystObject> map = new ConcurrentHashMap<>();
  public IAmethystObject getConstant(String key) {
    return map.get(key);
  }
  public void setConstant(String key, IAmethystObject value) {
    map.put(key, value);
  }
  MyRuntime runtime = new MyRuntime();
}

