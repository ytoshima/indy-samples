invokedynamic と guardWithTest を使用した inline cache の例, asm-4.0 対応版

* コンパイル、実行には asm-4.0.jar が必要です。
  http://forge.ow2.org/projects/asm/ などからダウンロードして、lib
  の下に置いて下さい。

1. DyanicIndy クラスを使用して、invokedynamic 命令を生成、talk という
   名前で、シグネチャ(型)は (Object)void, この場合の Object は receiver。
   bootstrap method は Pet.class の bsm を指定、bootstrap method の
   型は (Lookup, String, MethodType, Object)CallSite.  指定可能は
   bootstrap method のシグネチャは java.lang.invoke の package の API
   参照。
   生成した invokedynamic をもつメソッドの MethodHandle を mh に
   受け取る。

2. Cat, Dog, Cat, Dog の順で、それぞれ、4 回ずつ呼び出す。

3. 最初の invokeExact 呼び出して、Pet.bsm が呼び出される。

4. Pet.bsm は MutableCallSite のサブクラス InlineCacheCallSite のインスタンスを
   つくり、target に fallback() に CallSite を bind している。bootstrap は
   作った CallSite を return する。 InlineCacheCallSite は lookup, name, type
   をフィールドに持つ。

5. 続いて、fallback が呼ばれる。引数の最初の物の class を receiverClass に
   得る。InlineCacheCallSite に保存しておいた lookup, name, type を元に
   target を捜す。ここでは Cat.talk が見つかる。target の型を asType で
   調整。

6. test MethodHandle を生成。最終的に参照されるメソッドは 
   isCachedClass(Class, Object) だが、receiverClass を bind しておく。
   これで、前回 fallback が呼ばれた時の receiver の型と、次に呼ばれた
   時の receiver の型が一致しているかをテストできる。

7. fallback MethodHandle を生成、CallSite を bind しておく。

8. MethodHandles.guardWithTest で test, target, fallback を合成。

9. CallSite の target を更新

10. target を呼び出し、リターン。


指定している receiver の型が変わったときのみ fallback が呼び出され、
target が再設定されている事が分かる。target にキャッシュされた
MethodHandle が receiver の型が一致している限り呼ばれる。

output: ant run

run:
     [java] D: bsm: type: (Object)void
     [java] D: bsm: fallback: MethodHandle(Object)void
     [java] D: bsm: fallback: MethodHandle(Object)void
     [java] D: fallback: type: (Object)void
     [java] D: fallback: type.dropParameterTypes(0,1): ()void
     [java] D: fallback: receiverClass: class name.yt.icache.Cat
     [java] D: fallback: target: MethodHandle(Cat)void
     [java] D: fallback: target after asType: MethodHandle(Object)void
     [java] D: fallback: test: MethodHandle(Object)boolean
     [java] D: fallback: site.getTarget(): MethodHandle(Object)void
     [java] D: target type: (Object)void
     [java] D: fallback type: (Object)void
     [java] D: ttype.equals(ftype) = true
     [java] Meow
     [java] D: isCachedClass true.
     [java] Meow
     [java] D: isCachedClass true.
     [java] Meow
     [java] D: isCachedClass true.
     [java] Meow
     [java] D: isCachedClass false !
     [java] D: fallback: type: (Object)void
     [java] D: fallback: type.dropParameterTypes(0,1): ()void
     [java] D: fallback: receiverClass: class name.yt.icache.Dog
     [java] D: fallback: target: MethodHandle(Dog)void
     [java] D: fallback: target after asType: MethodHandle(Object)void
     [java] D: fallback: test: MethodHandle(Object)boolean
     [java] D: fallback: site.getTarget(): MethodHandle(Object)void
     [java] D: target type: (Object)void
     [java] D: fallback type: (Object)void
     [java] D: ttype.equals(ftype) = true
     [java] Bark
     [java] D: isCachedClass true.
     [java] Bark
     [java] D: isCachedClass true.
     [java] Bark
     [java] D: isCachedClass true.
     [java] Bark
     [java] D: isCachedClass false !
     [java] D: fallback: type: (Object)void
     [java] D: fallback: type.dropParameterTypes(0,1): ()void
     [java] D: fallback: receiverClass: class name.yt.icache.Cat
     [java] D: fallback: target: MethodHandle(Cat)void
     [java] D: fallback: target after asType: MethodHandle(Object)void
     [java] D: fallback: test: MethodHandle(Object)boolean
     [java] D: fallback: site.getTarget(): MethodHandle(Object)void
     [java] D: target type: (Object)void
     [java] D: fallback type: (Object)void
     [java] D: ttype.equals(ftype) = true
     [java] Meow
     [java] D: isCachedClass true.
     [java] Meow
     [java] D: isCachedClass true.
     [java] Meow
     [java] D: isCachedClass true.
     [java] Meow
     [java] D: isCachedClass false !
     [java] D: fallback: type: (Object)void
     [java] D: fallback: type.dropParameterTypes(0,1): ()void
     [java] D: fallback: receiverClass: class name.yt.icache.Dog
     [java] D: fallback: target: MethodHandle(Dog)void
     [java] D: fallback: target after asType: MethodHandle(Object)void
     [java] D: fallback: test: MethodHandle(Object)boolean
     [java] D: fallback: site.getTarget(): MethodHandle(Object)void
     [java] D: target type: (Object)void
     [java] D: fallback type: (Object)void
     [java] D: ttype.equals(ftype) = true
     [java] Bark
     [java] D: isCachedClass true.
     [java] Bark
     [java] D: isCachedClass true.
     [java] Bark
     [java] D: isCachedClass true.
     [java] Bark
　 
