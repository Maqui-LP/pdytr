package pdytr.example.grpc;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.grpc.*;

public class Client
{
    private static int deadlineMs = 3*1000;

    public static void main( String[] args ) throws Exception
    {
        if(args.length != 0){
            deadlineMs = Integer.valueOf(args[0]);
        }
        // Channel is the abstraction to connect to a service endpoint
      // Let's use plaintext communication because we don't have certs
      final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
        .usePlaintext(true)
        .build();
      
      // It is up to the client to determine whether to block the call
      // Here we create a blocking stub, but an async stub,
      // or an async stub with Future are always possible.
      GreetingServiceGrpc.GreetingServiceBlockingStub stub = 
        //GreetingServiceGrpc.newBlockingStub(channel).withDeadlineAfter(3,TimeUnit.SECONDS);
              GreetingServiceGrpc.newBlockingStub(channel).withDeadlineAfter(deadlineMs,TimeUnit.MILLISECONDS);

      GreetingServiceOuterClass.HelloRequest request =
        GreetingServiceOuterClass.HelloRequest.newBuilder()
          .setName("Ray")
          .build();

      // Finally, make the call using the stub
      GreetingServiceOuterClass.HelloResponse response = 
        stub.greeting(request);

      System.out.println(response);

      // A Channel should be shutdown before stopping the process.
      channel.shutdownNow();
    }
}