## Ejecutar los siguientes comandos:

---

 ```
>cd src
>javac -classpath lib/jade.jar -d Sum/classes Sum/SumAgent.java
>java -cp lib/jade.jar:classes jade.Boot -gui
``` 
En otra terminal luego ejecutar:
```
>cd Sum
> java -cp ../lib/jade.jar:classes jade.Boot -container -host localhost -agents 'mov:SumAgent(/home/usr/directory-1/.../practica4/src/Sum/temp/file)'
``` 
>**NOTA**: En algunos sistemas no es necesario englobar dentro de '' el pasaje de argumentos.
### SUERTE...