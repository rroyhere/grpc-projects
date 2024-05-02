 package greeting.server;

import com.proto.greeting.GreetingServiceGrpc;
import com.proto.greeting.GreetingResponse;
import com.proto.greeting.GreetingRequest;
import io.grpc.stub.StreamObserver;

import io.grpc.stub.StreamObserver;

public class GreetingServerImpl extends GreetingServiceGrpc.GreetingServiceImplBase{
    @Override
    public void greet(GreetingRequest request, StreamObserver<GreetingResponse> respobnObserver){
        respobnObserver.onNext(GreetingResponse.newBuilder().setResult("Hello " + request.getFirstName()).build());
        respobnObserver.onCompleted();
    }
    
}
