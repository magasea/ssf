package com.shellshellfish.aaas.datacollection;

import com.shellshellfish.aaas.datacollect.CollectionItem;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceBlockingStub;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceStub;
import com.shellshellfish.aaas.datacollect.ItemCollection;
import com.shellshellfish.aaas.datacollect.ItemCollectionResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public class DataCollectionClient {
  Logger logger = LoggerFactory.getLogger(DataCollectionClient.class);
  private ManagedChannel channel;
  private DataCollectionServiceBlockingStub blockingStub;
  private DataCollectionServiceStub asyncStub;

  private Random random = new Random();

  public DataCollectionClient(String host, int port){
    this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true));
  }

  public DataCollectionClient(ManagedChannelBuilder<?> channelBuilder){
    channel = channelBuilder.build();
    blockingStub = DataCollectionServiceGrpc.newBlockingStub(channel);
    asyncStub = DataCollectionServiceGrpc.newStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public List<String> CollectItems(List<String> collectDatas) throws Exception {
    if(CollectionUtils.isEmpty(collectDatas)){
      throw new Exception("Empty list of collectDatas");
    }
    ItemCollection request = ItemCollection.newBuilder().addAllItems(
        (Iterable<? extends CollectionItem>) collectDatas.iterator()).build();
    ItemCollectionResponse itemCollectionResponse = null;
    try{
      itemCollectionResponse = blockingStub.collectItems(request);
    }catch (StatusRuntimeException e){
      logger.warn("RPC failed: {0}", e.getStatus());
    }
    List<String> result = itemCollectionResponse.getItemsList().stream().map(it -> it
        .getName()).collect(Collectors.toList());
    return result;
  }

}
