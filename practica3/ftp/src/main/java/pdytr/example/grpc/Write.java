package pdytr.example.grpc;

import pdytr.example.grpc.GreetingServiceGrpc.GreetingServiceStub;
import pdytr.example.grpc.GreetingServiceOuterClass.WriteRequest;

public interface Write {
    void doWriteRequest(WriteRequest request, GreetingServiceStub stub);
}
