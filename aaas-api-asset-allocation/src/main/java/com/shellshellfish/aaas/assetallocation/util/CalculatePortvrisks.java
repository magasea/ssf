package com.shellshellfish.aaas.assetallocation.util;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.junit.Assert;


/**
 * @Author: yongquan.xiong
 * @Updated: pierre.chen
 * @Date: 2018-04-20
 * @Description:计算组合最大可能损失
 */
public class CalculatePortvrisks {
    /**
     * 计算组合最大可能损失
     * portReturn   每个投资组合在该期间的预期收益
     * portRisk     每个项目组合的标准偏差
     * confidenceInterval  置信区间
     * portValue   资产组合的总价值
     */
    public static Double calculatePortvrisk(double portReturn, double portRisk, double confidenceInterval, double portValue) {
        NormalDistribution n = new NormalDistribution(portReturn, portRisk);
        double x = n.inverseCumulativeProbability(confidenceInterval);
        double probability = 2 * portReturn - x;
        return portValue * probability;
    }

    public static void main(String[] args) {

        double portReturn = 0.019814685739371906;
        double portRisk = 0.09329109964638341;
        double confidenceInterval = 0.97;
        double portValue = 10000;
        System.out.println(calculatePortvrisk(portReturn, portRisk, confidenceInterval, portValue));

    }
}
