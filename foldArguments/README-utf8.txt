foldArguments のサンプル。
foldArguments(target, combiner) の返す MethodHandle は combiner に 
MethodHandle が呼ばれると、combiner のとる引数の数分の先頭の引数を
使い combiner を呼ぶ。combiner が void でなければ MethodHandle の
引数の先頭に combiner の戻り値を加え、target を呼ぶ。combiner が
void の場合、MethodHandle が呼ばれた時の引数で、target を呼び出す。

サンプルの場合、combiner は引数をプリントして、String "daddy" を
返す。fold の結果の mh_folded を 'd', 'n' を引数に呼び出すと
printHeader の戻り値を先頭に挟み、mh_replace を "daddy", 'd', 'n'
を引数に呼び出す。結果的に "daddy".replace('d', 'n') となる。

D: mh_folded type: (char,char)String
* params: d, n
D: mh_fold call: nanny

