package com.shellshellfish.gateway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.shellshellfish.bankaccount.grpc.BankAccount;
import com.shellshellfish.bankaccount.grpc.BankAccountNumber;
import com.shellshellfish.bankaccount.grpc.BankAccountServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

@Service
public class BankAccountService {
	private static final Logger logger = Logger.getLogger(BankAccountService.class.getName());

	private ManagedChannel managedChannel;
	
	private BankAccountServiceGrpc.BankAccountServiceBlockingStub blockingStub;

	private BankAccountServiceGrpc.BankAccountServiceStub asyncStub;
	
	public BankAccount getBankAccount(long bankAccountNumber) {
		BankAccountNumber.Builder builder = BankAccountNumber.newBuilder();
		builder.setAccountNumber(bankAccountNumber);
		return blockingStub.getBankAccount(builder.build());
	}
	
	public List<BankAccount> listBankAcount(List<Long> bankAccountNumbers) {
		final List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
		final CountDownLatch finishLatch = new CountDownLatch(1);
		
		StreamObserver<BankAccountNumber> requestObserver = 
				asyncStub.listBankAccounts(new StreamObserver<BankAccount>() {

					@Override
					public void onCompleted() {
						logger.info("End receiving BankAccount");
						finishLatch.countDown();						
					}

					@Override
					public void onError(Throwable err) {
						logger.info("Error receiving BankAccount: " + err.getStackTrace());
						finishLatch.countDown();
					}

					@Override
					public void onNext(BankAccount bankAccount) {
						logger.info("Received one BankAccount from grpc channel");
						bankAccounts.add(bankAccount);
					}
				});
		
		for (long number : bankAccountNumbers) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BankAccountNumber.Builder builder = BankAccountNumber.newBuilder();
			builder.setAccountNumber(number);
			
			logger.info("Sending one BandAccount request to grpc channel");
			requestObserver.onNext(builder.build());
		}
		requestObserver.onCompleted();
		
		try {
			finishLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return bankAccounts;
	}
	
	@PostConstruct
	private void initializeClient() {
		managedChannel = ManagedChannelBuilder.forAddress("localhost", 7000).usePlaintext(true).build();
		blockingStub = BankAccountServiceGrpc.newBlockingStub(managedChannel);
		asyncStub = BankAccountServiceGrpc.newStub(managedChannel);
	}
	
	@PreDestroy
	private void closeClient() {
		try {
			managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
