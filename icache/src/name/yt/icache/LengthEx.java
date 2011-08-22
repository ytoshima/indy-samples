package name.yt.icache;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class LengthEx {
  public static CallSite bsm(Lookup lookup, String name, MethodType methodType, Object arg) throws Throwable {
    D("construct the ichache call site.");
    D("methodType: " + methodType);
    MutableCallSite mcs = new MutableCallSite(methodType);
    MethodHandle mh = lookup.findStatic(LengthEx.class, "doLength", methodType); 
    mcs.setTarget(mh);
    return mcs;
  }
  public static int doLength(Object o) {
    if (o instanceof String) {
      System.out.println("doLength got String");
      return ((String)o).length();
    } else if (o instanceof Collection) {
      System.out.println("doLength got Collection");
      return ((Collection)o).size();
    } else {
      return -1;
    }
  }

  public static void main(String[] args) throws Throwable {
    DynamicIndy dynamicIndy = new DynamicIndy();
    MethodHandle mh = dynamicIndy.invokeDynamic("icache_length", 
        MethodType.methodType(int.class, Object.class),
        LengthEx.class, "bsm", 
        MethodType.methodType(CallSite.class, 
            Lookup.class, String.class, MethodType.class, Object.class), 
        "dummy");
        //new Object[] {});
     int r = (int)mh.invoke("hello");
     D("String hello length = " + r);
     List<String> al = new ArrayList<String>();
     al.add("foo"); al.add("bar");
     r = (int)mh.invoke(al);
     D("List foo bar length = " + r);
  }

  static void D(Object o) {
    System.out.println("D: " + o);
  }
}
