package pdytr.example.grpc;

import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Client
{
    private static Logger LOGGER = Logger.getLogger(Client.class.getName());

    public static void main( String[] args ) throws Exception
    {
      final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
        .usePlaintext(true)
        .build();

      GreetingServiceGrpc.GreetingServiceStub stub = GreetingServiceGrpc.newStub(channel);

      GreetingServiceOuterClass.HelloRequest request =
            GreetingServiceOuterClass.HelloRequest.newBuilder().setName("Macarena").build();
      stub.greeting(request, new StreamObserver<GreetingServiceOuterClass.HelloResponse>() {
          @Override
          public void onNext(GreetingServiceOuterClass.HelloResponse value) {
              LOGGER.info("Valor recibido: " + value);
          }

          @Override
          public void onError(Throwable t) {
            LOGGER.log(Level.WARNING,"Error: " + t.getCause().getMessage());
          }

          @Override
          public void onCompleted() {
            LOGGER.log(Level.FINE, "Fin.");
          }
      });

      //System.exit(9999);

      // A Channel should be shutdown before stopping the process.
      channel.shutdownNow();
    }
}