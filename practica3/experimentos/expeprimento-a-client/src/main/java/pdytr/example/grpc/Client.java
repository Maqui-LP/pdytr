package pdytr.example.grpc;

import io.grpc.*;
import io.grpc.stub.StreamObserver;
import pdytr.example.grpc.GreetingServiceOuterClass.HelloRequest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
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
      final CountDownLatch latch = new CountDownLatch(1);
      GreetingServiceGrpc.GreetingServiceStub stub = GreetingServiceGrpc.newStub(channel);
      StreamObserver<HelloRequest> requestObserver = stub.greetingClientStream(getStreamObserverResponse(latch));
        requestObserver.onNext(HelloRequest.newBuilder().setName("fantasma").build());
        requestObserver.onCompleted();
        //latch.await();
      // A Channel should be shutdown before stopping the process.
      channel.shutdownNow();
        /**
         * al cerrar el canal el servidor no tendra a donde realizar la respuesta.
         */
    }

    private static StreamObserver<GreetingServiceOuterClass.HelloResponse> getStreamObserverResponse(final CountDownLatch countDownLatch){
        return new StreamObserver<GreetingServiceOuterClass.HelloResponse>() {
            @Override
            public void onNext(GreetingServiceOuterClass.HelloResponse value) {
                LOGGER.info("respuesta del server: " +  value.getGreeting());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Fin");
                countDownLatch.countDown();
            }
        };
    }
}