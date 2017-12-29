package com.shellshellfish.datamanager.service.impl;

import com.shellshellfish.datamanager.service.FundInfoGrpcService;
import io.grpc.Server;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2017- 十二月 - 29
 */
@Service
public class DataManagerGRPCService extends Server{

  @Autowired
  FundInfoGrpcService fundInfoGrpcService;

  @Override
  public Server start() throws IOException {
    return null;
  }

  @Override
  public Server shutdown() {
    return null;
  }

  @Override
  public Server shutdownNow() {
    return null;
  }

  @Override
  public boolean isShutdown() {
    return false;
  }

  @Override
  public boolean isTerminated() {
    return false;
  }

  @Override
  public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
    return false;
  }

  @Override
  public void awaitTermination() throws InterruptedException {

  }
}
