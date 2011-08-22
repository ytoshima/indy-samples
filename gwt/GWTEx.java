import java.lang.invoke.*; import static java.lang.invoke.MethodType.*;
import static java.lang.invoke.MethodHandles.*;
public class GWTEx {
  public static void main(String[] args) throws Throwable {
    Lookup lookup = lookup();
    MethodHandle test   = lookup.findStatic(GWTEx.class, "isString",
        methodType(boolean.class, Object.class));
    MethodHandle target  = lookup.findStatic(GWTEx.class, "printString",
        methodType(void.class, Object.class, long.class));
    MethodHandle fallback = lookup.findStatic(GWTEx.class, "printObject",
        methodType(void.class, Object.class, long.class, long.class));
    MethodHandle test1 = dropArguments(test, 1, long.class);
    MethodHandle fallback1 = insertArguments(fallback, 2, 99);
    MethodHandle mh = guardWithTest(test1, target, fallback1);
    mh.invoke("This is a String", -1L); mh.invoke(new Integer(10), -1L);
    D("test     : " + test);      D("test1    : " + test1);
    D("target   : " + target);    D("fallback : " + fallback);
    D("fallback1: " + fallback1); D("mh       : " + mh);
  }
  static boolean isString(Object o) { return (o instanceof String); }
  static void printString(Object o, long t) {
    System.out.println("printString: " + (String)o + " t: " + t);
  }
  static void printObject(Object o, long t, long t2) {
    System.out.println("printObject: " + o + " t: " + t + " t2: " + t2);
  }
  static void D(Object o) { System.out.println("D: " + o); }
}
