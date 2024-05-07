package greeting.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.proto.greeting.GreetingServiceGrpc;
import com.proto.greeting.*;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {

    private static void doGreet(ManagedChannel channel) {
        System.out.println("Enter doGreet");
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
        GreetingResponse response = stub.greet(GreetingRequest.newBuilder().setFirstName("Clement").build());


        System.out.println("Greeting: " + response.getResult());
    }
    private static void doGreetManyTimes(ManagedChannel channel) {
        System.out.println("Enter doGreetManyTimes");
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
        stub.greetManyTimes(GreetingRequest.newBuilder().setFirstName("Clement").build()).forEachRemaining(response -> {
            System.out.println(response.getResult());
        });


    }

    private static void doLongGreet(ManagedChannel channel) throws InterruptedException {
        System.out.println("Enter doLongGreet");
        GreetingServiceGrpc.GreetingServiceStub stub = GreetingServiceGrpc.newStub(channel);
        List<String> names = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        Collections.addAll(names,  "clement", "Marie", "Rana");
        StreamObserver<GreetingRequest> stream = stub.longGreet(new StreamObserver<GreetingResponse>(){
            @Override
            public void onNext(GreetingResponse greetingResponse) {
                System.out.println(greetingResponse.getResult());
            }

            @Override
            public void onError(Throwable throwable) {}

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });
        for (String name: names){
            stream.onNext(GreetingRequest.newBuilder().setFirstName(name).build());
        }
        stream.onCompleted();;
        latch.await(3, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            System.out.println("Need one argument to work");
            return;
        }

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                // .intercept(new LogInterceptor())
                // .intercept(new AddHeaderInterceptor())
                .usePlaintext()
                .build();
        switch (args[0]) {
            case "greet": doGreet(channel); break;
            case "greet_many_times": doGreetManyTimes(channel); break;
            case "greet_long": doLongGreet(channel); break;
            // case "greet_everyone": doGreetEveryone(channel); break;
            // case "greet_with_deadline": doGreetWithDeadline(channel); break;
            default: System.out.println("Keyword Invalid: " + args[0]);
        }

        System.out.println("Shutting Down");
        channel.shutdown();
    }

}

    
