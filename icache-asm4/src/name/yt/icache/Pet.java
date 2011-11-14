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

public class Pet {
  public static CallSite bsm(Lookup lookup, String name, MethodType type, Object arg) throws Throwable {
    D("bsm: type: " + type);
    InlineCacheCallSite site = new InlineCacheCallSite(lookup, name, type);
    MethodHandle fallback = FALLBACK_MH.bindTo(site);
    fallback = fallback.asCollector(Object[].class, type.parameterCount());
    D("bsm: fallback: " + fallback);
    fallback = fallback.asType(type);
    D("bsm: fallback: " + fallback);
    site.setTarget(fallback);
    return site;
  }
  public static boolean isCachedClass(Class<?> cls, Object receiver) {
    if (cls != receiver.getClass()) D("isCachedClass false !");
    else D("isCachedClass true.");
    return cls == receiver.getClass();
  }
  public static void fallback(InlineCacheCallSite site, Object[] args) throws Throwable {
    MethodType type = site.type();
    D("fallback: type: " + type);
    D("fallback: type.dropParameterTypes(0,1): " + type.dropParameterTypes(0,1));
    Object receiver = args[0];
    Class<?> receiverClass = receiver.getClass();
    D("fallback: receiverClass: " + receiverClass);
    MethodHandle target = site.lookup.findVirtual(receiverClass,
      site.name, type.dropParameterTypes(0, 1));
    D("fallback: target: " + target);
    target = target.asType(site.getTarget().type());
    D("fallback: target after asType: " + target);
    MethodHandle test = CHECK_CLASS_MH.bindTo(receiverClass);
    D("fallback: test: " + test);
    test = test.asType(test.type().changeParameterType(0, type.parameterType(0)));
    D("fallback: site.getTarget(): " + site.getTarget());
    MethodType ttype = target.type();
    MethodType ftype = site.getTarget().type();
    D("target type: " + ttype);
    D("fallback type: " + ftype);
    D("ttype.equals(ftype) = " + ttype.equals(ftype));

    MethodHandle fallback = FALLBACK_MH.bindTo(site);
    fallback = fallback.asCollector(Object[].class, type.parameterCount());
    fallback = fallback.asType(type);

    //MethodHandle guard = MethodHandles.guardWithTest(test, target, site.getTarget());
    MethodHandle guard = MethodHandles.guardWithTest(test, target, fallback);
    site.setTarget(guard);
    target.invokeWithArguments(args);
  }

  public static void main(String[] args) throws Throwable {
    DynamicIndy dynamicIndy = new DynamicIndy();
    MethodHandle mh = dynamicIndy.invokeDynamic("talk", 
        MethodType.methodType(void.class, Object.class),
        Pet.class, "bsm", 
        MethodType.methodType(CallSite.class, 
            Lookup.class, String.class, MethodType.class, Object.class), 
        "dummy");
     Object[] pets = new Object[] {new Cat(), new Dog(), new Cat(), new Dog()};
     for (Object pet : pets) {
       for (int i = 0; i < 4; i++) {
         mh.invokeExact(pet);
       }
     }
  }

  static class InlineCacheCallSite extends MutableCallSite {
    final Lookup lookup;
    final String name;
    InlineCacheCallSite(Lookup lookup, String name, MethodType type) {
      super(type);
      this.lookup = lookup;
      this.name = name; 
    }
  }

  static void D(Object o) {
    System.out.println("D: " + o);
    System.out.flush();
  }
  private static final MethodHandle CHECK_CLASS_MH;
  private static final MethodHandle FALLBACK_MH;
  static {
    Lookup lookup = MethodHandles.lookup();
    try {
      CHECK_CLASS_MH = lookup.findStatic(Pet.class, "isCachedClass", 
          MethodType.methodType(boolean.class, Class.class, Object.class));
      FALLBACK_MH = lookup.findStatic(Pet.class, "fallback",
          MethodType.methodType(void.class, InlineCacheCallSite.class, 
              Object[].class)); 
    } catch (ReflectiveOperationException e) {
      throw (AssertionError)new AssertionError().initCause(e);
    }
  }
}
