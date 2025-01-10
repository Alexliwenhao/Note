# 热更新
使用命令`sc`查看当前所有类的信息，找到需要更新的类。
```
sc -d com.jly.controller.*
```

通过jad反编译代码，把它保存到另一个.java 文件中注意--source-only 要进跟在jad后面
```
jad  --source-only  com.jly.controller.Hello > C:\Hello.java
```

编辑C:\ Hello.java，修改代码
```
vim C:\Hello.java
```

查找这个类的类加载器，编译时不指定类加载器可能会失败 
```
sc -d *Hello | grep classLoaderHash
```
编译为class文件 
```
mc -c 18b4aac2 C:\Hello.java -d C:\
```

加载外部的class文件，实现热更新 
```
redefine -c 18b4aac2 C:\com\jly\controller\Hello.class
```

注意要修改路径为
```
redefine -c 18b4aac2 C:/test/com/jly/controller/Hello.class
```