package com.shellshellfish.aaas.assetallocation.util;

import com.mathworks.toolbox.javabuilder.MWException;
import com.yihui.MATLAB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/28
 * Desc:计算最大回撤（使用 MATLAB）
 */
public class CalculateMaxdrawdowns {

    private static final Logger logger = LoggerFactory.getLogger(CalculateMaxdrawdowns.class);

    private static final DecimalFormat decimalFormat = new DecimalFormat(".00000"); //保留 5 位

    /*
     * @Desc:计算最大回撤率
     * @Param:
     *        netValueArr:净值数据
     */
    @Deprecated
    public static Double calculateMaxdrawdown(double[] netValueArr) {
        Object[] result = null;
        Double maxdrawdownValue = null;
        try {
            MATLAB matLab = new MATLAB();
            result = matLab.calculateMaxdrawdown(1, netValueArr);
            matLab.dispose();
            if (result != null && result[0] != null) {
                maxdrawdownValue = Double.parseDouble(result[0].toString());
            }
        } catch (MWException e) {
            logger.error("Failed to calculateMaxdrawdown!");
            logger.error("exception:", e);
        }
        return maxdrawdownValue;
    }

    /**
     * @author yongquan.xiong
     * @Description: 计算最大回撤
     * @date: 2018年1月22日
     * @Param: data(list) 收益价格序列 （如 基金的复权单位净值 序列）
     */
    public static Double calculateMaxdrawdown(List<Double> data) {
        int length = data.size();
        List<Double> maxdraw = new ArrayList<Double>();
        if (length > 1) {
            for (int i = length - 1; i > 0; i--) {
                if (i != 0) {
                    if (data.get(i) <= 0) {
                        logger.info("Data cannot have values <= 0");
                        return null;
                    }
                    Double maxValue = Collections.max(data.subList(0, i));
                    Double temp = data.get(i) / maxValue - 1;
                    maxdraw.add(temp);
                }
            }
        } else {
            logger.info("The length of data must > 1");
            return null;
        }
        maxdraw.add(0.0);   //当数据都为非负数 时 添加 默认值 0.0

        return Double.parseDouble(decimalFormat.format(Collections.min(maxdraw)));   //保留 5 位
    }

    public static void main(String[] args) {
        String a = "123";
        List list = new ArrayList();
        list.add(a);
        list.add(list.get(0));
        System.out.println(list.size());
        System.out.println(list.get(0));
        System.out.println(list.get(1));
    }
}
