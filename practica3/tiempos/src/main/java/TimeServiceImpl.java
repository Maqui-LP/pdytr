package pdytr.example.grpc;

import io.grpc.stub.StreamObserver;

public class TimeServiceImpl extends TimeServiceGrpc.TimeServiceImplBase {
  @Override
  public void time(TimeServiceOuterClass.TimeRequest request,
        StreamObserver<TimeServiceOuterClass.TimeResponse> responseObserver) {

    TimeServiceOuterClass.TimeResponse response = TimeServiceOuterClass.TimeResponse.newBuilder()
    .setHello(true)
    .build();

    // Use responseObserver to send a single response back
    responseObserver.onNext(response);

    // When you are done, you must call onCompleted.
    responseObserver.onCompleted();
  }
}