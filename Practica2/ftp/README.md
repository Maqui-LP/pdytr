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
- copia: Se copia un archivo del servidor al sistema de archivos local y luego se vuelve a copiar al sistema de archivos del servidor. Se utiliza con el comando ejercicio-b
- tiempo de respuesta: Muestra el tiempo de respuesta de una invocación. Se utiliza el comando ejercicio-5-a
- timeout: se utiliza el comando timeout

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


#### Operacion ejercicio-b:
```sh
$ cd /pdytr/
$ java MainClient localhost ejercicio-b
```
> NOTA: El archivo a copiarse debe llamarse ejercicio-b. Todos los archivos serán alamacenados en la carpeta /pdytr/archivos/


#### Operacion ejercicio-5-a:
```sh
$ cd /pdytr/
$ java MainClient localhost ejercicio-5-a
```


#### Operacion timeout:
```sh
$ cd /pdytr/
$ java sun.rmi.transport.tcp.responseTimeout=[tiempoEnMilisegundosDeseado] MainClient localhost timeout
```

## Detener servidor:
```sh
$ make stop
```

## Borrar todos los archivos .class
```sh
$ make clean
```