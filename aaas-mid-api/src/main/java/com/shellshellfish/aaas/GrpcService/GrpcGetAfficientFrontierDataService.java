package com.shellshellfish.aaas.GrpcService;

import com.shellshellfish.aaas.grpc.GetAfficientFrontierDataServiceGrpc;
import com.shellshellfish.aaas.grpc.GetAfficientFrontierRequest;
import com.shellshellfish.aaas.grpc.GetAfficientFrontierResponse;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

/**
 * Author: Derek
 * Date: 2018/5/8
 * Desc:
 */
@Service
public class GrpcGetAfficientFrontierDataService extends GetAfficientFrontierDataServiceGrpc.GetAfficientFrontierDataServiceImplBase {

    @Override
    public void getAfficientFrontier(GetAfficientFrontierRequest request, StreamObserver<GetAfficientFrontierResponse> responseObserver) {
//        super.getAfficientFrontier(request, responseObserver);
        double name = request.toBuilder().getName();
        String str = request.toString();
        System.out.println("xxx: "+str);

        GetAfficientFrontierResponse.Builder response = GetAfficientFrontierResponse.newBuilder();
        response.addFloats(333.33F);
        response.addFloats(333.33F);
        response.addFloats(333.33F);
        response.addFloats(333.33F);
        GetAfficientFrontierResponse responseValue = response.build();

        System.out.println(responseValue.toString());
        responseObserver.onNext(responseValue);
        responseObserver.onCompleted();

        System.out.println("server is be called .... param: "+ name);

    }
}
