package name.yt.switchpoint;

import java.util.concurrent.ConcurrentHashMap;
import java.lang.invoke.*;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;
//import name.yt.icache.DynamicIndy;

/*

An experiment of SwitchPoint.

In main, a custum call site is created and constantFallback + call site argument
is set to its target.  

When the call site's target was called for the first time, constantFallback
creates a constant MethodHandle.  It then creates a SwitchPoint which takes
the constant method handle and the constantFallback method handle.

Until the switch point is invalidated, the call site target call would invoke
the constant method handle.  After the switch point invalidation, constantFallback
is called and setup a constant method handle with current value and setup
SwichPoint again.  Following call would return the new constant value.

In this way, the constant can be obtained quickly, without polling the change.

Sample output:

D: set value to Foo
D: In constantFallback value=IAmethystObject{Foo}
D: callsite invoke 1: IAmethystObject{Foo}
D: callsite invoke 2: IAmethystObject{Foo}
D: callsite invoke 3: IAmethystObject{Foo}
D: invalidated
D: set value to Bar
D: In constantFallback value=IAmethystObject{Bar}
D: callsite invoke 4: IAmethystObject{Bar}
D: callsite invoke 5: IAmethystObject{Bar}
D: callsite invoke 6: IAmethystObject{Bar}

*/


public class SwitchPointEx {
  public static void main(String... args) throws Throwable {
    boolean useIndy = false;
    for (String arg : args) {
      if (arg.equals("-h") || arg.equals("-help")) helpAndExit();
      if (arg.equals("-use-indy")) useIndy = true;
    }
    if (useIndy) {
      doWithIndy();
    } else {
      doWithoutIndy();
    }
  }
  static void helpAndExit() {
    String msg = "usage: java SwitchPointEx [-use-indy]\n" +
        "options:\n" +
        "  -h | -help : show this message\n" +
        "  -use-indy  : use invokedynamic\n";
    System.out.println(msg);
    System.exit(0);
  }
  public static CallSite bsm(Lookup lookup, String name, MethodType type, Object arg) throws Throwable {
    AmethystConstantCallSite site = new AmethystConstantCallSite(type, "site1");
    MethodType fallbackType = type.insertParameterTypes(0, AmethystConstantCallSite.class);
    MethodHandle myFallback = insertArguments(
                lookup.findStatic(SwitchPointEx.class, "constantFallback",
                fallbackType),
                0,
                site);
    site.setTarget(myFallback);

    return site;
  }

  static void doWithIndy() throws Throwable {
    MyThreadContext tc = new MyThreadContext();
    tc.setConstant("site1", new IAmethystObject("Foo"));

    DynamicIndy dynamicIndy = new DynamicIndy();
    MethodHandle mh = dynamicIndy.invokeDynamic("getconst",
        MethodType.methodType(IAmethystObject.class, MyThreadContext.class),
        SwitchPointEx.class, "bsm",
        MethodType.methodType(CallSite.class,
            Lookup.class, String.class, MethodType.class, Object.class),
            "dummy");
        //"hello");
    
    D("* Using invokedynamic !");

    IAmethystObject iro = null;
    iro = (IAmethystObject)mh.invokeExact(tc);
    D("callsite invoke 1: " + iro);
    iro = (IAmethystObject)mh.invokeExact(tc);
    D("callsite invoke 2: " + iro);
    iro = (IAmethystObject)mh.invokeExact(tc);
    D("callsite invoke 3: " + iro);
    tc.runtime.getConstantInvalidator().invalidate();
    D("invalidated");
    tc.setConstant("site1", new IAmethystObject("Bar"));
    D("set value to Bar");
    iro = (IAmethystObject)mh.invokeExact(tc);
    D("callsite invoke 4: " + iro);
    iro = (IAmethystObject)mh.invokeExact(tc);
    D("callsite invoke 5: " + iro);
    iro = (IAmethystObject)mh.invokeExact(tc);
    D("callsite invoke 6: " + iro);

  }
  static void doWithoutIndy() throws Throwable {
    MyThreadContext tc = new MyThreadContext();
    tc.setConstant("site1", new IAmethystObject("Foo"));

    MethodHandles.Lookup lookup = lookup();
    MethodType type = 
        methodType(IAmethystObject.class, MyThreadContext.class);
    AmethystConstantCallSite site = new AmethystConstantCallSite(type, "site1");
    D("set value to Foo");
    MethodType fallbackType = type.insertParameterTypes(0, AmethystConstantCallSite.class);
    MethodHandle myFallback = insertArguments(
                lookup.findStatic(SwitchPointEx.class, "constantFallback",
                fallbackType),
                0,
                site);
    site.setTarget(myFallback);

    IAmethystObject iro = null;
    iro = (IAmethystObject)site.getTarget().invokeExact(tc);
    D("callsite invoke 1: " + iro);
    iro = (IAmethystObject)site.getTarget().invokeExact(tc);
    D("callsite invoke 2: " + iro);
    iro = (IAmethystObject)site.getTarget().invokeExact(tc);
    D("callsite invoke 3: " + iro);
    tc.runtime.getConstantInvalidator().invalidate();
    D("invalidated");
    tc.setConstant("site1", new IAmethystObject("Bar"));
    D("set value to Bar");
    iro = (IAmethystObject)site.getTarget().invokeExact(tc);
    D("callsite invoke 4: " + iro);
    iro = (IAmethystObject)site.getTarget().invokeExact(tc);
    D("callsite invoke 5: " + iro);
    iro = (IAmethystObject)site.getTarget().invokeExact(tc);
    D("callsite invoke 6: " + iro);
  }

  public static IAmethystObject constantFallback(AmethystConstantCallSite site,
      MyThreadContext context) {
    IAmethystObject value = context.getConstant(site.name());
  
    if (value != null) {
      //if (AmethystInstanceConfig.LOG_INDY_CONSTANTS) LOG.info("constant " + site.name() + " bound directly");
      D("In constantFallback value=" + value); 
      MethodHandle valueHandle = constant(IAmethystObject.class, value);
      valueHandle = dropArguments(valueHandle, 0, MyThreadContext.class);

      MethodHandle fallback = insertArguments(
          findStatic(SwitchPointEx.class, "constantFallback",
          methodType(IAmethystObject.class, AmethystConstantCallSite.class, MyThreadContext.class)),
          0,
          site);

      SwitchPoint switchPoint = (SwitchPoint)context.runtime.getConstantInvalidator().getData();
      MethodHandle gwt = switchPoint.guardWithTest(valueHandle, fallback);
      site.setTarget(gwt);
    } else {
      D("In constantFallback value=null");
      //value = context.getCurrentScope().getStaticScope().getModule()
      //    .callMethod(context, "const_missing", context.getRuntime().fastNewSymbol(site.name()));
      value = new IAmethystObject("(null)");
    }

    return value;
  }

  private static MethodHandle findStatic(Class target, String name, MethodType type) {
    try {
      return lookup().findStatic(target, name, type);
    } catch (NoSuchMethodException nsme) {
      throw new RuntimeException(nsme);
    } catch (IllegalAccessException nae) {
      throw new RuntimeException(nae);
    }
  }


  public static void D(Object o) { System.out.println("D: " + o); }
}


