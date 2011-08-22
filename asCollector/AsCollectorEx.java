import java.lang.invoke.*;
import static java.lang.invoke.MethodType.*;
import static java.lang.invoke.MethodHandles.*;
public class AsCollectorEx {
  public static void main(String[] args) throws Throwable {
    Lookup lookup = lookup();
    MethodHandle m0 = lookup.findStatic(AsCollectorEx.class, "printAll",
        methodType(void.class, Object[].class));
    P("m0   : " + m0); 
    MethodHandle mcoll = m0.asCollector(Object[].class, 4);
    P("mcoll 1: " + mcoll);
    mcoll.invoke("Foo", "Bar", "Fizz", "Buzz");
    mcoll = mcoll.asType(methodType(void.class, 
        String.class, String.class, String.class, String.class));
    P("mcoll 2: " + mcoll);
    mcoll.invokeExact("Foo", "Bar", "Fizz", "Buzz");
    Object[] params = new Object[] {"Foo", "Bar", "Fizz", "Buzz"};
    MethodHandle mspr = mcoll.asSpreader(Object[].class, 4);
    P("mspr: " + mspr);
    mspr.invokeExact(params);
  }
  public static void printAll(Object[] args) {
    StringBuilder sb = new StringBuilder(); sb.append("(");
    for (Object o : args) sb.append(o == null ? "(null) " : o.toString() + " ");
    sb.append(")"); System.out.println(sb.toString());
  }
  static void P(Object o) { System.out.println(o); }
}
