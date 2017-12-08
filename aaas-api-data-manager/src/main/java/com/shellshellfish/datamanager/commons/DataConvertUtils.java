package com.shellshellfish.datamanager.commons;

import java.util.ArrayList;
import java.util.List;

import com.shellshellfish.datamanager.model.DailyFunds;
import com.shellshellfish.datamanager.model.IndicatorPoint;

public class DataConvertUtils {
   public static List<IndicatorPoint> getIndFromDailyData(List<DailyFunds>  dailylst){
	   int len=dailylst.size();
	   List<IndicatorPoint> indptlst=new ArrayList<IndicatorPoint>();  
	   for (int i=0;i<len;i++) {
		   IndicatorPoint indpt=new IndicatorPoint();
		   indpt.setPtdate(dailylst.get(i).getQuerydate());
		   indpt.setUnitvalue(dailylst.get(i).getNavadj());
		   indptlst.add(indpt);
	   }
	   
	   return indptlst;
   }
}
