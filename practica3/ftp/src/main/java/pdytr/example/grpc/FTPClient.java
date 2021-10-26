package pdytr.example.grpc;

import io.grpc.stub.StreamObserver;
import pdytr.example.grpc.GreetingServiceOuterClass.WriteResponse;

public class FTPClient implements StreamObserver<WriteResponse>{

    public FTPClient(){
    }

    @Override
    public void onNext(WriteResponse value) {
        System.out.println("File upload status :: " + value.getStatus());
        System.out.println("Cantidad de bytes: " + value.getTotalBytesWritten());

    }

    @Override
    public void onError(Throwable t) {
    }

    @Override
    public void onCompleted() {

    }

}
