## Docker

El ambiente se encuentra dockerizado, para ejecutarlo:

```sh
$ cd ./Practica2/ftp
$ docker run -itd -v "$(pwd)":/pdytr/ -p 5091:5091 -p 6901:6901 --name pdytr gmaron/pdytr
$ docker exec --user root -it pdytr bash
```

## Compilación y ejecución

### Servidor (terminal 1)

El servidor se ejecuta en localhost

```sh
$ cd /pdytr/
$ make
```
### Cliente (terminal 2)

```sh
$ cd /pdytr/
$ java MainClient localhost operacion sourceFileName sourceFilePath outputFileName outputFilePath chunkSize initialPosition
```

### Operaciones:
- leer: se utiliza el comando read
- escribir: se utiliza el comando write

## Detener servidor:
```sh
$ make stop
```

## Borrar todos los archivos .class
```sh
$ make clean
```