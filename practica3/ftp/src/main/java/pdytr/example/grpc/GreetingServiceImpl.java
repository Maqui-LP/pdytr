package pdytr.example.grpc;

import io.grpc.stub.StreamObserver;
import pdytr.example.grpc.GreetingServiceGrpc.GreetingServiceImplBase;
import pdytr.example.grpc.GreetingServiceOuterClass.Status;
import pdytr.example.grpc.GreetingServiceOuterClass.WriteRequest;
import pdytr.example.grpc.GreetingServiceOuterClass.WriteResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GreetingServiceImpl extends GreetingServiceImplBase {

    private final Path storageDirectory = Paths.get("/home/nico/gitProyects/pdytr/pdytr/practica3/files/server-files/");
    //private final Path pathMuestra = Paths.get("/pdytr/ftp/archivos-grpc/");
    private int totalBytes = 0;

    private final Logger LOGGER = Logger.getLogger(GreetingServiceImpl.class.getName());

    @Override
    public StreamObserver<WriteRequest> write(final StreamObserver<WriteResponse> responseObserver) {
        return new StreamObserver<WriteRequest>() {
            int totalBytesBeforeWrite;
            Path path;
            Status status = Status.IN_PROGRESS;

            @Override
            public void onNext(WriteRequest value) {
                path = getFullPath(value.getFilename());
                totalBytesBeforeWrite = getBytesBeforeWrite(path);
                try {
                    Files.write(path,value.getData().toByteArray(), StandardOpenOption.CREATE,StandardOpenOption.APPEND);
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE,"Error al escribir archivo, causa: " + e.getCause());
                    this.onError(e);
                }
                totalBytes += totalBytes + getBytesAfterWrite(path , totalBytesBeforeWrite);
            }

            @Override
            public void onError(Throwable t) {
                status = Status.FAILED;
                LOGGER.log(Level.SEVERE, "Error en el servidor al realizar el write, causa: " + t.getCause());
                this.onCompleted();
            }

            @Override
            public void onCompleted() {
                status = Status.IN_PROGRESS.equals(status) ? Status.SUCCESS : status;
                responseObserver.onNext(WriteResponse.newBuilder()
                        //.setTotalBytesWritten(getBytesAfterWrite(path,totalBytesBeforeWrite))
                                .setTotalBytesWritten(totalBytes)
                        .setStatus(status)
                        .build());
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
            private int getBytesBeforeWrite(Path path){
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
            private int getBytesAfterWrite(Path path, int bytesBeforeWrite){
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