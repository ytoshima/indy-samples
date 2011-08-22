import java.lang.invoke.*; import java.util.regex.*;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;
public class FilterEx {
  public static void main(String[] main) throws Throwable {
    MethodHandles.Lookup lookup = MethodHandles.lookup();

    MethodHandle mh_domatch = lookup.findStatic(FilterEx.class, "domatch",
        MethodType.methodType(Matcher.class, String.class, Pattern.class));

    MethodHandle mh_getPtn = lookup.findStatic(FilterEx.class, "getPtn",
      MethodType.methodType(Pattern.class, String.class));

    MethodHandle mh_filtered = MethodHandles.filterArguments(mh_domatch, 1,
      mh_getPtn);

    D("mh_filtered type: " + mh_filtered.type());
    D("mh_filtered call: " + mh_filtered.invoke("daddy", ".*dd.*"));
  }
  public static void D(Object o) { System.out.println("D: " + o); }
  public static Matcher domatch(String str, Pattern ptn) {
    Matcher matcher = ptn.matcher(str);
    if (matcher.matches()) {
      System.out.println("matched: str: " + str + " ptn: " + ptn);
    } else {
      System.out.println("Did not match: str: " + str + " ptn: " + ptn);
    }
    return matcher; 
  }
  public static Pattern getPtn(String sptn) { return Pattern.compile(sptn); }
}
