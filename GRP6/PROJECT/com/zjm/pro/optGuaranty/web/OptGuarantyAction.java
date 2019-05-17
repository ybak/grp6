package com.zjm.pro.optGuaranty.web;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.zjm.common.db.model.AjaxRes;
import com.zjm.common.db.model.PageTable;
import com.zjm.crm.client.service.ClientService;
import com.zjm.crm.db.model.Client;
import com.zjm.gworkFlow.db.map.OsGworkflowInstanceMapper;
import com.zjm.gworkFlow.db.model.OsGworkflowInstance;
import com.zjm.gworkFlow.db.model.OsGworkflowProjsuggest;
import com.zjm.gworkFlow.flowBuild.service.OsGworkflowProjsuggestService;
import com.zjm.pro.apply.service.ProjectApplyService;
import com.zjm.pro.costReturn.service.CostReturnService;
import com.zjm.pro.db.model.Pro_apply;
import com.zjm.pro.db.model.Pro_costReturn;
import com.zjm.pro.db.model.Pro_optGuaranty;
import com.zjm.pro.db.model.Pro_project;
import com.zjm.pro.db.model.Pro_projectfiles;
import com.zjm.pro.optGuaranty.service.OptGuarantyService;
import com.zjm.pro.project.service.ProjectService;
import com.zjm.pro.projectfiles.service.ProjectfilesService;
import com.zjm.sys.db.model.C_dictype;
import com.zjm.sys.dicType.service.DicTypeService;
import com.zjm.sys.util.RolesDataAccreditUtil;
import com.zjm.util.CustomDispatchServlet;
import com.zjm.util.SystemSession;
import com.zjm.util.Tool;

/**
*  @description 担保措施action
*  @company http://www.igit.com.cn/
*  @author wuhn	
*  @date  2017年7月4日 上午9:48:55
*/
@Controller
@RequestMapping(value="/optGuarantyAction")
public class OptGuarantyAction {
	

	@Resource
	private OptGuarantyService  optGuarantyService;
	
	@Autowired
	private DicTypeService  dicTypeService; //单级字典
	
	@Resource
	private ClientService   clientService;
	
	@Resource
	private ProjectfilesService  projectfilesService;
	
	@Resource
	private ProjectApplyService projectApplyService;
	
	@Resource
	private ProjectService projectService;
	
	@Resource
	private CostReturnService costReturnService;
	
	@Resource
	private OsGworkflowInstanceMapper osGworkflowInstanceMapper;
	
	@Resource
	private OsGworkflowProjsuggestService osGworkflowProjsuggestService;

	
	/**
	 * @description  初始化进入担保措施管理页面
	 * @author wuhn
	 * @date 2017年7月4日 下午3:55:22
	 */
	@RequestMapping(value="/initOptGuaranty")
	public ModelAndView  initOptGuaranty(String flag,String entityID ,String comming){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		if(flag != null && "project".equals(flag)){
			modeAndView.setViewName("/project/opt/optManager/optManagerList2");
		}else{
			modeAndView.setViewName("/project/opt/optManager/optManagerList");
			String sql=" and apply_ID = '"+entityID+"'";
			Pro_apply apply = projectApplyService.selectOneApplyByWhereSql(sql);
			modeAndView.getModel().put("apply", apply);
			if(null != flag && "luoshi".equals(flag)){
				modeAndView.getModel().put("luoshi", flag);
			}
		}
		
		if( null != comming && "projectDetail".equals(comming)){
			modeAndView.setViewName("/project/opt/optManager/optManagerInfo");
		}

		return modeAndView;
	}
	
	/**
	 * @description	初始化进入  已释放/处置保证措施 页面
	 * @author wuhn
	 * @date 2017年7月4日 下午3:56:36
	 */
	@RequestMapping(value="/initOptDispose")
	public ModelAndView  optDispose(){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		modeAndView.setViewName("/project/opt/optDispose/optDisposeList");
		return modeAndView;
	}
	
	/**
	 * @description  进入  已释放---高级查询页面
	 * @author wuhn
	 * @date 2017年7月5日 上午10:14:45
	 */
	@RequestMapping(value="/optReleaseQuery")
	public ModelAndView  optReleaseQuery(){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		modeAndView.setViewName("/project/opt/optDispose/optReleaseQuery");
		getDicType(modeAndView);
		return modeAndView;
	}
		
	
	/**
	 * @description  进入  已处置---高级查询页面
	 * @author wuhn
	 * @date 2017年7月5日 上午10:14:45
	 */
	@RequestMapping(value="/optDisposeQuery")
	public ModelAndView  optDisposeQuery(){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		modeAndView.setViewName("/project/opt/optDispose/optDisposeQuery");
		getDicType(modeAndView);
		return modeAndView;
	}
	
	/**
	 * @description  获取单级字典 ---保证方式、反担保物类型, 处置方式
	 * @author wuhn
	 * @date 2017年7月5日 上午11:08:25
	 */
	private void getDicType(ModelAndView modeAndView) {
		//保证方式类型
		List<C_dictype> baozhengList = dicTypeService.selectAllDicTypeList(" and dicTypePID='ba8a5545c9d8495ba6d3bd014d235c4e'");
		//保证方式类型为：抵押 的 反担保物类型
		List<C_dictype> DYoptTypeList = dicTypeService.selectAllDicTypeList(" and dicTypePID='12b5eece874947de8e692a31939cda44'");
		//保证方式类型为：质押 的 反担保物类型
		List<C_dictype> ZYoptTypeList = dicTypeService.selectAllDicTypeList(" and dicTypePID='686f264405e549b299a7ed815ea289d6'");
		// 担保措施管理--- 处置方式类型
		List<C_dictype> chuzhiList = dicTypeService.selectAllDicTypeList(" and dicTypePID='a5d161dd347342f4965d169ce970d9a4'");
		
		// 担保措施管理--- 反担保方式
		List<C_dictype> guaranteeList = dicTypeService.selectAllDicTypeList(" and dicTypePID='e0b5c4e0a5ed4b6dada5fc3b195b3d43'");
		
		modeAndView.getModel().put("baozhengList", baozhengList);
		modeAndView.getModel().put("DYoptTypeList", DYoptTypeList); 
		modeAndView.getModel().put("ZYoptTypeList", ZYoptTypeList); 
		modeAndView.getModel().put("chuzhiList", chuzhiList); 
		modeAndView.getModel().put("guaranteeList", guaranteeList); 
	}

	/**
	 * @description  保证措施分页列表
	 * @author wuhn
	 * @date 2017年7月4日 上午11:22:55
	 */
	@RequestMapping(value="/selectOptGuarantyPageTables",method=RequestMethod.POST)
	@ResponseBody
	public AjaxRes  selectOptGuarantyPageTables(@RequestBody PageTable<Pro_optGuaranty> pageTable){
		pageTable.getQueryCondition().setUser_uid(SystemSession.getUserSession().getUser_uid());//设置用户id通过匹配用户权限来查询
		String wheresql = "";
		String version = "v1";
		if("" != pageTable.getQueryCondition().getClient_ID()&&null != pageTable.getQueryCondition().getClient_ID()){
			wheresql = "and po.clientID = \'" + pageTable.getQueryCondition().getClient_ID()+"\'";
		}
		//菜单页查副版本v2       选择反担保物查主版本v1
		if(null!=pageTable.getQueryCondition().getMark()&&pageTable.getQueryCondition().getMark().equals("1"))//选择担保措施新增数据   v2版本
			version = "v1";
		else 
			version = "v2";
		wheresql = wheresql +" and po.versions= \'"+version+"\'";
		wheresql = wheresql + queryCondition(pageTable);
		pageTable.setWheresql(wheresql);
		pageTable= optGuarantyService.selectOptGuarantyPageTables(pageTable);
		pageTable.setWheresql("");
		AjaxRes ar =new AjaxRes();
		ar.setSucceed(pageTable);
		return ar;
	}

