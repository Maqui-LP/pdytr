package pdytr.example.grpc;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
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

        CountDownLatch latch = new CountDownLatch(10);
      for(int i=1 ; i<= 10; i++){
          try{
              GreetingServiceGrpc.GreetingServiceBlockingStub stub =
                      //GreetingServiceGrpc.newBlockingStub(channel).withDeadlineAfter(3,TimeUnit.SECONDS);
                      GreetingServiceGrpc.newBlockingStub(channel).withDeadlineAfter(deadlineMs,TimeUnit.MILLISECONDS);

              GreetingServiceOuterClass.HelloRequest request =
                      GreetingServiceOuterClass.HelloRequest.newBuilder()
                              .setName("Ray")
                              .build();
              GreetingServiceOuterClass.HelloResponse response =
                      stub.greeting(request);
                System.out.println(response);
          }catch (Exception e) {
              System.out.println("Ocurrio un error: " + e.getMessage());
          }finally {
              latch.countDown();
          }
      }
      latch.await();
      // A Channel should be shutdown before stopping the process.
      channel.shutdownNow();
    }
}