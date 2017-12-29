package com.shellshellfish.datamanager.service.impl;


import com.shellshellfish.aaas.datamanager.DayIndicatorCollection;
import com.shellshellfish.aaas.datamanager.DayIndicatorQuery;
import com.shellshellfish.aaas.datamanager.DayIndicatorRpc;
import com.shellshellfish.aaas.datamanager.DayIndicatorServiceGrpc.DayIndicatorServiceImplBase;
import com.shellshellfish.datamanager.model.DayIndicatorDTO;
import com.shellshellfish.datamanager.repositories.MongoDayIndicatorRepository;
import com.shellshellfish.datamanager.service.DayIndicatorService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
public class DayIndicatorServiceImpl extends DayIndicatorServiceImplBase implements DayIndicatorService {


	@Autowired
	MongoDayIndicatorRepository mongoDayIndicatorRepository;


	@Override
	public void findByCode(DayIndicatorQuery query, StreamObserver<DayIndicatorCollection> responseObserver) {
		List<DayIndicatorDTO> dayIndicatorDTOList = mongoDayIndicatorRepository.findByCode(query.getCode());


		DayIndicatorCollection.Builder collectionBuilder = DayIndicatorCollection.newBuilder();
		for (int i = 0; i < dayIndicatorDTOList.size(); i++) {
			DayIndicatorDTO dayIndicatorDTO = dayIndicatorDTOList.get(i);
			DayIndicatorRpc.Builder builder = DayIndicatorRpc.newBuilder();
			copyDayIndicatorProperities(builder, dayIndicatorDTO);
			collectionBuilder.addDayIndicator(builder.build());
		}
		responseObserver.onNext(collectionBuilder.build());
		responseObserver.onCompleted();
	}


	/**
	 * 拷贝dayIndicatorDTO 属性值到 DayIndicatorRpc.Builder
	 *
	 * @param builder
	 * @param dayIndicatorDTO
	 */
	private void copyDayIndicatorProperities(DayIndicatorRpc.Builder builder, DayIndicatorDTO dayIndicatorDTO) {
		if (!StringUtils.isEmpty(dayIndicatorDTO.getAmount())) {
			builder.setAmount(dayIndicatorDTO.getAmount());
		}
		if (!StringUtils.isEmpty(dayIndicatorDTO.getAvgprice())) {
			builder.setAvgprice(dayIndicatorDTO.getAvgprice());
		}
		if (!StringUtils.isEmpty(dayIndicatorDTO.getAmplitude())) {
			builder.setAmplitude(dayIndicatorDTO.getAmplitude());
		}
		if (!StringUtils.isEmpty(dayIndicatorDTO.getClose())) {
			builder.setClose(dayIndicatorDTO.getClose());
		}

		if (!StringUtils.isEmpty(dayIndicatorDTO.getCode())) {
			builder.setCode(dayIndicatorDTO.getCode());
		}

		if (!StringUtils.isEmpty(dayIndicatorDTO.getDiffer())) {
			builder.setDiffer(dayIndicatorDTO.getDiffer());
		}

		if (!StringUtils.isEmpty(dayIndicatorDTO.getDifferrange())) {
			builder.setDifferrange(dayIndicatorDTO.getDifferrange());
		}

		if (!StringUtils.isEmpty(dayIndicatorDTO.getDiscount())) {
			builder.setDiscount(dayIndicatorDTO.getDiscount());
		}

		if (!StringUtils.isEmpty(dayIndicatorDTO.getDiscountrate())) {
			builder.setDiscountrate(dayIndicatorDTO.getDiscountrate());
		}

		if (!StringUtils.isEmpty(dayIndicatorDTO.getHigh())) {
			builder.setHigh(dayIndicatorDTO.getHigh());
		}

		if (!StringUtils.isEmpty(dayIndicatorDTO.getId())) {
			builder.setId(dayIndicatorDTO.getId());
		}

		if (!StringUtils.isEmpty(dayIndicatorDTO.getLow())) {
			builder.setLow(dayIndicatorDTO.getLow());
		}

		if (!StringUtils.isEmpty(dayIndicatorDTO.getOpen())) {
			builder.setOpen(dayIndicatorDTO.getOpen());
		}

		if (!StringUtils.isEmpty(dayIndicatorDTO.getPreclose())) {
			builder.setPreclose(dayIndicatorDTO.getPreclose());
		}

		if (!StringUtils.isEmpty(dayIndicatorDTO.getQuerydate())) {
			builder.setQuerydate(dayIndicatorDTO.getQuerydate());
		}

		if (!StringUtils.isEmpty(dayIndicatorDTO.getTurn())) {
			builder.setTurn(dayIndicatorDTO.getTurn());
		}

		if (!StringUtils.isEmpty(dayIndicatorDTO.getUpdate())) {
			builder.setUpdate(dayIndicatorDTO.getUpdate());
		}

		if (!StringUtils.isEmpty(dayIndicatorDTO.getVolume()))

		{
			builder.setVolume(dayIndicatorDTO.getVolume());
		}

	}


}