	/**
	 * @description	分页列表查询条件
	 * @author wuhn
	 * @date 2017年7月4日 上午10:26:27
	 */
	private String queryCondition(PageTable<Pro_optGuaranty> pageTable) {
		
		StringBuffer sb= new StringBuffer();
		sb.append(" and po.unit_uid ='"+ SystemSession.getUserSession().getUnit_uid() +"'");// 添加机构id
		if(pageTable == null){
			return sb.toString();
		}
		
		//根据权限查看用户可查看到的数据sql
		if(null != pageTable.getQueryCondition().getUser_uid() && !"".equals(pageTable.getQueryCondition().getUser_uid())){
			String sql = RolesDataAccreditUtil.appendProSqlByRole(pageTable.getQueryCondition().getUser_uid(), RolesDataAccreditUtil.PRO_AMAN_SQL_STR, "pa.");
			if( null != sql ){
				sb.append(sql);
			}
		}
		
		//搜索框功能
		//当查询条件中包含中文时，get请求默认会使用ISO-8859-1编码请求参数，在服务端需要对其解码
		if ( null != pageTable.getSearchText()) {
			sb.append(" and pa.projectName like \'%"+pageTable.getSearchText().trim()+"%\'");
		}
		
		//项目编号 
		String busiCode = pageTable.getQueryCondition().getBusiCode();
		if(null != busiCode && !"".equals(busiCode)){
			sb.append(" and pa.busiCode like '%"+busiCode+"%'");
		}
		
		//项目名称 
		String projectName = pageTable.getQueryCondition().getProjectName();
		if(null != projectName && !"".equals(projectName)){
			sb.append(" and pa.projectName like '%"+projectName+"%'");
		}
		
		//权属人名称
		String ownerName = pageTable.getQueryCondition().getOwnerName();
		if(null != ownerName && !"".equals(ownerName)){
			sb.append(" and po.ownerName like '%"+ownerName+"%'");
		}
		
		//保证方式
		String guarantyTypeID = pageTable.getQueryCondition().getGuarantyTypeID();
		if(null != guarantyTypeID && !"".equals(guarantyTypeID)){
			sb.append(" and po.guarantyTypeID = '"+guarantyTypeID+"'");
		}
		
		//反担保物类型
		 String optTypeID = pageTable.getQueryCondition().getOptTypeID();
		if(null != optTypeID && !"".equals(optTypeID)){
			sb.append(" and po.optTypeID = '"+optTypeID+"'");
		}
		//经办人
		 String updateUserName = pageTable.getQueryCondition().getUpdateUserName();
		if(null != updateUserName && !"".equals(updateUserName)){
			sb.append(" and po.updateUserName = '"+updateUserName+"'");
		}
		
		//经办部门
		String depart_uid = pageTable.getQueryCondition().getDepart_uid();
		if(null != depart_uid && !"".equals(depart_uid)){

		}
		
		//是否已落实
		 Integer isWorkable = pageTable.getQueryCondition().getIsWorkable();
		if(null != isWorkable && !"".equals(isWorkable)){
			sb.append(" and po.isWorkable ='"+isWorkable+"'");
		}
		
		//是否已解除
		Integer isFree = pageTable.getQueryCondition().getIsFree();
		if(null != isFree && !"".equals(isFree)){
			sb.append(" and po.isFree ='"+isFree+"'");
		}
		
		//是否已处置
		 Integer isDispose = pageTable.getQueryCondition().getIsDispose();
		if(null != isDispose && !"".equals(isDispose)){
			sb.append(" and po.isDispose ='"+isDispose+"'");
		}
		
		//解除日期
		String freeBeginDate = pageTable.getQueryCondition().getFreeBeginDate();
		String freeEndDate = pageTable.getQueryCondition().getFreeEndDate();
		if(null != freeBeginDate && !"".equals(freeBeginDate)){
			if(null != freeEndDate && !"".equals(freeEndDate)){
				sb.append(" and po.freeDate >='"+freeBeginDate+"'"+" and po.freeDate <= '"+freeEndDate+"'");
			}else{
				sb.append(" and po.freeDate >='"+freeBeginDate+"'");
			}
		}else{
			if(null !=freeEndDate && !"".equals(freeEndDate)){
				sb.append(" and po.freeDate <= '"+freeEndDate+"'");
			}
		}
		
		//处置日期
		String disposeBeginDate = pageTable.getQueryCondition().getDisposeBeginDate();
		String disposeEndDate = pageTable.getQueryCondition().getDisposeEndDate();
		if(null != disposeBeginDate && !"".equals(disposeBeginDate)){
			if(null != disposeEndDate && !"".equals(disposeEndDate)){
				sb.append(" and po.disposeDate >='"+disposeBeginDate+"'"+" and po.disposeDate <= '"+disposeEndDate+"'");
			}else{
				sb.append(" and po.disposeDate >='"+disposeBeginDate+"'");
			}
		}else{
			if(null != disposeEndDate && !"".equals(disposeEndDate)){
				sb.append(" and po.disposeDate <= '"+disposeEndDate+"'");
			}
		}
		
		//已解除经办人
		String freeUserID = pageTable.getQueryCondition().getFreeUserID();
		if(freeUserID != null && !"".equals(freeUserID)){
			sb.append(" and po.freeUserID = '"+freeUserID+"'");
		}
		//已处置经办人
		String disposeUserId = pageTable.getQueryCondition().getDisposeUserId();
		if(disposeUserId != null && !"".equals(disposeUserId)){
			sb.append(" and po.disposeUserId = '"+disposeUserId+"'");
		}
		
		String apply_ID = pageTable.getQueryCondition().getApply_ID();
		if(apply_ID !=null && !"".equals(apply_ID)){
			sb .append(" and pa.apply_ID = '"+apply_ID+"'");
		}
		
		String entityID = pageTable.getQueryCondition().getEntityID();
		if(entityID !=null && !"".equals(entityID)){
			sb .append(" and pa.apply_ID = '"+entityID+"'");
		}
		
		
		String optGuaranty_ID = pageTable.getQueryCondition().getOptGuaranty_ID();//保证措施ID
		if(optGuaranty_ID !=null && !"".equals(optGuaranty_ID)){
			String[] split = optGuaranty_ID.split(",");
			String ids ="'"+org.apache.commons.lang3.StringUtils.join(split, "','")+"'";
			sb.append(" and po.optGuaranty_ID in ("+ids+")");
		}
		
		if(pageTable.getSortName()!=null && !pageTable.getSortName().equals("") && pageTable.getSortOrder()!=null && !pageTable.getSortOrder().equals("")){
			sb.append(" ORDER BY "+pageTable.getSortName().trim()+"  " +pageTable.getSortOrder()+"");
		}else{
			sb.append(" ORDER BY po.updateDateTime desc");
		}
		
		return sb.toString();
	}
	
