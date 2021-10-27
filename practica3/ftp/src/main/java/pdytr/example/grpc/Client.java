package pdytr.example.grpc;

import com.google.protobuf.ByteString;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import pdytr.example.grpc.GreetingServiceGrpc;
import pdytr.example.grpc.GreetingServiceOuterClass.WriteRequest;
import pdytr.example.grpc.GreetingServiceOuterClass.ReadRequest;
import pdytr.example.grpc.GreetingServiceOuterClass.ReadResponse;
import java.io.*;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.Iterator;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.Path;
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

        String opt = args.length > 0 ? args[0] : "default";
        String filename = args.length > 1 ? args[1] : "archivoPrueba.txt";
        switch (opt){
            case "write":
                final GreetingServiceGrpc.GreetingServiceStub writeStub = GreetingServiceGrpc.newStub(channel);
                writeOpt(writeStub);
                break;
            case "read":
                final GreetingServiceGrpc.GreetingServiceBlockingStub readStub = GreetingServiceGrpc.newBlockingStub(channel);
                readOpt(readStub, filename);
                break;
            default:
                System.out.println("Para ejecutar write utilizar el siguiente comando:");
                System.out.println("mvn package exec:java -Dexec.mainClass=pdytr.example.grpc.Client -Dexec.args=\"write\"");
                System.out.println("Para ejecutar read utilizar el siguiente comando:");
                System.out.println("mvn package exec:java -Dexec.mainClass=pdytr.example.grpc.Client -Dexec.args=\"read\"");
        }
        closeChannel(channel);
    }

    private static void readOpt(GreetingServiceGrpc.GreetingServiceBlockingStub greetingServiceStub, String filename)  throws IOException {
        try {
            ReadRequest readRequest = ReadRequest.newBuilder()
                .setFilename(filename)
                .setPosition(0)
                .setBytesToRead(2000)
                .build();
            
            Iterator<ReadResponse> stream = greetingServiceStub.read(readRequest);
            while(stream.hasNext()){
                ReadResponse response = stream.next();
                System.out.println(response.getData());
                Files.write(Paths.get(readRequest.getFilename() + "output"), response.getData().toByteArray(), StandardOpenOption.CREATE,StandardOpenOption.APPEND);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    private static void writeOpt(GreetingServiceGrpc.GreetingServiceStub greetingServiceStub) throws IOException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<WriteRequest> requestObserver = greetingServiceStub.write(getStreamResponse(latch));
        RandomAccessFile file = new RandomAccessFile("/home/nico/gitProyects/pdytr/pdytr/practica3/files/cliente-files/prueba","r");
        FileDescriptor fd = file.getFD();
        FileInputStream fis = new FileInputStream(fd);
        byte[] partialData = new byte[1024];
        int byteReaded;
        int totalBytesWritten = 0;
        while(fis.available() > 0){
            byteReaded = fis.read(partialData,0,Math.min(1024,fis.available()));
            byte[]cleanArray = Arrays.copyOf(partialData,byteReaded);
            final WriteRequest writeRequest = WriteRequest.newBuilder()
                    .setFilename("prueba-server")
                    .setData(ByteString.copyFrom(cleanArray))
                    .setTotalBytesToRead(byteReaded)
                    .build();

            requestObserver.onNext(writeRequest);
        }
        requestObserver.onCompleted();
        fis.close();
        latch.await();
    }

    private static StreamObserver<GreetingServiceOuterClass.WriteResponse> getStreamResponse(final CountDownLatch latch){
        return new StreamObserver<GreetingServiceOuterClass.WriteResponse>() {
            @Override
            public void onNext(GreetingServiceOuterClass.WriteResponse value) {
                System.out.println("Estado: " + value.getStatus());
                System.out.println("TOTAL: " + value.getTotalBytesWritten());
                latch.countDown();
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        };
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