package pdytr.example.grpc;

import com.google.protobuf.ByteString;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import pdytr.example.grpc.GreetingServiceGrpc;
import pdytr.example.grpc.GreetingServiceOuterClass.WriteRequest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

        closeChannel(channel);
    }

    private static void writeOpt(GreetingServiceGrpc.GreetingServiceStub greetingServiceStub) throws IOException {
        StreamObserver<WriteRequest> streamObserver = greetingServiceStub.write(new FTPClient());//greetingServiceStub.write(new FTPClient());
        RandomAccessFile file = new RandomAccessFile("/home/nico/gitProyects/pdytr/pdytr/practica3/files/cliente-files/prueba","r");
        FileDescriptor fd = file.getFD();
        FileInputStream fis = new FileInputStream(fd);
        byte[] partialData = new byte[1024];
        int byteReaded;
        int totalBytesWritten = 0;

        while(fis.available() > 0){
            byteReaded = fis.read(partialData,0,Math.min(1024,fis.available()));
            byte[]cleanArray = Arrays.copyOf(partialData,byteReaded);
            WriteRequest writeRequest = WriteRequest.newBuilder()
                    .setFilename("prueba-server")
                    .setData(ByteString.copyFrom(cleanArray))
                    .setTotalBytesToRead(byteReaded)
                    .build();
            streamObserver.onNext(writeRequest);
        }
        fis.close();
        streamObserver.onCompleted();

    }

    /**
     * Cierra el channel luego de 1 seg.
     * @param channel
     * @throws InterruptedException
     */
    private static void closeChannel(ManagedChannel channel) throws InterruptedException {
        channel.awaitTermination(1, TimeUnit.SECONDS);
        channel.shutdown();
    }
}