package pdytr.example.grpc;
import io.grpc.*;

import io.grpc.*;

public class Client
{
    public static void main( String[] args ) throws Exception
    {
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
            .usePlaintext(true)
            .build();
      
        TimeServiceGrpc.TimeServiceBlockingStub stub = TimeServiceGrpc.newBlockingStub(channel);
        TimeServiceOuterClass.TimeRequest request = TimeServiceOuterClass.TimeRequest.newBuilder()
        .setHello(true)
        .build();

      // Finally, make the call using the stub
        final long start = System.currentTimeMillis();
        TimeServiceOuterClass.TimeResponse response = stub.time(request);

        final long end = System.currentTimeMillis() - start; 
        //double miliSeconds = (double)end / 1000000.0;
        System.out.println("Tiempo en milisegundos: " + end);
        //System.out.println(response);


      // A Channel should be shutdown before stopping the process.
        channel.shutdownNow();
    }
}