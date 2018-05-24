package com.shellshellfish.aaas.userinfo.service;

/**
 * @Author pierre.chen
 * @Date 18-5-16
 * 校验ui_product_detail 中用户持有的基金份额和状态是否正确
 */
public interface CheckUiProductDetailService {

    /**
     * 检查ui_prod_detail 中的基金份额和状态是否正确
     * 并更正
     */
    void check();

}
