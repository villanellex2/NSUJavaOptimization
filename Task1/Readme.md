Запустить программу можно собрав проект самостоятельно, например, в IntelJ IDEA или же с помощью jar файла. Для этого необходимо прописать
```
java -jar build/libs/constantPool.jar 
```
Можно в качестве аргумента командной строки передать имя класс-файла, например:
```
java -jar build/libs/constantPool.jar build/classes/java/main/nsu/ru/edubinskaya/ConstantPool/CLI.class
```
Если этого не произошло, вас попросят ввести имя класс-файла позже.
Программа выводит констант пул указанного класс-файла.
