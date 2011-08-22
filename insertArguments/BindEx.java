import java.lang.invoke.*;
public class BindEx {
  public static void main(String[] main) throws Throwable {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    MethodHandle mh_replace = lookup.findVirtual(String.class, "replace",
        MethodType.methodType(String.class, char.class, char.class));
    D((String)mh_replace.invokeExact("daddy", 'd', 'n') 
        + " // " + mh_replace.type()); 
    D(mh_replace.invoke("daddy", 'd', 'n')); 
    MethodHandle mh_bound = mh_replace.bindTo("daddy");
    D("mh_bound: " + (String)mh_bound.invokeExact('d', 'n') 
        + " // " + mh_bound.type()); 
    mh_bound = MethodHandles.insertArguments(mh_replace, 0, "daddy");
    D("mh_bound: " + (String)mh_bound.invokeExact('d', 'n') 
        + " // " + mh_bound.type()); 
    MethodHandle mh_bound_all = MethodHandles.insertArguments(mh_bound,
      0, 'd', 'n');
    D((String)mh_bound_all.invokeExact() 
        + " // " + mh_bound_all.type()); 
  }
  public static void D(Object o) { System.out.println("D: " + o); }
}
