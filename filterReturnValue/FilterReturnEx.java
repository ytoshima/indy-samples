import java.lang.invoke.*; import java.math.*;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;
public class FilterReturnEx {
  public static void main(String[] main) throws Throwable {
    Lookup lookup = lookup();
    MethodHandle mh_d2s = lookup.findStatic(FilterReturnEx.class, 
        "double2String", methodType(String.class, double.class));

    MethodHandle mh_s2bi = lookup.findStatic(FilterReturnEx.class, 
      "string2BigDecimal", methodType(BigDecimal.class, String.class));

    MethodHandle mh_filtered = filterReturnValue(mh_d2s, mh_s2bi);
    D("mh_filtered type: " + mh_filtered.type());
    D("mh_filtered call: " + (BigDecimal)mh_filtered.invokeExact(123.456));
  }
  public static void D(Object o) { System.out.println("D: " + o); }
  public static String double2String(double dval) {
    return Double.toString(dval);
  }
  public static BigDecimal string2BigDecimal(String str) {
    return new BigDecimal(str);
  }
}
