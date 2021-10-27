package pdytr.example.grpc;

import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {
  @Override
  public void greeting(GreetingServiceOuterClass.HelloRequest request,
        StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver) {
    // HelloRequest has toString auto-generated.
    System.out.println(request);
    
    //Ex - pe - ri - men - to...

    // You must use a builder to construct a new Protobuffer object
    GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
      .setGreeting("Hello there, " + request.getName())
      .build();

    // Use responseObserver to send a single response back
    responseObserver.onNext(response);

    // When you are done, you must call onCompleted.
    responseObserver.onCompleted();
  }

  @Override
  public StreamObserver<GreetingServiceOuterClass.HelloRequest> greetingClientStream(final StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver) {
    return new StreamObserver<GreetingServiceOuterClass.HelloRequest>() {
      @Override
      public void onNext(GreetingServiceOuterClass.HelloRequest value) {
        System.out.println("Deberia hacer algo aca pero como no hay cliente no hago nada, solo duermo.");
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          System.out.println("Error al dormir thread.." + e.getCause());
          this.onError(e);
        }
        if(Context.current().isCancelled()){
          responseObserver.onError(
                  Status.CANCELLED
                  .withDescription("Cancelado por el cliente")
                  .asRuntimeException());
          return;
        }
      }

      @Override
      public void onError(Throwable t) {
        responseObserver.onError(
                Status.INTERNAL.withDescription("ERROR algo sucedio en el server, causa: " + t.getCause())
                        .asRuntimeException()
        );
      }

      @Override
      public void onCompleted() {
        responseObserver.onNext(GreetingServiceOuterClass.HelloResponse.newBuilder()
                .setGreeting("Hola juan carlos")
                .build());
        responseObserver.onCompleted();
      }
    };
  }
}