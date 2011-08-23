引数の型を変換するフィルタを設定するサンプルプログラム。

domatch() は String と java.util.regex.Pattern をとるが、
String から Pattern を生成するメソッド getPtn() を引数の 
filter に使用する事で、マッチされる文字列と、文字列で
指定した正規表現の引数をとる MethodHandle を得る。

output:
D: mh_filtered type: (String,String)Matcher
matched: str: daddy ptn: .*dd.*
D: mh_filtered call: java.util.regex.Matcher[pattern=.*dd.* region=0,5 lastmatch=daddy]

