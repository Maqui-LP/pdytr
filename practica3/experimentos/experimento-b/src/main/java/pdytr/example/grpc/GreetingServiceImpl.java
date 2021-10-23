package pdytr.example.grpc;

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
      Thread.sleep(2000);
      System.out.println("Fin del descanso!");

    } catch (Exception e) {
      e.printStackTrace();
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