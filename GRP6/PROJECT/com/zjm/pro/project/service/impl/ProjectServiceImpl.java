package com.zjm.pro.project.service.impl;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zjm.common.db.model.PageTable;
import com.zjm.common.db.model.User;
import com.zjm.common.log.service.LogService;
import com.zjm.common.sysDicData.service.SysDicDataService;
import com.zjm.crm.client.service.ClientService;
import com.zjm.crm.db.map.Crm_relationMainMapper;
import com.zjm.crm.db.model.Client;
import com.zjm.gworkFlow.db.model.OsGworkflowFlowtemplate;
import com.zjm.gworkFlow.startWorkFlow.service.GworkFlowService;
import com.zjm.pro.apply.service.ProjectApplyService;
import com.zjm.pro.applyDetail.service.ApplyDetailService;
import com.zjm.pro.calculationFactPay.service.CalculationFactPayService;
import com.zjm.pro.checkPlan.service.CheckPlanService;
import com.zjm.pro.db.map.Pro_applyDetailMapper;
import com.zjm.pro.db.map.Pro_applyMapper;
import com.zjm.pro.db.map.Pro_checkPlanMapper;
import com.zjm.pro.db.map.Pro_factPayMapper;
import com.zjm.pro.db.map.Pro_finishMapper;
import com.zjm.pro.db.map.Pro_meetingDetailMapper;
import com.zjm.pro.db.map.Pro_meetingResolutionMapper;
import com.zjm.pro.db.map.Pro_optGuarantyMapper;
import com.zjm.pro.db.map.Pro_projChangeRecMapper;
import com.zjm.pro.db.map.Pro_projectMapper;
import com.zjm.pro.db.map.Pro_projectfilesMapper;
import com.zjm.pro.db.map.Pro_projectoverdueMapper;
import com.zjm.pro.db.map.Pro_replaceMapper;
import com.zjm.pro.db.model.EChart;
import com.zjm.pro.db.model.Pro_apply;
import com.zjm.pro.db.model.Pro_applyDetail;
import com.zjm.pro.db.model.Pro_calculationFactPay;
import com.zjm.pro.db.model.Pro_checkPlan;
import com.zjm.pro.db.model.Pro_factPay;
import com.zjm.pro.db.model.Pro_finish;
import com.zjm.pro.db.model.Pro_loanPlan;
import com.zjm.pro.db.model.Pro_meetingDetail;
import com.zjm.pro.db.model.Pro_meetingResolution;
import com.zjm.pro.db.model.Pro_project;
import com.zjm.pro.db.model.Pro_projectCode;
import com.zjm.pro.db.model.Pro_projectForProVo;
import com.zjm.pro.db.model.Pro_projectoverdue;
import com.zjm.pro.db.model.Pro_relationProjectVo;
import com.zjm.pro.db.model.Pro_replace;
import com.zjm.pro.loan.service.LoanService;
import com.zjm.pro.meetDetail.service.MeetingDetailService;
import com.zjm.pro.meetResolution.service.MeetingResolutionService;
import com.zjm.pro.project.service.ProjectService;
import com.zjm.pro.projectCode.service.ProjectCodeService;
import com.zjm.pro.projectLawsuit.service.ProjectLawsuitService;
import com.zjm.sys.busisort.service.BusiSortService;
import com.zjm.sys.db.model.C_busiSort;
import com.zjm.sys.db.model.C_dictype;
import com.zjm.sys.dicType.service.DicTypeService;
import com.zjm.util.BigDecimalUtil;
import com.zjm.util.DateUtil;
import com.zjm.util.SystemSession;
import com.zjm.util.Tool;

@Service("projectService")
@Transactional
public class ProjectServiceImpl implements ProjectService {
	@Resource
	private Pro_projectMapper projectMapper;
	@Resource
	private LogService logService;
	@Resource
	private ApplyDetailService applyDetailService;
	@Resource
	private ProjectApplyService projectApplyService;
	@Resource
	private LoanService loanService;
	@Resource
	private CheckPlanService checkPlanService;
	@Resource
	private MeetingResolutionService meetingResolutionService;
	@Resource
	private MeetingDetailService meetingDetailService;
	@Resource
	private SysDicDataService sysDicDataService;
	@Resource
	private Pro_applyDetailMapper applyDetailMapper;
	
	@Resource
	private Pro_checkPlanMapper pro_checkPlanMapper;
	@Resource
	private Pro_finishMapper pro_finishMapper;

	@Resource
	private ProjectCodeService projectCodeService;
    
	//申请表mapper
	@Resource
	private Pro_applyMapper pro_applyMapper;
	@Resource
	private BusiSortService busiSortService;
	@Resource
	private Pro_factPayMapper factPayMapper;
	@Resource
	private Pro_replaceMapper replaceMapper;
	@Resource
	private Crm_relationMainMapper relationMainMapper;
	@Resource
	private ClientService clientService;
	@Resource
	private Pro_meetingDetailMapper meetingDetailMapper;
	@Resource
	private Pro_meetingResolutionMapper meetingResolutionMapper;
	@Resource
	private Pro_projectfilesMapper projectfilesMapper;
	@Resource
	private Pro_checkPlanMapper checkPlanMapper;
	@Resource
	private Pro_optGuarantyMapper optGuarantyMapper;
	@Resource
	private GworkFlowService gworkFlowService;
	@Resource 
	private Pro_projChangeRecMapper changeRecMapper;
	@Resource
	private Pro_projectoverdueMapper overdueMapper;
	@Resource
	private DicTypeService dicTypeService;
	@Resource
	private CalculationFactPayService calculationFactPayService;
	@Resource
	private ProjectLawsuitService lawSuitService;		
	
	BigDecimalUtil bigDecimal = new BigDecimalUtil();
	
	/**
	 * 根据输入的条件查询一条project信息
	 * project_id
	 */
	public Pro_project selectOneProjectInfoByWheresql(String WhereSql) {
		Pro_project project = projectMapper.selectOneProjectWhereSql(WhereSql);
		return project;
	}
	
	/**
     * 新增项目逾期确认
     * @param userSession
     * @param pro_project
     * @return
     */
	public Boolean insertOneProjectOver(User userSession, Pro_project pro_project) {
		Boolean b=false;
		pro_project.setUnit_uid(userSession.getUnit_uid());
		pro_project.setUpdateUserName(userSession.getUser_name());
		pro_project.setIsOver(1);
		Integer returnInfo = projectMapper.updateOneProjectInfo(pro_project);
		if(1==returnInfo){
			logService.insertOneOperatorLogInfo(userSession,"逾期确认", "更新", "更新一条项目逾期信息project_ID="+pro_project.getProject_ID());
			b=true;
		}
		return b;
	}

