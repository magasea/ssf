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
  
  private static final String C000217 = "华安黄金易ETF联接C";
  private static final String C000248 = "汇添富中证主要消费ETF联接";
  private static final String C000366 = "汇添富添富通货币A";
  private static final String C000406 = "汇添富双利增强债券A";
  private static final String C000614 = "华安德国30(DAX)ETF联接(QDIIEnum)";
  private static final String C000696 = "汇添富环保行业股票";
  private static final String C001541 = "汇添富民营新动力股票";
  private static final String C001694 = "华安沪港深外延增长灵活配置混合";
  private static final String C400009 = "东方稳健回报债券";
  private static final String C400013 = "东方成长收益灵活配置混合";
  private static final String C400016 = "东方强化收益债券";
  private static final String C400020 = "东方安心收益保本";
  private static final String C470007 = "汇添富上证综合指数";
  private static final String C470008 = "汇添富策略回报混合";

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
    userProdChgDetail1.setFundName(C000217);
    userProdChgDetail1.setFundType(FundClassEnum.COMMERSIAL.getFundClassId());
    userProdChgDetail1.setFundTypeName(FundClassEnum.COMMERSIAL.getFundClassComment());
    userProdChgDetail1.setModifySeq(1);
    userProdChgDetail1.setPercentAfter(3320L);
    userProdChgDetail1.setPercentBefore(0L);
    userProdChgDetail1.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail1);

    UserProdChgDetail userProdChgDetail2 = new UserProdChgDetail();
    userProdChgDetail2.setCode("000614.OF");
    userProdChgDetail2.setFundName(C000614);
    userProdChgDetail2.setFundTypeName(FundClassEnum.QDII.getFundClassComment());
    userProdChgDetail2.setFundType(FundClassEnum.QDII.getFundClassId());
    userProdChgDetail2.setModifySeq(1);
    userProdChgDetail2.setPercentAfter(2750L);
    userProdChgDetail2.setPercentBefore(0L);
    userProdChgDetail2.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail2);

    UserProdChgDetail userProdChgDetail3 = new UserProdChgDetail();
    userProdChgDetail3.setCode("000696.OF");
    userProdChgDetail3.setFundName(C000696);
    userProdChgDetail3.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail3.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail3.setModifySeq(1);
    userProdChgDetail3.setPercentAfter(900L);
    userProdChgDetail3.setPercentBefore(0L);
    userProdChgDetail3.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail3);

    UserProdChgDetail userProdChgDetail4 = new UserProdChgDetail();
    userProdChgDetail4.setCode("001541.OF");
    userProdChgDetail4.setFundName(C001541);
    userProdChgDetail4.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail4.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail4.setModifySeq(1);
    userProdChgDetail4.setPercentAfter(930L);
    userProdChgDetail4.setPercentBefore(0L);
    userProdChgDetail4.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail4);


    UserProdChgDetail userProdChgDetail5 = new UserProdChgDetail();
    userProdChgDetail5.setCode("470007.OF");
    userProdChgDetail5.setFundName(C470007);
    userProdChgDetail5.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail5.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail5.setModifySeq(1);
    userProdChgDetail5.setPercentAfter(400L);
    userProdChgDetail5.setPercentBefore(0L);
    userProdChgDetail5.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail5);

    UserProdChgDetail userProdChgDetail6 = new UserProdChgDetail();
    userProdChgDetail6.setCode("000248.OF");
    userProdChgDetail6.setFundName(C000248);
    userProdChgDetail6.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail6.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail6.setModifySeq(1);
    userProdChgDetail6.setPercentAfter(400L);
    userProdChgDetail6.setPercentBefore(0L);
    userProdChgDetail6.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail6);
    
    UserProdChgDetail userProdChgDetail7 = new UserProdChgDetail();
    userProdChgDetail7.setCode("000248.OF");
    userProdChgDetail7.setFundName(C000248);
    userProdChgDetail7.setFundType(FundClassEnum.ENHANCEIDX.getFundClassId());
    userProdChgDetail7.setFundTypeName(FundClassEnum.ENHANCEIDX.getFundClassComment());
    userProdChgDetail7.setModifySeq(1);
    userProdChgDetail7.setPercentAfter(800L);
    userProdChgDetail7.setPercentBefore(0L);
    userProdChgDetail7.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail7);

    UserProdChgDetail userProdChgDetail8 = new UserProdChgDetail();
    userProdChgDetail8.setCode("400013.OF");
    userProdChgDetail8.setFundName(C400013);
    userProdChgDetail8.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail8.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail8.setModifySeq(1);
    userProdChgDetail8.setPercentAfter(250L);
    userProdChgDetail8.setPercentBefore(0L);
    userProdChgDetail8.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail8);

    UserProdChgDetail userProdChgDetail9 = new UserProdChgDetail();
    userProdChgDetail9.setCode("001694.OF");
    userProdChgDetail9.setFundName(C001694);
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
    userProdChgDetail21.setFundName(C000217);
    userProdChgDetail21.setFundType(FundClassEnum.COMMERSIAL.getFundClassId());
    userProdChgDetail21.setFundTypeName(FundClassEnum.COMMERSIAL.getFundClassComment());
    userProdChgDetail21.setModifySeq(2);
    userProdChgDetail21.setPercentAfter(2160L);
    userProdChgDetail21.setPercentBefore(3320L);
    userProdChgDetail21.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail21);

    UserProdChgDetail userProdChgDetail22 = new UserProdChgDetail();
    userProdChgDetail22.setCode("000614.OF");
    userProdChgDetail22.setFundName(C000614);
    userProdChgDetail22.setFundTypeName(FundClassEnum.QDII.getFundClassComment());
    userProdChgDetail22.setFundType(FundClassEnum.QDII.getFundClassId());
    userProdChgDetail22.setModifySeq(2);
    userProdChgDetail22.setPercentAfter(2500L);
    userProdChgDetail22.setPercentBefore(2750L);
    userProdChgDetail22.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail22);

    UserProdChgDetail userProdChgDetail23 = new UserProdChgDetail();
    userProdChgDetail23.setCode("000696.OF");
    userProdChgDetail23.setFundName(C000696);
    userProdChgDetail23.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail23.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail23.setModifySeq(2);
    userProdChgDetail23.setPercentAfter(200L);
    userProdChgDetail23.setPercentBefore(900L);
    userProdChgDetail23.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail23);

    UserProdChgDetail userProdChgDetail24 = new UserProdChgDetail();
    userProdChgDetail24.setCode("001541.OF");
    userProdChgDetail24.setFundName(C001541);
    userProdChgDetail24.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail24.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail24.setModifySeq(2);
    userProdChgDetail24.setPercentAfter(600L);
    userProdChgDetail24.setPercentBefore(930L);
    userProdChgDetail24.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail24);


    UserProdChgDetail userProdChgDetail25 = new UserProdChgDetail();
    userProdChgDetail25.setCode("470007.OF");
    userProdChgDetail25.setFundName(C470007);
    userProdChgDetail25.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail25.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail25.setModifySeq(2);
    userProdChgDetail25.setPercentAfter(200L);
    userProdChgDetail25.setPercentBefore(400L);
    userProdChgDetail25.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail25);

    UserProdChgDetail userProdChgDetail26 = new UserProdChgDetail();
    userProdChgDetail26.setCode("000248.OF");
    userProdChgDetail26.setFundName(C000248);
    userProdChgDetail26.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail26.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail26.setModifySeq(2);
    userProdChgDetail26.setPercentAfter(300L);
    userProdChgDetail26.setPercentBefore(400L);
    userProdChgDetail26.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail26);

    UserProdChgDetail userProdChgDetail27 = new UserProdChgDetail();
    userProdChgDetail27.setCode("400013.OF");
    userProdChgDetail27.setFundName(C400013);
    userProdChgDetail27.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail27.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail27.setModifySeq(2);
    userProdChgDetail27.setPercentAfter(250L);
    userProdChgDetail27.setPercentBefore(250L);
    userProdChgDetail27.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail27);

    UserProdChgDetail userProdChgDetail28 = new UserProdChgDetail();
    userProdChgDetail28.setCode("001694.OF");
    userProdChgDetail28.setFundName(C001694);
    userProdChgDetail28.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail28.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail28.setModifySeq(2);
    userProdChgDetail28.setPercentAfter(250L);
    userProdChgDetail28.setPercentBefore(250L);
    userProdChgDetail28.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail28);

    UserProdChgDetail userProdChgDetail20 = new UserProdChgDetail();
    userProdChgDetail20.setCode("000406.OF");
    userProdChgDetail20.setFundName(C000406);
    userProdChgDetail20.setFundType(FundClassEnum.COMB2.getFundClassId());
    userProdChgDetail20.setFundTypeName(FundClassEnum.COMB2.getFundClassComment());
    userProdChgDetail20.setModifySeq(2);
    userProdChgDetail20.setPercentAfter(850L);
    userProdChgDetail20.setPercentBefore(0L);
    userProdChgDetail20.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail20);
    
    UserProdChgDetail userProdChgDetail29 = new UserProdChgDetail();
    userProdChgDetail29.setCode("400016.OF");
    userProdChgDetail29.setFundName(C400016);
    userProdChgDetail29.setFundType(FundClassEnum.COMB2.getFundClassId());
    userProdChgDetail29.setFundTypeName(FundClassEnum.COMB2.getFundClassComment());
    userProdChgDetail29.setModifySeq(2);
    userProdChgDetail29.setPercentAfter(750L);
    userProdChgDetail29.setPercentBefore(0L);
    userProdChgDetail29.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail29);

    UserProdChgDetail userProdChgDetail31 = new UserProdChgDetail();
    userProdChgDetail31.setCode("400020.OF");
    userProdChgDetail31.setFundName(C400020);
    userProdChgDetail31.setFundType(FundClassEnum.PROTECTBASE.getFundClassId());
    userProdChgDetail31.setFundTypeName(FundClassEnum.PROTECTBASE.getFundClassComment());
    userProdChgDetail31.setModifySeq(2);
    userProdChgDetail31.setPercentAfter(800L);
    userProdChgDetail31.setPercentBefore(0L);
    userProdChgDetail31.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail31);

    UserProdChgDetail userProdChgDetail32 = new UserProdChgDetail();
    userProdChgDetail32.setCode("400009.OF");
    userProdChgDetail32.setFundName(C400009);
    userProdChgDetail32.setFundType(FundClassEnum.COMB1.getFundClassId());
    userProdChgDetail32.setFundTypeName(FundClassEnum.COMB1.getFundClassComment());
    userProdChgDetail32.setModifySeq(2);
    userProdChgDetail32.setPercentAfter(800L);
    userProdChgDetail32.setPercentBefore(0L);
    userProdChgDetail32.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail32);

    UserProdChgDetail userProdChgDetail33 = new UserProdChgDetail();
    userProdChgDetail33.setCode("000248.OF");
    userProdChgDetail33.setFundName(C000248);
    userProdChgDetail33.setFundType(FundClassEnum.ENHANCEIDX.getFundClassId());
    userProdChgDetail33.setFundTypeName(FundClassEnum.ENHANCEIDX.getFundClassComment());
    userProdChgDetail33.setModifySeq(2);
    userProdChgDetail33.setPercentAfter(0L);
    userProdChgDetail33.setPercentBefore(800L);
    userProdChgDetail33.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail33);

    UserProdChgDetail userProdChgDetail34 = new UserProdChgDetail();
    userProdChgDetail34.setCode("000366.OF");
    userProdChgDetail34.setFundName(C000366);
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
    //3.1
    UserProdChgDetail userProdChgDetail003 = new UserProdChgDetail();
    userProdChgDetail003.setCode("000614.OF");
    userProdChgDetail003.setFundName(C000614);
    userProdChgDetail003.setFundType(FundClassEnum.QDII.getFundClassId());
    userProdChgDetail003.setFundTypeName(FundClassEnum.QDII.getFundClassComment());
    userProdChgDetail003.setModifySeq(3);
    userProdChgDetail003.setPercentAfter(3600L);
    userProdChgDetail003.setPercentBefore(2500L);
    userProdChgDetail003.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail003);
    
    //3.2
    userProdChgDetail003 = new UserProdChgDetail();
    userProdChgDetail003.setCode("000217.OF");
    userProdChgDetail003.setFundName(C000217);
    userProdChgDetail003.setFundType(FundClassEnum.COMMERSIAL.getFundClassId());
    userProdChgDetail003.setFundTypeName(FundClassEnum.COMMERSIAL.getFundClassComment());
    userProdChgDetail003.setModifySeq(3);
    userProdChgDetail003.setPercentAfter(2730L);
    userProdChgDetail003.setPercentBefore(2160L);
    userProdChgDetail003.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail003);
    //3.3
    userProdChgDetail003 = new UserProdChgDetail();
    userProdChgDetail003.setCode("470007.OF");
    userProdChgDetail003.setFundName(C470007);
    userProdChgDetail003.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail003.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail003.setModifySeq(3);
    userProdChgDetail003.setPercentAfter(500L);
    userProdChgDetail003.setPercentBefore(200L);
    userProdChgDetail003.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail003);
    //3.4
    userProdChgDetail003 = new UserProdChgDetail();
    userProdChgDetail003.setCode("000248.OF");
    userProdChgDetail003.setFundName(C000248);
    userProdChgDetail003.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail003.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail003.setModifySeq(3);
    userProdChgDetail003.setPercentAfter(500L);
    userProdChgDetail003.setPercentBefore(300L);
    userProdChgDetail003.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail003);
    //3.5
    userProdChgDetail003 = new UserProdChgDetail();
    userProdChgDetail003.setCode("001694.OF");
    userProdChgDetail003.setFundName(C001694);
    userProdChgDetail003.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail003.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail003.setModifySeq(3);
    userProdChgDetail003.setPercentAfter(700L);
    userProdChgDetail003.setPercentBefore(250L);
    userProdChgDetail003.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail003);
    //3.6
    userProdChgDetail003 = new UserProdChgDetail();
    userProdChgDetail003.setCode("400013.OF");
    userProdChgDetail003.setFundName(C400013);
    userProdChgDetail003.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail003.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail003.setModifySeq(3);
    userProdChgDetail003.setPercentAfter(250L);
    userProdChgDetail003.setPercentBefore(250L);
    userProdChgDetail003.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail003);
    //3.7
    userProdChgDetail003 = new UserProdChgDetail();
    userProdChgDetail003.setCode("000696.OF");
    userProdChgDetail003.setFundName(C000696);
    userProdChgDetail003.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail003.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail003.setModifySeq(3);
    userProdChgDetail003.setPercentAfter(200L);
    userProdChgDetail003.setPercentBefore(200L);
    userProdChgDetail003.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail003);
    //3.8
    userProdChgDetail003 = new UserProdChgDetail();
    userProdChgDetail003.setCode("001541.OF");
    userProdChgDetail003.setFundName(C001541);
    userProdChgDetail003.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail003.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail003.setModifySeq(3);
    userProdChgDetail003.setPercentAfter(600L);
    userProdChgDetail003.setPercentBefore(600L);
    userProdChgDetail003.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail003);
    //3.9
    userProdChgDetail003 = new UserProdChgDetail();
    userProdChgDetail003.setCode("000248.OF");
    userProdChgDetail003.setFundName(C000248);
    userProdChgDetail003.setFundType(FundClassEnum.ENHANCEIDX.getFundClassId());
    userProdChgDetail003.setFundTypeName(FundClassEnum.ENHANCEIDX.getFundClassComment());
    userProdChgDetail003.setModifySeq(3);
    userProdChgDetail003.setPercentAfter(600L);
    userProdChgDetail003.setPercentBefore(0L);
    userProdChgDetail003.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail003);
    //3.10
    userProdChgDetail003 = new UserProdChgDetail();
    userProdChgDetail003.setCode("400020.OF");
    userProdChgDetail003.setFundName(C400020);
    userProdChgDetail003.setFundType(FundClassEnum.PROTECTBASE.getFundClassId());
    userProdChgDetail003.setFundTypeName(FundClassEnum.PROTECTBASE.getFundClassComment());
    userProdChgDetail003.setModifySeq(3);
    userProdChgDetail003.setPercentAfter(200L);
    userProdChgDetail003.setPercentBefore(800L);
    userProdChgDetail003.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail003);
    //3.11
    userProdChgDetail003 = new UserProdChgDetail();
    userProdChgDetail003.setCode("000366.OF");
    userProdChgDetail003.setFundName(C000366);
    userProdChgDetail003.setFundType(FundClassEnum.MONEY.getFundClassId());
    userProdChgDetail003.setFundTypeName(FundClassEnum.MONEY.getFundClassComment());
    userProdChgDetail003.setModifySeq(3);
    userProdChgDetail003.setPercentAfter(120L);
    userProdChgDetail003.setPercentBefore(340L);
    userProdChgDetail003.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail003);
    //3.12
    userProdChgDetail003 = new UserProdChgDetail();
    userProdChgDetail003.setCode("000406.OF");
    userProdChgDetail003.setFundName(C000406);
    userProdChgDetail003.setFundType(FundClassEnum.COMB2.getFundClassId());
    userProdChgDetail003.setFundTypeName(FundClassEnum.COMB2.getFundClassComment());
    userProdChgDetail003.setModifySeq(3);
    userProdChgDetail003.setPercentAfter(0L);
    userProdChgDetail003.setPercentBefore(850L);
    userProdChgDetail003.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail003);
    //3.13
    userProdChgDetail003 = new UserProdChgDetail();
    userProdChgDetail003.setCode("400016.OF");
    userProdChgDetail003.setFundName(C400016);
    userProdChgDetail003.setFundType(FundClassEnum.COMB2.getFundClassId());
    userProdChgDetail003.setFundTypeName(FundClassEnum.COMB2.getFundClassComment());
    userProdChgDetail003.setModifySeq(3);
    userProdChgDetail003.setPercentAfter(0L);
    userProdChgDetail003.setPercentBefore(750L);
    userProdChgDetail003.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail003);
    //3.14
    userProdChgDetail003 = new UserProdChgDetail();
    userProdChgDetail003.setCode("400009.OF");
    userProdChgDetail003.setFundName(C400009);
    userProdChgDetail003.setFundType(FundClassEnum.COMB1.getFundClassId());
    userProdChgDetail003.setFundTypeName(FundClassEnum.COMB1.getFundClassComment());
    userProdChgDetail003.setModifySeq(3);
    userProdChgDetail003.setPercentAfter(0L);
    userProdChgDetail003.setPercentBefore(800L);
    userProdChgDetail003.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail003);
    
    //4. change
    UserProdChg userProdChg4 = new UserProdChg();
    MyBeanUtils.mapEntityIntoDTO(userProdChg, userProdChg4);
    userProdChg4.setModifyType(AdjustTypeEnum.REBALANCE.getTypeId());
    userProdChg4.setModifyComment("适当增加偏债类资产平衡组合风险");
    userProdChg4.setModifyTypeComment(AdjustTypeEnum.REBALANCE.getTypeComment());
    userProdChg4.setModifySeq(4);
    userProdChg4.setModifyTime(TradeUtil.getUTCTimeOfSpecificTime(2017,10,2,0,0));
    userProdChgs.add(userProdChg4);
    
    //4.1
    UserProdChgDetail userProdChgDetail004 = new UserProdChgDetail();
    userProdChgDetail004.setCode("000614.OF");
    userProdChgDetail004.setFundName(C000614);
    userProdChgDetail004.setFundType(FundClassEnum.QDII.getFundClassId());
    userProdChgDetail004.setFundTypeName(FundClassEnum.QDII.getFundClassComment());
    userProdChgDetail004.setModifySeq(4);
    userProdChgDetail004.setPercentAfter(3360L);
    userProdChgDetail004.setPercentBefore(3600L);
    userProdChgDetail004.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail004);
    //4.2
    userProdChgDetail004 = new UserProdChgDetail();
    userProdChgDetail004.setCode("000217.OF");
    userProdChgDetail004.setFundName(C000217);
    userProdChgDetail004.setFundType(FundClassEnum.COMMERSIAL.getFundClassId());
    userProdChgDetail004.setFundTypeName(FundClassEnum.COMMERSIAL.getFundClassComment());
    userProdChgDetail004.setModifySeq(4);
    userProdChgDetail004.setPercentAfter(2560L);
    userProdChgDetail004.setPercentBefore(2730L);
    userProdChgDetail004.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail004);
    //4.3
    userProdChgDetail004 = new UserProdChgDetail();
    userProdChgDetail004.setCode("470007.OF");
    userProdChgDetail004.setFundName(C470007);
    userProdChgDetail004.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail004.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail004.setModifySeq(4);
    userProdChgDetail004.setPercentAfter(0L);
    userProdChgDetail004.setPercentBefore(500L);
    userProdChgDetail004.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail004);
    //4.4
    userProdChgDetail004 = new UserProdChgDetail();
    userProdChgDetail004.setCode("000248.OF");
    userProdChgDetail004.setFundName(C000248);
    userProdChgDetail004.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail004.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail004.setModifySeq(4);
    userProdChgDetail004.setPercentAfter(832L);
    userProdChgDetail004.setPercentBefore(500L);
    userProdChgDetail004.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail004);
    //4.5
    userProdChgDetail004 = new UserProdChgDetail();
    userProdChgDetail004.setCode("000696.OF");
    userProdChgDetail004.setFundName(C000696);
    userProdChgDetail004.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail004.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail004.setModifySeq(4);
    userProdChgDetail004.setPercentAfter(200L);
    userProdChgDetail004.setPercentBefore(200L);
    userProdChgDetail004.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail004);
    //4.6
    userProdChgDetail004 = new UserProdChgDetail();
    userProdChgDetail004.setCode("001541.OF");
    userProdChgDetail004.setFundName(C001541);
    userProdChgDetail004.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail004.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail004.setModifySeq(4);
    userProdChgDetail004.setPercentAfter(600L);
    userProdChgDetail004.setPercentBefore(600L);
    userProdChgDetail004.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail004);
    //4.7
    userProdChgDetail004 = new UserProdChgDetail();
    userProdChgDetail004.setCode("001694.OF");
    userProdChgDetail004.setFundName(C001694);
    userProdChgDetail004.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail004.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail004.setModifySeq(4);
    userProdChgDetail004.setPercentAfter(0L);
    userProdChgDetail004.setPercentBefore(700L);
    userProdChgDetail004.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail004);
    //4.8
    userProdChgDetail004 = new UserProdChgDetail();
    userProdChgDetail004.setCode("400013.OF");
    userProdChgDetail004.setFundName(C400013);
    userProdChgDetail004.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail004.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail004.setModifySeq(4);
    userProdChgDetail004.setPercentAfter(750L);
    userProdChgDetail004.setPercentBefore(250L);
    userProdChgDetail004.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail004);
    //4.9
    userProdChgDetail004 = new UserProdChgDetail();
    userProdChgDetail004.setCode("400016.OF");
    userProdChgDetail004.setFundName(C400016);
    userProdChgDetail004.setFundType(FundClassEnum.COMB2.getFundClassId());
    userProdChgDetail004.setFundTypeName(FundClassEnum.COMB2.getFundClassComment());
    userProdChgDetail004.setModifySeq(4);
    userProdChgDetail004.setPercentAfter(678L);
    userProdChgDetail004.setPercentBefore(0L);
    userProdChgDetail004.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail004);
    //4.10
    userProdChgDetail004 = new UserProdChgDetail();
    userProdChgDetail004.setCode("000366.OF");
    userProdChgDetail004.setFundName(C000366);
    userProdChgDetail004.setFundType(FundClassEnum.MONEY.getFundClassId());
    userProdChgDetail004.setFundTypeName(FundClassEnum.MONEY.getFundClassComment());
    userProdChgDetail004.setModifySeq(4);
    userProdChgDetail004.setPercentAfter(520L);
    userProdChgDetail004.setPercentBefore(120L);
    userProdChgDetail004.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail004);
    //4.11
    userProdChgDetail004 = new UserProdChgDetail();
    userProdChgDetail004.setCode("400020.OF");
    userProdChgDetail004.setFundName(C400020);
    userProdChgDetail004.setFundType(FundClassEnum.PROTECTBASE.getFundClassId());
    userProdChgDetail004.setFundTypeName(FundClassEnum.PROTECTBASE.getFundClassComment());
    userProdChgDetail004.setModifySeq(4);
    userProdChgDetail004.setPercentAfter(500L);
    userProdChgDetail004.setPercentBefore(200L);
    userProdChgDetail004.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail004);
    //4.12
    userProdChgDetail004 = new UserProdChgDetail();
    userProdChgDetail004.setCode("000248.OF");
    userProdChgDetail004.setFundName(C000248);
    userProdChgDetail004.setFundType(FundClassEnum.ENHANCEIDX.getFundClassId());
    userProdChgDetail004.setFundTypeName(FundClassEnum.ENHANCEIDX.getFundClassComment());
    userProdChgDetail004.setModifySeq(4);
    userProdChgDetail004.setPercentAfter(0L);
    userProdChgDetail004.setPercentBefore(600L);
    userProdChgDetail004.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail004);
    
    //5. change
    UserProdChg userProdChg5 = new UserProdChg();
    MyBeanUtils.mapEntityIntoDTO(userProdChg, userProdChg5);
    userProdChg5.setModifyType(AdjustTypeEnum.REBALANCE.getTypeId());
    userProdChg5.setModifyComment("减少货币债券等配置，调高A股及海外资产比例");
    userProdChg5.setModifyTypeComment(AdjustTypeEnum.REBALANCE.getTypeComment());
    userProdChg5.setModifySeq(5);
    userProdChg5.setModifyTime(TradeUtil.getUTCTimeOfSpecificTime(2017,11,22,0,0));
    userProdChgs.add(userProdChg5);
    
    //5.1
    UserProdChgDetail userProdChgDetail005 = new UserProdChgDetail();
    userProdChgDetail005.setCode("000614.OF");
    userProdChgDetail005.setFundName(C000614);
    userProdChgDetail005.setFundType(FundClassEnum.QDII.getFundClassId());
    userProdChgDetail005.setFundTypeName(FundClassEnum.QDII.getFundClassComment());
    userProdChgDetail005.setModifySeq(5);
    userProdChgDetail005.setPercentAfter(4100L);
    userProdChgDetail005.setPercentBefore(3360L);
    userProdChgDetail005.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail005);
    //5.2
    userProdChgDetail005 = new UserProdChgDetail();
    userProdChgDetail005.setCode("000217.OF");
    userProdChgDetail005.setFundName(C000217);
    userProdChgDetail005.setFundType(FundClassEnum.COMMERSIAL.getFundClassId());
    userProdChgDetail005.setFundTypeName(FundClassEnum.COMMERSIAL.getFundClassComment());
    userProdChgDetail005.setModifySeq(5);
    userProdChgDetail005.setPercentAfter(1260L);
    userProdChgDetail005.setPercentBefore(2560L);
    userProdChgDetail005.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail005);
    //5.3
    userProdChgDetail005 = new UserProdChgDetail();
    userProdChgDetail005.setCode("400013.OF");
    userProdChgDetail005.setFundName(C400013);
    userProdChgDetail005.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail005.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail005.setModifySeq(5);
    userProdChgDetail005.setPercentAfter(1220L);
    userProdChgDetail005.setPercentBefore(750L);
    userProdChgDetail005.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail005);
    //5.4
    userProdChgDetail005 = new UserProdChgDetail();
    userProdChgDetail005.setCode("000248.OF");
    userProdChgDetail005.setFundName(C000248);
    userProdChgDetail005.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail005.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail005.setModifySeq(5);
    userProdChgDetail005.setPercentAfter(1150L);
    userProdChgDetail005.setPercentBefore(832L);
    userProdChgDetail005.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail005);
    //5.5
    userProdChgDetail005 = new UserProdChgDetail();
    userProdChgDetail005.setCode("400020.OF");
    userProdChgDetail005.setFundName(C400020);
    userProdChgDetail005.setFundType(FundClassEnum.PROTECTBASE.getFundClassId());
    userProdChgDetail005.setFundTypeName(FundClassEnum.PROTECTBASE.getFundClassComment());
    userProdChgDetail005.setModifySeq(5);
    userProdChgDetail005.setPercentAfter(1000L);
    userProdChgDetail005.setPercentBefore(500L);
    userProdChgDetail005.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail005);
    //5.6
    userProdChgDetail005 = new UserProdChgDetail();
    userProdChgDetail005.setCode("000696.OF");
    userProdChgDetail005.setFundName(C000696);
    userProdChgDetail005.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail005.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail005.setModifySeq(5);
    userProdChgDetail005.setPercentAfter(500L);
    userProdChgDetail005.setPercentBefore(200L);
    userProdChgDetail005.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail005);
    //5.7
    userProdChgDetail005 = new UserProdChgDetail();
    userProdChgDetail005.setCode("001541.OF");
    userProdChgDetail005.setFundName(C001541);
    userProdChgDetail005.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail005.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail005.setModifySeq(5);
    userProdChgDetail005.setPercentAfter(400L);
    userProdChgDetail005.setPercentBefore(600L);
    userProdChgDetail005.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail005);
    //5.8
    userProdChgDetail005 = new UserProdChgDetail();
    userProdChgDetail005.setCode("000248.OF");
    userProdChgDetail005.setFundName(C000248);
    userProdChgDetail005.setFundType(FundClassEnum.ENHANCEIDX.getFundClassId());
    userProdChgDetail005.setFundTypeName(FundClassEnum.ENHANCEIDX.getFundClassComment());
    userProdChgDetail005.setModifySeq(5);
    userProdChgDetail005.setPercentAfter(370L);
    userProdChgDetail005.setPercentBefore(0L);
    userProdChgDetail005.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail005);
    //5.9
    userProdChgDetail005 = new UserProdChgDetail();
    userProdChgDetail005.setCode("400016.OF");
    userProdChgDetail005.setFundName(C400016);
    userProdChgDetail005.setFundType(FundClassEnum.COMB2.getFundClassId());
    userProdChgDetail005.setFundTypeName(FundClassEnum.COMB2.getFundClassComment());
    userProdChgDetail005.setModifySeq(5);
    userProdChgDetail005.setPercentAfter(0L);
    userProdChgDetail005.setPercentBefore(678L);
    userProdChgDetail005.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail005);
    //5.9
    userProdChgDetail005 = new UserProdChgDetail();
    userProdChgDetail005.setCode("000366.OF");
    userProdChgDetail005.setFundName(C000366);
    userProdChgDetail005.setFundType(FundClassEnum.MONEY.getFundClassId());
    userProdChgDetail005.setFundTypeName(FundClassEnum.MONEY.getFundClassComment());
    userProdChgDetail005.setModifySeq(5);
    userProdChgDetail005.setPercentAfter(0L);
    userProdChgDetail005.setPercentBefore(520L);
    userProdChgDetail005.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail005);
    
    //6. change
    UserProdChg userProdChg6 = new UserProdChg();
    MyBeanUtils.mapEntityIntoDTO(userProdChg, userProdChg6);
    userProdChg6.setModifyType(AdjustTypeEnum.RISKSTART_USA.getTypeId());
    userProdChg6.setModifyComment("系统预期美股将有较大波动，开始调整风险比例");
    userProdChg6.setModifyTypeComment(AdjustTypeEnum.RISKSTART_USA.getTypeComment());
    userProdChg6.setModifySeq(6);
    userProdChg6.setModifyTime(TradeUtil.getUTCTimeOfSpecificTime(2018,1,30,0,0));
    userProdChgs.add(userProdChg6);
    
    //6.1
    UserProdChgDetail userProdChgDetail006 = new UserProdChgDetail();
    userProdChgDetail006.setCode("000614.OF");
    userProdChgDetail006.setFundName(C000614);
    userProdChgDetail006.setFundType(FundClassEnum.QDII.getFundClassId());
    userProdChgDetail006.setFundTypeName(FundClassEnum.QDII.getFundClassComment());
    userProdChgDetail006.setModifySeq(6);
    userProdChgDetail006.setPercentAfter(2800L);
    userProdChgDetail006.setPercentBefore(4100L);
    userProdChgDetail006.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail006);
    //6.2
    userProdChgDetail006 = new UserProdChgDetail();
    userProdChgDetail006.setCode("400020.OF");
    userProdChgDetail006.setFundName(C400020);
    userProdChgDetail006.setFundType(FundClassEnum.PROTECTBASE.getFundClassId());
    userProdChgDetail006.setFundTypeName(FundClassEnum.PROTECTBASE.getFundClassComment());
    userProdChgDetail006.setModifySeq(6);
    userProdChgDetail006.setPercentAfter(2200L);
    userProdChgDetail006.setPercentBefore(1000L);
    userProdChgDetail006.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail006);
    //6.3
    userProdChgDetail006 = new UserProdChgDetail();
    userProdChgDetail006.setCode("000366.OF");
    userProdChgDetail006.setFundName(C000366);
    userProdChgDetail006.setFundType(FundClassEnum.MONEY.getFundClassId());
    userProdChgDetail006.setFundTypeName(FundClassEnum.MONEY.getFundClassComment());
    userProdChgDetail006.setModifySeq(6);
    userProdChgDetail006.setPercentAfter(1932L);
    userProdChgDetail006.setPercentBefore(0L);
    userProdChgDetail006.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail006);
    //6.4
    userProdChgDetail006 = new UserProdChgDetail();
    userProdChgDetail006.setCode("000217.OF");
    userProdChgDetail006.setFundName(C000217);
    userProdChgDetail006.setFundType(FundClassEnum.COMMERSIAL.getFundClassId());
    userProdChgDetail006.setFundTypeName(FundClassEnum.COMMERSIAL.getFundClassComment());
    userProdChgDetail006.setModifySeq(6);
    userProdChgDetail006.setPercentAfter(860L);
    userProdChgDetail006.setPercentBefore(1260L);
    userProdChgDetail006.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail006);
    //6.4
    userProdChgDetail006 = new UserProdChgDetail();
    userProdChgDetail006.setCode("000248.OF");
    userProdChgDetail006.setFundName(C000248);
    userProdChgDetail006.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail006.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail006.setModifySeq(6);
    userProdChgDetail006.setPercentAfter(800L);
    userProdChgDetail006.setPercentBefore(1150L);
    userProdChgDetail006.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail006);
    //6.5
    userProdChgDetail006 = new UserProdChgDetail();
    userProdChgDetail006.setCode("400013.OF");
    userProdChgDetail006.setFundName(C400013);
    userProdChgDetail006.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail006.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail006.setModifySeq(6);
    userProdChgDetail006.setPercentAfter(720L);
    userProdChgDetail006.setPercentBefore(1220L);
    userProdChgDetail006.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail006);
    //6.6
    userProdChgDetail006 = new UserProdChgDetail();
    userProdChgDetail006.setCode("000696.OF");
    userProdChgDetail006.setFundName(C000696);
    userProdChgDetail006.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail006.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail006.setModifySeq(6);
    userProdChgDetail006.setPercentAfter(340L);
    userProdChgDetail006.setPercentBefore(500L);
    userProdChgDetail006.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail006);
    //6.7
    userProdChgDetail006 = new UserProdChgDetail();
    userProdChgDetail006.setCode("001541.OF");
    userProdChgDetail006.setFundName(C001541);
    userProdChgDetail006.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail006.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail006.setModifySeq(6);
    userProdChgDetail006.setPercentAfter(200L);
    userProdChgDetail006.setPercentBefore(400L);
    userProdChgDetail006.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail006);
    //6.8
    userProdChgDetail006 = new UserProdChgDetail();
    userProdChgDetail006.setCode("000248.OF");
    userProdChgDetail006.setFundName(C000248);
    userProdChgDetail006.setFundType(FundClassEnum.ENHANCEIDX.getFundClassId());
    userProdChgDetail006.setFundTypeName(FundClassEnum.ENHANCEIDX.getFundClassComment());
    userProdChgDetail006.setModifySeq(6);
    userProdChgDetail006.setPercentAfter(148L);
    userProdChgDetail006.setPercentBefore(370L);
    userProdChgDetail006.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail006);
    
    //7. change
    UserProdChg userProdChg7 = new UserProdChg();
    MyBeanUtils.mapEntityIntoDTO(userProdChg, userProdChg7);
    userProdChg7.setModifyType(AdjustTypeEnum.RISKCONTINUE_USA.getTypeId());
    userProdChg7.setModifyComment("美股短期持续，继续调低相关持仓");
    userProdChg7.setModifyTypeComment(AdjustTypeEnum.RISKCONTINUE_USA.getTypeComment());
    userProdChg7.setModifySeq(7);
    userProdChg7.setModifyTime(TradeUtil.getUTCTimeOfSpecificTime(2018,2,5,0,0));
    userProdChgs.add(userProdChg7);
    
    //7.1
    UserProdChgDetail userProdChgDetail007 = new UserProdChgDetail();
    userProdChgDetail007.setCode("000614.OF");
    userProdChgDetail007.setFundName(C000614);
    userProdChgDetail007.setFundType(FundClassEnum.QDII.getFundClassId());
    userProdChgDetail007.setFundTypeName(FundClassEnum.QDII.getFundClassComment());
    userProdChgDetail007.setModifySeq(7);
    userProdChgDetail007.setPercentAfter(2300L);
    userProdChgDetail007.setPercentBefore(2800L);
    userProdChgDetail007.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail007);
    //7.2
    userProdChgDetail007 = new UserProdChgDetail();
    userProdChgDetail007.setCode("000366.OF");
    userProdChgDetail007.setFundName(C000366);
    userProdChgDetail007.setFundType(FundClassEnum.MONEY.getFundClassId());
    userProdChgDetail007.setFundTypeName(FundClassEnum.MONEY.getFundClassComment());
    userProdChgDetail007.setModifySeq(7);
    userProdChgDetail007.setPercentAfter(4235L);
    userProdChgDetail007.setPercentBefore(1932L);
    userProdChgDetail007.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail007);
    //7.3
    userProdChgDetail007 = new UserProdChgDetail();
    userProdChgDetail007.setCode("400020.OF");
    userProdChgDetail007.setFundName(C400020);
    userProdChgDetail007.setFundType(FundClassEnum.PROTECTBASE.getFundClassId());
    userProdChgDetail007.setFundTypeName(FundClassEnum.PROTECTBASE.getFundClassComment());
    userProdChgDetail007.setModifySeq(7);
    userProdChgDetail007.setPercentAfter(2000L);
    userProdChgDetail007.setPercentBefore(2200L);
    userProdChgDetail007.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail007);
    //7.4
    userProdChgDetail007 = new UserProdChgDetail();
    userProdChgDetail007.setCode("000248.OF");
    userProdChgDetail007.setFundName(C000248);
    userProdChgDetail007.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail007.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail007.setModifySeq(7);
    userProdChgDetail007.setPercentAfter(632L);
    userProdChgDetail007.setPercentBefore(800L);
    userProdChgDetail007.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail007);
    //7.5
    userProdChgDetail007 = new UserProdChgDetail();
    userProdChgDetail007.setCode("400013.OF");
    userProdChgDetail007.setFundName(C400013);
    userProdChgDetail007.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail007.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail007.setModifySeq(7);
    userProdChgDetail007.setPercentAfter(533L);
    userProdChgDetail007.setPercentBefore(720L);
    userProdChgDetail007.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail007);
    //7.6
    userProdChgDetail007 = new UserProdChgDetail();
    userProdChgDetail007.setCode("000217.OF");
    userProdChgDetail007.setFundName(C000217);
    userProdChgDetail007.setFundType(FundClassEnum.COMMERSIAL.getFundClassId());
    userProdChgDetail007.setFundTypeName(FundClassEnum.COMMERSIAL.getFundClassComment());
    userProdChgDetail007.setModifySeq(7);
    userProdChgDetail007.setPercentAfter(300L);
    userProdChgDetail007.setPercentBefore(860L);
    userProdChgDetail007.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail007);
    //7.7
    userProdChgDetail007 = new UserProdChgDetail();
    userProdChgDetail007.setCode("000696.OF");
    userProdChgDetail007.setFundName(C000696);
    userProdChgDetail007.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail007.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail007.setModifySeq(7);
    userProdChgDetail007.setPercentAfter(0L);
    userProdChgDetail007.setPercentBefore(340L);
    userProdChgDetail007.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail007);
    //7.8
    userProdChgDetail007 = new UserProdChgDetail();
    userProdChgDetail007.setCode("001541.OF");
    userProdChgDetail007.setFundName(C001541);
    userProdChgDetail007.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail007.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail007.setModifySeq(7);
    userProdChgDetail007.setPercentAfter(0L);
    userProdChgDetail007.setPercentBefore(200L);
    userProdChgDetail007.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail007);
    //7.9
    userProdChgDetail007 = new UserProdChgDetail();
    userProdChgDetail007.setCode("000248.OF");
    userProdChgDetail007.setFundName(C000248);
    userProdChgDetail007.setFundType(FundClassEnum.ENHANCEIDX.getFundClassId());
    userProdChgDetail007.setFundTypeName(FundClassEnum.ENHANCEIDX.getFundClassComment());
    userProdChgDetail007.setModifySeq(7);
    userProdChgDetail007.setPercentAfter(0L);
    userProdChgDetail007.setPercentBefore(148L);
    userProdChgDetail007.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail007);
    
    
    //8. change
    UserProdChg userProdChg8 = new UserProdChg();
    MyBeanUtils.mapEntityIntoDTO(userProdChg, userProdChg8);
    userProdChg8.setModifyType(AdjustTypeEnum.RISKSTOK.getTypeId());
    userProdChg8.setModifyComment("美股短期风险仍然较大，大幅调低股票类权益类持仓");
    userProdChg8.setModifyTypeComment(AdjustTypeEnum.RISKSTOK.getTypeComment());
    userProdChg8.setModifySeq(8);
    userProdChg8.setModifyTime(TradeUtil.getUTCTimeOfSpecificTime(2018,2,8,0,0));
    userProdChgs.add(userProdChg8);
    
    //8.1
    UserProdChgDetail userProdChgDetail008 = new UserProdChgDetail();
    userProdChgDetail008.setCode("000614.OF");
    userProdChgDetail008.setFundName(C000614);
    userProdChgDetail008.setFundType(FundClassEnum.QDII.getFundClassId());
    userProdChgDetail008.setFundTypeName(FundClassEnum.QDII.getFundClassComment());
    userProdChgDetail008.setModifySeq(8);
    userProdChgDetail008.setPercentAfter(650L);
    userProdChgDetail008.setPercentBefore(2300L);
    userProdChgDetail008.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail008);
    //8.2
    userProdChgDetail008 = new UserProdChgDetail();
    userProdChgDetail008.setCode("000366.OF");
    userProdChgDetail008.setFundName(C000366);
    userProdChgDetail008.setFundType(FundClassEnum.MONEY.getFundClassId());
    userProdChgDetail008.setFundTypeName(FundClassEnum.MONEY.getFundClassComment());
    userProdChgDetail008.setModifySeq(8);
    userProdChgDetail008.setPercentAfter(8950L);
    userProdChgDetail008.setPercentBefore(4235L);
    userProdChgDetail008.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail008);
    //8.3
    userProdChgDetail008 = new UserProdChgDetail();
    userProdChgDetail008.setCode("000248.OF");
    userProdChgDetail008.setFundName(C000248);
    userProdChgDetail008.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail008.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail008.setModifySeq(8);
    userProdChgDetail008.setPercentAfter(200L);
    userProdChgDetail008.setPercentBefore(632L);
    userProdChgDetail008.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail008);
    //8.4
    userProdChgDetail008 = new UserProdChgDetail();
    userProdChgDetail008.setCode("400013.OF");
    userProdChgDetail008.setFundName(C400013);
    userProdChgDetail008.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail008.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail008.setModifySeq(8);
    userProdChgDetail008.setPercentAfter(200L);
    userProdChgDetail008.setPercentBefore(533L);
    userProdChgDetail008.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail008);
    //8.5
    userProdChgDetail008 = new UserProdChgDetail();
    userProdChgDetail008.setCode("400020.OF");
    userProdChgDetail008.setFundName(C400020);
    userProdChgDetail008.setFundType(FundClassEnum.PROTECTBASE.getFundClassId());
    userProdChgDetail008.setFundTypeName(FundClassEnum.PROTECTBASE.getFundClassComment());
    userProdChgDetail008.setModifySeq(8);
    userProdChgDetail008.setPercentAfter(0L);
    userProdChgDetail008.setPercentBefore(2000L);
    userProdChgDetail008.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail008);
    //8.6
    userProdChgDetail008 = new UserProdChgDetail();
    userProdChgDetail008.setCode("000217.OF");
    userProdChgDetail008.setFundName(C000217);
    userProdChgDetail008.setFundType(FundClassEnum.COMMERSIAL.getFundClassId());
    userProdChgDetail008.setFundTypeName(FundClassEnum.COMMERSIAL.getFundClassComment());
    userProdChgDetail008.setModifySeq(8);
    userProdChgDetail008.setPercentAfter(0L);
    userProdChgDetail008.setPercentBefore(300L);
    userProdChgDetail008.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail008);
    
    
    //9. change
    UserProdChg userProdChg9 = new UserProdChg();
    MyBeanUtils.mapEntityIntoDTO(userProdChg, userProdChg9);
    userProdChg9.setModifyType(AdjustTypeEnum.RISKDISMISS_END.getTypeId());
    userProdChg9.setModifyComment("美股波动减弱，本轮风控结束，恢复合适的配置比例");
    userProdChg9.setModifyTypeComment(AdjustTypeEnum.RISKDISMISS_END.getTypeComment());
    userProdChg9.setModifySeq(9);
    userProdChg9.setModifyTime(TradeUtil.getUTCTimeOfSpecificTime(2018,3,1,0,0));
    userProdChgs.add(userProdChg9);
    
    //9.1
    UserProdChgDetail userProdChgDetail009 = new UserProdChgDetail();
    userProdChgDetail009.setCode("000614.OF");
    userProdChgDetail009.setFundName(C000614);
    userProdChgDetail009.setFundType(FundClassEnum.QDII.getFundClassId());
    userProdChgDetail009.setFundTypeName(FundClassEnum.QDII.getFundClassComment());
    userProdChgDetail009.setModifySeq(9);
    userProdChgDetail009.setPercentAfter(4233L);
    userProdChgDetail009.setPercentBefore(650L);
    userProdChgDetail009.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail009);
    //9.2
    userProdChgDetail009 = new UserProdChgDetail();
    userProdChgDetail009.setCode("000248.OF");
    userProdChgDetail009.setFundName(C000248);
    userProdChgDetail009.setFundType(FundClassEnum.PASIDX.getFundClassId());
    userProdChgDetail009.setFundTypeName(FundClassEnum.PASIDX.getFundClassComment());
    userProdChgDetail009.setModifySeq(9);
    userProdChgDetail009.setPercentAfter(3039L);
    userProdChgDetail009.setPercentBefore(200L);
    userProdChgDetail009.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail009);
    //9.3
    userProdChgDetail009 = new UserProdChgDetail();
    userProdChgDetail009.setCode("400013.OF");
    userProdChgDetail009.setFundName(C400013);
    userProdChgDetail009.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail009.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail009.setModifySeq(9);
    userProdChgDetail009.setPercentAfter(728L);
    userProdChgDetail009.setPercentBefore(200L);
    userProdChgDetail009.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail009);
    //9.4
    userProdChgDetail009 = new UserProdChgDetail();
    userProdChgDetail009.setCode("001694.OF");
    userProdChgDetail009.setFundName(C001694);
    userProdChgDetail009.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail009.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail009.setModifySeq(9);
    userProdChgDetail009.setPercentAfter(500L);
    userProdChgDetail009.setPercentBefore(0L);
    userProdChgDetail009.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail009);
    //9.5
    userProdChgDetail009 = new UserProdChgDetail();
    userProdChgDetail009.setCode("470008.OF");
    userProdChgDetail009.setFundName(C470008);
    userProdChgDetail009.setFundType(FundClassEnum.LIKESTOK.getFundClassId());
    userProdChgDetail009.setFundTypeName(FundClassEnum.LIKESTOK.getFundClassComment());
    userProdChgDetail009.setModifySeq(9);
    userProdChgDetail009.setPercentAfter(500L);
    userProdChgDetail009.setPercentBefore(0L);
    userProdChgDetail009.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail009);
    //9.6
    userProdChgDetail009 = new UserProdChgDetail();
    userProdChgDetail009.setCode("001541.OF");
    userProdChgDetail009.setFundName(C001541);
    userProdChgDetail009.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail009.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail009.setModifySeq(9);
    userProdChgDetail009.setPercentAfter(500L);
    userProdChgDetail009.setPercentBefore(0L);
    userProdChgDetail009.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail009);
    //9.7
    userProdChgDetail009 = new UserProdChgDetail();
    userProdChgDetail009.setCode("000696.OF");
    userProdChgDetail009.setFundName(C000696);
    userProdChgDetail009.setFundType(FundClassEnum.COMMONSTOCK.getFundClassId());
    userProdChgDetail009.setFundTypeName(FundClassEnum.COMMONSTOCK.getFundClassComment());
    userProdChgDetail009.setModifySeq(9);
    userProdChgDetail009.setPercentAfter(500L);
    userProdChgDetail009.setPercentBefore(0L);
    userProdChgDetail009.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail009);
    //9.8
    userProdChgDetail009 = new UserProdChgDetail();
    userProdChgDetail009.setCode("000366.OF");
    userProdChgDetail009.setFundName(C000366);
    userProdChgDetail009.setFundType(FundClassEnum.MONEY.getFundClassId());
    userProdChgDetail009.setFundTypeName(FundClassEnum.MONEY.getFundClassComment());
    userProdChgDetail009.setModifySeq(9);
    userProdChgDetail009.setPercentAfter(0L);
    userProdChgDetail009.setPercentBefore(8950L);
    userProdChgDetail009.setProdId(userProdChg1.getProdId());
    userProdChgDetails.add(userProdChgDetail009);

    userProdChangeLogService.insertDetailChangeLogs(userProdChgDetails);

  }


  @Test
  public void insertDetailChangeLogs() throws Exception {




  }



}
