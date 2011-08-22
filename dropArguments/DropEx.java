import java.lang.invoke.*;
import java.util.*;
public class DropEx {
  public static void main(String[] main) throws Throwable {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    MethodHandle mh_replace = lookup.findVirtual(String.class, "replace",
        MethodType.methodType(String.class, char.class, char.class));
    MethodHandle mh_drop = MethodHandles.dropArguments(mh_replace, 
        1, Map.class, Set.class);
    D("mh_drop: " 
        + (String)mh_drop.invokeExact("daddy", 
            (Map)new HashMap(), (Set)new TreeSet(), 'd', 'n') 
        + " // " + mh_drop.type()); 
    D("mh_drop: " 
        + (String)mh_drop.invoke("daddy", new HashMap(), 
            new TreeSet(), 'd', 'n') 
        + " // " + mh_drop.type()); 
  }
  public static void D(Object o) {
    System.out.println("D: " + o);
  }
}
