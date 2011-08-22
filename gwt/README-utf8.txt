MethodHandles.guardWithTest のサンプル。

guardWithTest(test, target, fallback) は test を実行し、その boolean の
結果が true であれば target を、false であれば fallback を呼び出す
MethodHandle を返す。

test の戻り値が boolean である事以外、また test の引数が他の物より
短くてかまわない事以外、これらの MethodHandle のタイプは一致して
いる必要がある。場合により、insertArguments, dropArguments を使用
して、型を合わせる。

このサンプルの場合、test は引数が String のインスタンスであるかを
判断し、target には printString, fallback には printObject を指す
MethodHandle を指定している。

また、引数の型合わせも行っている。

output:
printString: This is a String t: -1
printObject: 10 t: -1 t2: 99
D: test     : MethodHandle(Object)boolean
D: test1    : MethodHandle(Object,long)boolean
D: target   : MethodHandle(Object,long)void
D: fallback : MethodHandle(Object,long,long)void
D: fallback1: MethodHandle(Object,long)void
D: mh       : MethodHandle(Object,long)void

