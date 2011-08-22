SwitchPoint を使ったサンプル。

* コンパイル、実行には asm-4.0_RC1.jar が必要です。
  http://forge.ow2.org/projects/asm/ などからダウンロードして、lib
  の下に置いて下さい。

-use-indy を使用すると invokedynamic を生成して、使用します。指定されていない
場合は CallSite を作り、その target の MethodHandle を呼び出しています。

1. bootstrap method は constantFallback に CallSite を覚えさせるなど
   引数を加工した物を target にセットし、return.

2. constantFallback が呼び出される。
   contet から呼び出した値が null でない場合、その値返す constant 
   MethodHandle を生成、valueHandle に保存。

3. fallback の MethodHandle も再作成。

4. SwitchPoint のインスタンスを context 経由で得る。

5. SwitchPoint に関して、guardWithTest(valueHandle, fallback) で
   test guard された MethodHandle を得る。test は指定されていないが、
   SwitchPoint が valid な間 (invalidate される前) には target,
   この場合 valueHandle が、invalidate されたら fallback が呼ばれる。

6. 値の初期値は "Foo".  invalidate されるまでは method handle 呼び出しは
   "Foo" を返す。値を "Bar" に変え、SwitchPoint を invalidate すると
   MethodHandle 呼び出しは "Bar" を返す様になる。


output:
$ java -cp classes:lib/asm-4.0_RC1.jar name.yt.switchpoint.SwitchPointEx -use-indy
D: * Using invokedynamic !
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

