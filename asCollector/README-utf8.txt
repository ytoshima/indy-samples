asCollector, asSpreader, asType のサンプル

配列を取る MethodHandle を指定した数の positional arguments をとる 
Adapter MethodHandle にする。

(Object[])A が (Object, Object, Object, Object)A の様になる。

asSpreader は逆の操作を行う。

asType は指定された MethodType に MethodHandle を変換する。機械的に
行う処理のため、変換にはルールがある。詳細は API 参照

invoke (in-exact invoke) は内部的に asType と同様の操作を行う。
また、その操作のため、invokeExact より変換が発生する場合には
少し遅い。


output:
m0   : MethodHandle(Object[])void
mcoll 1: MethodHandle(Object,Object,Object,Object)void
(Foo Bar Fizz Buzz )
mcoll 2: MethodHandle(String,String,String,String)void
(Foo Bar Fizz Buzz )
mspr: MethodHandle(Object[])void
(Foo Bar Fizz Buzz )