	@Override
	public PageTable<Pro_project> selectProjectPageTables(PageTable<Pro_project> pageTable) {
		List<Pro_project> projectList=new ArrayList<>();
		try {
			projectList = projectMapper.selectProjectPageTables(pageTable);
//			for (Pro_project pro_project : projectList) {
//				pro_project.setNotYetEndDate(differentDaysByMillisecond(pro_project.getLoadEndDate(),new Date()));
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Long total=0L;
		try {
			total = projectMapper.selectProjectPageTables_Count(pageTable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		pageTable.setRows(projectList);
		pageTable.setTotal(total);
		return pageTable;
	}
	@Override
	public Long selectProjectPageTables_Count(PageTable<Pro_project> pageTable) {
		return projectMapper.selectProjectPageTables_Count(pageTable);
	}
	
	/**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
	 * @throws ParseException 
     */
	 public static int differentDaysByMillisecond(Date date1,Date date2) throws ParseException{
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			String date1String = simpleDateFormat.format(date1);
			String date2String = simpleDateFormat2.format(date2);
			Date DateFormat1 = simpleDateFormat.parse(date1String);
			Date DateFormat2 = simpleDateFormat.parse(date2String);
	        int days = (int) ((DateFormat1.getTime() - DateFormat2.getTime()) / (1000*3600*24));
	        return days;
	    }
	 
	 /**
	  * 保后检查
	  */
	@Override
	public PageTable<Pro_project> selectCheckProjectPageTables(PageTable<Pro_project> pageTable) {
		List<Pro_project> projectList=new ArrayList<>();
		try {
			projectList = projectMapper.selectCheckProjectPageTables(pageTable);
			for (Pro_project pro_project : projectList) {
				pro_project.setNotYetEndDate(differentDaysByMillisecond(pro_project.getLoadEndDate(),new Date()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Long total=0L;
		try {
			total = projectMapper.selectCheckProjectPageTables_Count(pageTable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		pageTable.setRows(projectList);
		pageTable.setTotal(total);
		return pageTable;
	}
	@Override
	public Boolean updateOneProjectInfo(User user,Pro_project pro_project) {
		Integer i = projectMapper.updateOneProjectInfo(pro_project);
		if( i > 0){
			logService.insertOneOperatorLogInfo(user,"项目更新", "更新", "更新一条项目信息project_ID="+pro_project.getProject_ID());
			return true;
		}
		return false;
	}
	
	/**
	 * 添加一条实际放款信息
	 * 从页面传入的Pro_project信息:
	 * 	apply_ID, applyDetail_ID, busiTypeID, busiTypeName, bankID, bankName, subBankName, loadSum, loadBeginDate, loadEndDate, billBeginDate, billEndDate 
	 */
	@Override
	public Boolean insertOneProjectInfo(User user, Pro_project project) {
		try {
			project.setProject_ID(Tool.createUUID32());
			project.setUnit_uid(user.getUnit_uid());
			project.setUnit_uidName(user.getUnit_uidName());
			project.setUpdateUserName(user.getUser_name());
			
			Pro_apply apply = projectApplyService.selectOneApplyByWhereSql(" and apply_ID ='"+project.getApply_ID()+"'");
			Pro_applyDetail applyDetail = applyDetailService.selectApplyDetailList(" and applyDetail_ID = '"+project.getApplyDetail_ID()+"'").get(0);
			project.setProjectName(apply.getProjectName());		//项目名称
			project.setProjectCode(applyDetail.getProjectCode());	//项目编号
			project.setBusiClass(applyDetail.getBusiClass());	//业务大类(01：担保; 02：委贷)
			project.setGuarantyScale(applyDetail.getGuarantyScale());	//担保责任比例
			project.setGuarantyScope(applyDetail.getGuarantyScope());	//担保责任范围
			project.setGuarantyDutySum(applyDetail.getGuarantyDutySum());	//担保责任金额
			project.setGuarantySum(project.getLoadSum());	//在保余额,初始化为放款金额
			project.setPeriodMonth(applyDetail.getAgreeMonth());	//期限.月
			project.setPeriodDay(applyDetail.getAgreeDay());	//期限.天
			project.setPeriodMonthDay(applyDetail.getAgreeMonthDay());	//期限.月天
			project.setDelayBeginDate(project.getBillBeginDate());	//展期起始日期（初始为借据起始日期）
			project.setDelayEndDate(project.getBillEndDate());	//展期到期日期（初始为借据到期日期）
			project.setAmanID(applyDetail.getAmanID());		//项目经理A角ID
			project.setAmanName(applyDetail.getAmanName());	//项目经理A角名称
			project.setBmanID(applyDetail.getBmanID());		//项目经理B角ID
			project.setBmanName(applyDetail.getBmanName());	//项目经理B角名称
			
			project.setBeforeAManID(applyDetail.getAmanID());	//初始化移交前项目经理A角ID
			project.setBeforeAManName(applyDetail.getAmanName());	//初始化移交前项目经理A角名称
			project.setBeforeBManID(applyDetail.getBmanID());		//初始化移交前项目经理B角ID
			project.setBeforeBManName(applyDetail.getBmanName());	//初始化移交前项目经理B角名称
			
			project.setReviewManID(applyDetail.getReviewManID());	//风控评审人ID
			project.setReviewManName(applyDetail.getReviewManName());	//风控评审人名称
			project.setReportDate(project.getBillBeginDate());	//上报日期, 初始为展期起始日期
			project.setGuarantorsName(apply.getGuarantyOrgName());//同步更新冗余字段 在保名字
			
//			project.setLoadCode()	;//	放款单编号	Variable characters (30)
//			project.setLoanMethod()	;//	放款方式（中文：一次性放款/多次放款）	Variable characters (10)
//			project.setPayMethod()	;//	还款方式（中文：一次性还款/多次还款）	Variable characters (10)
			
			Pro_loanPlan loanPlan = new Pro_loanPlan();
			loanPlan.setLoanPlan_ID(project.getLoanPlan_ID());
			loanPlan.setLoanState("已放款");
			Boolean b1 = loanService.updatePlanLoanState(loanPlan);			
			if(1==projectMapper.insertOneProjectInfo(project) && b1){
				logService.insertOneOperatorLogInfo(user,"放款复核", "添加", "添加一条实际放款信息 project_ID="+project.getProject_ID());
				//生成检查计划
				Pro_meetingResolution meetingResolution = meetingResolutionService.selectOneResolutionByWhereSql(" and applyID ='"+project.getApply_ID()+"'");
				if(meetingResolution != null){
				/*	List<SysDicData> zhouqiList = sysDicDataService.selectDicTypeDicNoEableList("","e5265aaf696e4de2961ded8b358ed8b1");			
					for (SysDicData sysDicData : zhouqiList) {*/
					if(meetingResolution.getControlTypeID() != null){
						if (meetingResolution.getControlTypeID().equals("aaab198ff93b4b0c8cd1e6d0b34155cb")) {
							meetingResolution.setControlTypeNumber(6);
						}
						if (meetingResolution.getControlTypeID().equals("70ee36e96e4a485ab12b110acdd24c48")) {
							meetingResolution.setControlTypeNumber(12);
						}
						if (meetingResolution.getControlTypeID().equals("5aaa988f41a3456295366fc8009fd662")) {
							meetingResolution.setControlTypeNumber(3);
						} else {
							meetingResolution.setControlTypeNumber(1);
						}
						
	//				int checkdate=meetingResolution.getControlTypeNumber();		
	//				checkPlanService.insertCheckPlans( project, checkdate);		
						// 算出两个日期相差的月份（d1交大日期，d2较小日期）
						Date d1 = project.getDelayEndDate();
						Date d2 = project.getDelayBeginDate();
						Calendar c1 = Calendar.getInstance();
						Calendar c2 = Calendar.getInstance();
						c1.setTime(d1);
						c2.setTime(d2);
						if (c1.getTimeInMillis() > c2.getTimeInMillis()) {
							int year1 = c1.get(Calendar.YEAR);
							int year2 = c2.get(Calendar.YEAR);
							int month1 = c1.get(Calendar.MONTH);
							int month2 = c2.get(Calendar.MONTH);
							int day1 = c1.get(Calendar.DAY_OF_MONTH);
							int day2 = c2.get(Calendar.DAY_OF_MONTH);
							// 获取年的差值 假设 d1 = 2015-8-16 d2 = 2011-9-30
							int yearInterval = year1 - year2;
							// 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
							if (month1 < month2 || month1 == month2 && day1 < day2)
								yearInterval--;
							// 获取月数差值
							int monthInterval = (month1 + 12) - month2;
							if (day1 < day2)
								monthInterval--;
							monthInterval %= 12;
							// 相差的月数
							monthInterval = yearInterval * 12 + monthInterval;		
							int b=0;
							if(monthInterval%meetingResolution.getControlTypeNumber()==0){
								b=monthInterval/meetingResolution.getControlTypeNumber();
							}else{
								b=monthInterval/meetingResolution.getControlTypeNumber()+1;
							}
							for(int i=0;i<b;i++){
								Pro_checkPlan  proCheckPlan=new Pro_checkPlan();
								proCheckPlan.setCheckPlan_ID(Tool.createUUID32());
								proCheckPlan.setApply_ID(project.getApply_ID());
								proCheckPlan.setCreate_user(SystemSession.getUserSession().getUser_name());
								Date date1=new Date();
								proCheckPlan.setPlanCheckDate(date1);
								proCheckPlan.setCreate_user(SystemSession.getUserSession().getUser_name());
								proCheckPlan.setUnit_uid(SystemSession.getUserSession().getUnit_uid());				 
								pro_checkPlanMapper.insertOneCheckPlan(proCheckPlan);
							}		 
						}
					}
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 更新提前到期信息
	 */
	@Override
	public Boolean updateBeforeEndInfo(User user, Pro_project project) {
		try {
			project.setIsBeforeEnd(1);
			if(1==projectMapper.updateBeforeEndInfo(project)){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	//新增保后项目
	public Boolean insertOneProjectAfter(User userSession, Pro_project pro_project) {
		Boolean b = false;
		String  newProject_ID = Tool.createUUID32();
		String  newApply_ID = Tool.createUUID32();//创建新的Apply_ID
		String  newApplyDetail_ID = Tool.createUUID32();//创建新的ApplyDetail_ID	
		//设置编号
		Pro_projectCode projectCode = new Pro_projectCode();
		projectCode=projectCodeService.returnOneProjectCode(userSession, "applyDetail");
		//格式化客户编号之后再设置
		String projectBH = projectCode.getYears() + projectCodeFormat(projectCode.getApplyCode());
		pro_project.setProjectCode(projectBH);
		pro_project.setDelayBeginDate(pro_project.getLoadBeginDate());
		pro_project.setDelayEndDate(pro_project.getLoadEndDate());
		int[] monthDay = getMonthAndDay(pro_project.getLoadBeginDate(), pro_project.getDelayEndDate());
//		System.out.println("LoadBeginDate:"+pro_project.getLoadEndDate()+" LoadEndDate() "+pro_project.getLoadEndDate());
//		System.out.println(monthDay[0]+"个月"+monthDay[1]+"天");
		pro_project.setOldPeriodDay(monthDay[1]);
		pro_project.setOldPeriodMonth(monthDay[0]);
		pro_project.setPeriodDay(monthDay[1]);
		pro_project.setPeriodMonth(monthDay[0]);
		pro_project.setPeriodMonthDay(getMonthDayByMonthAndDay(monthDay[0],monthDay[1]));
		pro_project.setOldPeriodMonthDay(getMonthDayByMonthAndDay(monthDay[0],monthDay[1]));
		pro_project.setProject_ID(newProject_ID);//设置项目id
		pro_project.setApply_ID(newApply_ID);
		pro_project.setApplyDetail_ID(newApplyDetail_ID);
		pro_project.setUnit_uid(userSession.getUnit_uid());
		pro_project.setUnit_uidName(userSession.getUnit_uidName());
		pro_project.setGuarantySum(pro_project.getLoadSum());
		pro_project.setUpdateUserName(userSession.getUser_name());	
		pro_project.setBeforeAManID(pro_project.getAmanID());	//初始化移交前项目经理A角ID
		pro_project.setBeforeAManName(pro_project.getAmanName());	//初始化移交前项目经理A角名称
		pro_project.setBeforeBManID(pro_project.getBmanID());		//初始化移交前项目经理B角ID
		pro_project.setBeforeBManName(pro_project.getBmanName());	//初始化移交前项目经理B角名称
		pro_project.setGuarantorsName(pro_project.getGuarantyOrgName());//冗余担保机构
		
		/*根据业务品种ID, 查询业务字典, 取出所属的业务大类, 设置到applyDetail中  start*/
		C_busiSort busiSort = new C_busiSort();
		busiSort.setBusisortid(pro_project.getBusiTypeID());
		busiSort.setUnitUid(userSession.getUnit_uid());
		busiSort = busiSortService.selectOneBusiSortInfo(busiSort);
		pro_project.setBusiClass(busiSort.getBusiClass());
		/*根据业务品种ID, 查询业务字典, 取出所属的业务大类, 设置到applyDetail中  end*/
		pro_project.setIsCreditorMark(pro_project.getIsCreditorMark()==null ? 0 : pro_project.getIsCreditorMark());
		Integer returnInt = projectMapper.insertOneProjectInfo(pro_project);
		if(returnInt>0){
			Pro_apply apply=  new Pro_apply();
			//设置编号
			Pro_projectCode busiCode = new Pro_projectCode();
			busiCode=projectCodeService.returnOneProjectCode(userSession, "apply");
			//格式化客户编号之后再设置
			String busiCodeApply = busiCode.getYears() + projectCodeFormat(busiCode.getApplyCode());
			 apply.setProjectType("单笔");
			 apply.setBusiCode(busiCodeApply);
			 apply.setHostAreaID(pro_project.getHostAreaID());
			 apply.setHostAreaName(pro_project.getHostAreaName());//承办地
			 apply.setGuarantyOrgID(pro_project.getGuarantyOrgID());
			 apply.setGuarantyOrgName(pro_project.getGuarantyOrgName());//承办机构
			 apply.setAttributionID(pro_project.getAttributionID());
			 apply.setAttributionName(pro_project.getAttributionName());//属地划分
			 apply.setOprationCompanyID(pro_project.getOprationCompanyID());
			 apply.setOprationCompanyName(pro_project.getOprationCompanyName());//经办公司
			 apply.setAmanID(pro_project.getAmanID());//A角
			 apply.setAmanName(pro_project.getAmanName());
			 apply.setBmanID(pro_project.getBmanID());
			 apply.setBmanName(pro_project.getBmanName());//B角
			 apply.setBusiTypeNameList(pro_project.getBusiTypeName());//业务品种
			 apply.setBankNameList(pro_project.getBankName());//合作机构
			 apply.setBeforeAManID(pro_project.getAmanID());
			 apply.setBeforeBManID(pro_project.getBmanID());
			 apply.setFundType(pro_project.getFundType());//资金方类型
			 apply.setFundTypeID(pro_project.getFundTypeID());//资金方类型ID
			 apply.setFundName(pro_project.getFundName());//资金方名称
			 apply.setFundID(pro_project.getFundID());//资金方id
			 apply.setFundChinese(pro_project.getFundChinese());//资金方中文名称
			 apply.setApply_ID(newApply_ID);
			 apply.setApplySum(pro_project.getLoadSum());//设置申请金额;
			 apply.setAgreeSum(pro_project.getLoadSum());//设置同意金额;
			 apply.setClient_ID(pro_project.getClient_ID());
			 apply.setClientGUID(pro_project.getClientGUID());
			 apply.setClientName(pro_project.getClientName());
			 apply.setClientTypeID(pro_project.getClientTypeID());
			 apply.setProjectName(pro_project.getProjectName());
			 //Boolean returnInt2 = projectApplyService.insertOneApplyInfo(userSession, apply);
			
		    apply.setFundSource(pro_project.getFundSource());//资金来源
			apply.setUpdateUserName(userSession.getUser_name());//设置更新人名称;
			apply.setUnit_uid(userSession.getUnit_uid());//设置担保机构id
			apply.setUnit_uidName(userSession.getUnit_uidName());//设置担保机构名称;			
			if(pro_applyMapper.insertOneApplyInfo(apply) == 1){
				 logService.insertOneOperatorLogInfo(userSession,"申请登记", "新增", "业务申请信息表信息");
				 Pro_applyDetail applyDetail = new Pro_applyDetail();
				 applyDetail.setApplyDetail_ID(newApplyDetail_ID);
				 applyDetail.setApply_ID(newApply_ID);
				 applyDetail.setProjectType("单笔");
				 applyDetail.setProjectCode(projectBH);
				 applyDetail.setClient_ID(pro_project.getClient_ID());
				 applyDetail.setClientGUID(pro_project.getClientGUID());
				 applyDetail.setClientName(pro_project.getClientName());
				 applyDetail.setClientTypeID(pro_project.getClientTypeID());
				 applyDetail.setAmanID(pro_project.getAmanID());//A角
				 applyDetail.setAmanName(pro_project.getAmanName());
				 applyDetail.setBmanID(pro_project.getBmanID());
				 applyDetail.setBmanName(pro_project.getBmanName());//B角
				 applyDetail.setBusiTypeName(pro_project.getBusiTypeName());//业务品种
				 applyDetail.setBusiTypeID(pro_project.getBusiTypeID());
				 applyDetail.setBusiClass(pro_project.getBusiClass());//业务品种大类
				 applyDetail.setBankName(pro_project.getBankName());//合作机构
				 applyDetail.setBankID(pro_project.getBankID());
				 applyDetail.setSubBankName(pro_project.getSubBankName());//合作子机构
				 applyDetail.setBeforeAManID(pro_project.getAmanID());
				 applyDetail.setBeforeBManID(pro_project.getBmanID());
				 applyDetail.setPeriodDay(pro_project.getPeriodDay());
				 applyDetail.setPeriodMonth(pro_project.getPeriodMonth());
				 applyDetail.setPeriodMonthDay(pro_project.getPeriodMonthDay());
				 applyDetail.setUnit_uid(userSession.getUnit_uid());
				 applyDetail.setUnit_uidName(userSession.getUnit_uidName());
				 applyDetail.setUpdateUserName(userSession.getUser_name());	
				 applyDetail.setDcontractCode(pro_project.getDcontractCode());//委托担保合同号（委托贷款合同号）
				 applyDetail.setJcontractCode(pro_project.getJcontractCode());//借款合同号
				 applyDetail.setBcontractCode(pro_project.getBcontractCode());//保证合同号
				 applyDetail.setApplySum(pro_project.getLoadSum());//设置申请金额;
				 //applyDetailService.insertOneApplyDetailInfo(userSession, applyDetail);
				Integer insertResult = applyDetailMapper.insertOneApplyDetailInfo(applyDetail);//业务申请产品信息表				
				if(1 == insertResult){
				  logService.insertOneOperatorLogInfo(userSession,"申请登记", "新增", "业务申请产品信息表信息");					
			    }			
			}
			b=true;
		}
		return b;
	}
	
	@Override
	public Boolean updateOneProjectAfter(User userSession, Pro_project pro_project) {
		  
		C_busiSort busiSort = new C_busiSort();
		busiSort.setBusisortid(pro_project.getBusiTypeID());
		busiSort.setUnitUid(userSession.getUnit_uid());
		busiSort = busiSortService.selectOneBusiSortInfo(busiSort);
		//更新移交记录，以及项目原ab角
		Pro_project oldPro = projectMapper.selectOneProjectWhereSql(" and project_ID = '"+pro_project.getProject_ID()+"'");
		if( !pro_project.getAmanID().equals(oldPro.getAmanID()) || !pro_project.getBmanID().equals(oldPro.getBmanID())){//若更新时更新了项目的ab角
			//更新项目老ab角
			pro_project.setBeforeAManID(pro_project.getAmanID());
			pro_project.setBeforeAManName(pro_project.getAmanName());
			pro_project.setBeforeBManID(pro_project.getBmanID());
			pro_project.setBeforeBManName(pro_project.getBmanName());
			//若有移交记录删除移交记录
			changeRecMapper.deleteProjChangeRecByProject_ID(pro_project.getProject_ID());
		}
		pro_project.setBusiClass(busiSort.getBusiClass());
		
		//设置期限
		int[] monthDay = getMonthAndDay(pro_project.getLoadBeginDate(), pro_project.getDelayEndDate());
		pro_project.setPeriodMonth(monthDay[0]);
		pro_project.setPeriodDay(monthDay[1]);
		
		pro_project.setPeriodMonthDay(getMonthDayByMonthAndDay(monthDay[0],monthDay[1]));
		pro_project.setIsCreditorMark(pro_project.getIsCreditorMark()==null ? 0 : pro_project.getIsCreditorMark() );
		boolean updateResPro = this.updateOneProjectInfo(SystemSession.getUserSession(), pro_project);
		boolean updateResApply = projectApplyService.updateOneApplyByApply_ID(SystemSession.getUserSession(), pro_project);
		boolean updateResApplyDetail = applyDetailService.updateOneApplyDetailByApplyDetail_ID(SystemSession.getUserSession(), pro_project);	 
		if(updateResPro && updateResApply && updateResApplyDetail){
			return true;
		}
		return false;
	}
	
	//编号位数生成
		private String projectCodeFormat(Integer number) {
			String projectcodes="" + number;
			for(int i=0;projectcodes.length()<4;i++){
				projectcodes="0"+projectcodes;
			}
			return projectcodes;
		}
	
		
		
	//通过期限年和月获取年月
	public String getMonthDayByMonthAndDay(Integer periodMonth,Integer periodDay){
			String periodMonthDay = "";
			if(periodMonth!=null && periodMonth != 0){
				periodMonthDay +=periodMonth+"个月";
			}
			if(periodDay!=null){	
				periodMonthDay +=periodDay+"天";					
			}
			return periodMonthDay.toString();
	}
	/**
	 * 放款与解除
	 */
	@Override
	public PageTable<Pro_project> selectDutyRemovePage(PageTable<Pro_project> pageTable) {
		List<Pro_project> projectList=new ArrayList<>();
		try {
			projectList = projectMapper.selectDutyRemovePage(pageTable);
			for (Pro_project pro_project : projectList) {
				if(null != pro_project.getLoadEndDate()){
					pro_project.setNotYetEndDate(differentDaysByMillisecond(pro_project.getLoadEndDate(),new Date()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Long total=0L;
		try {
			total = projectMapper.selectDutyRemovePage_Count(pageTable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		pageTable.setRows(projectList);
		pageTable.setTotal(total);
		return pageTable;
	}
	/**
	 * 放款与解除--记录数
	 */
	@Override
	public Long selectDutyRemovePage_Count(PageTable<Pro_project> pageTable) {
		return projectMapper.selectProjectPageTables_Count(pageTable);
	}
	/**
	 * 担保责任确认
	 */
	@Override
	public Boolean updateDutyRemove(User user, Pro_project project) {
		String project_ID = project.getProject_ID();
		
		Pro_project project1 = projectMapper.selectOneProjectWhereSql(" and project_ID = \'"+project_ID+"\'");
		
		Pro_factPay factPay = new Pro_factPay();//业务还款信息表pro_factPay
		Pro_replace replace = new Pro_replace();//业务代偿信息表pro_replace
		
		String unit_uid = user.getUnit_uid();
		String updateUserName = user.getUser_name();
		//担保责任解除方式
		String freeMethodID = project.getFreeMethodID();
		String freeTypeID = project.getFreeTypeID();
		String freeTypeName = project.getFreeTypeName();
		Double guarantyScale = project1.getGuarantyScale() == null ? 0D :project1.getGuarantyScale();//担保责任比例
		String apply_ID = project.getApply_ID();
		
		Date freeDate = project.getFreeDate();
		
		Double guarantyDutyResSum = project.getGuarantyDutyResSum() == null ? 0D :project.getGuarantyDutyResSum();//担保责任余金额
		Double guarantySum = project.getGuarantySum() == null ? 0D :project.getGuarantySum();//在保余额
		
		if(guarantySum==0){
			Pro_project project2 = new Pro_project();
			project2.setProject_ID(project_ID);
			project2.setIsFree(1);
			try {
				Integer count = projectMapper.updateDutyRemove(project2);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return false;
		}
		
		Double normalFreeSum1 = project1.getNormalFreeSum() == null ? 0D :project1.getNormalFreeSum();//还款总金额
		Double normalCapitalSum1 = project1.getNormalCapitalSum() == null ? 0D :project1.getNormalCapitalSum();//其中：还款本金
		Double normalInterestSum1 = project1.getNormalInterestSum() == null ? 0D :project1.getNormalInterestSum();//其中：还款利息
		
		Double normalFreeSum = 0.0;//还款总金额
		Double normalCapitalSum = 0.0;//其中：还款本金
		Double normalInterestSum = 0.0;//其中：还款利息
		
		Double	replaceFreeSum = 0.0;     //	代偿总金额
		Double	replaceCapitalSum = 0.0;  //分类1其中：代偿本金
		Double	replaceInterestSum = 0.0; //分类1其中：代偿利息
		Double	replaceOtherSum = 0.0;    //分类1其中：代偿其它
		Double	selfReplaceSum = 0.0;     //分类2其中：自有资金代偿
		Double	dangerReplaceSum = 0.0;   //分类2其中：准备金冲抵
		
		Double	replaceFreeSum1 = project1.getReplaceFreeSum() == null ? 0D :project1.getReplaceFreeSum();        //	代偿总金额   
		Double	replaceCapitalSum1 = project1.getReplaceCapitalSum() == null ? 0D :project1.getReplaceCapitalSum();  //分类1其中：代偿本金  
		Double	replaceInterestSum1 = project1.getReplaceInterestSum() == null ? 0D :project1.getReplaceInterestSum();//分类1其中：代偿利息  
		Double	replaceOtherSum1 = project1.getReplaceOtherSum() == null ? 0D :project1.getReplaceOtherSum();      //分类1其中：代偿其它  
		Double	selfReplaceSum1 = project1.getSelfReplaceSum() == null ? 0D :project1.getSelfReplaceSum();        //分类2其中：自有资金代偿
		Double	dangerReplaceSum1 = project1.getDangerReplaceSum() == null ? 0D :project1.getDangerReplaceSum();    //分类2其中：准备金冲抵 
		
		Integer count = 0;
		Boolean isTrue = false;
		try {
			if("3d08a203ab804ddf80e8d3c827bd98ae".equals(freeMethodID)){//无代偿解除  向pro_factPay插入一条数据
				normalFreeSum = project.getNormalFreeSum() == null ? 0D :project.getNormalFreeSum();
				normalCapitalSum = project.getNormalCapitalSum() == null ? 0D :project.getNormalCapitalSum();
				normalInterestSum = project.getNormalInterestSum() == null ? 0D :project.getNormalInterestSum();
				
				factPay.setFactPay_ID(Tool.createUUID32());
				factPay.setApplyID(apply_ID);
				factPay.setProject_ID(project_ID);
				factPay.setUnit_uid(unit_uid);
				factPay.setUpdateUserName(updateUserName);
				factPay.setPayDate(freeDate);
				
				factPay.setFreeTypeID(freeTypeID);
				factPay.setFreeTypeName(freeTypeName);
				
				factPay.setPaySum(normalFreeSum);
				factPay.setPayCapitalSum(normalCapitalSum);
				factPay.setPayInterestSum(normalInterestSum);
				
				
				guarantySum = bigDecimal.sub(guarantySum, normalCapitalSum);
				Double normalCapitalSum2 = 0D;
				if(guarantyScale != null){
					normalCapitalSum2 = bigDecimal.mul(normalCapitalSum, guarantyScale)/100;
				}
				
				guarantyDutyResSum = bigDecimal.sub(guarantyDutyResSum, normalCapitalSum2);
				factPayMapper.insertOneFactPayInfo(factPay);
				
				
			}else{//代偿解除  向pro_replace插入一条数据
				replaceFreeSum = project.getReplaceFreeSum() == null ? 0D :project.getReplaceFreeSum();
				replaceCapitalSum = project.getReplaceCapitalSum() == null ? 0D :project.getReplaceCapitalSum();
				replaceInterestSum = project.getReplaceInterestSum() == null ? 0D :project.getReplaceInterestSum();
				replaceOtherSum = project.getReplaceOtherSum() == null ? 0D :project.getReplaceOtherSum();
				selfReplaceSum = project.getSelfReplaceSum() == null ? 0D :project.getSelfReplaceSum();
				dangerReplaceSum = project.getDangerReplaceSum() == null ? 0D :project.getDangerReplaceSum();
				
				replace.setReplace_ID(Tool.createUUID32());
				replace.setApply_ID(apply_ID);
				replace.setProject_ID(project_ID);
				replace.setUnit_uid(unit_uid);
				replace.setUpdateUserName(updateUserName);
				replace.setReplaceDate(freeDate);
				replace.setReplaceSum(replaceFreeSum);
				replace.setReplaceCapitalSum(replaceCapitalSum);
				replace.setReplaceInterestSum(replaceInterestSum);
				replace.setReplaceOtherSum(replaceOtherSum);
				replace.setSelfReplaceSum(selfReplaceSum);
				replace.setDangerReplaceSum(dangerReplaceSum);
				
				replace.setRepalceState("通过");
				
				guarantyDutyResSum = bigDecimal.sub(guarantyDutyResSum, replaceCapitalSum);
				
				Double replaceCapitalSum2 = 0D;
				if(guarantyScale != null && guarantyScale != 0){
					replaceCapitalSum2 = bigDecimal.mul(replaceCapitalSum, 100D)/guarantyScale;
				}
				
				
				guarantySum = bigDecimal.sub(guarantySum, replaceCapitalSum2);
				replaceMapper.insertOneReplaceInfo(replace);
			}
			
			
			
			project.setNormalFreeSum(bigDecimal.add(normalFreeSum, normalFreeSum1));
			project.setNormalCapitalSum(bigDecimal.add(normalCapitalSum, normalCapitalSum1));
			project.setNormalInterestSum(bigDecimal.add(normalInterestSum, normalInterestSum1));
			project.setReplaceFreeSum(bigDecimal.add(replaceFreeSum, replaceFreeSum1));
			project.setReplaceCapitalSum(bigDecimal.add(replaceCapitalSum, replaceCapitalSum1));
			project.setReplaceInterestSum(bigDecimal.add(replaceInterestSum, replaceInterestSum1));
			project.setReplaceOtherSum(bigDecimal.add(replaceOtherSum, replaceOtherSum1));
			project.setSelfReplaceSum(bigDecimal.add(selfReplaceSum, selfReplaceSum1));
			project.setDangerReplaceSum(bigDecimal.add(dangerReplaceSum, dangerReplaceSum1));
			
			
			project.setGuarantyDutyResSum(guarantyDutyResSum);
			project.setGuarantySum(guarantySum);
			
			project.setIsFree(1);
			
			
			//更新项目表
			count = projectMapper.updateDutyRemove(project);
			
		} catch (Exception e) {
			count = 0;
			e.printStackTrace();
		}
		
		if(count>0){
			isTrue=true;
			logService.insertOneOperatorLogInfo(user,"担保责任解除", "担保责任解除确认", "保后（贷后）产品放款明细表");
		}
		return isTrue;
	}
	
	
	/**
	 * 删除一个保后项目
	 * @param userSession
	 * @param pro_project
	 * @return
	 */
	public Boolean deleteOneProjectAfter(User user, Pro_project pro_project) {
		
		Boolean  b = false;
		String whereSql = " and project_ID =\'"+pro_project.getProject_ID()+"\'";
		//删除与保后相关表信息---start
  		 
	  	
		
		
		//删除与保后相关表信息---end
		Integer returnResult = projectMapper.deleteOneProjectAfterBySql(whereSql);
		if(returnResult== 1){
			logService.insertOneOperatorLogInfo(user,"保后项目", "删除", "删除一条保后产品信息表信息项目名称="+pro_project.getProjectName());
			b= true;
		}			
		return  b;
	}
	
	/**
	 * 查询多个保后项目
	 */
	public List<Pro_project> selectProjectListByWheresql(String WhereSql) {
		 List<Pro_project> projectList = projectMapper.selectProjectListByWheresql(WhereSql);
		return projectList;
	}
	
	/**
	 * 按金额查询本年在保金额
	 */
	public EChart guarantySumYearBySum(User user) {
		List<EChart> eList = new ArrayList<>();
		EChart eChart = new EChart();
		String wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1  and YEAR(loadBeginDate) = YEAR(NOW()) and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and guarantySum < 100";
		Pro_project project = projectMapper.selectProjectDataWhereSql(wheresql);
	    /**
	     *按年统计
	     */
		EChart pEChart = new EChart();
		pEChart.setName("100万以下");
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		pEChart = new EChart();
		pEChart.setName("100万(含)~300万");
		wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1  and YEAR(loadBeginDate) = YEAR(NOW()) and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and guarantySum >= 100 and guarantySum <= 300";
		project = projectMapper.selectProjectDataWhereSql(wheresql);
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		pEChart = new EChart();
		pEChart.setName("300万(含)~500万");
		wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1  and YEAR(loadBeginDate) = YEAR(NOW()) and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and guarantySum >= 300 and guarantySum <= 500";
		project = projectMapper.selectProjectDataWhereSql(wheresql);
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		pEChart = new EChart();
		pEChart.setName("500万(含)~1000万");
		wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1  and YEAR(loadBeginDate) = YEAR(NOW()) and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and guarantySum >= 500 and guarantySum <= 1000";
		project = projectMapper.selectProjectDataWhereSql(wheresql);
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		pEChart = new EChart();
		pEChart.setName("1000万(含)以上");
		wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1  and YEAR(loadBeginDate) = YEAR(NOW()) and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and guarantySum >= 1000";
		project = projectMapper.selectProjectDataWhereSql(wheresql);
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		eChart.setEchartList(eList);
		String[] nameStr = {"100万以下","100万(含)~300万","300万(含)~500万","500万(含)~1000万","1000万(含)以上"};
		eChart.setNameStr(nameStr);
		return eChart;
	}
	
	
	/**
	 * 按金额查询累计在保金额
	 */
	public EChart guarantySumBySum(User user) {
		List<EChart> eList = new ArrayList<>();
		EChart eChart = new EChart();
		String wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1 and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and guarantySum < 100";
		Pro_project project = projectMapper.selectProjectDataWhereSql(wheresql);
		/**
		 *按年统计
		 */
		EChart pEChart = new EChart();
		pEChart.setName("100万以下");
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		pEChart = new EChart();
		pEChart.setName("100万(含)~300万");
		wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1 and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and guarantySum >= 100 and guarantySum <= 300";
		project = projectMapper.selectProjectDataWhereSql(wheresql);
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		pEChart = new EChart();
		pEChart.setName("300万(含)~500万");
		wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1 and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and guarantySum >= 300 and guarantySum <= 500";
		project = projectMapper.selectProjectDataWhereSql(wheresql);
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		pEChart = new EChart();
		pEChart.setName("500万(含)~1000万");
		wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1 and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and guarantySum >= 500 and guarantySum <= 1000";
		project = projectMapper.selectProjectDataWhereSql(wheresql);
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		pEChart = new EChart();
		pEChart.setName("1000万(含)以上");
		wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1 and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and guarantySum >= 1000";
		project = projectMapper.selectProjectDataWhereSql(wheresql);
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		eChart.setEchartList(eList);
		String[] nameStr = {"100万以下","100万(含)~300万","300万(含)~500万","500万(含)~1000万","1000万(含)以上"};
		eChart.setNameStr(nameStr);
		return eChart;
	}
	
	/**
	 * 按期限查询本年在保金额
	 */
	public EChart guarantySumYearByPeriod(User user) {
		List<EChart> eList = new ArrayList<>();
		EChart eChart = new EChart();
		String wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1  and YEAR(loadBeginDate) = YEAR(NOW()) and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and periodMonth < 3";
		Pro_project project = projectMapper.selectProjectDataWhereSql(wheresql);
	    /**
	     *按年统计
	     */
		EChart pEChart = new EChart();
		pEChart.setName("3个月以下");
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		pEChart = new EChart();
		pEChart.setName("3个月(含)~6个月");
		wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1  and YEAR(loadBeginDate) = YEAR(NOW()) and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and periodMonth >= 3 and periodMonth < 6";
		project = projectMapper.selectProjectDataWhereSql(wheresql);
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		pEChart = new EChart();
		pEChart.setName("6个月(含)~12个月");
		wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1  and YEAR(loadBeginDate) = YEAR(NOW()) and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and periodMonth >= 6 and periodMonth <= 12";
		project = projectMapper.selectProjectDataWhereSql(wheresql);
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		pEChart = new EChart();
		pEChart.setName("12个月(含)以上");
		wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1  and YEAR(loadBeginDate) = YEAR(NOW()) and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and periodMonth >= 12";
		project = projectMapper.selectProjectDataWhereSql(wheresql);
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		eChart.setEchartList(eList);
		String[] nameStr = {"3个月以下","3个月(含)~6个月","6个月(含)~12个月","12个月(含)以上"};
		eChart.setNameStr(nameStr);
		return eChart;
	}
	
	/**
	 * 按期限查询在保金额
	 */
	public EChart guarantySumByPeriod(User user) {
		List<EChart> eList = new ArrayList<>();
		EChart eChart = new EChart();
		String wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1 and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and periodMonth < 3";
		Pro_project project = projectMapper.selectProjectDataWhereSql(wheresql);
		/**
		 *按年统计
		 */
		EChart pEChart = new EChart();
		pEChart.setName("3个月以下");
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		pEChart = new EChart();
		pEChart.setName("3个月(含)~6个月");
		wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1 and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and periodMonth >= 3 and periodMonth < 6";
		project = projectMapper.selectProjectDataWhereSql(wheresql);
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		pEChart = new EChart();
		pEChart.setName("6个月(含)~12个月");
		wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1 and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and periodMonth >= 6 and periodMonth <= 12";
		project = projectMapper.selectProjectDataWhereSql(wheresql);
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		pEChart = new EChart();
		pEChart.setName("12个月(含)以上");
		wheresql =" SELECT IFNULL(sum(guarantySum), 0) AS guarantySum FROM pro_project  where 1=1 and unit_uid  = \'" + user.getUnit_uid() +"\'";
		wheresql = wheresql + " and periodMonth >= 12";
		project = projectMapper.selectProjectDataWhereSql(wheresql);
		pEChart.setValue(String.valueOf(project.getGuarantySum()));
		eList.add(pEChart);
		eChart.setEchartList(eList);
		String[] nameStr = {"3个月以下","3个月(含)~6个月","6个月(含)~12个月","12个月(含)以上"};
		eChart.setNameStr(nameStr);
		return eChart;
	}
	/**
	 *核销损失
	 */
	public Boolean projectFinishLoss(User user, Pro_project pro_project) {
		Integer returnInt = 0; 
		Boolean  b =false;
		pro_project.setBadDate(new Date());
		pro_project.setBadUserID(user.getUser_id());//核销人id
		pro_project.setBadUserName(user.getUser_name());//核销人姓名
		returnInt = projectMapper.updateOneProjectInfo(pro_project);
		if(returnInt>0){
			logService.insertOneOperatorLogInfo(user,"项目完结-核销损失", "修改", "保后产品信息表ID ="+pro_project.getProject_ID());
			b = true;
		}
		return b;
	}
	/**
	 * 项目完结
	 * @throws Exception 
	 */
	public Boolean projectFinishRegister(User user, Pro_project pro_project) throws Exception {
		Integer returnInt = 0; 
		Boolean  b =false;
		//项目完结状态由子流程最终控制
		//pro_project.setIsFree(1);
		returnInt = projectMapper.updateOneProjectInfo(pro_project);
		com.zjm.common.db.model.User usr = SystemSession.getUserSession();
		
		if(returnInt>0){
			Pro_project new_pro = projectMapper.selectOneProjectWhereSql(" and project_ID = \'"+pro_project.getProject_ID()+"\'");
			if (null != new_pro) {
				Pro_finish finish = new Pro_finish();
				//检测数据库中是否有当前项目的审批中子流程
				Map<String, Object> map = new HashMap<>();
				map.put("applyId",new_pro.getApply_ID());
				map.put("finishstate", "审批中");
				List<Pro_finish> finishes = pro_finishMapper.getPageListByMap(map);
				if (null != finishes && finishes.size() > 0) {
					throw new Exception("当前项目存在未完成完结解保子流程");
				}
				//项目保证金
				Double guaranteeSum = optGuarantyMapper.selectGuarantySumByWheresql(" and apply_ID = '"+new_pro.getApply_ID()
						+"' and guarantyTypeID = 'e2e83da45eea46cba8c6776c5736c78b' and (isFree = 0 or isFree is null)");
				if (null != guaranteeSum) {
					finish.setMargin(new BigDecimal(guaranteeSum));
				}
				finish.setFinishId(Tool.createUUID32());
				finish.setProjectId(new_pro.getProject_ID());
				finish.setApplyId(new_pro.getApply_ID());
				finish.setFinishstate("审批中");
				finish.setOperationmanid(usr.getUser_id());
				finish.setOperationmanname(usr.getUser_name());
				finish.setUnitUid(usr.getUnit_uid());
				finish.setUpdatedatetime(new Date());
				finish.setUpdateusername(usr.getUser_name());
				
				if (1l == pro_finishMapper.insert(finish)) {
					OsGworkflowFlowtemplate flow = new OsGworkflowFlowtemplate();
					flow.setUnit_uid(user.getUnit_uid());
					flow.setUser_uid(user.getUser_uid());
					flow.setProjectID(finish.getApplyId());
					flow.setEntityName("完结解保");

					flow.setBusinessId(finish.getFinishId());
					flow.setBusinessType(finish.getClass().getName());

					flow.setFlowTemplateName("委贷完结解除子流程");
					flow.setFlowTemplateID("G201");
					flow.setFlowType("02");
					Long startResult = gworkFlowService.createworkflowInstance(flow);
					if (startResult == 0) {
						throw new RuntimeException("流程模板配置有误");
					}
					
					logService.insertOneOperatorLogInfo(user,"完结解保", "新增", "完结解保子流程表ID ="+finish.getFinishId());	
				}
			}
			logService.insertOneOperatorLogInfo(user,"项目完结", "修改", "保后产品信息表ID ="+pro_project.getProject_ID());	
			b = true;
		}
		return b;
	}

	/**
	 * 查询重点项目关联系的分页列表
	 */
	@SuppressWarnings("static-access")
	@Override
	public PageTable<Pro_projectForProVo> selectMultiProjectPageTable(PageTable<Pro_projectForProVo> pageTable) {
		try {
			List<Pro_projectForProVo> volist = relationMainMapper.selectKeyProjectRelationMain(pageTable);
			// 封装数据
			/*for (Pro_projectForProVo proVo : volist) {
				Double capitalSum = 0.0;		//总资产
				Double liabilitySum = 0.0;     	//负债总额
				String mainClient_ID = proVo.getClientID();		//主体客户ID, 历史导入的数据有可能为空
				if(mainClient_ID!=null && !"".equals(mainClient_ID)){
					Client mainClient = clientService.selectOneClientByWheresql(" and client_ID='"+mainClient_ID+"'");
					capitalSum += (mainClient.getCapitalSum()==null?0:mainClient.getCapitalSum());
					liabilitySum += (mainClient.getLiabilitySum()==null?0:mainClient.getLiabilitySum());
				}
				List<Crm_client_relationMain> cmlist = proVo.getCmlist();
				if(cmlist!=null){
					for (Crm_client_relationMain ccr : cmlist) {
						String relationClient_ID = ccr.getClient_ID(); 	//关联企业ID
						if(relationClient_ID.equals(mainClient_ID)){	//这一步判断是防止出现主体客户也在关联企业列表中时重复计算的问题
							break;
						}else{
							Client relationClient = clientService.selectOneClientByWheresql(" and client_ID='"+relationClient_ID+"'");
							capitalSum += (relationClient.getCapitalSum()==null?0:relationClient.getCapitalSum());
							liabilitySum += (relationClient.getLiabilitySum()==null?0:relationClient.getLiabilitySum());
						}
					}
				}
				proVo.setCapitalSum(capitalSum);
				proVo.setLiabilitySum(liabilitySum);
			}*/
			Double v2 = 10000.0;		//金额单位,万元转换成亿元, 除以10000
			String sql="";
			sql =	"SELECT "+ 
					"	cr.relationMain_ID, "+
					"	SUM(IFNULL(cc.capitalSum,0)) capitalSum, "+			// 资产总额
					"	SUM(IFNULL(cc.liabilitySum,0)) liabilitySum, "+		// 负债总额
					"	SUM(IFNULL(cc.creditorGuarSum,0)) creditorGuarSum, "+	// 敏感债权人在保金额 
					"	SUM(IFNULL(cc.timePointSum,0)) timePointSum, "+		//2015年1月末担保余额(最高时点金额)
					"	SUM(IFNULL(cc.timePointEntrustSum,0)) timePointEntrustSum "+	//2015年1月末担保集团委贷余额(最高时点金额(委贷))
					"FROM crm_relationmain cr "+
					"LEFT JOIN crm_client_relationmain ccr ON cr.relationmain_id = ccr.relationmain_ID "+
					"LEFT JOIN crm_client cc ON cc.client_id=ccr.client_id "+
					"GROUP BY cr.relationmain_ID";
			HashMap<String, HashMap<String, Object>> clientDataMap = projectMapper.selectMapBySql(sql);
			
			//融投系2015年1月末担保集团委贷余额
			sql =   "SELECT  "+
				    " 	cr.relationMain_ID,  "+
				    "	SUM(IFNULL(tab3.guarantySum,0))  rtTimePointEntrustSum "+
					"FROM  "+
					"	crm_relationmain cr  "+
					"LEFT JOIN crm_client_relationmain ccr ON cr.relationMain_ID = ccr.relationMain_ID  "+
					"LEFT JOIN crm_client cc ON cc.client_id = ccr.client_id "+
					"LEFT JOIN pro_apply pa on pa.clientGUID = cc.clientGUID "+
					"LEFT JOIN "+
					"(SELECT "+
					"		pp.apply_ID, "+
					"		pp.project_ID, "+
					"		pp.delayBeginDate, "+
					"		IFNULL(pp.loadSum,0)-IFNULL(tab1.payCapitalSum,0)-IFNULL(tab2.replaceCapitalSum,0) AS guarantySum "+
					"	FROM pro_project pp "+
					"	LEFT JOIN ( "+
					"	SELECT "+
					"			pfp.project_ID, "+
					"			SUM(IFNULL(pfp.payCapitalSum,0)) AS payCapitalSum   "+       
					"		FROM "+
					"			pro_factpay pfp "+
					"		WHERE  DATE_FORMAT(pfp.payDate,'%Y-%m')<'2015-02-01' "+
					/*"		WHERE  DATE_FORMAT(pfp.factDate,'%Y-%m')<'2015-02-01' "+*/
					"		GROUP BY pfp.project_ID "+
					"	)tab1 ON tab1.project_ID =  pp.project_ID "+
					"	LEFT JOIN ( "+
					"		SELECT "+
					"			pre.project_ID,  "+      
					"			SUM(IFNULL(pre.replaceCapitalSum,0)) AS replaceCapitalSum "+         
					"		FROM "+
					"			pro_replace pre "+
					"		WHERE DATE_FORMAT(pre.replaceDate,'%Y-%m')<'2015-02-01' "+
					"		GROUP BY pre.project_ID "+
					"	)tab2 ON tab2.project_ID =  pp.project_ID "+
					"	WHERE	pp.busiClass= '02' "+
					"	and DATE_FORMAT(pp.delayBeginDate,'%Y-%m-%d')<'2015-02-01' "+
					") tab3 on tab3.apply_ID = pa.apply_ID "+
					"GROUP BY cr.relationMain_ID ";
			HashMap<String, HashMap<String, Object>> rt_timePointEntrustSumMap = projectMapper.selectMapBySql(sql);
			
			// 在保余额
			sql =	"SELECT "+ 
					"	cr.relationMain_ID, "+
					"	SUM(IFNULL(pp.guarantySum,0)) guaReSum_zaibao "+
					"FROM crm_relationmain cr "+
					"LEFT JOIN crm_client_relationmain ccr ON cr.relationmain_id = ccr.relationmain_ID "+
					"LEFT JOIN crm_client cc ON cc.client_id=ccr.client_id "+
					"LEFT JOIN pro_apply pa ON pa.clientGUID=cc.clientGUID "+
					"LEFT JOIN pro_project pp ON pa.apply_ID=pp.apply_ID "+
					"GROUP BY cr.relationmain_ID";		
			HashMap<String, HashMap<String, Object>> guaReSum_zaibaoMap = projectMapper.selectMapBySql(sql);
			// 委贷余额--担保集团担保
			sql =	"SELECT "+ 
					"	cr.relationMain_ID, "+
					"	SUM(IFNULL(pp.guarantySum,0)) entReSum_gua "+
					"FROM crm_relationmain cr "+
					"LEFT JOIN crm_client_relationmain ccr ON cr.relationmain_id = ccr.relationmain_ID "+
					"LEFT JOIN crm_client cc ON cc.client_id=ccr.client_id "+
					"LEFT JOIN pro_apply pa ON pa.clientGUID=cc.clientGUID "+
					"LEFT JOIN pro_project pp ON pa.apply_ID=pp.apply_ID "+
					"WHERE pp.busiClass='02' AND pp.isGuaranty=1 "+
					"GROUP BY cr.relationmain_ID";		
			HashMap<String, HashMap<String, Object>> entReSum_guaMap = projectMapper.selectMapBySql(sql);
			// 委贷余额--没有担保集团担保
			sql =	"SELECT "+ 
					"	cr.relationMain_ID, "+
					"	SUM(IFNULL(pp.guarantySum,0)) entReSum_noGua "+
					"FROM crm_relationmain cr "+
					"LEFT JOIN crm_client_relationmain ccr ON cr.relationmain_id = ccr.relationmain_ID "+
					"LEFT JOIN crm_client cc ON cc.client_id=ccr.client_id "+
					"LEFT JOIN pro_apply pa ON pa.clientGUID=cc.clientGUID "+
					"LEFT JOIN pro_project pp ON pa.apply_ID=pp.apply_ID "+
					"WHERE pp.busiClass='02' AND pp.isGuaranty=0 "+
					"GROUP BY cr.relationmain_ID";		
			HashMap<String, HashMap<String, Object>> entReSum_noGuaMap = projectMapper.selectMapBySql(sql);
			// 代偿余额
			sql =	"SELECT "+ 
					"	cr.relationMain_ID, "+
					"	SUM(IFNULL(pre.replaceSum,0))-SUM(IFNULL(prd.returnSum,0)) replaceReSum "+
					"FROM crm_relationmain cr "+
					"LEFT JOIN crm_client_relationmain ccr ON cr.relationmain_id = ccr.relationmain_ID "+
					"LEFT JOIN crm_client cc ON cc.client_id=ccr.client_id "+
					"LEFT JOIN pro_apply pa ON pa.clientGUID=cc.clientGUID "+
					"LEFT JOIN pro_project pp ON pa.apply_ID=pp.apply_ID "+
					"LEFT JOIN pro_replace pre ON pp.project_ID=pre.project_ID "+
					"LEFT JOIN pro_returndetail prd ON pp.project_ID=prd.projectID "+
					"WHERE pp.busiClass='01' "+
					"GROUP BY cr.relationmain_ID";		
			HashMap<String, HashMap<String, Object>> replaceReSumMap = projectMapper.selectMapBySql(sql);
			// 委贷还款本金和利息
			sql =	"SELECT "+ 
					"	cr.relationMain_ID, "+
					"	SUM(IFNULL(pfp.payCapitalSum,0))+SUM(IFNULL(pfp.payInterestSum,0)) ent_PaySum "+
					"FROM crm_relationmain cr "+
					"LEFT JOIN crm_client_relationmain ccr ON cr.relationmain_id = ccr.relationmain_ID "+
					"LEFT JOIN crm_client cc ON cc.client_id=ccr.client_id "+
					"LEFT JOIN pro_apply pa ON pa.clientGUID=cc.clientGUID "+
					"LEFT JOIN pro_project pp ON pa.apply_ID=pp.apply_ID "+
					"LEFT JOIN pro_factpay pfp ON pp.project_ID=pfp.project_ID "+
					"WHERE pp.busiClass='02' "+
					"GROUP BY cr.relationmain_ID";		
			HashMap<String, HashMap<String, Object>> collectDebtsSum_rtMap1 = projectMapper.selectMapBySql(sql);
			// 追偿金额
			sql =	"SELECT "+ 
					"	cr.relationMain_ID, "+
					"	SUM(IFNULL(prd.returnSum,0)) returnSum "+
					"FROM crm_relationmain cr "+
					"LEFT JOIN crm_client_relationmain ccr ON cr.relationmain_id = ccr.relationmain_ID "+
					"LEFT JOIN crm_client cc ON cc.client_id=ccr.client_id "+
					"LEFT JOIN pro_apply pa ON pa.clientGUID=cc.clientGUID "+
					"LEFT JOIN pro_project pp ON pa.apply_ID=pp.apply_ID "+
					"LEFT JOIN pro_returndetail prd ON pp.project_ID=prd.projectID "+
					"WHERE pp.busiClass='01' "+
					"GROUP BY cr.relationmain_ID";		
			HashMap<String, HashMap<String, Object>> collectDebtsSum_rtMap2 = projectMapper.selectMapBySql(sql);
			// 担保费
			sql =	"SELECT "+ 
					"	cr.relationMain_ID, "+
					"	SUM(IFNULL(pcf.factCostSum,0)) factCostSum "+
					"FROM crm_relationmain cr "+
					"LEFT JOIN crm_client_relationmain ccr ON cr.relationmain_id = ccr.relationmain_ID "+
					"LEFT JOIN crm_client cc ON cc.client_id=ccr.client_id "+
					"LEFT JOIN pro_apply pa ON pa.clientGUID=cc.clientGUID "+
					"LEFT JOIN pro_costfact pcf ON pcf.apply_ID=pa.apply_ID "+
					"WHERE pcf.costTypeID='f0bd2361808d4da9980f18629c236d5c' "+
					"GROUP BY cr.relationmain_ID";		
			HashMap<String, HashMap<String, Object>> collectDebtsSum_rtMap3 = projectMapper.selectMapBySql(sql);
			
			for (Pro_projectForProVo proVo : volist) {
				//总资产
				proVo.setCapitalSum(bigDecimal.div(Double.parseDouble(clientDataMap.get(proVo.getRelationMain_ID()).get("capitalSum").toString()),v2));
				//负债总额
				proVo.setLiabilitySum(bigDecimal.div(Double.parseDouble(clientDataMap.get(proVo.getRelationMain_ID()).get("liabilitySum").toString()),v2));
				//敏感债权人在保金额 
				proVo.setCreditorSum(bigDecimal.div(Double.parseDouble(clientDataMap.get(proVo.getRelationMain_ID()).get("creditorGuarSum").toString()),v2));
				//2015年1月末担保余额(最高时点金额)
				proVo.setGuarantySum(bigDecimal.div(Double.parseDouble(clientDataMap.get(proVo.getRelationMain_ID()).get("timePointSum").toString()),v2));
				//2015年1月末担保集团委贷余额(最高时点金额(委贷))
				proVo.setGuarantyEntrustSum(bigDecimal.div(Double.parseDouble(clientDataMap.get(proVo.getRelationMain_ID()).get("timePointEntrustSum").toString()),v2));
				//2015年1月末融投系委贷余额(融投系最高时点金额(委贷))
				proVo.setEntrustSum(bigDecimal.div(Double.parseDouble(rt_timePointEntrustSumMap.get(proVo.getRelationMain_ID()).get("rtTimePointEntrustSum").toString()),v2));
				//在保余额
				proVo.setGuaReSum_zaibao(bigDecimal.div(Double.parseDouble(guaReSum_zaibaoMap.get(proVo.getRelationMain_ID()).get("guaReSum_zaibao").toString()),v2));
				//委贷余额_有担保集团担保的
				if(entReSum_guaMap.get(proVo.getRelationMain_ID())!=null){
					double entReSum_gua = bigDecimal.div(Double.parseDouble(entReSum_guaMap.get(proVo.getRelationMain_ID()).get("entReSum_gua").toString()),v2);
					proVo.setEntReSum_gua(entReSum_gua);
					proVo.setEntReSum_guaGroup(entReSum_gua);	//担保集团委贷余额, 目前与 委贷余额-担保 取值相同 
				}
				//委贷余额_没有担保集团担保的
				if(entReSum_noGuaMap.get(proVo.getRelationMain_ID())!=null){
					proVo.setEntReSum_noGua(bigDecimal.div(Double.parseDouble(entReSum_noGuaMap.get(proVo.getRelationMain_ID()).get("entReSum_noGua").toString()),v2));
				}
				//融投系业务合计_有担保集团担保的
				proVo.setTotalSum_gua_rt(proVo.getEntReSum_gua());	
				//融投系业务合计_没有担保集团担保的
				proVo.setTotalSum_noGua_rt(proVo.getEntReSum_noGua());
				if(replaceReSumMap.get(proVo.getRelationMain_ID())!=null){
					proVo.setReplaceReSum(bigDecimal.div(Double.parseDouble(replaceReSumMap.get(proVo.getRelationMain_ID()).get("replaceReSum").toString()),v2));
				}
				//融投系清收清欠金额	= (委贷清收本金 + 委贷清收利息) + 担保的追偿金额 + 保费清收
				Double ent_PaySum = 0.0;	// 委贷项目还款本金+利息
				if(collectDebtsSum_rtMap1.get(proVo.getRelationMain_ID())!=null){
					ent_PaySum = bigDecimal.div(Double.parseDouble(collectDebtsSum_rtMap1.get(proVo.getRelationMain_ID()).get("ent_PaySum").toString()),v2);
				}
				Double returnSum = 0.0;		// 担保项目追偿金额
				if(collectDebtsSum_rtMap2.get(proVo.getRelationMain_ID())!=null){
					returnSum = bigDecimal.div(Double.parseDouble(collectDebtsSum_rtMap2.get(proVo.getRelationMain_ID()).get("returnSum").toString()),v2);
				}
				Double factCostSum = 0.0;		// 担保费
				if(collectDebtsSum_rtMap3.get(proVo.getRelationMain_ID())!=null){
					factCostSum = bigDecimal.div(Double.parseDouble(collectDebtsSum_rtMap3.get(proVo.getRelationMain_ID()).get("factCostSum").toString()),v2);
				}
				Double collectDebtsSum_rt = bigDecimal.add(bigDecimal.add(ent_PaySum,returnSum),factCostSum);
				proVo.setCollectDebtsSum_rt(collectDebtsSum_rt);
				//融投系合计
				Double totalSum_rt = bigDecimal.add(bigDecimal.add(proVo.getGuaReSum_zaibao()==null?0:proVo.getGuaReSum_zaibao(), proVo.getEntReSum_noGua()==null?0:proVo.getEntReSum_noGua()),proVo.getReplaceReSum()==null?0:proVo.getReplaceReSum());
				proVo.setTotalSum_rt(totalSum_rt);
				//项目总共化解金额
				proVo.setDeRiskSum(bigDecimal.add(totalSum_rt,proVo.getCreditorSum()));
			}
			pageTable.setRows(volist);
			Long count = relationMainMapper.selectKeyProjectRelationMain_count(pageTable);
			pageTable.setTotal(count);
			return pageTable;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *  查询某关联系下的所有项目
	 */
	public PageTable<Pro_relationProjectVo> selectRelationProjectTable(PageTable<Pro_relationProjectVo> pageTable){
		try {
			List<Pro_relationProjectVo> relationProjectList = projectMapper.selectRelationProjectTable(pageTable);
			pageTable.setRows(relationProjectList);
			Long count = projectMapper.selectRelationProjectTable_count(pageTable);
			pageTable.setTotal(count);
			return pageTable;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 完结项目--续作
	 */
	public Boolean projectContinue(User user, Pro_project pro_project) {
		Boolean b = false ;
		String project_ID = "and project_ID = \'"+pro_project.getProject_ID()+"\'";
		String newApplyID = Tool.createUUID32();
		String newApplyDetailID = Tool.createUUID32();
		String newClientID = Tool.createUUID32();
	
		Pro_project project = projectMapper.selectOneProjectWhereSql(project_ID);
		try {
			if(null != project && null != project.getApply_ID()){
				Pro_apply apply = projectApplyService.selectOneApplyByWhereSql(" and apply_ID = \'"+project.getApply_ID()+"\'");
				if(null != apply && null != apply.getClientGUID()){
				 Client client = clientService.selectOneClientByWheresql(" and isMainVersion = 1  and clientGUID = \'"+apply.getClientGUID()+"\'");
				     if(null != client && null != client.getClient_ID()){//拷贝客户信息
				    	 clientService.copyMainClientToNewClient(user,client.getClient_ID(),newClientID);
				     }
				}
				if(null != apply){
					Pro_applyDetail applyDetail = applyDetailService.selectOneApplyDetailByWhereSql(" and apply_ID = \'"+apply.getApply_ID()+"\'");
					//拷贝申请表表信息
					apply.setApply_ID(newApplyID);
					apply.setClient_ID(newClientID);
					apply.setProjectStageID("流程未启动");
					apply.setProjectStageName("流程未启动");
					apply.setIsContinue(1);//是否续作项目
					Boolean returnBool = projectApplyService.insertOneApplyInfo(user, apply);
					String  meetingResolution_ID = Tool.createUUID32();
					
					if(returnBool){//在决议表中添加数据
						Pro_meetingResolution meetResolution = new Pro_meetingResolution();				
						meetResolution.setMeetingResolution_ID(meetingResolution_ID);
						meetResolution.setApply_ID(apply.getApply_ID());
						meetingResolutionService.insertOneMeetingResolution(user,meetResolution);
						 
						
						logService.insertOneOperatorLogInfo(user,"项目完结", "新增", "申请表ID ="+apply.getApply_ID());
					}
					if(null != applyDetail){//拷贝申请明细信息
						applyDetail.setApply_ID(newApplyID);
						applyDetail.setApplyDetail_ID(newApplyDetailID);
						applyDetail.setClient_ID(newClientID);
						Integer returnInt = applyDetailMapper.insertOneApplyDetailInfo(applyDetail);
						
						if(returnInt > 0 ){
							meetingDetailService.insertMeetingDetail(user, applyDetail, meetingResolution_ID);
							logService.insertOneOperatorLogInfo(user,"项目完结", "新增", "申请明细表ID ="+applyDetail.getApplyDetail_ID());
							
						}
					}
					
					
				}
				
				/*//拷贝保后项目信息
				project.setProject_ID(newProjectID);
				project.setApply_ID(newApplyID);
				project.setApplyDetail_ID(newApplyDetailID);
				project.setIsFree(0);
				project.setFreeDate(null);
				project.setFinishDate(null);
				project.setGuarantySum(project.getLoadSum());
				project.setDelayBeginDate(project.getLoadBeginDate());
				project.setDelayEndDate(project.getLoadEndDate());
				Integer returnInt = pro_projectMapper.insertOneProjectInfo(project);
				if(returnInt > 0){
					logService.insertOneOperatorLogInfo(user,"项目完结", "新增", "保后产品信息表ID ="+pro_project.getProject_ID());
					
				}*/
			}
		 b  = true ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	
	//已确认放款
	@Override
	public Boolean addConfirmLoan(User user, Pro_project project) {
		
		//根据ID查询一条  评审会产品明细  信息
		String meetingDetail_ID = project.getMeetingDetail_ID();
		Pro_meetingDetail meetingDetail = meetingDetailMapper.selectOneMeetingDetailByWhereSql(" and meetingDetail_ID=\'"+meetingDetail_ID+"\'");
		//向pro_project表放数据
		project = setPro_Project(user,meetingDetail,project);
		//向pro_project插入一条数据
		Integer count = 0;
		Boolean isTrue = false;
		try {
			count = projectMapper.insertOneProjectInfo(project);
			//根据ID查询一条  评审会决议表  信息
			String meetingResolution_ID = meetingDetail.getMeetingResolution_ID();
			Pro_meetingResolution meetingResolution = meetingResolutionMapper.selectOneResolutionByWhereSql(" and meetingResolution_ID=\'"+meetingResolution_ID+"\'");
			
			//向pro_checkPlan表中插入一条记录
			Pro_checkPlan checkPlan = new Pro_checkPlan();
			
			checkPlan.setApply_ID(meetingDetail.getApply_ID());
			checkPlan.setProject_ID(project.getProject_ID());
			checkPlan.setCreate_user(user.getCreate_user());
			checkPlan.setUnit_uid(user.getUnit_uid());
			checkPlan.setIsFinish("0");
			
			Date billBeginDate = project.getBillBeginDate();
	        Calendar calendar = Calendar.getInstance();  
	        calendar.setTime(billBeginDate);  
	        calendar.add(Calendar.DAY_OF_MONTH, -1);  
	        billBeginDate = calendar.getTime();  //获取当前时间的前一天
	        
			Date billEndDate = project.getBillEndDate();
			String controlTypeName = meetingResolution.getControlTypeName();//保后监控周期名称
			int num = 0;
			if(null!=controlTypeName){
				if("月".equals(controlTypeName)){
					num=1;
					//向pro_checkPlan表中插入一条记录
					insertOneCheckPlan(billBeginDate, num,billEndDate,checkPlan);
				}else if("季度".equals(controlTypeName)){
					num=3;
					//向pro_checkPlan表中插入一条记录
					insertOneCheckPlan(billBeginDate, num,billEndDate,checkPlan);
				}else if("半年".equals(controlTypeName)){
					num=6;
					//向pro_checkPlan表中插入一条记录
					insertOneCheckPlan(billBeginDate, num,billEndDate,checkPlan);
				}else if("年".equals(controlTypeName)){//年
					num=12;
					//向pro_checkPlan表中插入一条记录
					insertOneCheckPlan(billBeginDate, num,billEndDate,checkPlan);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(count>0){
			isTrue = true;
			logService.insertOneOperatorLogInfo(user, "已放款确认", "增加", "保后（贷后）产品放款明细表pro_project");
		}
		
		return isTrue;
	}
	/**
	 * 向pro_checkPlan表中插入一条记录
	 * @param beginDate
	 * @param num
	 * @param endDate
	 * @param checkPlan
	 * @throws ParseException
	 */
	private void insertOneCheckPlan(Date beginDate, int num, Date endDate,Pro_checkPlan checkPlan) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);
		cal.add(cal.MONTH, num);
		//cal.add(cal.DAY_OF_MONTH, 0);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		String syncTime=df.format(cal.getTime()).toString();
		beginDate = df.parse(syncTime);
		
		int i = beginDate.compareTo(endDate);
		
		if (i <= 0) {
			checkPlan.setCheckPlan_ID(Tool.createUUID32());//主键
			checkPlan.setPlanCheckDate(beginDate);
			//向pro_checkPlan表中插入数据
			checkPlanMapper.insertOneCheckPlan(checkPlan);//计划检查日期planCheckDate
			insertOneCheckPlan(beginDate, num, endDate,checkPlan);
		}

	}
	//向pro_project表中存放数据
	private Pro_project setPro_Project(User user,Pro_meetingDetail meetingDetail,Pro_project project){
		String applyDetail_ID = meetingDetail.getApplyDetail_ID();
		Pro_applyDetail applyDetail = null;
		if(null != applyDetail_ID){
			applyDetail = applyDetailMapper.selectOneApplyDetailWhereSql(" and applyDetail_ID='"+applyDetail_ID+"' ");
		}
		
		//根据ID查询一条Pro_apply记录
		Pro_apply apply = pro_applyMapper.selectOneApplyWhereSql(" and apply_ID=\'"+meetingDetail.getApply_ID()+"\'");
		
		project.setApply_ID(meetingDetail.getApply_ID());//对应业务申请ID
		project.setApplyDetail_ID(meetingDetail.getApplyDetail_ID());//对应产品ID
		//项目编号
		project.setProjectName(apply.getProjectName());		//项目名称
		project.setProjectCode(apply.getBusiCode()); //项目编号
		
		project.setBusiTypeID(meetingDetail.getBusiTypeID());//业务品种ID
		project.setBusiTypeName(meetingDetail.getBusiTypeName());//业务品种名称
		project.setBankID(meetingDetail.getBankID());//合作机构ID
		project.setBankName(meetingDetail.getBankName());//合作机构名称
		project.setSubBankName(meetingDetail.getSubBankName());//合作子机构（或个人）（手工录入）
		
		Double loanSum = project.getLoadSum()==null ? 0D:project.getLoadSum();//放款金额
		Double guarantyScale= meetingDetail.getGuarantyScale()==null ? 0D:meetingDetail.getGuarantyScale();//担保责任比例
		Double guarantyDutySum = 0D;//担保责任金额
				
		if(loanSum!=0 ){
			guarantyDutySum = (loanSum*guarantyScale)/100;
		}
		
		project.setGuarantySum(loanSum);//在保余额,初始化为放款金额
		project.setGuarantyDutySum(guarantyDutySum);	//担保责任金额
		project.setGuarantyDutyResSum(guarantyDutySum);//担保责任余额
		
		project.setBzScale(meetingDetail.getBzScale());//保证金比例
		
		project.setGuarantyScale(guarantyScale);	//担保责任比例
		project.setGuarantyScope(meetingDetail.getGuarantyScope());	//担保责任范围
		
		project.setPeriodDay(meetingDetail.getPeriodDay());//期限.天
		project.setPeriodMonth(meetingDetail.getPeriodMonth());//期限.月
		project.setPeriodMonthDay(meetingDetail.getPeriodMonthDay());//期限.月天
		project.setDelayBeginDate(project.getBillBeginDate());//展期起始日期（初始为借据起始日期）
		project.setDelayEndDate(project.getBillEndDate());//展期到期日期（初始为借据到期日期）
		
		project.setUnit_uid(user.getUnit_uid());
		project.setUnit_uidName(user.getUnit_uidName());
		project.setUpdateUserName(user.getUser_name());//最后修改人姓名
		
		project.setIsDelay(0);//是否展期
		project.setIsFree(0);//担保责任是否完全解除
		project.setIsOver(0);//正式确认是否逾期
		project.setIsBeforeEnd(0);//是否提前到期
		project.setIsCreditor(0);//是否涉及敏感债权人
		project.setIsGuaranty(0);//是否担保集团担保
		
		
		if(null != applyDetail){
			project.setProjectCode(applyDetail.getProjectCode());//项目编号
			project.setBusiClass(applyDetail.getBusiClass());//业务大类(01：担保02：委贷)
			project.setAmanID(applyDetail.getAmanID());		//项目经理A角ID
			project.setAmanName(applyDetail.getAmanName());	//项目经理A角名称
			project.setBmanID(applyDetail.getBmanID());		//项目经理B角ID
			project.setBmanName(applyDetail.getBmanName());	//项目经理B角名称
			project.setReviewManID(applyDetail.getReviewManID());	//风控评审人ID
			project.setReviewManName(applyDetail.getReviewManName());	//风控评审人名称
		}
		
		return project;
	}
	/**
	 * 撤销已确认放款
	 */
	@Override
	public Boolean cancelLoanConfirm(User user, Pro_project project) {
		String project_ID = project.getProject_ID();
		Integer count = 0;
		Boolean isTrue = false;
		try {
			//删除相关附件
			projectfilesMapper.deleteOneProFilesByEntityID(" and entityID='"+project_ID+"' ");
			//根据条件删除保后检查计划表pro_checkPlan
			String whereSql = " and apply_ID='"+project.getApply_ID()+"' and project_ID='"+project.getProject_ID()+"' and unit_uid = '"+SystemSession.getUserSession().getUnit_uid()+"' ";
			checkPlanMapper.deleteCheckPlansByWhereSql(whereSql);
			//删除对应放款信息
			count = projectMapper.deleteOneProjectAfterBySql(" and project_ID='"+project_ID+"' ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(count>0){
			isTrue = true;
			logService.insertOneOperatorLogInfo(user, "撤销已放款确认", "删除", "保后（贷后）产品放款明细表pro_project");
		}
		return isTrue;
	}
	/**
	 * 查询某关联系下的所有关联企业及项目信息
	 */
	public PageTable<Pro_project> selectRelationClientProTable(PageTable<Pro_project> pageTable) {
		try {
			List<Pro_project> relationProjectList = projectMapper.selectRelationClientProTable(pageTable);
			Double v2 = 10000.0;		//金额单位,万元转换成亿元, 除以10000
			for (Pro_project project : relationProjectList) {
				project.setLoadSum(BigDecimalUtil.div(project.getLoadSum(),v2));
				project.setGuarantySum(BigDecimalUtil.div(project.getGuarantySum(),v2));
			}
			pageTable.setRows(relationProjectList);
			Long count = projectMapper.selectRelationClientProTable_count(pageTable);
			pageTable.setTotal(count);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageTable;
	}
	
	@Override
	public Boolean insertProjectInfoAfter(User userSession, Pro_project pro_project) {
		boolean a = false;
		String  projectID = Tool.createUUID32();//创建新的ApplyDetail_ID	
		Pro_apply apply = pro_applyMapper.selectOneApplyWhereSql(" and apply_ID=\'"+pro_project.getApply_ID()+"\'");
		Pro_applyDetail applyDetail = applyDetailMapper.selectOneApplyDetailWhereSql(" and apply_ID=\'"+pro_project.getApply_ID()+"\'");
		apply.setdContractCode(pro_project.getDcontractCode());
		apply.setjContractCode(pro_project.getJcontractCode());
		apply.setbContractCode(pro_project.getBcontractCode());//添加合同号
		pro_applyMapper.updateOneApplyInfo(apply);
		applyDetail.setDcontractCode(pro_project.getDcontractCode());
		applyDetail.setJcontractCode(pro_project.getJcontractCode());
		applyDetail.setBcontractCode(pro_project.getBcontractCode());
		applyDetailMapper.updateOneApplyDetailInfo(applyDetail);
		pro_project.setProject_ID(projectID);
		pro_project.setAmanID(apply.getAmanID());
		pro_project.setAmanName(apply.getAmanName());
		pro_project.setApply_ID(apply.getApply_ID());
		pro_project.setApplyDetail_ID(applyDetail.getApplyDetail_ID());
		pro_project.setAttributionID(apply.getAttributionID());
		pro_project.setAttributionName(apply.getAttributionName());
		pro_project.setBankID(applyDetail.getBankID());
		pro_project.setBankName(applyDetail.getBankName());
		pro_project.setBmanID(apply.getBmanID());
		pro_project.setBmanName(apply.getBmanName());
		pro_project.setBusiClass(applyDetail.getBusiClass());
		pro_project.setBusiTypeID(applyDetail.getBusiTypeID());
		pro_project.setBusiTypeName(applyDetail.getBusiTypeName());
		pro_project.setBzScale(applyDetail.getBzScale());
		pro_project.setClient_ID(apply.getClient_ID());
		pro_project.setClientGUID(apply.getClientGUID());
		pro_project.setFundChinese(apply.getFundChinese());
		pro_project.setFundID(apply.getFundID());
		pro_project.setFundSource(apply.getFundSource());
		pro_project.setFundType(apply.getFundType());
		pro_project.setFundTypeID(apply.getFundTypeID());
		pro_project.setGuarantyDutySum(applyDetail.getGuarantyDutySum());
		pro_project.setGuarantyScale(applyDetail.getGuarantyScale());
		pro_project.setGuarantyScope(applyDetail.getGuarantyScope());
		pro_project.setInterestMethodID(applyDetail.getInterestMethodID());
		pro_project.setInterestMethodName(applyDetail.getInterestMethodName());
		pro_project.setPayMethod(apply.getPayMethod());
		pro_project.setReviewManID(apply.getReviewManID());
		pro_project.setReviewManName(apply.getReviewManName());
//			pro_project.setServiceRate(applyDetail.getServiceRate());
		pro_project.setSubBankName(applyDetail.getSubBankName());
		pro_project.setUnit_uid(apply.getUnit_uid());
		pro_project.setUnit_uidName(apply.getUnit_uidName());
		Integer returnInt = projectMapper.insertOneProjectInfo(pro_project);
		if(returnInt>0){
			a = true;
		}
		return a;
	}
	

	/**
	 * 用来获取相差多少月多少日
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	private int[]  getMonthAndDay(Date fromDate,Date toDate) {
		Calendar  from  =  Calendar.getInstance();
	    from.setTime(fromDate);
	    Calendar  to  =  Calendar.getInstance();
	    to.setTime(toDate);
	    int fromYear = from.get(Calendar.YEAR);
	    int fromMonth = from.get(Calendar.MONTH);
	    int fromDay = from.get(Calendar.DAY_OF_MONTH);
	    int toYear = to.get(Calendar.YEAR);
	    int toMonth = to.get(Calendar.MONTH);
	    int toDay = to.get(Calendar.DAY_OF_MONTH);
	    int year = toYear  -  fromYear;
	    int day = toDay  - fromDay;
	    int month = year*12 + toMonth  - fromMonth;
	    if( day< 0){
	    	switch (toMonth) {
			case 0://一月
				day = 31 +day; //1月的时间加上负的天数
				break;

			default:
				Calendar cal = Calendar.getInstance(); 
		    	cal.set(Calendar.YEAR,toYear); 
		    	cal.set(Calendar.MONTH, toMonth-1);
		    	int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
		    	day =dateOfMonth+day;
				break;
			}
	    	month = year*12 + toMonth  - fromMonth -1 ;
	    }
	   
	    int[] monthDay = new int[]{month,day};
	    return monthDay;
	}
	
	
	@Override
	public void insertOverDueInfo() {

		//and project_ID = 'ba94f77892ad11e7b87d000000143522'
		List<Pro_project> list = projectMapper.selectProjectListForInterestByWheresql("");

		for(Pro_project project : list ){
			if(null == project.getBankRate()){
				continue;
			}
			Double bankRate = BigDecimalUtil.div(project.getBankRate(),360*100);
			Double punishRate = BigDecimalUtil.div(project.getPunishRate(),360*100);
			
//			Date startTime = project.getLoadBeginDate();
			Date endTime = project.getDelayEndDate();
			Date now = new Date();
//			Double InterestSum = 0D;
//			int Day = 0;
			String wheresql1 = "and project_ID = '"+project.getProject_ID()+"'";
			
			Pro_projectoverdue model = overdueMapper.selectOneOverdueInfo(wheresql1);
			if(project.getInterestMethodID()!=null){
				System.out.println(project.getInterestMethodID()+"======"+project.getProject_ID());
			}
			//没有罚息记录；
			if(null == model) {
//				if(project.getProject_ID().equals("ba94f77892ad11e7b87d000000143522")){
//					Day=1;
//				}
				//计算利息
				if(null==project.getInterestMethodID()||project.getInterestMethodID().equals("b85bf0a498964da49851e1d26b9a26d9")){//利随本清
					model = insertInterest(project);
				}else if(project.getInterestMethodID().equals("fb089b600736427698abfb6c9706349c")){//按月
					model = insertInterestForMonth(project);
				}else if(project.getInterestMethodID().equals("2bc16cfc9d3a41fa925bb6b385f155b8")){//按季
					model = insertInterestForSeason(project);
				}
				//model = insertInterest(project);
				if(now.getTime()>endTime.getTime()) {
					//未逾期的 利息信息；
					project.setInterest(model.getInterest()==null?0:model.getInterest());
					project.setInterestSum(model.getInterestSum()==null?0:model.getInterestSum());
					projectMapper.updateOneProjectInfo(project);
//					overdueMapper.insertOneOverdueInfo(model);
				} else {
					//逾期的 利息信息;
					project.setInterest(model.getInterest()==null?0:model.getInterest());
					project.setInterestSum(model.getInterestSum()==null?0:model.getInterestSum());
					projectMapper.updateOneProjectInfo(project);
//					overdueMapper.insertOneOverdueInfo(model);
				}
			
//			} else { //有罚息记录
//				int day = DateUtil.getDaysBetweenDates(model.getCreateTime(),now)+1;
//				Pro_projectoverdue newOverdue = new Pro_projectoverdue();
//				newOverdue.setOverdue_ID(Tool.createUUID32());
//				newOverdue.setProject_ID(project.getProject_ID());
//				newOverdue.setStartTime(model.getEndTime());
//				newOverdue.setEndTime(now);
//				newOverdue.setCreateTime(now);
//				
//				//未逾期，计算利息
//				if(now.getTime()>endTime.getTime()) {
//					Double interest = BigDecimalUtil.mul(model.getCapital(), bankRate);
//					interest = BigDecimalUtil.add(model.getInterest(), interest);
//					newOverdue.setDefautInterest(interest);
//					newOverdue.setInterest(interest);
//					newOverdue.setCompoundInterest(0D);
//					newOverdue.setCapital(model.getCapital());
//					newOverdue.setInterestSum(model.getInterest());
//					//更新项目利息信息
//					project.setInterest(model.getInterest());
//					project.setInterestSum(model.getInterestSum());
//					projectMapper.updateOneProjectInfo(project);
//				}else {
//				//已逾期，计算罚息
//					Double interest = BigDecimalUtil.mul(model.getCapital(), punishRate);
//					Double compoundInterest = BigDecimalUtil.mul(model.getDefautInterest(), punishRate);
//					
//					
//					
//					
//					
//				}
			}
		}
		
	}
	
	//初始化累计利息（利随本清）
	public Pro_projectoverdue insertInterest(Pro_project project) {
		//利率
		Double lilv = project.getBankRate()/100/360;
		Date startTime = project.getLoadBeginDate();
		Date endTime =  project.getDelayEndDate();
		Date now = new Date();
		Double interest = 0D;
		Double capital = project.getLoadSum();
			
		//根据项目查询逾期前的还款记录
		List<Pro_factPay> payList = factPayMapper.selectFactPayListByWheresql("and project_ID = '"+project.getProject_ID()+"'"
				 + "and factDate <= '"+DateUtil.dateStr(endTime,"yyyy-MM-dd")+"'" );
		//判断还款记录
		if(null == payList || payList.isEmpty()) {
			//无还款记录
			int day  = DateUtil.getDaysBetweenDates(startTime,endTime);
			//没有逾期,计算截止当前时间利息
			if(endTime.getTime() > now.getTime()) {
				day = DateUtil.getDaysBetweenDates(startTime,now);
				interest = BigDecimalUtil.mul(lilv,BigDecimalUtil.mul(project.getLoadSum(),day));
			}else{//逾期,计算项目起止时间到结束时间
				day =  DateUtil.getDaysBetweenDates(startTime,endTime);
				//项目截止天数，计算复利基数
				interest=BigDecimalUtil.mul(lilv,BigDecimalUtil.mul(project.getLoadSum(),day));
			}
		} else {
			//有还款记录
			Date payStart = startTime;
			Pro_factPay pro_factPay=new Pro_factPay();
//				for(Pro_factPay factPay : payList) {
			for (int i = 0; i < payList.size(); i++) {
				Pro_factPay factPay=payList.get(i);	
				Date payEnd = factPay.getFactDate();
				int payDay = DateUtil.getDaysBetweenDates(payStart,payEnd);
				//计算上一次未还本金的利息
				//计算利息额
				double paySum = BigDecimalUtil.mul(lilv,BigDecimalUtil.mul(capital, payDay));
				interest = BigDecimalUtil.add(interest,paySum);
				payStart = payEnd;
				//计算本次还款后的未还本金
				capital = BigDecimalUtil.sub(capital, factPay.getPayCapitalSum());
				//计算本次还款后的累计欠利息
				interest = BigDecimalUtil.sub(interest, factPay.getPayInterestSum());
				factPay.setLxinterest(interest);
				factPay.setInterest(interest);
				factPayMapper.updateOneFactPay(factPay);
//				}
			}
			//判断是否逾期
			Date payEnd = endTime;
			if(endTime.getTime() > now.getTime()) {
				payEnd = now;
			}
			//计算当前时间或项目结束时间到最后一次还款时间的累计利息
			int payDay = DateUtil.getDaysBetweenDates(payStart,payEnd)+1;
			double paySum = BigDecimalUtil.mul(lilv,BigDecimalUtil.mul(capital, payDay));
			interest = BigDecimalUtil.add(interest,paySum);
			
		}
		//封装实体
		Pro_projectoverdue overdue = new Pro_projectoverdue();
		overdue.setOverdue_ID(Tool.createUUID32());
		overdue.setProject_ID(project.getProject_ID());
		overdue.setStartTime(startTime);
		overdue.setEndTime(now);
		overdue.setCapital(capital);
		overdue.setInterest(interest);
		overdue.setDefautInterest(interest);	
		overdue.setCompoundInterest(0D);
		overdue.setInterestSum(interest);
		overdue.setCreateTime(now);
//				overdueMapper.insertOneOverdueInfo(overdue);
		return overdue;
	}
		
	// 初始化累计利息（按月还款）
	@SuppressWarnings("null")
	public Pro_projectoverdue insertInterestForMonth(Pro_project project) {
		// 利率
		Double lilv = project.getBankRate() / 100 / 360;
		// 罚息利率
		Double fxlv = project.getPunishRate() / 100 / 360;
		Date startTime = project.getLoadBeginDate();
		Date endTime = project.getDelayEndDate();
		Date now = new Date();
		Double interest = 0D;
		Double fxInterest = 0D;
		Double capital = project.getLoadSum();

		Calendar startCalendar = Calendar.getInstance();
		Calendar nowCalendar = Calendar.getInstance();
		Calendar paramCalendar = Calendar.getInstance();
		startCalendar.setTime(startTime);
		nowCalendar.setTime(now);
		int starDay = startCalendar.get(Calendar.DAY_OF_MONTH);
		int nowDay = nowCalendar.get(Calendar.DAY_OF_MONTH);
		// 根据项目查询逾期前的还款记录
		List<Pro_factPay> payList = factPayMapper.selectFactPayListByWheresql("and project_ID = '"
				+ project.getProject_ID() + "'" + "and factDate <= '" + DateUtil.dateStr(endTime, "yyyy-MM-dd") + "'");
		// 判断还款记录
		// if(null == payList || payList.isEmpty()) {
		int startMonth = startCalendar.get(Calendar.MONTH) + 1;
		int startYear = startCalendar.get(Calendar.YEAR);
		// int nowtMonth = nowCalendar.get(Calendar.MONTH)+1;
		// int nowrtYear = nowCalendar.get(Calendar.YEAR);
		Date dateParam = new Date();
		// 无还款记录
		if (endTime.getTime() > now.getTime())
			nowCalendar.setTime(now);
		else
			nowCalendar.setTime(endTime);

		int monthNum = absMonth(startTime, nowCalendar.getTime());
		// if(nowDay<=20){//当前时间 20号前
		if ((starDay < 20 && nowDay <= 20) || (starDay > 20 && nowDay > 20))
			monthNum += 1;
		else if (starDay < 20 && nowDay > 20)
			monthNum += 2;

		Pro_factPay pro_factPay = new Pro_factPay();
		pro_factPay.setProject_ID(project.getProject_ID());
		Pro_project pro_project = new Pro_project();
		for (int i = 0; i < monthNum; i++) {
			// if(starDay<=20){//项目起始时间20号前
			if (i == 0) {
				// 第一个还款日
				String startD1 = startYear + "-" + startMonth + "-" + 20;
				if (starDay > 20) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
					startCalendar.add(Calendar.MONTH, 1);
					String stardtr = formatter.format(startCalendar.getTime());
					startD1 = stardtr + "-" + 20;
				}
				int day = DateUtil.getDaysBetweenDates(startTime, DateUtil.getDate(startD1, "yyyy-MM-dd"));
				pro_factPay.setFactDate(DateUtil.getDate(startD1, "yyyy-MM-dd"));
				pro_project = projectMapper.countGuarantySumForFactDate(pro_factPay);
				interest = BigDecimalUtil.mul(lilv, BigDecimalUtil.mul(pro_project.getGuarantySum(), day));
				// 第一个月没有复利额
				double flSum = 0;
				dateParam = DateUtil.getDate(startD1, "yyyy-MM-dd");

				calculationFactPay(project, payList, startTime, DateUtil.getDate(startD1, "yyyy-MM-dd"), interest,
						flSum, fxInterest);
			} else if (i == monthNum - 1) {// 截止当前最后一个还款日
				// 最后个还款日 （当前时间 20号前，查询上一个月20号）
				int day = DateUtil.getDaysBetweenDates(dateParam, nowCalendar.getTime());
				pro_factPay.setFactDate(nowCalendar.getTime());
				pro_project = projectMapper.countGuarantySumForFactDate(pro_factPay);
				// 上月20号到完结时间利息
				double paySum = BigDecimalUtil.mul(lilv, BigDecimalUtil.mul(pro_project.getGuarantySum(), day));
				// 到完结时间累计利息
				interest = BigDecimalUtil.add(interest, paySum);
				// 上月20号到完结时间罚息额
				// fxSum = BigDecimalUtil.mul(fxlv,
				// BigDecimalUtil.mul(pro_project.getLoadSum(), day));
				// 累计罚息
				// fxInterest = BigDecimalUtil.add(fxInterest, fxSum);
				// 完结时间复利额
				double flSum = BigDecimalUtil.mul(fxlv, BigDecimalUtil.mul(interest + fxInterest, day));
				if (nowCalendar.getTime() == project.getDelayEndDate()) {
					project.setInterest(interest);
				}
				calculationFactPay(project, payList, dateParam, nowCalendar.getTime(), interest, flSum, fxInterest);
			} else {
				// 上次截止20号时间到本次截止20号时间
				paramCalendar.setTime(dateParam);
				paramCalendar.add(Calendar.MONTH, 1);
				int day = DateUtil.getDaysBetweenDates(dateParam, paramCalendar.getTime());
				pro_factPay.setFactDate(paramCalendar.getTime());
				pro_project = projectMapper.countGuarantySumForFactDate(pro_factPay);
				// 本月利息额
				double paySum = BigDecimalUtil.mul(lilv, BigDecimalUtil.mul(pro_project.getGuarantySum(), day));
				// 累计利息
				interest = BigDecimalUtil.add(interest, paySum);
				// 本月罚息额
				// double fxSum = BigDecimalUtil.mul(fxlv,
				// BigDecimalUtil.mul(pro_project.getLoadSum(), day));
				// 累计罚息
				// fxInterest = BigDecimalUtil.add(fxInterest, fxSum);
				// 本月复利额
				double flSum = BigDecimalUtil.mul(fxlv, BigDecimalUtil.mul(interest + fxInterest, day));
				dateParam = paramCalendar.getTime();

				calculationFactPay(project, payList, dateParam, paramCalendar.getTime(), interest, flSum, fxInterest);
			}
			// } else {
			// if (i == 0) {
			// // 第一个还款日
			// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
			// startCalendar.add(Calendar.MONTH, 1);
			// String stardtr = formatter.format(startCalendar.getTime());
			// String startD1 = stardtr + "-" + 20;
			// // String startD1 = startYear + "-" + (startMonth+1) +
			// // "-" + 20;
			// int day = DateUtil.getDaysBetweenDates(startTime,
			// DateUtil.getDate(startD1, "yyyy-MM-dd"));
			// interest = BigDecimalUtil.mul(lilv,
			// BigDecimalUtil.mul(project.getLoadSum(), day));
			// dateParam = DateUtil.getDate(startD1, "yyyy-MM-dd");
			// double flSum = 0;
			//
			// calculationFactPay(project,payList, startTime,
			// DateUtil.getDate(startD1,
			// "yyyy-MM-dd"),interest,fxInterest,flSum);
			// } else if (i == monthNum) {// 截止当前最后一个还款日
			// // 最后个还款日 （当前时间 20号前，查询上一个月20号）
			// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
			// nowCalendar.add(Calendar.MONTH, 0);
			// String stardtr = formatter.format(nowCalendar.getTime());
			// String endD1 = stardtr + "-" + 20;
			// int day = DateUtil.getDaysBetweenDates(startTime,
			// DateUtil.getDate(endD1, "yyyy-MM-dd"));
			// // 上月20号到完结时间利息
			// double paySum = BigDecimalUtil.mul(lilv,
			// BigDecimalUtil.mul(project.getLoadSum(), day));
			// // 到完结时间累计利息
			// interest = BigDecimalUtil.add(interest, paySum);
			// // 上月20号到完结时间罚息额
			// double fxSum = BigDecimalUtil.mul(fxlv,
			// BigDecimalUtil.mul(project.getLoadSum(), day));
			// // 累计罚息
			// fxInterest = BigDecimalUtil.add(fxInterest, fxSum);
			// // 完结时间复利额
			// double flSum = BigDecimalUtil.mul(fxlv,
			// BigDecimalUtil.mul(interest + fxInterest, day));
			//
			// calculationFactPay(project,payList, dateParam,
			// nowCalendar.getTime(),interest,fxInterest,flSum);
			// } else {
			// paramCalendar.setTime(dateParam);
			// paramCalendar.add(Calendar.MONTH, 1);
			// int day = DateUtil.getDaysBetweenDates(dateParam,
			// paramCalendar.getTime());
			// // 本月利息额
			// double paySum = BigDecimalUtil.mul(lilv,
			// BigDecimalUtil.mul(project.getLoadSum(), day));
			// // 累计利息
			// interest = BigDecimalUtil.add(interest, paySum);
			// // 本月罚息额
			// double fxSum = BigDecimalUtil.mul(fxlv,
			// BigDecimalUtil.mul(project.getLoadSum(), day));
			// // 累计罚息
			// fxInterest = BigDecimalUtil.add(fxInterest, fxSum);
			// // 本月复利额
			// double flSum = BigDecimalUtil.mul(fxlv,
			// BigDecimalUtil.mul(interest + fxInterest, day));
			// dateParam = paramCalendar.getTime();
			//
			// calculationFactPay(project,payList, dateParam,
			// nowCalendar.getTime(),interest,fxInterest,flSum);
			// }
			// }
		}
		// }
		// }
		// 封装实体
		Pro_projectoverdue overdue = new Pro_projectoverdue();
		overdue.setOverdue_ID(Tool.createUUID32());
		overdue.setProject_ID(project.getProject_ID());
		overdue.setStartTime(startTime);
		overdue.setEndTime(now);
		overdue.setCapital(capital);
		overdue.setInterest(interest);
		overdue.setDefautInterest(interest);
		overdue.setCompoundInterest(0D);
		overdue.setInterestSum(interest);
		overdue.setCreateTime(now);
		// overdueMapper.insertOneOverdueInfo(overdue);
		return overdue;
	}
	
	/**
	 * 计算还款后的利息(按月/按季)
	 */
	@SuppressWarnings("unused")
	public void calculationFactPay(Pro_project project,List<Pro_factPay> payList,Date startTime,Date endTime,double interest,double flSum,double fxInterest){
//		List<Pro_calculationFactPay> calculations = calculationFactPayService.selectCalculationFactPayListByWhereSql("and project_ID = '"+pro_calculationFactPay.getProject_ID()+"'"
//								 + "and interestDate <= '"+endTime+"'" );
		
		Pro_calculationFactPay pro_calculationFactPay = calculationFactPayService.selectOneCalculationFactPayByWhereSql("and project_ID = '"+project.getProject_ID()+"'"
				 + "and interestDate = '"+DateUtil.dateStr(endTime,"yyyy-MM-dd")+"'" );
		if(null!=pro_calculationFactPay){
			pro_calculationFactPay.setApply_ID(project.getApply_ID());
			pro_calculationFactPay.setProject_ID(project.getProject_ID());
			pro_calculationFactPay.setInterest(interest);
			pro_calculationFactPay.setFlinterest(flSum);
			pro_calculationFactPay.setFxinterest(fxInterest);
			pro_calculationFactPay.setSurplusInterest(interest);
			pro_calculationFactPay.setSurplusFlinterest(flSum);
			pro_calculationFactPay.setSurplusFxinterest(fxInterest);
			pro_calculationFactPay.setInterestDate(endTime);
			calculationFactPayService.updateCalculationFactPay(pro_calculationFactPay);
		}else{
			pro_calculationFactPay = new Pro_calculationFactPay();
			pro_calculationFactPay.setCalculation_ID(Tool.createUUID32());
			pro_calculationFactPay.setApply_ID(project.getApply_ID());
			pro_calculationFactPay.setProject_ID(project.getProject_ID());
			pro_calculationFactPay.setInterest(interest);
			pro_calculationFactPay.setFlinterest(flSum);
			pro_calculationFactPay.setFxinterest(fxInterest);
			pro_calculationFactPay.setSurplusInterest(interest);
			pro_calculationFactPay.setSurplusFlinterest(flSum);
			pro_calculationFactPay.setSurplusFxinterest(fxInterest);
			pro_calculationFactPay.setInterestDate(endTime);
			calculationFactPayService.insertOneCalculationFactPayInfo(SystemSession.getUserSession(), pro_calculationFactPay);
		}
		if(payList.size()!=0){
			for (Pro_factPay j_pro_factPay : payList) {
				int starDay = DateUtil.getDaysBetweenDates(startTime,j_pro_factPay.getFactDate());
				int endDay = DateUtil.getDaysBetweenDates(j_pro_factPay.getFactDate(),endTime);
				if(starDay>0){//判断还款时间是否在当月或当月之前
					double flpaySum = pro_calculationFactPay.getSurplusFlinterest();
					double fxpaySum = pro_calculationFactPay.getSurplusFxinterest();
					if(BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum())<=0){
						pro_calculationFactPay.setSurplusFlinterest((double)0);
						if(BigDecimalUtil.add(fxpaySum, BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum()))<=0){
							pro_calculationFactPay.setSurplusFxinterest((double)0);
							if(BigDecimalUtil.add(pro_calculationFactPay.getSurplusInterest(),BigDecimalUtil.add(fxpaySum, BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum())))<=0){
								pro_calculationFactPay.setSurplusInterest((double)0);
//								pro_calculationFactPay.setSurplusSum(Math.abs(BigDecimalUtil.add(pro_calculationFactPay.getSurplusInterest(),BigDecimalUtil.add(fxpaySum, BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum())))));
							}else{
								pro_calculationFactPay.setSurplusInterest(BigDecimalUtil.sub(pro_calculationFactPay.getSurplusInterest(),BigDecimalUtil.add(pro_calculationFactPay.getSurplusInterest(),BigDecimalUtil.add(fxpaySum, BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum())))));
							}
						}else{
							pro_calculationFactPay.setFxinterest(BigDecimalUtil.sub(fxpaySum, BigDecimalUtil.add(fxpaySum, BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum()))));
						}
					}else{//还的利息不够复利减
						pro_calculationFactPay.setSurplusFlinterest(BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum()));
					}
				}
			}
			calculationFactPayService.updateCalculationFactPay(pro_calculationFactPay);
		}
	}

	// 初始化累计利息（按季还款）
	@SuppressWarnings("unused")
	public Pro_projectoverdue insertInterestForSeason(Pro_project project) {
		// 利率
		Double lilv = project.getBankRate() / 100 / 360;
		// 罚息利率
		Double fxlv = project.getPunishRate() / 100 / 360;
		Date startTime = project.getLoadBeginDate();
		Date endTime = project.getDelayEndDate();
		Date now = new Date();
		Double interest = 0D;
		Double fxInterest = 0D;
		Double capital = project.getLoadSum();

		Calendar startCalendar = Calendar.getInstance();
		Calendar nowCalendar = Calendar.getInstance();
		Calendar paramCalendar = Calendar.getInstance();
		startCalendar.setTime(startTime);
		int starDay = startCalendar.get(Calendar.DAY_OF_MONTH);

		// 根据项目查询逾期前的还款记录
		List<Pro_factPay> payList = factPayMapper.selectFactPayListByWheresql("and project_ID = '"
				+ project.getProject_ID() + "'" + "and factDate <= '" + DateUtil.dateStr(endTime, "yyyy-MM-dd") + "'");
		// 判断还款记录
		// if (null == payList || payList.isEmpty()) {
		int startMonth = startCalendar.get(Calendar.MONTH) + 1;
		int startYear = startCalendar.get(Calendar.YEAR);
		// int nowtMonth = nowCalendar.get(Calendar.MONTH)+1;
		// int nowrtYear = nowCalendar.get(Calendar.YEAR);
		Date dateParam = new Date();

		// 无还款记录
		if (endTime.getTime() > now.getTime())
			nowCalendar.setTime(now);
		else
			nowCalendar.setTime(endTime);

		int nowDay = nowCalendar.get(Calendar.DAY_OF_MONTH);
		int monthNum = absMonth(startTime, nowCalendar.getTime()) / 3;
		// if(nowDay<=20){//当前时间 20号前
		if ((starDay < 20 && nowDay <= 20) || (starDay > 20 && nowDay > 20))
			monthNum += 1;
		else if (starDay < 20 && nowDay > 20)
			monthNum += 2;

		Pro_factPay pro_factPay = new Pro_factPay();
		pro_factPay.setProject_ID(project.getProject_ID());
		Pro_project pro_project = new Pro_project();

		for (int i = 0; i < monthNum; i++) {
			// if (starDay <= 20) {// 项目起始时间20号前
			if (i == 0) {
				// 第一个还款日
				String startD1 = startYear + "-" + 3 + "-" + 20;
				String startD2 = startYear + "-" + 6 + "-" + 20;
				String startD3 = startYear + "-" + 9 + "-" + 20;
				String startD4 = startYear + "-" + 12 + "-" + 20;
				// startCalendar.add(Calendar.MONTH, 3);
				// 项目起始时间小于3月20号，第一个还款时间由项目起始时间到3月20号
				int day = 0;
				Date dEnd = DateUtil.getDate(startD1, "yyyy-MM-dd");
				if (startTime.before(DateUtil.getDate(startD1, "yyyy-MM-dd"))
						|| startTime.equals(DateUtil.getDate(startD1, "yyyy-MM-dd"))) {
					day = DateUtil.getDaysBetweenDates(startTime, DateUtil.getDate(startD1, "yyyy-MM-dd"));
				} else if ((startTime.before(DateUtil.getDate(startD2, "yyyy-MM-dd"))
						|| startTime.equals(DateUtil.getDate(startD2, "yyyy-MM-dd")))
						&& (startTime.after(DateUtil.getDate(startD1, "yyyy-MM-dd")))) {
					day = DateUtil.getDaysBetweenDates(startTime, DateUtil.getDate(startD2, "yyyy-MM-dd"));
					dEnd = DateUtil.getDate(startD2, "yyyy-MM-dd");
				} else if ((startTime.before(DateUtil.getDate(startD3, "yyyy-MM-dd"))
						|| startTime.equals(DateUtil.getDate(startD3, "yyyy-MM-dd")))
						&& (startTime.after(DateUtil.getDate(startD2, "yyyy-MM-dd")))) {
					day = DateUtil.getDaysBetweenDates(startTime, DateUtil.getDate(startD3, "yyyy-MM-dd"));
					dEnd = DateUtil.getDate(startD3, "yyyy-MM-dd");
				} else if ((startTime.before(DateUtil.getDate(startD4, "yyyy-MM-dd"))
						|| startTime.equals(DateUtil.getDate(startD4, "yyyy-MM-dd")))
						&& (startTime.after(DateUtil.getDate(startD3, "yyyy-MM-dd")))) {
					day = DateUtil.getDaysBetweenDates(startTime, DateUtil.getDate(startD4, "yyyy-MM-dd"));
					dEnd = DateUtil.getDate(startD4, "yyyy-MM-dd");
				} else if (startTime.after(DateUtil.getDate(startD4, "yyyy-MM-dd"))) {
					String startD5 = startYear + 1 + "-" + 3 + "-" + 20;
					day = DateUtil.getDaysBetweenDates(startTime, DateUtil.getDate(startD5, "yyyy-MM-dd"));
					dEnd = DateUtil.getDate(startD5, "yyyy-MM-dd");
				}
				// int day = DateUtil.getDaysBetweenDates(startTime,
				// DateUtil.getDate(startD1, "yyyy-MM-dd"));
				pro_factPay.setFactDate(dEnd);
				pro_project = projectMapper.countGuarantySumForFactDate(pro_factPay);
				interest = BigDecimalUtil.mul(lilv, BigDecimalUtil.mul(pro_project.getGuarantySum(), day));
				// 复利额
				double flSum = 0;
				dateParam = dEnd;

				calculationFactPay(project, payList, startTime, dEnd, interest, fxInterest, flSum);
			} else if (i == monthNum - 1) {// 截止当前最后一个还款日
				// 最后个还款日 （当前时间 20号前，查询上一个月20号）
				int day = DateUtil.getDaysBetweenDates(dateParam, nowCalendar.getTime());
				pro_factPay.setFactDate(nowCalendar.getTime());
				pro_project = projectMapper.countGuarantySumForFactDate(pro_factPay);
				// 上月20号到完结时间利息
				double paySum = BigDecimalUtil.mul(lilv, BigDecimalUtil.mul(pro_project.getGuarantySum(), day));
				// 到完结时间累计利息
				interest = BigDecimalUtil.add(interest, paySum);
				// 上月20号到完结时间罚息额
				// fxSum = BigDecimalUtil.mul(fxlv,
				// BigDecimalUtil.mul(pro_project.getLoadSum(), day));
				// 累计罚息
				// fxInterest = BigDecimalUtil.add(fxInterest, fxSum);
				// 完结时间复利额
				double flSum = BigDecimalUtil.mul(fxlv, BigDecimalUtil.mul(interest + fxInterest, day));
				if (nowCalendar.getTime() == project.getDelayEndDate()) {
					project.setInterest(interest);
				}
				calculationFactPay(project, payList, dateParam, nowCalendar.getTime(), interest, flSum, fxInterest);
			} else {
				// 上次截止20号时间到本次截止20号时间
				paramCalendar.setTime(dateParam);
				paramCalendar.add(Calendar.MONTH, 3);
				int day = DateUtil.getDaysBetweenDates(dateParam, paramCalendar.getTime());
				pro_factPay.setFactDate(paramCalendar.getTime());
				pro_project = projectMapper.countGuarantySumForFactDate(pro_factPay);
				// 本月利息额
				double paySum = BigDecimalUtil.mul(lilv, BigDecimalUtil.mul(pro_project.getGuarantySum(), day));
				// 累计利息
				interest = BigDecimalUtil.add(interest, paySum);
				// 本月罚息额
				// double fxSum = BigDecimalUtil.mul(fxlv,
				// BigDecimalUtil.mul(pro_project.getLoadSum(), day));
				// 累计罚息
				// fxInterest = BigDecimalUtil.add(fxInterest, fxSum);
				// 本月复利额
				double flSum = BigDecimalUtil.mul(fxlv, BigDecimalUtil.mul(interest + fxInterest, day));
				dateParam = paramCalendar.getTime();

				calculationFactPay(project, payList, dateParam, paramCalendar.getTime(), interest, flSum, fxInterest);
			}
			// } else {
			// if (i == 0) {
			// // 第一个还款日
			// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
			// startCalendar.add(Calendar.MONTH, 1);
			// String stardtr = formatter.format(startCalendar.getTime());
			// String startD1 = stardtr + "-" + 20;
			// // String startD1 = startYear + "-" + (startMonth+1) +
			// // "-" + 20;
			// int day = DateUtil.getDaysBetweenDates(startTime,
			// DateUtil.getDate(startD1, "yyyy-MM-dd"));
			// interest = BigDecimalUtil.mul(lilv,
			// BigDecimalUtil.mul(project.getLoadSum(), day));
			// dateParam = DateUtil.getDate(startD1, "yyyy-MM-dd");
			// double flSum = 0;
			//
			// calculationFactPay(project, payList, startTime,
			// DateUtil.getDate(startD1, "yyyy-MM-dd"),
			// interest, fxInterest, flSum);
			// } else if (i == monthNum) {// 截止当前最后一个还款日
			// // 最后个还款日 （当前时间 20号前，查询上一个月20号）
			// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
			// nowCalendar.add(Calendar.MONTH, 0);
			// String stardtr = formatter.format(nowCalendar.getTime());
			// String endD1 = stardtr + "-" + 20;
			// int day = DateUtil.getDaysBetweenDates(startTime,
			// DateUtil.getDate(endD1, "yyyy-MM-dd"));
			// // 上月20号到完结时间利息
			// double paySum = BigDecimalUtil.mul(lilv,
			// BigDecimalUtil.mul(project.getLoadSum(), day));
			// // 到完结时间累计利息
			// interest = BigDecimalUtil.add(interest, paySum);
			// // 上月20号到完结时间罚息额
			// double fxSum = BigDecimalUtil.mul(fxlv,
			// BigDecimalUtil.mul(project.getLoadSum(), day));
			// // 累计罚息
			// fxInterest = BigDecimalUtil.add(fxInterest, fxSum);
			// // 完结时间复利额
			// double flSum = BigDecimalUtil.mul(fxlv,
			// BigDecimalUtil.mul(interest + fxInterest, day));
			//
			// calculationFactPay(project, payList, dateParam,
			// nowCalendar.getTime(), interest, fxInterest,
			// flSum);
			// } else {
			// paramCalendar.setTime(dateParam);
			// paramCalendar.add(Calendar.MONTH, 1);
			// int day = DateUtil.getDaysBetweenDates(dateParam,
			// paramCalendar.getTime());
			// // 本月利息额
			// double paySum = BigDecimalUtil.mul(lilv,
			// BigDecimalUtil.mul(project.getLoadSum(), day));
			// // 累计利息
			// interest = BigDecimalUtil.add(interest, paySum);
			// // 本月罚息额
			// double fxSum = BigDecimalUtil.mul(fxlv,
			// BigDecimalUtil.mul(project.getLoadSum(), day));
			// // 累计罚息
			// fxInterest = BigDecimalUtil.add(fxInterest, fxSum);
			// // 本月复利额
			// double flSum = BigDecimalUtil.mul(fxlv,
			// BigDecimalUtil.mul(interest + fxInterest, day));
			// dateParam = paramCalendar.getTime();
			//
			// calculationFactPay(project, payList, dateParam,
			// nowCalendar.getTime(), interest, fxInterest,
			// flSum);
			// }
			// }
			// }
			// }
		}
		// 封装实体
		Pro_projectoverdue overdue = new Pro_projectoverdue();
		overdue.setOverdue_ID(Tool.createUUID32());
		overdue.setProject_ID(project.getProject_ID());
		overdue.setStartTime(startTime);
		overdue.setEndTime(now);
		overdue.setCapital(capital);
		overdue.setInterest(interest);
		overdue.setDefautInterest(interest);
		overdue.setCompoundInterest(0D);
		overdue.setInterestSum(interest);
		overdue.setCreateTime(now);
		// overdueMapper.insertOneOverdueInfo(overdue);
		return overdue;
	}

	public int absMonth(Date d1,Date d2) {
		Calendar bef = Calendar.getInstance();
		Calendar aft = Calendar.getInstance();
    	bef.setTime(d1);
		aft.setTime(d2);
		int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
		int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
		
    	return Math.abs(month + result);   
	}
	
	@Override
	public void payInterestSum() {
		//查询所有逾期后的还款
		//"and pp.project_ID='ba94f77892ad11e7b87d000000143522'"
		List<Pro_factPay> listpfp = factPayMapper.selectOverdueFactPayListByWheresql("");
		for (int i = 0; i < listpfp.size(); i++) {
			Pro_factPay i_pro_factPay = listpfp.get(i);
			Pro_factPay factPay = new Pro_factPay();
			//根据项目id查询所有逾期还款
			List<Pro_factPay> list = factPayMapper.selectOverdueFactPayListByWheresql("AND pp.project_ID = '"+i_pro_factPay.getProject_ID()+"'");
			for (int j = 0; j < list.size(); j++) {
				Pro_factPay j_pro_factPay = list.get(j);
				//计算当前还款时间的项目余额
				Pro_project pro_project = projectMapper.countGuarantySumForFactDate(j_pro_factPay);
				Double fxlilv = 0D;
				//罚利率
				if(null!=pro_project){
					if(null!=pro_project.getPunishRate()){
						fxlilv = pro_project.getPunishRate()/100/360;
					}
					if(null!=j_pro_factPay&&null!=j_pro_factPay.getFactDate()){
						if(j==0){//计算逾期后第一笔还款的利息
							int daycount = DateUtil.getDaysBetweenDates(pro_project.getLoadEndDate(),j_pro_factPay.getFactDate())-1;
							//计算本次还款的罚息额
							double fxpaySum = BigDecimalUtil.mul(fxlilv,BigDecimalUtil.mul(pro_project.getGuarantySum(), daycount));
							//计算本次还款的复利额
							double flpaySum = BigDecimalUtil.mul(fxlilv,BigDecimalUtil.mul(pro_project.getInterest(), daycount));
							//计算本次还款的累计欠息
							Double suminterest = BigDecimalUtil.add(BigDecimalUtil.add(pro_project.getInterest(), flpaySum), fxpaySum);
							j_pro_factPay.setInterest(BigDecimalUtil.sub(suminterest,j_pro_factPay.getPayInterestSum()));
							//如果有还利息   优先级：先还复利，在还罚息，在还利息
							//计算本次还款后的复利余额
							if(BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum())<=0){
								j_pro_factPay.setFlinterest((double)0);
								if(BigDecimalUtil.add(fxpaySum, BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum()))<=0){
									j_pro_factPay.setFxinterest((double)0);
									if(BigDecimalUtil.add(pro_project.getInterest(),BigDecimalUtil.add(fxpaySum, BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum())))<=0){
										j_pro_factPay.setLxinterest((double)0);
									}else{
										j_pro_factPay.setLxinterest(BigDecimalUtil.sub(pro_project.getInterest(),BigDecimalUtil.add(pro_project.getInterest(),BigDecimalUtil.add(fxpaySum, BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum())))));
									}
								}else{
									j_pro_factPay.setFxinterest(BigDecimalUtil.sub(fxpaySum, BigDecimalUtil.add(fxpaySum, BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum()))));
									j_pro_factPay.setLxinterest(pro_project.getInterest());
								}
							}else{//还的利息不够复利减
								j_pro_factPay.setFlinterest(BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum()));
								j_pro_factPay.setFxinterest(fxpaySum);
								j_pro_factPay.setLxinterest(pro_project.getInterest());
							}
//							j_pro_factPay.setFlinterest(BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum()));
//							//计算本次还款后的罚息余额
//							j_pro_factPay.setFxinterest(BigDecimalUtil.sub(fxpaySum, BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum())));
							//计算本次还款后的利息余额
//							j_pro_factPay.setLxinterest(BigDecimalUtil.sub(pro_project.getInterest(),BigDecimalUtil.sub(fxpaySum, BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum()))));
							factPayMapper.updateOneFactPay(j_pro_factPay);
							//保留数据，下一条还款数据需要
							BeanUtils.copyProperties(j_pro_factPay, factPay, Pro_factPay.class);
						}else{
							int daycount = DateUtil.getDaysBetweenDates(factPay.getFactDate(),j_pro_factPay.getFactDate());
							//计算本次还款的罚息额
							double fxpaySum = BigDecimalUtil.mul(fxlilv,BigDecimalUtil.mul(pro_project.getGuarantySum(), daycount));
							//计算本次还款的复利额
							double flpaySum = BigDecimalUtil.mul(fxlilv,BigDecimalUtil.mul(pro_project.getInterest(), daycount));
							//计算本次还款的累计欠息
							Double suminterest = BigDecimalUtil.add(BigDecimalUtil.add(factPay.getInterest(), flpaySum), fxpaySum);
							j_pro_factPay.setInterest(BigDecimalUtil.sub(suminterest,j_pro_factPay.getPayInterestSum()));
							//如果有还利息   优先级：先还复利，在还罚息，在还利息
							if(BigDecimalUtil.sub(BigDecimalUtil.add(flpaySum, factPay.getFlinterest()),j_pro_factPay.getPayInterestSum())<=0){
								j_pro_factPay.setFlinterest((double)0);
								if(BigDecimalUtil.add(BigDecimalUtil.add(fxpaySum, factPay.getFxinterest()),BigDecimalUtil.sub(BigDecimalUtil.add(flpaySum, factPay.getFlinterest()),j_pro_factPay.getPayInterestSum()))<=0){
									j_pro_factPay.setFxinterest((double)0);
									if(BigDecimalUtil.add(factPay.getLxinterest(),BigDecimalUtil.add(BigDecimalUtil.add(fxpaySum, factPay.getFxinterest()),BigDecimalUtil.sub(BigDecimalUtil.add(flpaySum, factPay.getFlinterest()),j_pro_factPay.getPayInterestSum())))<=0){
										j_pro_factPay.setLxinterest((double)0);
									}else{
										j_pro_factPay.setLxinterest(BigDecimalUtil.sub(factPay.getLxinterest(),BigDecimalUtil.add(factPay.getLxinterest(),BigDecimalUtil.add(BigDecimalUtil.add(fxpaySum, factPay.getFxinterest()),BigDecimalUtil.sub(BigDecimalUtil.add(flpaySum, factPay.getFlinterest()),j_pro_factPay.getPayInterestSum())))));
									}
								}else{
									j_pro_factPay.setFxinterest(BigDecimalUtil.add(BigDecimalUtil.add(fxpaySum, factPay.getFxinterest()),BigDecimalUtil.sub(BigDecimalUtil.add(flpaySum, factPay.getFlinterest()),j_pro_factPay.getPayInterestSum())));
									j_pro_factPay.setLxinterest(factPay.getLxinterest());
								}
							}else{//还的利息不够复利减
								j_pro_factPay.setFlinterest(BigDecimalUtil.sub(BigDecimalUtil.add(flpaySum, factPay.getFlinterest()), j_pro_factPay.getPayInterestSum()));
								j_pro_factPay.setFxinterest(BigDecimalUtil.add(fxpaySum,factPay.getFxinterest()));
								j_pro_factPay.setLxinterest(factPay.getLxinterest());
							}
							//计算本次还款后的复利余额
//							j_pro_factPay.setFlinterest(BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum()));
//							//计算本次还款后的罚息余额
//							j_pro_factPay.setFxinterest(BigDecimalUtil.sub(fxpaySum, BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum())));
//							//计算本次还款后的利息余额
//							j_pro_factPay.setLxinterest(BigDecimalUtil.sub(pro_project.getInterest(),BigDecimalUtil.sub(fxpaySum, BigDecimalUtil.sub(flpaySum, j_pro_factPay.getPayInterestSum()))));
							factPayMapper.updateOneFactPay(j_pro_factPay);
							//保留数据，下一条还款数据需要
							BeanUtils.copyProperties(j_pro_factPay, factPay, Pro_factPay.class);
						}
					}
				}
			}
		}
	}
		
	@Override
	public List<Pro_project> selectProjectListForInterestByWheresql(String WhereSql) {
		List<Pro_project> projectList = projectMapper.selectProjectListForInterestByWheresql(WhereSql);
		return projectList;
	}

	/**
	 * 按已诉讼和未诉讼，五级分类将项目分组
	 */
	@Override
	public Map<String, Object> selectRiskLevelList(String string) {
		String pids = lawSuitService.concatProjectID();
		String[] ids = pids.split(",");
		String idsSql = "";
		for (int i = 0; i < ids.length; i++) {
			if (i < ids.length - 1)
				idsSql += "'" + ids[i] + "',";
			else
				idsSql += "'" + ids[i] + "'";
		}
		// 获取风险类别
		// List<C_dictype> costTypeList = dicTypeService.selectAllDicTypeList("
		// and dicTypePID='50f858be37284937af4e6a8d3c9bee4b'");
		// 查询已诉讼案件
		List<Pro_project> projects = projectMapper.selectProjectISLawsuitByWheresql(idsSql);
		List<Pro_project> isLawsuitShList = new ArrayList<>();
		List<Pro_project> isLawsuiKyList = new ArrayList<>();
		List<Pro_project> isLawsuiZcList = new ArrayList<>();
		List<Pro_project> isLawsuiGzLlist = new ArrayList<>();
		List<Pro_project> isLawsuiCjLlist = new ArrayList<>();
		List<Pro_project> isLawsuiQtLlist = new ArrayList<>();
		Map<String, Object> isMap = new HashMap<>();
		for (Pro_project project : projects) {
			if (null == project.getRiskLevelName())
				isLawsuiQtLlist.add(project);
			else if (project.getRiskLevelName().equals("损失"))
				isLawsuitShList.add(project);
			else if (project.getRiskLevelName().equals("可疑"))
				isLawsuiKyList.add(project);
			else if (project.getRiskLevelName().equals("正常"))
				isLawsuiZcList.add(project);
			else if (project.getRiskLevelName().equals("关注"))
				isLawsuiGzLlist.add(project);
			else if (project.getRiskLevelName().equals("次级"))
				isLawsuiCjLlist.add(project);
		}
		isMap.put("其他", projectGuarantySumGroupByRisk(isLawsuiQtLlist));
		isMap.put("次级", projectGuarantySumGroupByRisk(isLawsuiCjLlist));
		isMap.put("关注", projectGuarantySumGroupByRisk(isLawsuiGzLlist));
		isMap.put("正常", projectGuarantySumGroupByRisk(isLawsuiZcList));
		isMap.put("可疑", projectGuarantySumGroupByRisk(isLawsuiKyList));
		isMap.put("损失", projectGuarantySumGroupByRisk(isLawsuitShList));
		// 查询已诉讼案件
		List<Pro_project> projects2 = projectMapper.selectProjectNotLawsuitByWheresql(idsSql);
		List<Pro_project> notLawsuitShList = new ArrayList<>();
		List<Pro_project> notLawsuiKyList = new ArrayList<>();
		List<Pro_project> notLawsuiZcList = new ArrayList<>();
		List<Pro_project> notLawsuiGzLlist = new ArrayList<>();
		List<Pro_project> notLawsuiCjLlist = new ArrayList<>();
		List<Pro_project> notLawsuiQtLlist = new ArrayList<>();
		Map<String, Object> notMap = new HashMap<>();
		for (Pro_project project : projects2) {
			if (null == project.getRiskLevelName())
				notLawsuiQtLlist.add(project);
			else if (project.getRiskLevelName().equals("损失"))
				notLawsuitShList.add(project);
			else if (project.getRiskLevelName().equals("可疑"))
				notLawsuiKyList.add(project);
			else if (project.getRiskLevelName().equals("正常"))
				notLawsuiZcList.add(project);
			else if (project.getRiskLevelName().equals("关注"))
				notLawsuiGzLlist.add(project);
			else if (project.getRiskLevelName().equals("次级"))
				notLawsuiCjLlist.add(project);
		}
		notMap.put("损失", projectGuarantySumGroupByRisk(notLawsuitShList));
		notMap.put("可疑", projectGuarantySumGroupByRisk(notLawsuiKyList));
		notMap.put("正常", projectGuarantySumGroupByRisk(notLawsuiZcList));
		notMap.put("关注", projectGuarantySumGroupByRisk(notLawsuiGzLlist));
		notMap.put("次级", projectGuarantySumGroupByRisk(notLawsuiCjLlist));
		notMap.put("其他", projectGuarantySumGroupByRisk(notLawsuiQtLlist));
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("未诉", notMap);
		returnMap.put("已诉", isMap);
		// List<Map<String, Object>> list = new ArrayList<>();
		// list.add(notMap);
		// list.add(isMap);
		return returnMap;
	}

	@Override
	public List<Pro_project> projectCostCount(String string) {
		return projectMapper.projectCostCount(string);
	}

	// 初始化累计利息
	public Pro_projectoverdue insertInterest2(Pro_project project) {
		// 利率
		Double lilv = project.getBankRate() / 100 / 360;
		Date startTime = project.getLoadBeginDate();
		Date endTime = project.getDelayEndDate();
		Date now = new Date();
		Double interest = 0D;
		Double capital = project.getLoadSum();

		List<Pro_factPay> payList = factPayMapper.selectFactPayListByWheresql("and project_ID = '"
				+ project.getProject_ID() + "'" + "and factDate < '" + DateUtil.dateStr(endTime, "yyyy-MM-dd") + "'");
		// 判断还款记录
		if (null == payList || payList.isEmpty()) {
			// 无还款记录
			int day = DateUtil.getDaysBetweenDates(startTime, endTime) + 1;
			if (endTime.getTime() > now.getTime()) {
				day = DateUtil.getDaysBetweenDates(startTime, now) + 1;
			}
			interest = BigDecimalUtil.mul(lilv, BigDecimalUtil.mul(project.getLoadSum(), day));
		} else {
			// 有还款记录
			Date payStart = startTime;

			// for(Pro_factPay factPay : payList) {
			for (int i = 0; i < payList.size(); i++) {
				Pro_factPay factPay = payList.get(i);
				Date payEnd = factPay.getFactDate();
				int payDay = DateUtil.getDaysBetweenDates(payStart, payEnd);
				// 计算上一次未还本金的利息
				if (i == 0) {
					payDay += 1;
				}
				double paySum = BigDecimalUtil.mul(lilv, BigDecimalUtil.mul(capital, payDay));
				interest = BigDecimalUtil.add(interest, paySum);
				payStart = payEnd;
				// 计算下一次未还本金、利息
				capital = BigDecimalUtil.sub(capital, factPay.getPayCapitalSum());
				interest = BigDecimalUtil.sub(interest, factPay.getPayInterestSum());
				// }
			}
			// 判断是否逾期
			Date payEnd = endTime;
			if (endTime.getTime() > now.getTime()) {
				payEnd = now;
			}
			int payDay = DateUtil.getDaysBetweenDates(payStart, payEnd);
			double paySum = BigDecimalUtil.mul(lilv, BigDecimalUtil.mul(capital, payDay));
			interest = BigDecimalUtil.add(interest, paySum);

		}

		// project

		// 封装实体
		Pro_projectoverdue overdue = new Pro_projectoverdue();
		overdue.setOverdue_ID(Tool.createUUID32());
		overdue.setProject_ID(project.getProject_ID());
		overdue.setStartTime(startTime);
		overdue.setEndTime(now);
		overdue.setCapital(capital);
		overdue.setInterest(interest);
		overdue.setDefautInterest(interest);
		overdue.setCompoundInterest(0D);
		overdue.setInterestSum(interest);
		overdue.setCreateTime(now);
		// overdueMapper.insertOneOverdueInfo(overdue);
		return overdue;
	}

	public static void main(String[] args) {

		// Date start = DateUtil.valueOf("2014-07-31", DateUtil.DATE_PATTERN);
		// Date end = DateUtil.valueOf("2015-07-09", DateUtil.DATE_PATTERN);
		// Double lilv = 0.224/100/360;
		// BigDecimalUtil.add(5203788.89 ,18554760.000000 );
		// Double interest =
		// BigDecimalUtil.mul(lilv,BigDecimalUtil.mul(29850000,624));
		//
		// Double b = BigDecimalUtil.sub(100D, 100D);
		// Date data = new Date();
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(data);
		//
		// int i= calendar.get(Calendar.DAY_OF_MONTH);
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		// String str1 = "2018-02-05";
		// String str2 = "2018-07-05";
		// Calendar bef = Calendar.getInstance();
		// Calendar aft = Calendar.getInstance();
		// try {
		// bef.setTime(sdf.parse(str1));
		// aft.setTime(sdf.parse(str2));
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// int startMonth = bef.get(Calendar.MONTH)+1;
		// int startYear = bef.get(Calendar.YEAR);
		// String d1 = startYear + "-" + startMonth + "-" + 20;
		// int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
		// int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
		//// System.out.println(d1);
		//// System.out.println(Math.abs(month + result));
		// //System.out.println("d="+
		// i);//DateUtil.getDaysBetweenDates(start,end)+1
		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		// Calendar nowCalendar = Calendar.getInstance();
		// nowCalendar.setTime(new Date());
		// nowCalendar.add(Calendar.MONTH, -1);
		// String stardtr = formatter.format(nowCalendar.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str1 = "2017-12-15";
		String str2 = "2018-08-25";

		Calendar bef = Calendar.getInstance();
		Calendar aft = Calendar.getInstance();
		try {
			bef.setTime(sdf.parse(str1));
			aft.setTime(sdf.parse(str2));

			int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
			int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;

			Math.abs(month + result);
			System.out.println(sdf.parse(str1).before(sdf.parse(str2)));
			System.out.println(Math.abs(month + result) / 3);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setInterestMethod() {
		List<Pro_project> list = projectMapper.selectProjectListForInterestByWheresql(
				"AND InterestMethodName IS NOT NULL AND InterestMethodID IS NULL");
		for (Pro_project pro_project : list) {
			if (pro_project.getInterestMethodName().equals("贷款到期一次性还本付息")
					|| pro_project.getInterestMethodName().equals("贷款到期一次性付息")
					|| pro_project.getInterestMethodName().equals("利随本清")) {
				pro_project.setInterestMethodID("b85bf0a498964da49851e1d26b9a26d9");
			} else if (pro_project.getInterestMethodName().equals("按月付息一次性还本")
					|| pro_project.getInterestMethodName().equals("按月付息")) {
				pro_project.setInterestMethodID("fb089b600736427698abfb6c9706349c");
			} else {
				pro_project.setInterestMethodID("2bc16cfc9d3a41fa925bb6b385f155b8");
			}
			projectMapper.updateOneProjectInfo(pro_project);
		}
	}

	@Override
	public void updateProjectName() {
		List<Pro_project> projects = projectMapper.updateProjectName();
		for (Pro_project pro_project : projects) {
			pro_project.setProjectName(pro_project.getClientName());
			projectMapper.updateOneProjectInfo(pro_project);
		}
	}
	
	public Double projectGuarantySumGroupByRisk(List<Pro_project> projects) {
		Double guarantySum = 0D;
		for (Pro_project pro_project : projects) {
			guarantySum+=pro_project.getGuarantySum();
		}
		return guarantySum;
	}

	@Override
	public List<Pro_project> projectCostRiskLevel(String string) {
		// TODO Auto-generated method stub
		List<Pro_project> projects = projectMapper.projectCostRiskLevel(string);
		return projects;
	}
	
	@Override
	public BigDecimal findSumByRelationMainName(String relationMainName) {
		
		BigDecimal loadSum = projectMapper.findSumByRelationMainName(relationMainName);
		
		return loadSum;
	}
	 
}