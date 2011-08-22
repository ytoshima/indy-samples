MethodHandles.insertArguments のサンプル

insertArguments は指定された MethodHandle の指定された位置に、後に続く Object のリストを insert する。MethodHandle.bindTo は最初の引数に指定された引数のオブジェクトを設定する。

static String.replace(char, char) を 

findVirtual(String.class, "replace", methodType(String.class /*return value type*/, char.class, char.class)) 

で得た MethodHandle に bindTo("daddy") とする事で、(char,char)String の 
MethodHandle が得られる。この操作で、"daddy" を引数の先頭に挟んで、元の 
String.replace をさす Direct MethodHandle を指定された引数で呼び出す 
Bound MethodHandle が返る。

MethodHandles.insertArguments(mh_replace, 0, "daddy") も同様の結果になる。

insertArguments の結果、引数が関連づけられるので、返ってくる MethodHandle の type からは引数の数が減っている。

概念的には関数型言語の部分適用、currying と似ている。

MethodHandle 生成時に覚えさせておきたい引数を覚えさせておく事ができる。
MutableCallSite/VolatileCallSite を fallback に覚えさせておけば、
fallback 時に CallSite を書き換えるために使用できる。

D: nanny // (String,char,char)String
D: nanny
D: mh_bound: nanny // (char,char)String
D: mh_bound: nanny // (char,char)String
D: nanny // ()String

