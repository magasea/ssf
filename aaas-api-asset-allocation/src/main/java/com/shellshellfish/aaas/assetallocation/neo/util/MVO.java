package com.shellshellfish.aaas.assetallocation.neo.util;

import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import com.yihui.MATLAB;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyinuo on 2017/11/22.
 */
public class MVO {

    /**
     *计算有效前沿线值
     * @param ExpReturn 期望收益率
     * @param ExpCovariance 协方差矩阵
     * @param count 有效前沿线生成的点数
     * @return
     */
    public static List<float [][]> efficientFrontier(Double [] ExpReturn, Double[][] ExpCovariance, int count,Double lb,Double ub){
        Object[] resust = null;
        List<float [][]> list = new ArrayList<>();
        try{

            double[] lowBound=new double[ExpReturn.length];
            double[] upBound=new double[ExpReturn.length];
            for(int i=0;i<lowBound.length;i++){
                lowBound[i]=lb;
                upBound[i]=ub;
            }

            MATLAB ml = new MATLAB();
            resust = ml.efficientFrontier(3,ExpReturn,ExpCovariance,count,lowBound,upBound);
            MWNumericArray temp = (MWNumericArray)resust[0];
            float [][] weights=(float[][])temp.toFloatArray();
            MWNumericArray temp1 = (MWNumericArray)resust[1];
            float [][] weights1=(float[][])temp1.toFloatArray();
            MWNumericArray temp2 = (MWNumericArray)resust[2];
            float [][] weights2=(float[][])temp2.toFloatArray();
            list.add(weights);
            list.add(weights1);
            list.add(weights2);
            ml.dispose();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     *已知组合收益率，协方差，权重计算组合预期收益率，预期风险率
     * @param ExpReturn 期望收益率
     * @param ExpCovariance 协方差矩阵
     * @param PortWts 权重
     * @return
     */
    public static Object[] incomeAndRisk(Double [] ExpReturn,Double[][] ExpCovariance,Double [] PortWts){
        Object[] resust = null;
        try{
            MATLAB ml = new MATLAB();
            resust = ml.riskAndIncome(2,ExpReturn,ExpCovariance,PortWts);
            MWNumericArray temp = (MWNumericArray)resust[0];
            float [][] weights=(float[][])temp.toFloatArray();
            MWNumericArray temp1 = (MWNumericArray)resust[1];
            float [][] weights1=(float[][])temp1.toFloatArray();
            resust[0]=weights;
            resust[1]=weights1;
            ml.dispose();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return resust;
    }

    /**
     * 计算夏普比率
     * @param asset 每天基金复权单位净值*权重相加
     * @param cash 0.0013
     * @return
     */
    public static Object sharpeRatio(Double[] asset ,Double cash){
        Object result = null;
        try {
            MATLAB ml = new MATLAB();
            result = ml.sharpeRatio(1,asset,cash)[0];
            ml.dispose();
        } catch (MWException e) {
            e.printStackTrace();
        }
        return result;
    }
}
