MethodHandle の戻り値を filterReturnValue で変換するサンプル。

mh_d2s で double2String を呼び、double が String に変換されて返され、
mh_s2bi で String を BigDecimal に変換する。

filterReturnValue で mh_d2s, mh_s2bi で、これらの操作を行う MethodHandle 
mh_filtered を得て、呼び出す。

output:
D: mh_filtered type: (double)BigDecimal
D: mh_filtered call: 123.456

