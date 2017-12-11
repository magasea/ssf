package com.shellshellfish.aaas.assetallocation.tools;


import com.mathworks.toolbox.javabuilder.Disposable;
import com.mathworks.toolbox.javabuilder.MWComponentOptions;
import com.mathworks.toolbox.javabuilder.MWCtfDirectorySource;
import com.mathworks.toolbox.javabuilder.MWCtfExtractLocation;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.internal.MWComponentInstance;
import com.mathworks.toolbox.javabuilder.internal.MWFunctionSignature;
import com.mathworks.toolbox.javabuilder.internal.MWMCR;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MatLab extends MWComponentInstance<MatLab> {
    private static final Set<Disposable> sInstances = new HashSet();
    private static final MWFunctionSignature sCalculateMaxdrawdownSignature = new MWFunctionSignature(1, false, "calculateMaxdrawdown", 1, false);
    private static final MWFunctionSignature sCalculatePortvriskSignature = new MWFunctionSignature(1, false, "calculatePortvrisk", 4, false);
    private static final MWFunctionSignature sCalculatePriceToYieldSignature = new MWFunctionSignature(2, false, "calculatePriceToYield", 3, false);
    private static final MWFunctionSignature sCalculateYieldToPriceSignature = new MWFunctionSignature(2, false, "calculateYieldToPrice", 5, false);
    private static final MWFunctionSignature sEfficientFrontierSignature = new MWFunctionSignature(3, false, "efficientFrontier", 3, false);
    private static final MWFunctionSignature sRiskAndIncomeSignature = new MWFunctionSignature(2, false, "riskAndIncome", 3, false);

    private MatLab(MWMCR var1) throws MWException {
        super(var1);
        Class var2 = MatLab.class;
        synchronized(MatLab.class) {
            sInstances.add(this);
        }
    }

    public MatLab() throws MWException, IOException {
        this(YihuiMCRFactory.newInstance());
    }

    private static MWComponentOptions getPathToComponentOptions(String var0) {
        MWComponentOptions var1 = new MWComponentOptions(new Object[]{new MWCtfExtractLocation(var0), new MWCtfDirectorySource(var0)});
        return var1;
    }

    /** @deprecated */
    public MatLab(String var1) throws MWException, IOException {
        this(YihuiMCRFactory.newInstance(getPathToComponentOptions(var1)));
    }

    public MatLab(MWComponentOptions var1) throws MWException, IOException {
        this(YihuiMCRFactory.newInstance(var1));
    }

    public void dispose() {
        boolean var9 = false;

        try {
            var9 = true;
            super.dispose();
            var9 = false;
        } finally {
            if (var9) {
                Class var4 = MatLab.class;
                synchronized(MatLab.class) {
                    sInstances.remove(this);
                }
            }
        }

        Class var1 = MatLab.class;
        synchronized(MatLab.class) {
            sInstances.remove(this);
        }
    }

    public static void main(String[] var0) {
        try {
            MWMCR var1 = YihuiMCRFactory.newInstance();
            var1.runMain(sCalculateMaxdrawdownSignature, var0);
            var1.dispose();
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    public static void disposeAllInstances() {
        Class var0 = MatLab.class;
        synchronized(MatLab.class) {
            Iterator var1 = sInstances.iterator();

            while(var1.hasNext()) {
                Disposable var2 = (Disposable)var1.next();
                var2.dispose();
            }

            sInstances.clear();
        }
    }

    public void calculateMaxdrawdown(List var1, List var2) throws MWException {
        this.fMCR.invoke(var1, var2, sCalculateMaxdrawdownSignature);
    }

    public void calculateMaxdrawdown(Object[] var1, Object[] var2) throws MWException {
        this.fMCR.invoke(Arrays.asList(var1), Arrays.asList(var2), sCalculateMaxdrawdownSignature);
    }

    public Object[] calculateMaxdrawdown(int var1, Object... var2) throws MWException {
        Object[] var3 = new Object[var1];
        this.fMCR.invoke(Arrays.asList(var3), MWMCR.getRhsCompat(var2, sCalculateMaxdrawdownSignature), sCalculateMaxdrawdownSignature);
        return var3;
    }

    public void calculatePortvrisk(List var1, List var2) throws MWException {
        this.fMCR.invoke(var1, var2, sCalculatePortvriskSignature);
    }

    public void calculatePortvrisk(Object[] var1, Object[] var2) throws MWException {
        this.fMCR.invoke(Arrays.asList(var1), Arrays.asList(var2), sCalculatePortvriskSignature);
    }

    public Object[] calculatePortvrisk(int var1, Object... var2) throws MWException {
        Object[] var3 = new Object[var1];
        this.fMCR.invoke(Arrays.asList(var3), MWMCR.getRhsCompat(var2, sCalculatePortvriskSignature), sCalculatePortvriskSignature);
        return var3;
    }

    public void calculatePriceToYield(List var1, List var2) throws MWException {
        this.fMCR.invoke(var1, var2, sCalculatePriceToYieldSignature);
    }

    public void calculatePriceToYield(Object[] var1, Object[] var2) throws MWException {
        this.fMCR.invoke(Arrays.asList(var1), Arrays.asList(var2), sCalculatePriceToYieldSignature);
    }

    public Object[] calculatePriceToYield(int var1, Object... var2) throws MWException {
        Object[] var3 = new Object[var1];
        this.fMCR.invoke(Arrays.asList(var3), MWMCR.getRhsCompat(var2, sCalculatePriceToYieldSignature), sCalculatePriceToYieldSignature);
        return var3;
    }

    public void calculateYieldToPrice(List var1, List var2) throws MWException {
        this.fMCR.invoke(var1, var2, sCalculateYieldToPriceSignature);
    }

    public void calculateYieldToPrice(Object[] var1, Object[] var2) throws MWException {
        this.fMCR.invoke(Arrays.asList(var1), Arrays.asList(var2), sCalculateYieldToPriceSignature);
    }

    public Object[] calculateYieldToPrice(int var1, Object... var2) throws MWException {
        Object[] var3 = new Object[var1];
        this.fMCR.invoke(Arrays.asList(var3), MWMCR.getRhsCompat(var2, sCalculateYieldToPriceSignature), sCalculateYieldToPriceSignature);
        return var3;
    }

    public void efficientFrontier(List var1, List var2) throws MWException {
        this.fMCR.invoke(var1, var2, sEfficientFrontierSignature);
    }

    public void efficientFrontier(Object[] var1, Object[] var2) throws MWException {
        this.fMCR.invoke(Arrays.asList(var1), Arrays.asList(var2), sEfficientFrontierSignature);
    }

    public Object[] efficientFrontier(int var1, Object... var2) throws MWException {
        Object[] var3 = new Object[var1];
        this.fMCR.invoke(Arrays.asList(var3), MWMCR.getRhsCompat(var2, sEfficientFrontierSignature), sEfficientFrontierSignature);
        return var3;
    }

    public void riskAndIncome(List var1, List var2) throws MWException {
        this.fMCR.invoke(var1, var2, sRiskAndIncomeSignature);
    }

    public void riskAndIncome(Object[] var1, Object[] var2) throws MWException {
        this.fMCR.invoke(Arrays.asList(var1), Arrays.asList(var2), sRiskAndIncomeSignature);
    }

    public Object[] riskAndIncome(int var1, Object... var2) throws MWException {
        Object[] var3 = new Object[var1];
        this.fMCR.invoke(Arrays.asList(var3), MWMCR.getRhsCompat(var2, sRiskAndIncomeSignature), sRiskAndIncomeSignature);
        return var3;
    }
}