	/**
	 * @description	保证措施管理--进入 新增保证措施 页面
	 * @author wuhn
	 * @date 2017年7月5日 15:41:22
	 */
	@RequestMapping(value="/addOptGuarantyPage")
	public ModelAndView  addOptGuaranty(String apply_ID){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		getDicType(modeAndView);
		if(null != apply_ID && !"".equals(apply_ID)){
			Pro_apply apply = projectApplyService.selectOneApplyByWhereSql(" and apply_ID='"+apply_ID+"'");
			modeAndView.getModel().put("apply", apply);
			modeAndView.getModel().put("apply_ID", apply_ID);
		}
		modeAndView.setViewName("/project/opt/optManager/optGuarantyAdd");
		return modeAndView;
	}
	
	/**
	 * @description	保证措施管理--进入 新增保证措施 -- 选择项目名称 -- 页面
	 * @author wuhn
	 * @throws InterruptedException 
	 * @date 2017年7月5日 15:41:22
	 */
	@RequestMapping(value="/chooseProjectPage")
	public ModelAndView  chooseProject(String flag) throws InterruptedException{
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		if("01".equals(flag)){//加载 企业类型权属人新增页面 
			modeAndView.setViewName("/project/opt/optManager/companyOwnerAdd");
		}else{
			modeAndView.setViewName("/project/opt/optManager/personOwnerAdd");
		}
		return modeAndView;
	}
	
	/**
	 * @description	保证措施管理--进入 新增保证措施 -- 下一步  页面
	 * @author wuhn
	 * @date 2017年7月5日 15:41:22
	 */
	@RequestMapping(value="/addOptGuarantyNextPage")
	public ModelAndView  addOptGuarantyNext(Pro_optGuaranty  optGuaranty){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		getDicType(modeAndView);
		optGuaranty.setOptGuaranty_ID(Tool.createUUID32());
		modeAndView.getModel().put("opt", optGuaranty);
		chooseOptGuaranty(optGuaranty,modeAndView);
		System.out.println(JSON.toJSONString(optGuaranty));
		return modeAndView;
	}
	
