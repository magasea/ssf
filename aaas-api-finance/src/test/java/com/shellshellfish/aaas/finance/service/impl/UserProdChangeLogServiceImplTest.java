package com.shellshellfish.aaas.finance.service.impl;

import com.shellshellfish.aaas.common.enums.AdjustTypeEnum;
import com.shellshellfish.aaas.common.enums.FundClassEnum;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.FinanceApp;
import com.shellshellfish.aaas.finance.model.dto.UserProdChg;
import com.shellshellfish.aaas.finance.model.dto.UserProdChgDetail;
import com.shellshellfish.aaas.finance.service.UserProdChangeLogService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by chenwei on 2018- 三月 - 15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles="dev")
public class UserProdChangeLogServiceImplTest {

  @Autowired
  UserProdChangeLogService userProdChangeLogService;

  @Test
  public void getGeneralChangeLogs() throws Exception {
  }

  @Test
  public void getDetailChangeLogs() throws Exception {
  }

  @Test
  public void insertGeneralChangeLogs() throws Exception {


    List<UserProdChg> userProdChgs = new ArrayList<>();
    for(int idx = 1; idx <= 15; idx ++){
      UserProdChg userProdChg = new UserProdChg();
      userProdChg.setProdId(new Long(idx));
      userProdChg.setCreateTime(TradeUtil.getUTCTimeOfSpecificTime(2017,8,8,0,0));
      String groupId = String.format("%d0048",idx);
      userProdChg.setGroupId(Integer.parseInt(groupId));
      duplicateChgs(userProdChg, userProdChgs);
    }
    userProdChangeLogService.insertGeneralChangeLogs(userProdChgs);
  }

  private void duplicateChgs(UserProdChg userProdChg, List<UserProdChg> userProdChgs) {
    List<UserProdChgDetail> userProdChgDetails = new ArrayList<>();
    //1. change
    UserProdChg userProdChg1 = new UserProdChg();
    MyBeanUtils.mapEntityIntoDTO(userProdChg, userProdChg1);
    userProdChg1.setModifyType(AdjustTypeEnum.ESTABLISH.getTypeId());
    userProdChg1.setModifyComment("组合建立买入持仓");
    userProdChg1.setModifyTypeComment(AdjustTypeEnum.ESTABLISH.getTypeComment());
    userProdChg1.setModifySeq(1);
    userProdChg1.setModifyTime(TradeUtil.getUTCTimeOfSpecificTime(2017,8,8,0,0));
    userProdChgs.add(userProdChg1);


    UserProdChgDetail userProdChgDetail1 = new UserProdChgDetail();
    userProdChgDetail1.setCode("000217.OF");
    userProdChgDetail1.setFundName("华安黄金易ETF联接C");
    userProdChgDetail1.setFundType(FundClassEnum.COMMERSIAL.getFundClassId());
    userProdChgDetail1.setFundTypeName(FundClassEnum.COMMERSIAL.getFundClassComment());
    userProdChgDetail1.setModifySeq(1);
    userProdChgDetail1.setPercentAfter(3320L);
    userProdChgDetail1.setPercentBefore(0L);
    userProdChgDetail1.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail1);

