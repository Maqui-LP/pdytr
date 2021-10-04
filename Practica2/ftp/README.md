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

### Operaciones:
- leer: se utiliza el comando read
- escribir: se utiliza el comando write

#### Operacion Read:
```sh
$ cd /pdytr/
$ java MainClient localhost read
```
Una vez ejecutado el comando el cliente solicitará:
- Nombre del archivo a ser leido.
- Path en donde se encuentra ubicado el archivo solicitado.
- Nombre del archivo destino.
- Path en donde se encuentra (o será creado) el archivo destino.
- Cantidad de bytes a leer.
- Posición inicial.

#### Operacion Write:
```sh
$ cd /pdytr/
$ java MainClient localhost write
```
Una vez ejecutado el comando el cliente solicitará:
- Nombre del archivo a escribir.
- El contenido a ser escrito.
> NOTA: El servidor almacenará el archivo en la carpeta /pdytr/archivos/

## Detener servidor:
```sh
$ make stop
```

## Borrar todos los archivos .class
```sh
$ make clean
```