<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%-- <%@ include file="/common_timestamp.jsp" %> --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
$('.date-picker').datepicker({autoclose:true}).next().on(ace.click_event, function(){
	$(this).prev().focus();
});
</script>
 <div class="modal fade" id="projectAfterSelect" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"  data-backdrop="static" data-keyboard="false">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">高级查询</h4>
                  </div>
                  <div class="modal-body">

                <form class="form-horizontal" role="form" id="projectAfterSelect_form">
                        <!--   <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">项目编号1： </label>
                              <div class="col-sm-8">
                                  <input type="text"  id="projectCode"  name="projectCode" class="col-xs-10 col-sm-11 " />
                              </div>
                          </div> -->

						<div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">查询时间： </label>
            			        <div class="col-sm-9">
            					  <div class="input-group col-sm-4" style="float:left;">
            						<input  type="text" class="form-control date-picker" name="selectlDateStart"  id="selectlDateStart" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            					   </div> 

            					   <p class="col-sm-1" style="line-height:28px;">-</p>
            					   <div class="input-group col-sm-4">
            						<input  type="text" class="form-control date-picker" name="selectlDateEnd"  id="selectlDateEnd" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            						</div>
            					</div>
                         </div>  
                          
							
                          <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">项目名称： </label>
                              <div class="col-sm-8">
                                  <input type="text" name="projectName" id="projectName"  class="col-xs-10 col-sm-11 ztb_add_mod_name ztb_add validate[maxSize[50]]" />
                              </div>
                          </div>
                          
                           <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">客户所属系： </label>
                              <div class="col-sm-8">
                                  <input type="text" name="relationMainName" id="relationMainName"  class="col-xs-10 col-sm-11 ztb_add_mod_name ztb_add validate[maxSize[50]]" />
                              </div>
                          </div>
						
						<div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">项目编号： </label>
                              <div class="col-sm-8">
                                  <input type="text" name="projectCode" id="projectCode"  class="col-xs-10 col-sm-11 ztb_add_mod_name ztb_add validate[maxSize[50]]" />
                              </div>
                          </div>
						

                       <%--    <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-input">业务性质1： </label>
                             <div class="col-sm-5">
                                  <select id="busiNatureList" class="selectList  col-md-12 "  name="busiNatureID"  >		
										<option value="">&nbsp;请选择</option>
										<c:forEach items="${busiNatureList}" var="busiNature">
											<option value="${busiNature.dicTypeID}">${busiNature.dicTypeName}</option>
										</c:forEach>
			    				  </select>
                             </div>
                          </div> --%>

                       <%--    <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-input">项目类型1： </label>
                             <div class="col-sm-5">
                                <select id="projectTypeList" class="selectList  col-md-12 "  name="projectTypeID01"  >		
										<option value="">&nbsp;请选择</option>
										<c:forEach items="${projectTypeList}" var="projectType">
											<option value="${projectType.dicTypeID}">${projectType.dicTypeName}</option>
										</c:forEach>
				 				 </select>
                             </div>
                          </div> --%>

                          <%-- <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-input">客户类型： </label>
                             <div class="col-sm-5">
                              		  <select id="clientTypeList0" class="selectList  col-md-12 "  name="clientTypeID" >  		
											<option value="">&nbsp;请选择</option>                                    
											<c:forEach items="${clientTypeList}" var="clientType">
												<option value="${clientType.dicTypeID}">${clientType.dicTypeName}</option>
											</c:forEach>
					 				 </select>
                             </div>
                          </div> --%>

                          <!-- <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">客户名称： </label>
                              <div class="col-sm-8">
                                  <input type="text" name="clientName" id="clientName"  class="col-xs-10 col-sm-11  ztb_add validate[maxSize[50]]" />
                              </div>
                          </div> -->
                          <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-input">业务大类： </label>
                             <div class="col-sm-5">
                              	<select id="" class="  col-md-12 "  name="busiClass" > 
	                               	<option value="">&nbsp;请选择</option>
									<option value="01">担保</option>
									<option value="02">委贷</option>
								</select>
                             </div>
                          </div>
                          <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-input">业务品种： </label>
                             <div class="col-sm-5">
                                <div class="col-md-12 input-group busiSortDicTree">
									<input  type="text"  class="form-control " autoField="busiTypeID"   id="busiSortDicTree"  value="" dataValue="" name="busiTypeName"  readonly="readonly"/>
									<span class="input-group-addon ">
										<i class="icon-caret-down bigger-110"></i>
									</span>
				    		  </div>
                             </div>
                          </div>

                          <%-- <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-input">绿色通道1： </label>
                             <div class="col-sm-5">
                                 <select id="greenChannelList" class="col-md-12"  name="greenChannelID"  >		
										<option value="">&nbsp;请选择</option>
										<c:forEach items="${greenChannelList}" var="greenChannel">
											<option value="${greenChannel.dicTypeID}">${greenChannel.dicTypeName}</option>
										</c:forEach>
					    		</select>
                             </div>
                          </div> --%>

                          <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">项目金额： </label>
            			        <div class="col-sm-8">
            						<input  type="text" class="col-sm-3 ztb_add" name="loadSumStart"  id="loadSumStart"/>
            						<p class="col-sm-1">-</p>
            						<input  type="text" class="col-sm-3 ztb_add" name="loadSumEnd"  id="loadSumEnd"/>
            						<span style="line-height:28px;margin-left:1em;">
            								万元
            						</span>
            					</div>
                          </div>
                          
                          <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">放款日期： </label>
            			        <div class="col-sm-9">
            					  <div class="input-group col-sm-4" style="float:left;">
            						<input  type="text" class="form-control date-picker   " name="loanDateStart"  id="loanDateStart" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            					   </div> 

            					   <p class="col-sm-1" style="line-height:28px;">-</p>
            					   <div class="input-group col-sm-4">
            						<input  type="text" class="form-control date-picker   " name="loanDateEnd"  id=""loanDateEnd"" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            						</div>
            					</div>
                         </div>  
                          
                          
                          
						<div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">在保余额： </label>
            			        <div class="col-sm-8">
            						<input  type="text" class="col-sm-3 ztb_add" name="guarantySumStart"  id="guarantySumStart"/>
            						<p class="col-sm-1">-</p>
            						<input  type="text" class="col-sm-3 ztb_add" name="guarantySumEnd"  id="guarantySumEnd"/>
            						<span style="line-height:28px;margin-left:1em;">
            								万元
            						</span>
            					</div>
                          </div>
                          
                          <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">展期起始日期： </label>
            			        <div class="col-sm-9">
            					  <div class="input-group col-sm-4" style="float:left;">
            						<input  type="text" class="form-control date-picker   " name="delayBeginDateStart"  id="delayBeginDateStart" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            					   </div>

            					   <p class="col-sm-1" style="line-height:28px;">-</p>
            					   <div class="input-group col-sm-4">
            						<input  type="text" class="form-control date-picker   " name="delayBeginDateEnd"  id="delayBeginDateEnd" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            						</div>
            					</div>
                         </div>
                         
                          <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">展期到期日期： </label>
            			        <div class="col-sm-9">
            					  <div class="input-group col-sm-4" style="float:left;">
            						<input  type="text" class="form-control date-picker   " name="delayEndDateStart"  id="delayEndDateStart" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            					   </div>
            					   <p class="col-sm-1" style="line-height:28px;">-</p>
            					   <div class="input-group col-sm-4">
            						<input  type="text" class="form-control date-picker   " name="delayEndDateEnd"  id="delayEndDateEnd" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            						</div>
            					</div>
                         </div>
                          
                          
                          
                          <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">展期余额： </label>
            			        <div class="col-sm-8">
            						<input  type="text" class="col-sm-3 ztb_add" name="delaySumStart"  id="delaySumStart"/>
            						<p class="col-sm-1">-</p>
            						<input  type="text" class="col-sm-3 ztb_add" name="delaySumEnd"  id="delaySumEnd"/>
            						<span style="line-height:28px;margin-left:1em;">
            								万元
            						</span>
            					</div>
                          </div>
                          
                            <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">还款金额： </label>
            			        <div class="col-sm-8">
            						<input  type="text" class="col-sm-3 ztb_add" name="normalFreeSumStart"  id="normalFreeSumStart"/>
            						<p class="col-sm-1">-</p>
            						<input  type="text" class="col-sm-3 ztb_add" name="normalFreeSumEnd"  id="normalFreeSumEnd"/>
            						<span style="line-height:28px;margin-left:1em;">
            								万元
            						</span>
            					</div>
                          </div>
                          
                          <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">还款日期： </label>
            			        <div class="col-sm-8">
            						<div class="input-group col-sm-4" style="float:left;">
            						<input  type="text" class="form-control date-picker   " name="payDateStart"  id="payDateStart" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            					   </div>

            					   <p class="col-sm-1" style="line-height:28px;">-</p>
            					   <div class="input-group col-sm-4">
            						<input  type="text" class="form-control date-picker   " name="payDateEnd"  id="payDateEnd" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            						</div>
            					</div>
                          </div>
                          
                          <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">代偿总金额： </label>
            			        <div class="col-sm-8">
            						<input  type="text" class="col-sm-3 ztb_add" name="replaceFreeSumStart"  id="replaceFreeSumStart"/>
            						<p class="col-sm-1">-</p>
            						<input  type="text" class="col-sm-3 ztb_add" name="replaceFreeSumEnd"  id="replaceFreeSumEnd"/>
            						<span style="line-height:28px;margin-left:1em;">
            								万元
            						</span>
            					</div>
                          </div>
                          
                           <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">代偿日期： </label>
            			        <div class="col-sm-8">
            			        	<div class="input-group col-sm-4" style="float:left;">
            						<input  type="text" class="form-control date-picker   " name="replaceDateStart"  id="replaceDateStart" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            					   </div>

            					   <p class="col-sm-1" style="line-height:28px;">-</p>
            					   <div class="input-group col-sm-4">
            						<input  type="text" class="form-control date-picker   " name="replaceDateEnd"  id="replaceDateEnd" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            						</div>
            					</div>
                          </div>
                          
                          <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">追偿总金额： </label>
            			        <div class="col-sm-8">
            						<input  type="text" class="col-sm-3 ztb_add" name="returnSumStart"  id="returnSumStart"/>
            						<p class="col-sm-1">-</p>
            						<input  type="text" class="col-sm-3 ztb_add" name="returnSumEnd"  id="returnSumEnd"/>
            						<span style="line-height:28px;margin-left:1em;">
            								万元
            						</span>
            					</div>
                          </div>
                          
                          <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">追偿日期： </label>
            			        <div class="col-sm-8">
            			        	<div class="input-group col-sm-4" style="float:left;">
            						<input  type="text" class="form-control date-picker   " name="returnDateStart"  id="returnDateStart" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            					   </div>

            					   <p class="col-sm-1" style="line-height:28px;">-</p>
            					   <div class="input-group col-sm-4">
            						<input  type="text" class="form-control date-picker   " name="returnDateEnd"  id="returnDateEnd" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            						</div>
            					</div>
                          </div>
                          
                          <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">是否涉及敏感债权人： </label>
            			        <div class="col-sm-8">
            						<div class="radio">
	                                      <label style="margin-top:-10px;">
	                                          <input type="radio" name="isCreditor"  value="1" class="ace" >
	                                          <span class="lbl">是</span>
	                                      </label>
	                                      <label style="margin-top:-10px;">
	                                          <input type="radio" name="isCreditor"  value="0" class="ace" >
	                                          <span class="lbl">否</span>
	                                      </label>
	                                  </div>		
            					</div>
                          </div>
                          
                          <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">债权人数量： </label>
            			        <div class="col-sm-8">
            						<input  type="text" class="col-sm-3 ztb_add" name="creditorCountStart"  id="creditorCountStart"/>
            						<p class="col-sm-1">-</p>
            						<input  type="text" class="col-sm-3 ztb_add" name="creditorCountEnd"  id="creditorCountEnd"/>
            					</div>
                          </div>
                          
                           <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-input">资金方类型： </label>
                             <div class="col-sm-5">
                                 <div class="col-md-12 input-group fundTypesTree">
									<input  type="text"  class="form-control " autoField="fundTypes"   id="fundTypesTree"  value="" dataValue="" name="fundTypesName"  readonly="readonly"/>
									<span class="input-group-addon ">
										<i class="icon-caret-down bigger-110"></i>
									</span>
				   			 	</div>
                             </div>
                          </div>
                          
                          
                           <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-input">资金方： </label>
                             <div class="col-sm-5">
                                 <div class="col-md-12 input-group fundTree">
									<input  type="text"  class="form-control " autoField="fundID"   id="fundTree"  value="" dataValue="" name="fundChinese"  readonly="readonly"/>
									<span class="input-group-addon ">
										<i class="icon-caret-down bigger-110"></i>
									</span>
				   			 	</div>
                             </div>
                          </div>
                          
                          <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">资金方名称子机构： </label>
                              <div class="col-sm-8">
                                  <input type="text" name="fundName" id="fundName"  class="col-xs-10 col-sm-11 ztb_add_mod_name ztb_add validate[maxSize[50]]" />
                              </div>
                          </div>
                          
                          <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">资金来源： </label>
            			        <div class="col-sm-8">
            						<select id="" class="  col-md-12 "  name="fundSource" > 
		                               	<option value="">&nbsp;请选择</option>
										<option value="省内">省内</option>
										<option value="省外">省外</option>
									</select>
            					</div>
                          </div>
                          
                          <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">承保机构： </label>
                              <div class="col-sm-8">
                                <div class="col-md-12 input-group guarantyOrgTree">
									<input  type="text"  class="form-control " autoField="guarantyOrgID"   id="guarantyOrgTree"  value="" dataValue="" name="guarantyOrgName"  readonly="readonly"/>
									<span class="input-group-addon ">
										<i class="icon-caret-down bigger-110"></i>
									</span>
				   					</div>
                              </div>
                          </div>
                          <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">承办地区： </label>
                              <div class="col-sm-8">
	                                <div class="col-md-12 input-group hostAreasTree">
										<input  type="text"  class="form-control " autoField="hostAreasID"   id="hostAreasTree"  value="" dataValue="" name="hostAreasName"  readonly="readonly"/>
										<span class="input-group-addon ">
											<i class="icon-caret-down bigger-110"></i>
										</span>
					   				</div>
                              </div>
                          </div>
                          <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">属地划分： </label>
                              <div class="col-sm-8">
                                <div class="col-md-12 input-group attributionsTree">
									<input  type="text"  class="form-control " autoField="attributionsID"   id="attributionsTree"  value="" dataValue="" name="attributionsName"  readonly="readonly"/>
									<span class="input-group-addon ">
										<i class="icon-caret-down bigger-110"></i>
									</span>
				   					</div>
                              </div>
                          </div>
                          <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">经办公司： </label>
                              <div class="col-sm-8">
                                    <div class="col-md-12 input-group oprationCompanysTree">
										<input  type="text"  class="form-control" autoField="oprationCompanysID"   id="oprationCompanysTree"  value="" dataValue="" name="oprationCompanysName"  readonly="readonly"/>
										<span class="input-group-addon ">
											<i class="icon-caret-down bigger-110"></i>
										</span>
								</div>
                              </div>
                          </div>
                          <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">期限.月天： </label>
                              <div class="col-sm-8 ">
					                  <input type="text" id="periodMonthStart"  name="periodMonthStart"  style="width:4em;" />
					                   &nbsp;月&nbsp;
					                  <input type="text" id="periodDayStart"  name="periodDayStart" style="width:4em;" />
					                   &nbsp;天&nbsp;
	           							-	
					                  <input type="text" id="periodMonthEnd"  name="periodMonthEnd"  style="width:4em;" />
					                   &nbsp;月&nbsp;
					                  <input type="text" id="periodDayEnd"  name="periodDayEnd" style="width:4em;" />
					                   &nbsp;天&nbsp;
                              </div>
                          </div>
                          
                          <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">a角名字： </label>
                              <div class="col-sm-8">
                                  <input type="text" name="amanName" id="amanName"  class="col-xs-10 col-sm-11 ztb_add_mod_name ztb_add validate[maxSize[50]]" />
                              </div>
                          </div>
						<div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">b角名字： </label>
                              <div class="col-sm-8">
                                  <input type="text" name="bmanName" id="bmanName"  class="col-xs-10 col-sm-11 ztb_add_mod_name ztb_add validate[maxSize[50]]" />
                              </div>
                          </div>
                          <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">五级分类： </label>
                              <div class="col-sm-8">
                              		<div class="col-md-12 input-group riskLevelTree">
										<input  type="text"  class="form-control " autoField="riskLevelID"   id="riskLevelTree"  value="" dataValue="" name="riskLevelName"  readonly="readonly"/>
										<span class="input-group-addon ">
											<i class="icon-caret-down bigger-110"></i>
										</span>
				   			 		</div>
                              		
                              </div>
                          </div>
                          
                          <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">是否债权转让： </label>
            			        <div class="col-sm-8">
            						<div class="radio">
	                                      <label style="margin-top:-10px;">
	                                          <input type="radio" name="isCreditorMark"  value="1" class="ace" >
	                                          <span class="lbl">是</span>
	                                      </label>
	                                      <label style="margin-top:-10px;">
	                                          <input type="radio" name="isCreditorMark"  value="0" class="ace" ">
	                                          <span class="lbl">否</span>
	                                      </label>
	                                  </div>		
            					</div>
                          </div>
                          
                          <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">转让金额： </label>
            			        <div class="col-sm-8">
            						<input  type="text" class="col-sm-3 ztb_add" name="creditorSumStart"  id="creditorSumStart"/>
            						<p class="col-sm-1">-</p>
            						<input  type="text" class="col-sm-3 ztb_add" name="creditorSumEnd"  id="creditorSumEnd"/>
            						<span style="line-height:28px;margin-left:1em;">
            								万元
            						</span>
            					</div>
                          </div>
                          
                          <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">转让日期： </label>
            			        <div class="col-sm-8">
            			        	<div class="input-group col-sm-4" style="float:left;">
            						<input  type="text" class="form-control date-picker   " name="creditorDateStart"  id="creditorDateStart" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            					   </div>

            					   <p class="col-sm-1" style="line-height:28px;">-</p>
            					   <div class="input-group col-sm-4">
            						<input  type="text" class="form-control date-picker   " name="creditorDateEnd"  id="creditorDateEnd" data-date-format="yyyy-mm-dd" />
            						<span class="input-group-addon">
            							<i class="icon-calendar bigger-110"></i>
            						</span>
            						</div>
            					</div>
                          </div>
                          
                          
                        <!--   <div class="space-4"></div>
                          <div class="form-group">
            				   	<label class="col-sm-3 control-label no-padding-right" for="form-input">申请期限1： </label>
            			        <div class="col-sm-8">
            						<input  type="text" class="col-sm-3 ztb_add" name="periodMonthStrat"  id="periodMonthStrat"/>
            						<p class="col-sm-1">-</p>
            						<input  type="text" class="col-sm-3 ztb_add" name="periodMonthEnd"  id="periodMonthEnd"/>
            						<span style="line-height:28px;margin-left:1em;">
            								个月
            						</span>
            					</div>
                          </div> -->

                          <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-input">合作机构： </label>
                             <div class="col-sm-5">
                                 <div class="col-md-12 input-group cooperationTree">
									<input  type="text"  class="form-control " autoField="bankID"   id="cooperationTree"  value="" dataValue="" name="bankName"  readonly="readonly"/>
									<span class="input-group-addon ">
										<i class="icon-caret-down bigger-110"></i>
									</span>
				   			 	</div>
                             </div>
                          </div>
	
						<div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">合作子机构： </label>
                              <div class="col-sm-8">
                                  <input type="text" name="subBankName" id="subBankName"  class="col-xs-10 col-sm-11 ztb_add_mod_name ztb_add validate[maxSize[50]]" />
                              </div>
                          </div>
						
                          
                         <%--  <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-input">客户来源1： </label>
                             <div class="col-sm-5">
                                 <select id="projectSourceList" class="col-md-12 "  name="projectSourceID"  >		
										<option value="">&nbsp;请选择</option>
										<c:forEach items="${projectSourceList}" var="projectSource">
											<option value="${projectSource.dicTypeID}">${projectSource.dicTypeName}</option>
										</c:forEach>
					    		</select>
                             </div>
                          </div> --%>

                          <!-- <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-input">经办部门： </label>
                             <div class="col-sm-5">
                                 <div class="col-md-12 input-group allDepartsTree">
										<input  type="text"  class="form-control" autoField="departID"   id="allDepartsTree"  value="" dataValue="" name="departName"  readonly="readonly"/>
										<span class="input-group-addon ">
											<i class="icon-caret-down bigger-110"></i>
										</span>
								</div>
                             </div>
                          </div>

                           <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">经办人： </label>
                              <div class="col-sm-4">
                                  <input type="text" name="createManName" id="createManName"  class="col-xs-10 col-sm-11   validate[maxSize[50]]" />
                              </div>
                          </div> -->
					
                       	 <div class="space-4"></div>
                          <div class="form-group">
                              <label class="col-sm-3 control-label no-padding-right" for="form-field-1">原债权人/现债权人： </label>
                              <div class="col-sm-8">
                                  <input type="text" name="creditor" id="creditor"  class="col-xs-10 col-sm-11 ztb_add_mod_name ztb_add validate[maxSize[50]]" />
                              </div>
                          </div>
						
                      <form>

                  </div>
                  <div class="modal-footer">
                      <button class="btn btn-primary btn_submit" type="button">
                          <i class="icon-search bigger-110"></i>查询
                      </button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                          <i class="icon-remove bigger-110"></i>关闭
                    </button>
      		</div>
       </div>
    </div>
 </div>