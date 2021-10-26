package pdytr.example.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import pdytr.example.grpc.GreetingServiceGrpc.GreetingServiceImplBase;
import pdytr.example.grpc.GreetingServiceOuterClass.WriteRequest;
import pdytr.example.grpc.GreetingServiceOuterClass.WriteResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GreetingServiceImpl extends GreetingServiceImplBase {

    private final Path storageDirectory = Paths.get("/home/nico/gitProyects/pdytr/pdytr/practica3/files/server-files/");
    //private final Path pathMuestra = Paths.get("/pdytr/ftp/archivos-grpc/");

    private final Logger LOGGER = Logger.getLogger(GreetingServiceImpl.class.getName());

    @Override
    public StreamObserver<WriteRequest> write(final StreamObserver<WriteResponse> responseObserver) {
        return new StreamObserver<WriteRequest>() {

            Path path;

            @Override
            public void onNext(WriteRequest value) {
                path = getFullPath(value.getFilename());

                try {
                    Files.write(path,value.getData().toByteArray(),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
                } catch (IOException e) {
                    LOGGER.warning("Error al escribir el archivo, causa: " + e.getCause());
                    responseObserver.onError(
                            Status.INTERNAL
                            .withDescription("Error al escribir el archivo")
                            .asRuntimeException());
                    return;
                }

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(WriteResponse.newBuilder()
                        .setStatus(pdytr.example.grpc.GreetingServiceOuterClass.Status.SUCCESS)
                        .build()
                );
                responseObserver.onCompleted();
            }


            /**
             * Dado el nombre de un archivo se obtiene el
             * Path a dicho archivo
             * @param filename
             * @return path al archivo
             */
            private Path getFullPath(String filename){
                return storageDirectory.resolve(filename);
            }

            /**
             * Dado un Path se retorna la cantidad de bytes del archivo
             * @param path
             * @return cantidad de bytes antes de la escritura, -1 si no se pudo determinar
             */
            private  int getBytesBeforeWrite(Path path){
                try {
                    return Files.exists(path) ? Files.readAllBytes(path).length : 0;
                }catch (IOException e){
                    LOGGER.log(Level.SEVERE, "Error al determinar los bytes antes de escribir, causa: " + e.getCause());
                    return -1;
                }
            }

            /**
             * Actualiza el total de bytes luego de haber realizado la
             * escritura
             * @param path
             * @param bytesBeforeWrite
             * @return total de bytes finales, -1 si falla
             */
            private  int getBytesAfterWrite(Path path, int bytesBeforeWrite){
                int total = 0;
                try{
                    if(bytesBeforeWrite > 0){
                        total = Files.readAllBytes(path).length - bytesBeforeWrite;

                    }else {
                        total = Files.readAllBytes(path).length;
                    }
                }catch (IOException e){
                 LOGGER.log(Level.SEVERE,"Error al actualizar el total de bytes, causa: " + e.getCause());
                 return -1;
                }
                return total;
            }
        };
    }
}