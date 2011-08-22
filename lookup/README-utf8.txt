Lookup object の振る舞いを確認。
Lookup object を得たメソッドをもつクラスが lookupClass に設定され、find* の正否は
そのクラスと、find* の引数の間のアクセスチェックに依存する。また lookupPublic で
得た lookup は public なメンバしか lookup できない。


D: MethodHandles.lookup()
D: lookup: LookupEx
D: lookup.lookupClass: class LookupEx
D: lookup.lookupModes: 15 PACKAGE PRIVATE PROTECTED PUBLIC 
This is a private method
Foo#aPackageMethod()
Foo#aProtectedMethod()
java.lang.IllegalAccessException: member is private: Foo.aPrivateMethod()void, from LookupEx
	at java.lang.invoke.MemberName.makeAccessException(MemberName.java:507)
	at java.lang.invoke.MethodHandles$Lookup.checkAccess(MethodHandles.java:1155)
	at java.lang.invoke.MethodHandles$Lookup.checkMethod(MethodHandles.java:1136)
	at java.lang.invoke.MethodHandles$Lookup.accessVirtual(MethodHandles.java:644)
	at java.lang.invoke.MethodHandles$Lookup.findVirtual(MethodHandles.java:637)
	at LookupEx.callFooPrivateEx(LookupEx.java:43)
	at LookupEx.main(LookupEx.java:10)
D: Foo.lookup()
D: lookup: Foo
D: lookup.lookupClass: class Foo
D: lookup.lookupModes: 15 PACKAGE PRIVATE PROTECTED PUBLIC 
java.lang.IllegalAccessException: member is private: LookupEx.aPrivateMethod()void, from Foo
	at java.lang.invoke.MemberName.makeAccessException(MemberName.java:507)
	at java.lang.invoke.MethodHandles$Lookup.checkAccess(MethodHandles.java:1155)
	at java.lang.invoke.MethodHandles$Lookup.checkMethod(MethodHandles.java:1136)
	at java.lang.invoke.MethodHandles$Lookup.accessStatic(MethodHandles.java:587)
	at java.lang.invoke.MethodHandles$Lookup.findStatic(MethodHandles.java:583)
	at LookupEx.callex(LookupEx.java:34)
	at LookupEx.main(LookupEx.java:16)
Foo#aPackageMethod()
Foo#aProtectedMethod()
Foo#aPrivateMethod()
D: MethodHandles.publicLookup()
D: lookup: java.lang.Object/public
D: lookup.lookupClass: class java.lang.Object
D: lookup.lookupModes: 1 PUBLIC 
java.lang.IllegalAccessException: member is private: LookupEx.aPrivateMethod()void, from java.lang.Object/public
	at java.lang.invoke.MemberName.makeAccessException(MemberName.java:507)
	at java.lang.invoke.MethodHandles$Lookup.checkAccess(MethodHandles.java:1155)
	at java.lang.invoke.MethodHandles$Lookup.checkMethod(MethodHandles.java:1136)
	at java.lang.invoke.MethodHandles$Lookup.accessStatic(MethodHandles.java:587)
	at java.lang.invoke.MethodHandles$Lookup.findStatic(MethodHandles.java:583)
	at LookupEx.callex(LookupEx.java:34)
	at LookupEx.main(LookupEx.java:23)

