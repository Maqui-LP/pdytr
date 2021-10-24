package pdytr.example.grpc;

import com.google.protobuf.ByteString;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import pdytr.example.grpc.GreetingServiceGrpc;
import pdytr.example.grpc.GreetingServiceOuterClass.WriteRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Client
{
    public static void main( String[] args ) throws Exception
    {
      // Channel is the abstraction to connect to a service endpoint
      // Let's use plaintext communication because we don't have certs
      final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
        .usePlaintext(true)
        .build();

        final GreetingServiceGrpc.GreetingServiceStub stub = GreetingServiceGrpc.newStub(channel);
        String opt = args.length > 0 ? args[0] : "default";
        switch (opt){
            case "write":
                writeOpt(stub);
                break;
            default:
                System.out.println("Para ejecutar write utilizar el siguiente comando:");
                System.out.println("mvn package exec:java -Dexec.mainClass=pdytr.example.grpc.Client -Dexec.args=\"write\"");
        }
    }

    private static void writeOpt(GreetingServiceGrpc.GreetingServiceStub greetingServiceStub) throws IOException {
        StreamObserver<WriteRequest> streamObserver = greetingServiceStub.write(new FTPClient());
        InputStream inputStream = Files.newInputStream(Paths.get("/home/nico/gitProyects/pdytr/pdytr/practica3/files/cliente-files/prueba.txt"));
        byte[] bytes = new byte[3];
        int size;
        System.out.println("Cantidad total de bytes a enviar: " + Files.readAllBytes(Paths.get("/home/nico/gitProyects/pdytr/pdytr/practica3/files/cliente-files/prueba.txt")).length);
        while((size = inputStream.read(bytes)) > 0){
            WriteRequest writeRequest = WriteRequest.newBuilder()
                    .setFilename("pruba-server.txt")
                    .setData(ByteString.copyFrom(bytes,0,size))
                    .setTotalBytesToRead(3)
                    .build();
            System.out.println("Enviando datos al servidor");
            streamObserver.onNext(writeRequest);
        }
        inputStream.close();
        streamObserver.onCompleted();
    }

}