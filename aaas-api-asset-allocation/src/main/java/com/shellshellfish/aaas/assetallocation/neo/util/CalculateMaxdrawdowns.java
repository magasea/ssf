package com.shellshellfish.aaas.assetallocation.neo.util;

import com.mathworks.toolbox.javabuilder.MWException;
import com.yihui.MATLAB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Author: yongquan.xiong
 * Date: 2017/11/28
 * Desc:计算最大回撤（使用 MATLAB）
 */
public class CalculateMaxdrawdowns {

    private static final Logger logger = LoggerFactory.getLogger(CalculateMaxdrawdowns.class);

    /*
     * @Desc:计算最大回撤率
     * @Param:
     *        netValueArr:净值数据
     */
    public static Double calculateMaxdrawdown(double[] netValueArr) {
        Object[] result = null;
        Double maxdrawdownValue = null;
        try {
            MATLAB matLab = new MATLAB();
            result = matLab.calculateMaxdrawdown(1,netValueArr);
            matLab.dispose();
            if (result != null && result[0] != null) {
                maxdrawdownValue = Double.parseDouble(result[0].toString());
            }
        } catch (MWException e) {
            logger.error("Failed to calculateMaxdrawdown!");
            e.printStackTrace();
        }
        return maxdrawdownValue;
    }
}