	/**
	 * @description	根据 保证方式，反担保物类型  跳转对应的反担保物品页面
	 * @author wuhn
	 * @date 2017年7月6日 下午4:32:42
	 */
	private void chooseOptGuaranty(Pro_optGuaranty optGuaranty , ModelAndView modeAndView) {
			String _guarantyType = optGuaranty.getGuarantyTypeID(); //保证方式id
			String _optType = optGuaranty.getOptTypeID(); // 反担保物类型id
			String pageFlag = optGuaranty.getPageFlag(); //页面标识
				
		if(_guarantyType.equals("c0b07f297c6f4e23981d9e1fed84c5f9")){ //c0b07f297c6f4e23981d9e1fed84c5f9 质押
			if(_optType.equals("f78302a7e603483983546d21f1f49f5f")){//f78302a7e603483983546d21f1f49f5f 股权
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYequityPledgeEdit");
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYequityPledgeView");
				}else{
					Client client=new Client();
					client.setClient_ID(optGuaranty.getClientID());
					client = clientService.selectOneClientInfo(client);
					String clientTypeID = client.getClientTypeID();
					if("01".equals(clientTypeID)){//质押股权权属人类型是：企业
						modeAndView.getModel().put("client", client);
					}
					modeAndView.setViewName("/project/opt/optManager/ZYequityPledge");//  1
				}
			}else if(_optType.equals("b0b4fab401a34a67969c3a3703b43860")){//b0b4fab401a34a67969c3a3703b43860 股票
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYstockPledgeEdit");  // 1
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYstockPledgeView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/ZYstockPledge"); // 1
				}
			}else if(_optType.equals("ce31dcbfe2444232a87127f1e6a54068")){// ce31dcbfe2444232a87127f1e6a54068  土地租赁权
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYlandLeasePledgeEdit");//1
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYlandLeasePledgeView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/ZYlandLeasePledge");//1
				}
			}else if(_optType.equals("62cfddd7d6d3419190d847c83f7f22a6")){// 62cfddd7d6d3419190d847c83f7f22a6 应收账款
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYreceivablesPledgeEdit");//1
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYreceivablesPledgeView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/ZYreceivablesPledge");//1
				}
			}else if(_optType.equals("21272aff7d2b41189d000cbbf8c86367")){// 21272aff7d2b41189d000cbbf8c86367 物件质押
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYarticlePledgeEdit");//1
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYarticlePledgeView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/ZYarticlePledge");//1
				}
			}else if(_optType.equals("df062f6440c143d6a7533ac9c5bcabcd")){// df062f6440c143d6a7533ac9c5bcabcd 存单质押
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZydepositPledgeEdit");//1
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZydepositPledgeView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/ZydepositPledge");//1
				}
			}else if(_optType.equals("84a0787c08874c89845d55c81c6a237e") ){// 84a0787c08874c89845d55c81c6a237e 专利权
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYpatentPledgeEdit");//1
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYpatentPledgeView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/ZYpatentPledge");//1
				}
			}else if(_optType.equals("63fbad5868e74acfbb5f12c16dc546ef")){// 63fbad5868e74acfbb5f12c16dc546ef 商标质押
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYbrandPledgeEdit");
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYbrandPledgeView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/ZYbrandPledge");
				}
			}else if(_optType.equals("26fe32eb65354c21828ff408d1f17403")){// 26fe32eb65354c21828ff408d1f17403  存货
				if("edit".equals(pageFlag)){	
					modeAndView.setViewName("/project/opt/optManager/ZYinventoryPledgeEdit");//1
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYinventoryPledgeView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/ZYinventoryPledge");//1
				}
			}else if(_optType.equals("3a4fc65dd7fb4763b1c916976e4324a0")){	// 3a4fc65dd7fb4763b1c916976e4324a0  库存
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYrepertoryPledgeEdit");//1
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYrepertoryPledgeView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/ZYrepertoryPledge");//1
				}
			}else if(_optType.equals("b5dfc9c1d86446c6919fec58c68fe14c")){// b5dfc9c1d86446c6919fec58c68fe14c  其他质押
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYotherPledgeEdit");
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/ZYotherPledgeView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/ZYotherPledge");
				}
			}
		}else if(_guarantyType.equals("596d424eb6594b1485ecc724a2533c1c")){// 596d424eb6594b1485ecc724a2533c1c  抵押
			if(_optType.equals("6d435c4ef8fc4586abe53bd805c5678c")){// 6d435c4ef8fc4586abe53bd805c5678c 土地
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/DYlandGuarantyEdit");//1
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/DYlandGuarantyView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/DYlandGuaranty");//1
				}
			}else if(_optType.equals("b2732e3492c948918ef5c1939cd6fe50")){// b2732e3492c948918ef5c1939cd6fe50  房产
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/DYhouseGuarantyEdit");
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/DYhouseGuarantyView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/DYhouseGuaranty");
				}
			}else if(_optType.equals("eeef485a1ccc4363b04e0ea656f4eb0e")){// eeef485a1ccc4363b04e0ea656f4eb0e  在建工程
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/DYconstructionGuarantyEdit");
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/DYconstructionGuarantyView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/DYconstructionGuaranty");
				}
			}else if(_optType.equals("fefcf7d7d20941659b565e3334f47bb9")){// fefcf7d7d20941659b565e3334f47bb9 机动车
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/DYcarGuarantyEdit");
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/DYcarGuarantyView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/DYcarGuaranty");
				}
			}else if(_optType.equals("03814af5473a4296b4d256fae0cda1a8")){// 03814af5473a4296b4d256fae0cda1a8  机器设备
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/DYmachinesGuarantyEdit");
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/DYmachinesGuarantyView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/DYmachinesGuaranty");
				}
			}else if(_optType.equals("772ddcd946be493f8f32a6e0be7106d1")){// 772ddcd946be493f8f32a6e0be7106d1  其他抵押 
				if("edit".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/DYotherGuarantyEdit");
				}else if("view".equals(pageFlag)){
					modeAndView.setViewName("/project/opt/optManager/DYotherGuarantyView");
				}else{
					modeAndView.setViewName("/project/opt/optManager/DYotherGuaranty");
				}
			}
		}else if(_guarantyType.equals("4efae31cb89847b598faf3a05273f0fa")){ //4efae31cb89847b598faf3a05273f0fa 企业信用
			if("edit".equals(pageFlag)){
				modeAndView.setViewName("/project/opt/optManager/enterpriseCreditEdit");
			}else if("view".equals(pageFlag)){
				modeAndView.setViewName("/project/opt/optManager/enterpriseCreditView");
			}else{
				modeAndView.setViewName("/project/opt/optManager/enterpriseCredit");
			}
		}else if(_guarantyType.equals("6305ed68f1674830ad65b420109c6340")){// 6305ed68f1674830ad65b420109c6340  个人信用
			if("edit".equals(pageFlag)){
				modeAndView.setViewName("/project/opt/optManager/personalCreditEdit");
			}else if("view".equals(pageFlag)){
				modeAndView.setViewName("/project/opt/optManager/personalCreditView");
			}else{
				modeAndView.setViewName("/project/opt/optManager/personalCredit");
			}
		}else if(_guarantyType.equals("e2e83da45eea46cba8c6776c5736c78b")){// e2e83da45eea46cba8c6776c5736c78b 保证金
			if("edit".equals(pageFlag)){
				modeAndView.setViewName("/project/opt/optManager/pledgeMoenyCreditEdit");
				//获取保证金种类
				List<C_dictype> list = dicTypeService.selectAllDicTypeList(" and dicTypePID='cc276fa0cc8e47b482d244e3228c7e26'");
				modeAndView.getModel().put("depositTypeList", list);
			}else if("view".equals(pageFlag)){
				modeAndView.setViewName("/project/opt/optManager/pledgeMoenyCreditView");
			}else{
				modeAndView.setViewName("/project/opt/optManager/pledgeMoenyCredit");
				//获取保证金种类
				List<C_dictype> list = dicTypeService.selectAllDicTypeList(" and dicTypePID='cc276fa0cc8e47b482d244e3228c7e26'");
				modeAndView.getModel().put("depositTypeList", list);
			}
			
		}else if(_guarantyType.equals("c1d86a3c256c4004b46b3d20b5a56dd4")){// c1d86a3c256c4004b46b3d20b5a56dd4  其他
			if("edit".equals(pageFlag)){
				modeAndView.setViewName("/project/opt/optManager/otherCreditEdit");
			}else if("view".equals(pageFlag)){
				modeAndView.setViewName("/project/opt/optManager/otherCreditView");
			}else{
				modeAndView.setViewName("/project/opt/optManager/otherCredit");
			}
		}
		
	}

	/**
	 * @description	保证措施管理--进入 新增保证措施 -- 选择 权属人-- 页面
	 * @author wuhn
	 * @date 2017年7月5日 15:41:22
	 */
	@RequestMapping(value="/choosePersonPage")
	public ModelAndView  choosePerson(){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		getDicType(modeAndView);
		modeAndView.setViewName("/project/opt/optManager/choosePerson");
		return modeAndView;
	}
	
	
	/**
	 * @description	保证措施管理--进入  落实 担保措施页面 / 查看已落实保证措施
	 * @author wuhn
	 * @date 2017年7月5日 15:41:22
	 */
	@RequestMapping(value="/realizeOptGuarantyPage")
	public ModelAndView  realizeOptGuaranty(Pro_optGuaranty  optGuaranty){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		Pro_optGuaranty	newOptGuaranty = optGuarantyService.selectOneOptGuarantyInfo(optGuaranty.getOptGuaranty_ID());
		modeAndView.getModel().put("opt", newOptGuaranty);
		getDicType(modeAndView);
		String relieveFlag = optGuaranty.getRelieveFlag();
		if(null != relieveFlag && "realizeView".equals(relieveFlag)){// 查看落实保证措施
			modeAndView.setViewName("/project/opt/optManager/realizeOptGuarantyView");
			
		}else{
			modeAndView.setViewName("/project/opt/optManager/realizeOptGuaranty");
		}
		return modeAndView;
	}
	
	/**
	 * @description	保证措施管理--进入  解除  担保措施页面
	 * @author wuhn
	 * @date 2017年7月5日 15:41:22
	 */
	@RequestMapping(value="/relieveOptGuarantyPage")
	public ModelAndView  relieveOptGuaranty(Pro_optGuaranty  optGuaranty){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		optGuaranty = optGuarantyService.selectOneOptGuarantyInfo(optGuaranty.getOptGuaranty_ID());
		modeAndView.getModel().put("opt", optGuaranty);
		getDicType(modeAndView);
		modeAndView.setViewName("/project/opt/optManager/relieveOptGuaranty");
		return modeAndView;
	}
	
	/**
	 * @description	保证措施管理--进入  落实 担保措施页面
	 * @author wuhn
	 * @date 2017年7月5日 15:41:22
	 */
	@RequestMapping(value="/disposeOptGuarantyPage")
	public ModelAndView  disposeOptGuaranty(Pro_optGuaranty  optGuaranty){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		optGuaranty = optGuarantyService.selectOneOptGuarantyInfo(optGuaranty.getOptGuaranty_ID());
		modeAndView.getModel().put("opt", optGuaranty);
		getDicType(modeAndView);
		modeAndView.setViewName("/project/opt/optManager/disposeOptGuaranty");
		return modeAndView;
	}
	
	/**
	 * @description	保证措施管理--进入  删除所选  担保措施页面
	 * @author wuhn
	 * @date 2017年7月5日 15:41:22
	 */
	@RequestMapping(value="/deleteOptGuarantyPage")
	public ModelAndView  deleteOptGuaranty(){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		getDicType(modeAndView);
		modeAndView.setViewName("/project/opt/optManager/deleteOptGuaranty");
		return modeAndView;
	}
	
	/**
	 * @description	保证措施管理--进入  高级查询   担保措施页面
	 * @author wuhn
	 * @date 2017年7月5日 15:41:22
	 */
	@RequestMapping(value="/highOptQueryPage")
	public ModelAndView  highOptQuery(){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		getDicType(modeAndView);
		modeAndView.setViewName("/project/opt/optManager/highOptQuery");
		return modeAndView;
	}
	
	
	/**
	 * @description	 添加一条保证措施信息
	 * @author wuhn
	 * @date 2017年7月4日 上午10:45:00
	 */
	@RequestMapping(value="/insertOneOptGuarantyInfo")
	@ResponseBody
	public AjaxRes insertOneOptGuarantyInfo(@RequestBody Pro_optGuaranty  optGuaranty,String versions){
		com.zjm.crm.db.model.Client client =new com.zjm.crm.db.model.Client();
		client.setClient_ID(optGuaranty.getClientID());
		client = clientService.selectOneClientInfo(client);
		client.setIsOptGaranty(1);
		clientService.updateOneCompanyClientInfo(SystemSession.getUserSession(), client); //更新客户为: 反担保客户
		
		com.zjm.crm.db.model.Client otherClient =new com.zjm.crm.db.model.Client();
		Integer isOther = optGuaranty.getIsOther();
		if(null != isOther && isOther.equals(1)){//存在第三方权属人
			String otherOwnerID = optGuaranty.getOtherOwnerID();//第三方权属人ID
			if(null !=otherOwnerID && !"".equals(otherOwnerID)){// 第三方权属人存在 更新权属人信息
			}else{// 不存在，添加权属人信息
					setClientValue(optGuaranty,otherClient);
					otherClient = clientService.insertClientAndReturnClientID(SystemSession.getUserSession(), otherClient);
					optGuaranty.setOtherOwnerID(otherClient.getClient_ID());
			}
		}
		
		Integer isCompanyStock = optGuaranty.getIsCompanyStock();//是否存在其他公司股权
		if(null!= isCompanyStock && isCompanyStock.equals(1)){//是其他公司股权
			setClientValue(optGuaranty,otherClient);
			otherClient.setClient_ID(optGuaranty.getOtherOwnerID());
			clientService.updateOneCompanyClientInfo(SystemSession.getUserSession(), otherClient);
		}else{// 不是其他公司股权,更新当前权属人信息
			setClientValue(optGuaranty,otherClient);
			otherClient.setClient_ID(optGuaranty.getClientID());
			clientService.updateOneCompanyClientInfo(SystemSession.getUserSession(), otherClient);
		}
//		optGuaranty.setVersions("v1");
		Boolean info = optGuarantyService.insertOneOptGuarantyInfo(SystemSession.getUserSession(),optGuaranty);
		AjaxRes ar =new AjaxRes();
		ar.setSucceed(info);
		return   ar;
	}
	
	/**
	 * @description	 根据已有的担保物，添加一条保证措施信息
	 * @author wuhn
	 * @date 2017年7月4日 上午10:45:00
	 */
	@RequestMapping(value="/insertOneOptGuarantyInfoByExisting")
	@ResponseBody
	public AjaxRes insertOneOptGuarantyInfoByExisting(@RequestBody Pro_optGuaranty  optGuaranty){
		com.zjm.crm.db.model.Client client =new com.zjm.crm.db.model.Client();
		Boolean info = false;
		//根据id 查询出存在的担保物信息用来新增一条新的担保物信息
		if(null!=optGuaranty.getOptGuarantyIds()&&""!=optGuaranty.getOptGuarantyIds()){
			String[] ids = optGuaranty.getOptGuarantyIds().split(",");
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				//随便取一条用来更改客户信息，因为权属人是一样的
				Pro_optGuaranty selectOptGuaranty = optGuarantyService.selectOneOptGuarantyInfo(id);
				
				com.zjm.crm.db.model.Client otherClient =new com.zjm.crm.db.model.Client();
				client.setClient_ID(selectOptGuaranty.getClientID());
				client = clientService.selectOneClientInfo(client);
				client.setIsOptGaranty(1);
				clientService.updateOneCompanyClientInfo(SystemSession.getUserSession(), client); //更新客户为: 反担保客户
				
				Integer isOther = selectOptGuaranty.getIsOther();
				if(null != isOther && isOther.equals(1)){//存在第三方权属人
					String otherOwnerID = selectOptGuaranty.getOtherOwnerID();//第三方权属人ID
					if(null !=otherOwnerID && !"".equals(otherOwnerID)){// 第三方权属人存在 更新权属人信息
					}else{// 不存在，添加权属人信息
							setClientValue(selectOptGuaranty,otherClient);
							otherClient = clientService.insertClientAndReturnClientID(SystemSession.getUserSession(), otherClient);
							selectOptGuaranty.setOtherOwnerID(otherClient.getClient_ID());
					}
				}
				
				Integer isCompanyStock = selectOptGuaranty.getIsCompanyStock();//是否存在其他公司股权
				if(null!= isCompanyStock && isCompanyStock.equals(1)){//是其他公司股权
					setClientValue(selectOptGuaranty,otherClient);
					otherClient.setClient_ID(selectOptGuaranty.getOtherOwnerID());
					clientService.updateOneCompanyClientInfo(SystemSession.getUserSession(), otherClient);
				}else{// 不是其他公司股权,更新当前权属人信息
					setClientValue(selectOptGuaranty,otherClient);
					otherClient.setClient_ID(optGuaranty.getClientID());
					clientService.updateOneCompanyClientInfo(SystemSession.getUserSession(), otherClient);
				}
				//根据原有数据，修改项目id和客户id后进行保存
				selectOptGuaranty.setClientID(optGuaranty.getClientID());
				selectOptGuaranty.setApply_ID(optGuaranty.getApply_ID());
//				selectOptGuaranty.setVersions("v2");
				info = optGuarantyService.insertOneOptGuarantyInfo(SystemSession.getUserSession(),selectOptGuaranty);
			}
		}
		AjaxRes ar =new AjaxRes();
		ar.setSucceed(info);
		return   ar;
	}
	
	/**
	 * @description	给客户表赋值
	 * @author wuhn
	 * @date 2017年7月13日 下午1:49:54
	 */
	private void setClientValue(Pro_optGuaranty optGuaranty, com.zjm.crm.db.model.Client otherClient) {
		String otherType = optGuaranty.getOtherType();
		otherClient.setClient_ID(optGuaranty.getOtherOwnerID()); 
		otherClient.setClientTypeID(otherType);
		otherClient.setZipCode(optGuaranty.getOtherPostCode());
		otherClient.setIsOptGaranty(1);
		otherClient.setFax(optGuaranty.getOtherFax());
		if("01".equals(otherType)){ // 企业客户
			otherClient.setClientName(optGuaranty.getOtherOwner());
			otherClient.setCertificateCode(optGuaranty.getOtherCreditCode());
			otherClient.setLegalPerson(optGuaranty.getOtherLegalPerson());
			otherClient.setPhoneOne(optGuaranty.getOtherLegalPhone());
			otherClient.setWorkAddress(optGuaranty.getOtherLegalAddress());
		}else{
			otherClient.setPersonName(optGuaranty.getOtherOwner());
			otherClient.setPersonNum(optGuaranty.getOtherPersonNum());
			otherClient.setPhone(optGuaranty.getOtherPersonPhone());
			otherClient.setHouseAddress(optGuaranty.getOtherPersonAddress());
		}
	}

	/**
	 * @description    进入修改担保措施页面
	 * @author wuhn
	 * @date 2017年7月4日 上午10:47:24
	 */
	@RequestMapping(value="/updateOptGuarantyPage")
	public ModelAndView updateOptGuarantyPage(Pro_optGuaranty optGuaranty){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		getDicType(modeAndView);
		chooseOptGuaranty(optGuaranty, modeAndView);
		optGuaranty = optGuarantyService.selectOneOptGuarantyInfo(optGuaranty.getOptGuaranty_ID());
		Integer isCompanyStock = optGuaranty.getIsCompanyStock();//判断是否是其他公司股权--- 质押 --股权
		if(null != isCompanyStock && isCompanyStock.equals(1)){
			Client client=new  Client();
			client.setClient_ID(optGuaranty.getOtherOwnerID());
			client= clientService.selectOneClientInfo(client);
			modeAndView.getModel().put("client", client);
		}else{
			Client client=new  Client();
			client.setClient_ID(optGuaranty.getClientID());
			client= clientService.selectOneClientInfo(client);
			modeAndView.getModel().put("client", client);
		}
		modeAndView.getModel().put("opt", optGuaranty);
		return modeAndView;
	}
	
	/**
	 * @description  修改一条担保措施信息
	 * @author wuhn
	 * @date 2017年7月4日 上午10:49:48
	 */
	@RequestMapping(value="/updateOneOptGuarantyInfo")
	@ResponseBody
	public AjaxRes  updateOneOptGuarantyInfo(@RequestBody  Pro_optGuaranty   optGuaranty){
		
		com.zjm.crm.db.model.Client client =new com.zjm.crm.db.model.Client();
		client.setClient_ID(optGuaranty.getClientID());
		client.setIsOptGaranty(1);
		clientService.updateOneCompanyClientInfo(SystemSession.getUserSession(), client); //更新客户为: 反担保客户
		
		Integer isOther = optGuaranty.getIsOther();
		com.zjm.crm.db.model.Client otherClient =new com.zjm.crm.db.model.Client();
		if(null != isOther && isOther.equals(1)){//存在第三方权属人
			String otherOwnerID = optGuaranty.getOtherOwnerID();//第三方权属人ID
			if(null !=otherOwnerID && !"".equals(otherOwnerID)){// 第三方权属人存在 更新权属人信息
					setClientValue(optGuaranty,otherClient);
				    clientService.updateOneCompanyClientInfo(SystemSession.getUserSession(), otherClient);
			}else{// 不存在，添加权属人信息
					setClientValue(optGuaranty,otherClient);
					otherClient = clientService.insertClientAndReturnClientID(SystemSession.getUserSession(), otherClient);
					optGuaranty.setOtherOwnerID(otherClient.getClient_ID());
			}
		}
		
		Integer isCompanyStock = optGuaranty.getIsCompanyStock();//是否存在其他公司股权
		if(null!= isCompanyStock && isCompanyStock.equals(1)){//是其他公司股权
			setClientValue(optGuaranty,otherClient);
			otherClient.setClient_ID(optGuaranty.getOtherOwnerID());
			clientService.updateOneCompanyClientInfo(SystemSession.getUserSession(), otherClient);
		}else{// 不是其他公司股权,更新当前权属人信息
			setClientValue(optGuaranty,otherClient);
			otherClient.setClient_ID(optGuaranty.getClientID());
			clientService.updateOneCompanyClientInfo(SystemSession.getUserSession(), otherClient);
		}
		
		AjaxRes  ar =new AjaxRes();
		Boolean info = optGuarantyService.updateOneOptGuarantyInfo(SystemSession.getUserSession(), optGuaranty);
		ar.setSucceed(info);
		return ar;
		
	}
	
	/**
	 * @description	 删除一条担保措施信息
	 * @author wuhn
	 * @date 2017年7月4日 上午10:49:26
	 */
	@RequestMapping(value="/deleteOneOptGuarantyInfo")
	@ResponseBody
	public AjaxRes  deleteOneOptGuarantyInfo(Pro_optGuaranty optGuaranty){
		AjaxRes  ar =new AjaxRes();
		Boolean info = optGuarantyService.deleteOneOptGuarantyInfo(
				SystemSession.getUserSession(), optGuaranty.getOptGuaranty_ID());
		ar.setSucceed(info);
		return ar;
		
	}
	
	
	/**
	 * @description	查看 一条担保措施信息
	 * @author wuhn
	 * @date 2017年7月19日 上午9:54:15
	 */
	@RequestMapping(value="/selectOneOptGuarantyInfo")
	public ModelAndView selectOneOptGuarantyInfo(Pro_optGuaranty optGuaranty){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		getDicType(modeAndView);
		chooseOptGuaranty(optGuaranty, modeAndView);
		Pro_optGuaranty	optGuarantyNew = optGuarantyService.selectOneOptGuarantyInfo(optGuaranty.getOptGuaranty_ID());
		optGuarantyNew.setRelieveFlag(optGuaranty.getRelieveFlag());
		
		Integer isCompanyStock = optGuarantyNew.getIsCompanyStock();//判断是否是其他公司股权--- 质押 --股权
		if(null != isCompanyStock && isCompanyStock.equals(1)){
			Client client=new  Client();
			client.setClient_ID(optGuarantyNew.getOtherOwnerID());
			client= clientService.selectOneClientInfo(client);
			modeAndView.getModel().put("client", client);
		}else{
			Client client=new  Client();
			client.setClient_ID(optGuarantyNew.getClientID());
			client= clientService.selectOneClientInfo(client);
			modeAndView.getModel().put("client", client);
		}
		
		modeAndView.getModel().put("opt", optGuarantyNew);
		return modeAndView;
	}
	
	/**
	 * @description 查看已解除担保措施详情
	 * @author wuhn
	 * @date 2017年7月24日 上午10:39:42
	 */
	@RequestMapping(value="/loadRelievePage")
	public  ModelAndView  loadRelievePage(Pro_optGuaranty optGuaranty){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		optGuaranty = optGuarantyService.selectOneOptGuarantyInfo(optGuaranty.getOptGuaranty_ID());
		modeAndView.getModel().put("opt", optGuaranty);
		modeAndView.setViewName("/project/opt/optDispose/optRelieveDetailView");
		return modeAndView;
	}
	
	/**
	 * @description	获取附件列表
	 * @author TLJ
	 * @date 2017年8月1日 下午1:55:03
	 */
	@RequestMapping(value="/selectProFilesListByCheckPlanID")
	@ResponseBody
	public AjaxRes   selectProFilesListByCheckPlanID(String checkPlan_ID){
		AjaxRes ar =new AjaxRes();
		List<Pro_projectfiles> fileList = projectfilesService.selectProFilesListByCheckPlanID(
									SystemSession.getUserSession(), checkPlan_ID);
		ar.setSucceed(fileList);
		return ar;
	}
	/**
	 * @description	获取附件列表
	 * @author wuhn
	 * @date 2017年8月1日 下午1:55:03
	 */
	@RequestMapping(value="/selectProFilesListByEntityID")
	@ResponseBody
	public AjaxRes   selectProFilesListByEntityID(String entityID){
		AjaxRes ar =new AjaxRes();
		List<Pro_projectfiles> fileList = projectfilesService.selectProFilesListByEntityID(
									SystemSession.getUserSession(), entityID);
		ar.setSucceed(fileList);
		return ar;
	}
	
	/**
	 * @description	 删除附件列表
	 * @author wuhn
	 * @date 2017年8月1日 下午1:57:00
	 */
	@RequestMapping(value="/deleteOneInfoByProFiles_ID")
	@ResponseBody
	public AjaxRes   deleteOneInfoByProFiles_ID(HttpServletRequest request,Pro_projectfiles projectfiles ){
		AjaxRes ar =new AjaxRes();
		deleteFile(request,projectfiles);
		Boolean info = projectfilesService.deleteOneInfoByProFiles_ID(SystemSession.getUserSession(), projectfiles.getProjectFiles_ID());
		ar.setSucceed(info);
		return ar;
	}
	
	/**
	 * @description	删除物理文件
	 * @author wuhn
	 * @date 2017年8月1日 下午2:36:04
	 */
	public void deleteFile(HttpServletRequest request,Pro_projectfiles projectfiles){
		String rootPath = request.getServletContext().getRealPath("");
		String filePath=rootPath+projectfiles.getPathFile();
		File file=new File(filePath);
		if(!file.isFile()){
			System.out.println(projectfiles.getPathFile()+"文件不存在.");
		}else{
			file.delete();
		}
	}
	
	
	/**
	 * @description	 抵（质）押物原件管理
	 * @author wuhn
	 * @date 2017年8月21日 下午2:29:39
	 */
	@RequestMapping(value="/initPledgeManagerPage")
	public ModelAndView  pledgeManager(String flag,String entityID){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		if(flag !=null && "project".equals(flag)){
			modeAndView.setViewName("/project/opt/pledgeManager/pledgeManagerList2");
		}else{
			modeAndView.getModel().put("apply_ID", entityID);
			modeAndView.setViewName("/project/opt/pledgeManager/pledgeManagerList");
		}
		return modeAndView;
	}
	
	/**
	 * @description	抵（质）押物原件管理 ---在库记录---入库 页面
	 * @author wuhn
	 * @date 2017年8月29日 下午1:47:46
	 */
	@RequestMapping(value="/putStockPage")
	public ModelAndView putStockPage(String optGuaranty_IDS){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		modeAndView.getModel().put("optGuaranty_IDS", optGuaranty_IDS);
		modeAndView.setViewName("/project/opt/pledgeManager/putStock");
		return modeAndView;
	}
	
	/**
	 * @description	 执行入库操作
	 * @author wuhn
	 * @date 2017年8月29日 下午4:31:51
	 */
	@RequestMapping(value="/putStockExecut")
	@ResponseBody
	public AjaxRes  putStockExecut(@RequestBody Pro_optGuaranty  optGuaranty){
		AjaxRes ar =new AjaxRes();
		String optGuaranty_IDS = optGuaranty.getOptGuaranty_ID();
		String[] split = optGuaranty_IDS.split(",");
		Boolean info=false;
		if(split != null){
			for (String str : split) {
				optGuaranty.setOptGuaranty_ID(str);
				optGuaranty.setCustodyStatus("已入库");
				 info = optGuarantyService.updateOneOptGuarantyInfo(SystemSession.getUserSession(), optGuaranty);
			}
		}
		ar.setSucceed(info);
		return ar;
	}
	
	/**
	 * @description	抵（质）押物原件管理 ---在库记录---借出 页面
	 * @author wuhn
	 * @date 2017年8月29日 下午1:47:46
	 */
	@RequestMapping(value="/loanStockPage")
	public ModelAndView loanStockPage(){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		modeAndView.setViewName("/project/opt/pledgeManager/loanStock");
		return modeAndView;
	}
	
	
	/**
	 * @description	抵（质）押物原件管理 ---在库记录---出库 页面
	 * @author wuhn
	 * @date 2017年8月29日 下午1:47:46
	 */
	@RequestMapping(value="/outStockPage")
	public ModelAndView outStockPage(){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		modeAndView.setViewName("/project/opt/pledgeManager/outStock");
		return modeAndView;
	}
	
	
	/**
	 * @description	抵（质）押物原件管理 ---借出记录---归还 页面
	 * @author wuhn
	 * @date 2017年8月29日 下午1:47:46
	 */
	@RequestMapping(value="/returnStockPage")
	public ModelAndView returnStockPage(){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		modeAndView.setViewName("/project/opt/pledgeManager/returnStock");
		return modeAndView;
	}
	
	/**
	 * @description	抵（质）押物原件管理 ---出库记录---撤销 页面
	 * @author wuhn
	 * @date 2017年8月29日 下午1:47:46
	 */
	@RequestMapping(value="/backoutStockPage")
	public ModelAndView backoutStockPage(){
		ModelAndView modeAndView = CustomDispatchServlet.getModeAndView();
		modeAndView.setViewName("/project/opt/pledgeManager/backoutStock");
		return modeAndView;
	}
	
	
	/**
	 * @description	处理老数据没有版本号问题
	 * @author jchen  将老数据关联项目id置空 (添加版本号v1) ， 新增一条副版本数据   (添加版本号v2)
	 * @date 2017年8月29日 下午1:47:46
	 */
	@RequestMapping(value="/disposeOldData")
	public AjaxRes disposeOldData(){
		AjaxRes ar =new AjaxRes();
		boolean i = false;
		List<Pro_optGuaranty> pro_optGuaranties = optGuarantyService.selectOptGuarantyByWheresql("");// and versions = \'"+""+"\'
		for (Pro_optGuaranty pro_optGuaranty : pro_optGuaranties) {
			Pro_optGuaranty optGuarnNew = new Pro_optGuaranty();
			BeanUtils.copyProperties(pro_optGuaranty, optGuarnNew, Pro_optGuaranty.class);
			//optGuarnNew =pro_optGuaranty;
			optGuarnNew.setVersions("v1");
			optGuarnNew.setApply_ID("");
			optGuarnNew.setOptGuaranty_ID(Tool.createUUID32());
			optGuarantyService.insertOptGuarantyInfo(null, optGuarnNew);
			
			//修改当前对象为副版本信息
			pro_optGuaranty.setVersions("v2");
			pro_optGuaranty.setVersionsId(optGuarnNew.getOptGuaranty_ID());
			optGuarantyService.updateOneOptGuarantyInfo(SystemSession.getUserSession(), pro_optGuaranty);
//			optGuarantyService
//			String optId = optGuaranty.getOptGuaranty_ID();
//			String applyId = optGuaranty.getApply_ID();
//			String applyDetailId = optGuaranty.getApplyDetail_ID();
//			//添加副本
//			optGuaranty.setVersionsId(optId);
//			optGuaranty.setOptGuaranty_ID(Tool.createUUID32());
//			optGuaranty.setApply_ID(applyId);
//			optGuaranty.setApplyDetail_ID(applyDetailId);
//			optGuaranty.setVersions("v2");
//			boolean a = optGuarantyService.insertOneOptGuarantyInfo(SystemSession.getUserSession(), optGuaranty);
//			if(a){//修改主版本
//				optGuaranty.setOptGuaranty_ID(optId);
		//	pro_optGuaranty.setApply_ID("");
			//pro_optGuaranty.setVersions("v1");
			//	i = optGuarantyService.updateOneOptGuarantyInfo(SystemSession.getUserSession(), optGuaranty);
//			}
		}
		/*List<Pro_optGuaranty> pro_optGuaranties2 = optGuarantyService.selectOptGuarantyByWheresql(" and versions = \'"+"v1"+"\'");
		for (Pro_optGuaranty optGuaranty:pro_optGuaranties2) {
			optGuaranty.setVersionsId(optGuaranty.getOptGuaranty_ID());
			optGuaranty.setOptGuaranty_ID(Tool.createUUID32());
			optGuaranty.setApply_ID(optGuaranty.getApply_ID());
			optGuaranty.setVersions("v2");
			int a = optGuarantyService.insertOptGuarantyInfo(SystemSession.getUserSession(), optGuaranty);
//			boolean a = optGuarantyService.insertOneOptGuarantyInfo(SystemSession.getUserSession(), optGuaranty);
		}*/
		ar.setSucceed(i);
		return ar;
	}
	
	/**
	 * 打开退回保证金页面
	 * pro_project
	 * @return
	 */
	@RequestMapping(value="/projectCostReturnPage")
	public ModelAndView projectCostReturnPage(String project_ID){
//		ModelAndView mv = CustomDispatc	hServlet.getModeAndView();
		Pro_project  project= new Pro_project();
		project = projectService.selectOneProjectInfoByWheresql(" and project_ID = \'"+project_ID+"\'");
		Pro_apply apply = new Pro_apply(); // 还款页面中的放款机构数据抽取为资金方数据
		apply = projectApplyService.selectOneApplyByWhereSql("and apply_ID = \'"+project.getApply_ID()+"\' ");
		//获取费用类别
		List<C_dictype> costTypeList = dicTypeService.selectAllDicTypeList(" and dicTypePID='386a21c7b12a45c88a70e462fb0cfdc7'");
//		List<Pro_costReturn> costReturns = costReturnService.selectCostReturnListByWhereSql("and apply_ID = \'"+project.getApply_ID()+"\' ");
		ModelAndView mv = CustomDispatchServlet.getModeAndView();
		mv.getModelMap().put("apply", apply);
		//initSelect(mv);//获取下拉框
		mv.getModelMap().put("project",project);
		mv.getModelMap().put("costTypeList",costTypeList);
		mv.setViewName("/project/apply/projectCostReturn");
		return mv;		
	}
	
	/**
	 * 项目组上传退回保证金通知单
	 * @param businessId
	 * @return
	 */
	@RequestMapping(value="/gworkFlowUploadFile")
	public ModelAndView gworkFlowUploadFile(String businessId){
		ModelAndView mv = CustomDispatchServlet.getModeAndView();
		Pro_costReturn costReturn = null;
		try {
			costReturn = costReturnService.selectOneCostReturnByWhereSql(" and costReturn_ID=\'"+businessId+"\' ");
			//查询业务/授信申请信息表
			Pro_apply apply = projectApplyService.selectOneApplyByWhereSql(" and apply_ID=\'"+costReturn.getApply_ID()+"\' ");
			Pro_project project = projectService.selectOneProjectInfoByWheresql(" and apply_ID=\'"+costReturn.getApply_ID()+"\' ");
			List<Pro_optGuaranty> pro_optGuaranty = optGuarantyService.selectOptGuarantyByWheresql(" and apply_ID=\'"+costReturn.getApply_ID()+"\'"+"and guarantyTypeID = \'"+"e2e83da45eea46cba8c6776c5736c78b"+"\'");
			OsGworkflowInstance instance = new OsGworkflowInstance();
			instance.setBusinessId(businessId);
			instance.setBusinessType(Pro_costReturn.class.getName());
			instance = osGworkflowInstanceMapper.getOneByBusiness(instance);
			
			OsGworkflowProjsuggest osGworkflowProjsuggest = new OsGworkflowProjsuggest();
			osGworkflowProjsuggest.setFlowID(instance.getEntryId());
			List<OsGworkflowProjsuggest> osSuggestList=osGworkflowProjsuggestService.selectAllOsGworkflowProjsuggest(osGworkflowProjsuggest,SystemSession.getUserSession());

			// 考虑到有些项目不需要上会, 评审会决议表信息会在项目刚申请的时候就插入一条, 所以此处meetingResolution一定能查出一条
			List<Pro_projectfiles> list = projectfilesService.selectListProFilesByWhereSql(" and entityID = \'"+apply.getApply_ID()+"\'" + " and fileType = \'"+"03"+"\'");
			mv.getModelMap().put("projectfiles", list);
			mv.getModelMap().put("osSuggestList", osSuggestList);
			mv.getModelMap().put("flowID", instance.getEntryId());//流程实例id
			mv.getModelMap().put("project", project);
			mv.getModelMap().put("optGuaranty", pro_optGuaranty.get(0));
			mv.getModelMap().put("apply", apply);
			mv.getModelMap().put("apply_ID", apply.getApply_ID());
			mv.getModelMap().put("costReturn", costReturn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("/gworkFlow/optreturn/gworkFlowUploadFile");
		return mv;
	}
	
	/**
	 * 委贷还款办理 --财务审核文件
	 */
	@RequestMapping(value="/gworkFlowSelectFile")
	public ModelAndView gworkFlowSelectFile(String businessId){
		ModelAndView mv = CustomDispatchServlet.getModeAndView();
		Pro_costReturn costReturn = null;
		try {
			costReturn = costReturnService.selectOneCostReturnByWhereSql(" and costReturn_ID=\'"+businessId+"\' ");
			//查询业务/授信申请信息表
			Pro_apply apply = projectApplyService.selectOneApplyByWhereSql(" and apply_ID=\'"+costReturn.getApply_ID()+"\' ");
			
			OsGworkflowInstance instance = new OsGworkflowInstance();
			instance.setBusinessId(businessId);
			instance.setBusinessType(Pro_costReturn.class.getName());
			instance = osGworkflowInstanceMapper.getOneByBusiness(instance);
			List<Pro_optGuaranty> pro_optGuaranty = optGuarantyService.selectOptGuarantyByWheresql(" and apply_ID=\'"+costReturn.getApply_ID()+"\'"+"and guarantyTypeID = \'"+"e2e83da45eea46cba8c6776c5736c78b"+"\'");
			OsGworkflowProjsuggest osGworkflowProjsuggest = new OsGworkflowProjsuggest();
			osGworkflowProjsuggest.setFlowID(instance.getEntryId());
			List<OsGworkflowProjsuggest> osSuggestList=osGworkflowProjsuggestService.selectAllOsGworkflowProjsuggest(osGworkflowProjsuggest,SystemSession.getUserSession());

			// 考虑到有些项目不需要上会, 评审会决议表信息会在项目刚申请的时候就插入一条, 所以此处meetingResolution一定能查出一条
			List<Pro_projectfiles> list = projectfilesService.selectListProFilesByWhereSql(" and entityID = \'"+apply.getApply_ID()+"\'" + " and fileType = \'"+"03"+"\'");
			mv.getModelMap().put("projectfiles", list);
			mv.getModelMap().put("osSuggestList", osSuggestList);
			mv.getModelMap().put("flowID", instance.getEntryId());//流程实例id
			mv.getModelMap().put("optGuaranty", pro_optGuaranty.get(0));
			mv.getModelMap().put("apply", apply);
			mv.getModelMap().put("apply_ID", apply.getApply_ID());
			mv.getModelMap().put("costReturn", costReturn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("/gworkFlow/optreturn/gworkFlowSelectFile");
		return mv;
	}
}