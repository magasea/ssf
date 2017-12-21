package com.shellshellfish.aaas.assetallocation.neo.util;

import com.mathworks.toolbox.javabuilder.MWException;

import com.yihui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Author: yongquan.xiong
 * Date: 2017/11/28
 * Desc:计算最大回撤（使用 MATLAB）
 */
public class CalculateMaxdrawdowns {

    private static final Logger logger= LoggerFactory.getLogger(CalculateMaxdrawdowns.class);

    /*
     * 计算最大回撤率
     */
    public Double calculateMaxdrawdown( double[] temp){
        Object[] result=null;
        Double maxdrawdownValue=null;
        try {
            MATLAB matLab= new MATLAB();
            result=matLab.calculateMaxdrawdown(1,temp);

            if(result!=null && result[0]!=null){

                maxdrawdownValue=Double.parseDouble(result[0].toString());

            }

        } catch (MWException e) {
            logger.error("Failed to calculateMaxdrawdown!");
            e.printStackTrace();
        }

        return maxdrawdownValue;
    }

}
