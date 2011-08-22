public class Foo {
  private void aPrivateMethod() {
    System.out.println("Foo#aPrivateMethod()");
  }
  void aPackageMethod() {
    System.out.println("Foo#aPackageMethod()");
  }
  protected void aProtectedMethod() {
    System.out.println("Foo#aProtectedMethod()");
  }
  java.lang.invoke.MethodHandles.Lookup getLookup() {
    return java.lang.invoke.MethodHandles.lookup();
  }
}
