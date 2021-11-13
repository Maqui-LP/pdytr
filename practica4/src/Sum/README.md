##Ejecutar los siguientes comandos:
 ```
>cd src
>javac -classpath lib/jade.jar -d Sum/classes Sum/SumAgent.java
>java -cp lib/jade.jar:classes jade.Boot -gui
``` 
En otra terminal luego ejecutar:
```
>cd Sum
>java -cp ../lib/jade.jar:classes jade.Boot -container -host localhost -agents mov:SumAgent
``` 
###SUERTE...