    UserProdChgDetail userProdChgDetail2 = new UserProdChgDetail();
    userProdChgDetail2.setCode("000614.OF");
    userProdChgDetail2.setFundName("华安德国30(DAX)ETF联接(QDII)");
    userProdChgDetail2.setFundTypeName(FundClassEnum.QDII.getFundClassComment());
    userProdChgDetail2.setFundType(FundClassEnum.QDII.getFundClassId());
    userProdChgDetail2.setModifySeq(1);
    userProdChgDetail2.setPercentAfter(2750L);
    userProdChgDetail2.setPercentBefore(0L);
    userProdChgDetail2.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail2);

    UserProdChgDetail userProdChgDetail3 = new UserProdChgDetail();
    userProdChgDetail3.setCode("000696.OF");
    userProdChgDetail3.setFundName("汇添富环保行业股票");
    userProdChgDetail3.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail3.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail3.setModifySeq(1);
    userProdChgDetail3.setPercentAfter(900L);
    userProdChgDetail3.setPercentBefore(0L);
    userProdChgDetail3.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail3);

    UserProdChgDetail userProdChgDetail4 = new UserProdChgDetail();
    userProdChgDetail4.setCode("001541.OF");
    userProdChgDetail4.setFundName("汇添富民营新动力股票");
    userProdChgDetail4.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail4.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail4.setModifySeq(1);
    userProdChgDetail4.setPercentAfter(930L);
    userProdChgDetail4.setPercentBefore(0L);
    userProdChgDetail4.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail4);


    UserProdChgDetail userProdChgDetail5 = new UserProdChgDetail();
    userProdChgDetail5.setCode("470007.OF");
    userProdChgDetail5.setFundName("汇添富上证综合指数");
    userProdChgDetail5.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail5.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail5.setModifySeq(1);
    userProdChgDetail5.setPercentAfter(400L);
    userProdChgDetail5.setPercentBefore(0L);
    userProdChgDetail5.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail5);

    UserProdChgDetail userProdChgDetail6 = new UserProdChgDetail();
    userProdChgDetail6.setCode("000248.OF");
    userProdChgDetail6.setFundName("汇添富中证主要消费ETF联接");
    userProdChgDetail6.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail6.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail6.setModifySeq(1);
    userProdChgDetail6.setPercentAfter(400L);
    userProdChgDetail6.setPercentBefore(0L);
    userProdChgDetail6.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail6);
    
    UserProdChgDetail userProdChgDetail7 = new UserProdChgDetail();
    userProdChgDetail7.setCode("000248.OF");
    userProdChgDetail7.setFundName("汇添富中证主要消费ETF联接");
    userProdChgDetail7.setFundType(FundClassEnum.ENHANCEIDX.getFundClassId());
    userProdChgDetail7.setFundTypeName(FundClassEnum.ENHANCEIDX.getFundClassComment());
    userProdChgDetail7.setModifySeq(1);
    userProdChgDetail7.setPercentAfter(800L);
    userProdChgDetail7.setPercentBefore(0L);
    userProdChgDetail7.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail7);

    UserProdChgDetail userProdChgDetail8 = new UserProdChgDetail();
    userProdChgDetail8.setCode("400013.OF");
    userProdChgDetail8.setFundName("东方成长收益灵活配置混合");
    userProdChgDetail8.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail8.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail8.setModifySeq(1);
    userProdChgDetail8.setPercentAfter(250L);
    userProdChgDetail8.setPercentBefore(0L);
    userProdChgDetail8.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail8);

    UserProdChgDetail userProdChgDetail9 = new UserProdChgDetail();
    userProdChgDetail9.setCode("001694.OF");
    userProdChgDetail9.setFundName("华安沪港深外延增长灵活配置混合");
    userProdChgDetail9.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail9.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail9.setModifySeq(1);
    userProdChgDetail9.setPercentAfter(250L);
    userProdChgDetail9.setPercentBefore(0L);
    userProdChgDetail9.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail9);

    //2. change
    UserProdChg userProdChg2 = new UserProdChg();
    MyBeanUtils.mapEntityIntoDTO(userProdChg, userProdChg2);
    userProdChg2.setModifyType(AdjustTypeEnum.INCREASE_DEBET.getTypeId());
    userProdChg2.setModifyComment("依据近期市场走势，调高偏债类资产配置");
    userProdChg2.setModifyTypeComment(AdjustTypeEnum.INCREASE_DEBET.getTypeComment());
    userProdChg2.setModifySeq(2);
    userProdChg2.setModifyTime(TradeUtil.getUTCTimeOfSpecificTime(2017,8,31,0,0));
    userProdChgs.add(userProdChg2);

    UserProdChgDetail userProdChgDetail21 = new UserProdChgDetail();
    userProdChgDetail21.setCode("000217.OF");
    userProdChgDetail21.setFundName("华安黄金易ETF联接C");
    userProdChgDetail21.setFundType(FundClassEnum.COMMERSIAL.getFundClassId());
    userProdChgDetail21.setFundTypeName(FundClassEnum.COMMERSIAL.getFundClassComment());
    userProdChgDetail21.setModifySeq(2);
    userProdChgDetail21.setPercentAfter(3320L);
    userProdChgDetail21.setPercentBefore(0L);
    userProdChgDetail21.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail21);

    UserProdChgDetail userProdChgDetail22 = new UserProdChgDetail();
    userProdChgDetail22.setCode("000614.OF");
    userProdChgDetail22.setFundName("华安德国30(DAX)ETF联接(QDII)");
    userProdChgDetail22.setFundTypeName(FundClassEnum.QDII.getFundClassComment());
    userProdChgDetail22.setFundType(FundClassEnum.QDII.getFundClassId());
    userProdChgDetail22.setModifySeq(2);
    userProdChgDetail22.setPercentAfter(2750L);
    userProdChgDetail22.setPercentBefore(0L);
    userProdChgDetail22.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail22);

    UserProdChgDetail userProdChgDetail23 = new UserProdChgDetail();
    userProdChgDetail23.setCode("000696.OF");
    userProdChgDetail23.setFundName("汇添富环保行业股票");
    userProdChgDetail23.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail23.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail23.setModifySeq(2);
    userProdChgDetail23.setPercentAfter(900L);
    userProdChgDetail23.setPercentBefore(0L);
    userProdChgDetail23.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail23);

    UserProdChgDetail userProdChgDetail24 = new UserProdChgDetail();
    userProdChgDetail24.setCode("001541.OF");
    userProdChgDetail24.setFundName("汇添富民营新动力股票");
    userProdChgDetail24.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail24.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail24.setModifySeq(2);
    userProdChgDetail24.setPercentAfter(930L);
    userProdChgDetail24.setPercentBefore(0L);
    userProdChgDetail24.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail24);


    UserProdChgDetail userProdChgDetail25 = new UserProdChgDetail();
    userProdChgDetail25.setCode("470007.OF");
    userProdChgDetail25.setFundName("汇添富上证综合指数");
    userProdChgDetail25.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail25.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail25.setModifySeq(2);
    userProdChgDetail25.setPercentAfter(400L);
    userProdChgDetail25.setPercentBefore(0L);
    userProdChgDetail25.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail25);

    UserProdChgDetail userProdChgDetail26 = new UserProdChgDetail();
    userProdChgDetail26.setCode("000248.OF");
    userProdChgDetail26.setFundName("汇添富中证主要消费ETF联接");
    userProdChgDetail26.setFundType(FundClassEnum.ENHANCEIDX.getFundClassId());
    userProdChgDetail26.setFundTypeName(FundClassEnum.ENHANCEIDX.getFundClassComment());
    userProdChgDetail26.setModifySeq(2);
    userProdChgDetail26.setPercentAfter(0L);
    userProdChgDetail26.setPercentBefore(800L);
    userProdChgDetail26.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail26);

    UserProdChgDetail userProdChgDetail27 = new UserProdChgDetail();
    userProdChgDetail27.setCode("400013.OF");
    userProdChgDetail27.setFundName("东方成长收益灵活配置混合");
    userProdChgDetail27.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail27.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail27.setModifySeq(2);
    userProdChgDetail27.setPercentAfter(250L);
    userProdChgDetail27.setPercentBefore(0L);
    userProdChgDetail27.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail27);

    UserProdChgDetail userProdChgDetail28 = new UserProdChgDetail();
    userProdChgDetail28.setCode("001694.OF");
    userProdChgDetail28.setFundName("华安沪港深外延增长灵活配置混合");
    userProdChgDetail28.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail28.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail28.setModifySeq(2);
    userProdChgDetail28.setPercentAfter(250L);
    userProdChgDetail28.setPercentBefore(0L);
    userProdChgDetail28.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail28);

    UserProdChgDetail userProdChgDetail20 = new UserProdChgDetail();
    userProdChgDetail20.setCode("000406.OF");
    userProdChgDetail20.setFundName("汇添富双利增强债券A");
    userProdChgDetail20.setFundType(FundClassEnum.COMB2.getFundClassId());
    userProdChgDetail20.setFundTypeName(FundClassEnum.COMB2.getFundClassComment());
    userProdChgDetail20.setModifySeq(2);
    userProdChgDetail20.setPercentAfter(850L);
    userProdChgDetail20.setPercentBefore(0L);
    userProdChgDetail20.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail20);
    
    UserProdChgDetail userProdChgDetail29 = new UserProdChgDetail();
    userProdChgDetail29.setCode("400016.OF");
    userProdChgDetail29.setFundName("东方强化收益债券");
    userProdChgDetail29.setFundType(FundClassEnum.COMB2.getFundClassId());
    userProdChgDetail29.setFundTypeName(FundClassEnum.COMB2.getFundClassComment());
    userProdChgDetail29.setModifySeq(2);
    userProdChgDetail29.setPercentAfter(750L);
    userProdChgDetail29.setPercentBefore(0L);
    userProdChgDetail29.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail29);

    UserProdChgDetail userProdChgDetail31 = new UserProdChgDetail();
    userProdChgDetail31.setCode("400020.OF");
    userProdChgDetail31.setFundName("东方安心收益保本");
    userProdChgDetail31.setFundType(FundClassEnum.PROTECTBASE.getFundClassId());
    userProdChgDetail31.setFundTypeName(FundClassEnum.PROTECTBASE.getFundClassComment());
    userProdChgDetail31.setModifySeq(2);
    userProdChgDetail31.setPercentAfter(800L);
    userProdChgDetail31.setPercentBefore(0L);
    userProdChgDetail31.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail31);

    UserProdChgDetail userProdChgDetail32 = new UserProdChgDetail();
    userProdChgDetail32.setCode("400009.OF");
    userProdChgDetail32.setFundName("东方稳健回报债券");
    userProdChgDetail32.setFundType(FundClassEnum.COMB1.getFundClassId());
    userProdChgDetail32.setFundTypeName(FundClassEnum.COMB1.getFundClassComment());
    userProdChgDetail32.setModifySeq(2);
    userProdChgDetail32.setPercentAfter(800L);
    userProdChgDetail32.setPercentBefore(0L);
    userProdChgDetail32.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail32);

    UserProdChgDetail userProdChgDetail33 = new UserProdChgDetail();
    userProdChgDetail33.setCode("000248.OF");
    userProdChgDetail33.setFundName("汇添富中证主要消费ETF联接");
    userProdChgDetail33.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail33.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail33.setModifySeq(2);
    userProdChgDetail33.setPercentAfter(300L);
    userProdChgDetail33.setPercentBefore(400L);
    userProdChgDetail33.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail33);

    UserProdChgDetail userProdChgDetail34 = new UserProdChgDetail();
    userProdChgDetail34.setCode("000366.OF");
    userProdChgDetail34.setFundName("汇添富添富通货币A");
    userProdChgDetail34.setFundType(FundClassEnum.MONEY.getFundClassId());
    userProdChgDetail34.setFundTypeName(FundClassEnum.MONEY.getFundClassComment());
    userProdChgDetail34.setModifySeq(2);
    userProdChgDetail34.setPercentAfter(340L);
    userProdChgDetail34.setPercentBefore(0L);
    userProdChgDetail34.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail34);

    //3. change
    UserProdChg userProdChg3 = new UserProdChg();
    MyBeanUtils.mapEntityIntoDTO(userProdChg, userProdChg3);
    userProdChg3.setModifyType(AdjustTypeEnum.REBALANCE.getTypeId());
    userProdChg3.setModifyComment("提高权益类资产比例");
    userProdChg3.setModifyTypeComment(AdjustTypeEnum.REBALANCE.getTypeComment());
    userProdChg3.setModifySeq(3);
    userProdChg3.setModifyTime(TradeUtil.getUTCTimeOfSpecificTime(2017,9,18,0,0));
    userProdChgs.add(userProdChg3);
    //4. change
    UserProdChg userProdChg4 = new UserProdChg();
    MyBeanUtils.mapEntityIntoDTO(userProdChg, userProdChg4);
    userProdChg4.setModifyType(AdjustTypeEnum.REBALANCE.getTypeId());
    userProdChg4.setModifyComment("适当增加偏债类资产平衡组合风险");
    userProdChg4.setModifyTypeComment(AdjustTypeEnum.REBALANCE.getTypeComment());
    userProdChg4.setModifySeq(4);
    userProdChg4.setModifyTime(TradeUtil.getUTCTimeOfSpecificTime(2017,10,2,0,0));
    userProdChgs.add(userProdChg4);
    //5. change
    UserProdChg userProdChg5 = new UserProdChg();
    MyBeanUtils.mapEntityIntoDTO(userProdChg, userProdChg5);
    userProdChg5.setModifyType(AdjustTypeEnum.REBALANCE.getTypeId());
    userProdChg5.setModifyComment("减少货币债券等配置，调高A股及海外资产比例");
    userProdChg5.setModifyTypeComment(AdjustTypeEnum.REBALANCE.getTypeComment());
    userProdChg5.setModifySeq(5);
    userProdChg5.setModifyTime(TradeUtil.getUTCTimeOfSpecificTime(2017,11,22,0,0));
    userProdChgs.add(userProdChg5);
    //6. change
    UserProdChg userProdChg6 = new UserProdChg();
    MyBeanUtils.mapEntityIntoDTO(userProdChg, userProdChg6);
    userProdChg6.setModifyType(AdjustTypeEnum.RISKSTART_USA.getTypeId());
    userProdChg6.setModifyComment("系统预期美股将有较大波动，开始调整风险比例");
    userProdChg6.setModifyTypeComment(AdjustTypeEnum.RISKSTART_USA.getTypeComment());
    userProdChg6.setModifySeq(6);
    userProdChg6.setModifyTime(TradeUtil.getUTCTimeOfSpecificTime(2018,1,30,0,0));
    userProdChgs.add(userProdChg6);
    //7. change
    UserProdChg userProdChg7 = new UserProdChg();
    MyBeanUtils.mapEntityIntoDTO(userProdChg, userProdChg7);
    userProdChg7.setModifyType(AdjustTypeEnum.RISKCONTINUE_USA.getTypeId());
    userProdChg7.setModifyComment("美股短期持续，继续调低相关持仓");
    userProdChg7.setModifyTypeComment(AdjustTypeEnum.RISKCONTINUE_USA.getTypeComment());
    userProdChg7.setModifySeq(7);
    userProdChg7.setModifyTime(TradeUtil.getUTCTimeOfSpecificTime(2018,2,5,0,0));
    userProdChgs.add(userProdChg7);
    //8. change
    UserProdChg userProdChg8 = new UserProdChg();
    MyBeanUtils.mapEntityIntoDTO(userProdChg, userProdChg8);
    userProdChg8.setModifyType(AdjustTypeEnum.RISKSTOK.getTypeId());
    userProdChg8.setModifyComment("美股短期风险仍然较大，大幅调低股票类权益类持仓");
    userProdChg8.setModifyTypeComment(AdjustTypeEnum.RISKSTOK.getTypeComment());
    userProdChg8.setModifySeq(8);
    userProdChg8.setModifyTime(TradeUtil.getUTCTimeOfSpecificTime(2018,2,8,0,0));
    userProdChgs.add(userProdChg8);
    //9. change
    UserProdChg userProdChg9 = new UserProdChg();
    MyBeanUtils.mapEntityIntoDTO(userProdChg, userProdChg9);
    userProdChg9.setModifyType(AdjustTypeEnum.RISKDISMISS_END.getTypeId());
    userProdChg9.setModifyComment("美股波动减弱，本轮风控结束，恢复合适的风险配置比例");
    userProdChg9.setModifyTypeComment(AdjustTypeEnum.RISKDISMISS_END.getTypeComment());
    userProdChg9.setModifySeq(9);
    userProdChg9.setModifyTime(TradeUtil.getUTCTimeOfSpecificTime(2018,3,1,0,0));
    userProdChgs.add(userProdChg9);

    userProdChangeLogService.insertDetailChangeLogs(userProdChgDetails);

  }


  @Test
  public void insertDetailChangeLogs() throws Exception {




  }



}
