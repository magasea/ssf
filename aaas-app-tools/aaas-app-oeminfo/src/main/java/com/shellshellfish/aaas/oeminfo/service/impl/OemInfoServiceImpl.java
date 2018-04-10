package com.shellshellfish.aaas.oeminfo.service.impl;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.oeminfo.configuration.OemInfoProperties;
import com.shellshellfish.aaas.oeminfo.configuration.OemInfoProperties.OemInfo;
import com.shellshellfish.aaas.oeminfo.service.OemInfoService;
import com.shellshellfish.aaas.tools.oeminfo.OemInfoItem;
import com.shellshellfish.aaas.tools.oeminfo.OemInfoResult;
import com.shellshellfish.aaas.tools.oeminfo.OemInfoServiceGrpc.OemInfoServiceImplBase;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class OemInfoServiceImpl extends OemInfoServiceImplBase implements OemInfoService {


  private OemInfoProperties oemInfoProperties;


  @Autowired
  public void setOemInfoProperties( OemInfoProperties oemInfoProperties ){
    this.oemInfoProperties = oemInfoProperties;
  }

  @Override
  public void getOemInfoByOemId(com.shellshellfish.aaas.tools.oeminfo.OemInfoQuery request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.tools.oeminfo.OemInfoResult> responseObserver) {
    try{
      OemInfoResult.Builder oirBuilder = OemInfoResult.newBuilder();
      OemInfoItem.Builder oiiBuilder = OemInfoItem.newBuilder();
      Map<String, String> result = getOemInfo((long) request.getOemId());
      result.forEach(
          (key, value) ->{
            oiiBuilder.setFieldName(key);
            oiiBuilder.setFieldValue(value);
            oirBuilder.addOemInfoItems(oiiBuilder);
            oiiBuilder.clear();
          }
      );
      responseObserver.onNext(oirBuilder.build());
    }catch (Exception ex){
      responseObserver.onError(ex);
    }
    responseObserver.onCompleted();
  }

  @Override
  public Map<String, String> getOemInfo(Long oemId) {
    List<OemInfo> oemInfos = oemInfoProperties.getOemInfos();
    for (OemInfo item : oemInfos) {

      if (item.getOemId().equals(oemId.toString())) {
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, String> map = oMapper.convertValue(item, Map.class);
        return map;
      }
    }
    return null;
  }

}
