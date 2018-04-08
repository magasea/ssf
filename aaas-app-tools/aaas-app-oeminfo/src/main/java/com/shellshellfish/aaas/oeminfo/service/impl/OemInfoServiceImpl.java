package com.shellshellfish.aaas.oeminfo.service.impl;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

import com.shellshellfish.aaas.oeminfo.configuration.OemInfoProperties;
import com.shellshellfish.aaas.oeminfo.service.OemInfoService;
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

  }

  @Override
  public Map<String, String> getOemInfo(Long oemId) {
    List<Map<String, String>> oemInfo = oemInfoProperties.getOemInfo();
    for (Map<String, String> item : oemInfo) {
      if (item.get("oem_id").equals(oemId)) {
        return item;
      }
    }
    return null;
  }

}
