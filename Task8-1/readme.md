##Для запуска:
1) Добавить переменную среды GRAALVM_HOME, в моём случае: 
```
export GRAALVM_HOME=~/programs/graalvm-ce-java11-21.1.0
```
2) 
```
clang -g -O1 -c -emit-llvm --target=x86_64-unknown-linux-gnu  -I$GRAALVM_HOME/languages/llvm/include src/cpuinfo.c
```
3)
```
java Main.java
```
Либо же просто жмём на зелененькую стрелочку в IDEA.
Если на этом моменте у нас выводится ошибка о том, что target OS не та - необходимо в флаг --target на втором этапе указать правильную, которая выводится в ошибке.
