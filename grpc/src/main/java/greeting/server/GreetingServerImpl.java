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

    @Override
    public void greetManyTimes(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        GreetingResponse response = GreetingResponse.newBuilder().setResult("Hello" + request.getFirstName()).build();

        for (int i = 0; i < 10; ++i){
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GreetingRequest> longGreet(StreamObserver<GreetingResponse> responseObserver) {
        StringBuilder sb = new StringBuilder();

        return new StreamObserver<GreetingRequest>() {
            @Override
            public void onNext(GreetingRequest greetingRequest) {
                sb.append("Hello ");
                sb.append(greetingRequest.getFirstName());
                sb.append("!\n");
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(GreetingResponse.newBuilder().setResult(sb.toString()).build());
                responseObserver.onCompleted();
            }
        };
    }
}
