import java.lang.invoke.*;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;
public class FoldEx {
  public static void main(String[] main) throws Throwable {
    Lookup lookup = lookup();
    MethodHandle mh_replace = lookup.findVirtual(String.class, "replace",
        methodType(String.class, char.class, char.class));
    MethodHandle mh_hdr = lookup.findStatic(FoldEx.class, "printHeader",
        methodType(String.class, char.class, char.class));
    MethodHandle mh_folded = foldArguments(mh_replace, mh_hdr);
    D("mh_folded type: " + mh_folded.type());
    D("mh_fold call: " + mh_folded.invoke('d', 'n'));
  }
  public static void D(Object o) {
    System.out.println("D: " + o);
  }
  public static String printHeader(char fromC, char toC) {
    System.out.println("* params: " + fromC + ", " + toC);
    return "daddy";
  }
}
