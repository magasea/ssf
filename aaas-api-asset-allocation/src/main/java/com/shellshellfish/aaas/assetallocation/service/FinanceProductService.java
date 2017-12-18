package com.shellshellfish.aaas.assetallocation.service;


import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductDetailInfoPage;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductDetailQueryInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import java.util.List;

/**
 *  给别的模块提供grpc调用接口
 */

public interface FinanceProductService {

  /**
   *
   * @param productBaseInfo 1. prod_id; 2. group_id;
   * @return List<ProductMakeUpInfo>
   *   list of (1. prod_id; 2. group_id; 3.prod_name; 4. fund_code; 5. fund_quantity;)
   */
  List<ProductMakeUpInfo> getProductInfo(ProductBaseInfo productBaseInfo);

  /**
   *
   * @param productDetailQueryInfo 1. prod_id; 2. group_id;
   * @return List<ProductMakeUpInfo>
   *   list of (1. prod_id; 2. group_id; 3.prod_name; 4. fund_code; 5. fund_quantity;)
   */
  ProductDetailInfoPage getProductDetailInfo(ProductDetailQueryInfo productDetailQueryInfo);
}
