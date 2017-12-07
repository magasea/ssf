package com.shellshellfish.aaas.assetallocation.tools;


import com.mathworks.toolbox.javabuilder.pooling.Poolable;
import java.rmi.RemoteException;

public interface MatLabRemote extends Poolable {
    Object[] calculateMaxdrawdown(int var1, Object... var2) throws RemoteException;

    Object[] calculatePortvrisk(int var1, Object... var2) throws RemoteException;

    Object[] calculatePriceToYield(int var1, Object... var2) throws RemoteException;

    Object[] calculateYieldToPrice(int var1, Object... var2) throws RemoteException;

    Object[] efficientFrontier(int var1, Object... var2) throws RemoteException;

    Object[] riskAndIncome(int var1, Object... var2) throws RemoteException;

    void dispose() throws RemoteException;
}
