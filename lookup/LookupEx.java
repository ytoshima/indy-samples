import java.lang.invoke.*;

/* LookupEx -- check lookup object behavior */
public class LookupEx {
  public static void main(String[] args) {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    doex(lookup, "MethodHandles.lookup()");
    callex(lookup, "MethodHandles.lookup() call private");
    callFooPackageEx(lookup, "MethodHandles.lookup call Foo package");
    callFooProtectedEx(lookup, "MethodHandles.lookup call Foo protected");
    callFooPrivateEx(lookup, "MethodHandles.lookup call Foo private");

    Foo foo = new Foo();
    lookup = foo.getLookup();

    doex(lookup, "Foo.lookup()");
    callex(lookup, "Foo.lookup() call private");
    callFooPackageEx(lookup, "Foo.lookup call Foo package");
    callFooProtectedEx(lookup, "Foo.lookup call Foo protected");
    callFooPrivateEx(lookup, "Foo.lookup call Foo private");

    lookup = MethodHandles.publicLookup();
    doex(lookup, "MethodHandles.publicLookup()");
    callex(lookup, "MethodHandles.publicLookup() call private");
  }

  static void doex(MethodHandles.Lookup lookup, String header) {
    D(header);
    D("lookup: " + lookup);
    D("lookup.lookupClass: " + lookup.lookupClass()); 
    D("lookup.lookupModes: " + lookup.lookupModes() + " " + 
         decodeModes(lookup.lookupModes())); 
  }

  static void callex(MethodHandles.Lookup lookup, String header) {
    try {
      MethodHandle mh = lookup.findStatic(LookupEx.class, "aPrivateMethod",
          MethodType.methodType(void.class));
      mh.invoke();
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  static void callFooPrivateEx(MethodHandles.Lookup lookup, String header) {
    try {
      MethodHandle mh = lookup.findVirtual(Foo.class, "aPrivateMethod",
          MethodType.methodType(void.class));
      Foo foo = new Foo();
      mh.invoke(foo);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
  static void callFooProtectedEx(MethodHandles.Lookup lookup, String header) {
    try {
      MethodHandle mh = lookup.findVirtual(Foo.class, "aProtectedMethod",
          MethodType.methodType(void.class));
      Foo foo = new Foo();
      mh.invoke(foo);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
  static void callFooPackageEx(MethodHandles.Lookup lookup, String header) {
    try {
      MethodHandle mh = lookup.findVirtual(Foo.class, "aPackageMethod",
          MethodType.methodType(void.class));
      Foo foo = new Foo();
      mh.invoke(foo);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
  static String decodeModes(int v) {
    StringBuilder sb = new StringBuilder();
    if ((v & MethodHandles.Lookup.PACKAGE) != 0) sb.append("PACKAGE ");
    if ((v & MethodHandles.Lookup.PRIVATE) != 0) sb.append("PRIVATE ");
    if ((v & MethodHandles.Lookup.PROTECTED) != 0) sb.append("PROTECTED ");
    if ((v & MethodHandles.Lookup.PUBLIC) != 0) sb.append("PUBLIC ");
    return sb.toString();
  }
  static void D(Object o) {
    System.out.println("D: " + o);
  }
  private static void aPrivateMethod() {
    System.out.println("This is a private method");
  }
}
