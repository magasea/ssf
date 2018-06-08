package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dto.UiProductDetailDTO;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.service.UiProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author pierre
 * 17-12-28
 */
@Service
public class UiProductServiceImpl implements UiProductService {

    @Autowired
    UiProductDetailRepo uiProductDetailRepo;

    @Autowired
    UiProductRepo uiProductRepo;

    @Override
    public List<UiProductDetailDTO> getProductDetailsByProdId(Long prodId) {
        List<UiProductDetail> uiProductDetails = uiProductDetailRepo.findAllByUserProdId(prodId);

        List<UiProductDetailDTO> result = new ArrayList<>(uiProductDetails.size());
        for (int i = 0; i < uiProductDetails.size(); i++) {
            UiProductDetailDTO detailDTO = new UiProductDetailDTO();
            BeanUtils.copyProperties(uiProductDetails.get(i), detailDTO);
            result.add(detailDTO);
        }
        return result;
    }

    @Override
    public List<Long> getProdIds() {
        return uiProductRepo.getAllId();
    }
}
