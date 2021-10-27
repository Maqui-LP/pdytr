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
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GreetingServiceImpl extends GreetingServiceImplBase {

    private Path store = Paths.get("src/main/resources/server-files/");
    private final Logger LOGGER = Logger.getLogger(GreetingServiceImpl.class.getName());
    private ReentrantLock block = new ReentrantLock();

    @Override
    public StreamObserver<WriteRequest> write(final StreamObserver<WriteResponse> responseObserver) {
        return new StreamObserver<WriteRequest>() {

            Path path;
            long total = 0;
            long beforeWrite = 0;

            @Override
            public void onNext(WriteRequest value) {
                path = getFullPath(value.getFilename());
                block.lock();
                beforeWrite = getBytesBeforeWrite(path);
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
                total += getBytesAfterWrite(path, beforeWrite);
                block.unlock();
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(
                        Status.INTERNAL
                        .withDescription("Error en el servidor, causa: " + t.getCause())
                        .asRuntimeException()
                );
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(WriteResponse.newBuilder()
                        .setStatus(pdytr.example.grpc.GreetingServiceOuterClass.Status.SUCCESS)
                        .setTotalBytesWritten(total)
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
                //return storageDirectory.resolve(filename);
                return store.resolve(filename);
            }

            /**
             * Dado un Path se retorna la cantidad de bytes del archivo
             * @param path
             * @return cantidad de bytes antes de la escritura, -1 si no se pudo determinar
             */
            private  long getBytesBeforeWrite(Path path){
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
            private  long getBytesAfterWrite(Path path, long bytesBeforeWrite){
                long total = 0;
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