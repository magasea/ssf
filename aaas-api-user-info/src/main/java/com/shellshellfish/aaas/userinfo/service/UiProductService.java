package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.userinfo.model.dto.UiProductDetailDTO;

import java.util.List;

/**
 * @Author pierre
 * 17-12-28
 */
public interface UiProductService {

	List<UiProductDetailDTO> getProductDetailsByProdId(Long prodId);

	List<Long> getProdIds();

}
