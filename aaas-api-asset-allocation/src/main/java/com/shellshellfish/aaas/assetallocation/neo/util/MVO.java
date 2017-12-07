package com.shellshellfish.aaas.assetallocation.neo.util;

import com.mathworks.toolbox.javabuilder.MWNumericArray;
import com.shellshellfish.aaas.assetallocation.tools.MatLab;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyinuo on 2017/11/22.
 */
public class MVO {

    /**
     *
     * @param ExpReturn 期望收益率
     * @param ExpCovariance 协方差矩阵
     * @param count 有效前沿线生成的点数
     * @return
     */
    public static List<float [][]> efficientFrontier(Double [] ExpReturn, Double[][] ExpCovariance, int count){
        Object[] resust = null;
        List<float [][]> list = new ArrayList<>();
        try{
            MatLab ml = new MatLab();
            resust = ml.efficientFrontier(3,ExpReturn,ExpCovariance,count);
            MWNumericArray temp = (MWNumericArray)resust[0];
            float [][] weights=(float[][])temp.toFloatArray();
            MWNumericArray temp1 = (MWNumericArray)resust[1];
            float [][] weights1=(float[][])temp1.toFloatArray();
            MWNumericArray temp2 = (MWNumericArray)resust[2];
            float [][] weights2=(float[][])temp2.toFloatArray();
            list.add(weights);
            list.add(weights1);
            list.add(weights2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     *
     * @param ExpReturn 期望收益率
     * @param ExpCovariance 协方差矩阵
     * @param PortWts 权重
     * @return
     */
    public static Object[] incomeAndRisk(Double [] ExpReturn,Double[][] ExpCovariance,Double [] PortWts){
        Object[] resust = null;
        try{
            MatLab ml = new MatLab();
            resust = ml.riskAndIncome(2,ExpReturn,ExpCovariance,PortWts);
            MWNumericArray temp = (MWNumericArray)resust[0];
            float [][] weights=(float[][])temp.toFloatArray();
            MWNumericArray temp1 = (MWNumericArray)resust[1];
            float [][] weights1=(float[][])temp1.toFloatArray();
            resust[0]=weights;
            resust[1]=weights1;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return resust;
    }
}
