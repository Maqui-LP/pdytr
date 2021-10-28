package pdytr.example.grpc;

import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {
  @Override
  public void greeting(GreetingServiceOuterClass.HelloRequest request,
        StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver){
    // HelloRequest has toString auto-generated.
    System.out.println(request);
    
    //Ex - pe - ri - men - to...
    try {
      System.out.println("Inicio de descanso..");
      Thread.sleep(10);
      System.out.println("Fin del descanso!");

    } catch (Exception e) {
      e.printStackTrace();
    }
    if(Context.current().isCancelled()){
      System.out.println("EL deadline ha vencido...");
      responseObserver.onError(
              Status.CANCELLED
              .withDescription("Request cancelada por deadline")
              .asRuntimeException());
      return;
    }
    // You must use a builder to construct a new Protobuffer object
    GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
      .setGreeting("Hello there, " + request.getName())
      .build();

    // Use responseObserver to send a single response back
    responseObserver.onNext(response);

    // When you are done, you must call onCompleted.
    responseObserver.onCompleted();
  }
}