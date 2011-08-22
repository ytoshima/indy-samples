MethodHandles.dropArguments のサンプル。

指定された場所に、指定した型の引数を挿入したシグネチャ MethodHandle を得られる。
MehtodHandle の合成時に、他の MethodHandle と型を合わせるために使用できる。

この例では (String,char,char)String を (String,Map,Set,char,char)String
に変換。合成された mh_drop は (String,Map,Set,char,char)String で呼び出す
必要がある。dropArguments で増えた引数は実際には target には渡されない。

dropArguments で加えた引数を落として、元の target を呼び出す Adaptor 
MethodHandle を返しているとも言える。

output:
D: mh_drop: nanny // (String,Map,Set,char,char)String
D: mh_drop: nanny // (String,Map,Set,char,char)String

