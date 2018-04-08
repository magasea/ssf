package com.shellshellfish.aaas.transfer.service.impl;

    import com.shellshellfish.aaas.tools.oeminfo.OemInfoItem;
    import com.shellshellfish.aaas.tools.oeminfo.OemInfoQuery;
    import com.shellshellfish.aaas.tools.oeminfo.OemInfoServiceGrpc.OemInfoServiceBlockingStub;
    import com.shellshellfish.aaas.transfer.service.GrpcOemInfoService;
    import io.grpc.ManagedChannel;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2018- 四月 - 08
 */
@Service
public class GrpcOemInfoServiceImpl implements GrpcOemInfoService{

  @Autowired
  OemInfoServiceBlockingStub oemInfoServiceBlockingStub;

  @Override
  public Map<String, String> getOemInfoById(Long oemId) {
    OemInfoQuery.Builder OIBuilder = OemInfoQuery.newBuilder();
     OIBuilder.setOemId(oemId);
    List<OemInfoItem> oemInfoItemList = oemInfoServiceBlockingStub.getOemInfoByOemId(OIBuilder.build()).getOemInfoItemsList();
    Map<String, String> oemInfos = new HashMap<>();
    oemInfoItemList.forEach(
        item -> {
          oemInfos.put(item.getFieldName(), item.getFieldValue());
        }
    );
    return oemInfos;
  }
}
