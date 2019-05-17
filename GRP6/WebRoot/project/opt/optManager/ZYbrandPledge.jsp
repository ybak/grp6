<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<style>
	table.table_busiLimit{
		font-size:13px;
		border:1px solid #ddd;
		table-layout:fixed
	}
	table.table_busiLimit tr th,td{
		border:1px solid #ddd;
		text-align: center;
		vertical-align:middle
	}
	
</style>

				<div id="divTwo">	<!-- divTwo 开始-->

						<div class="page-header">
							<h5>填写商标详情</h5>
						</div>

						<form class="form-horizontal" role="form" id="form_baseTwo">
						
                              <div class="form-group col-sm-6">
                                  <label class="col-sm-4 control-label no-padding-right" for="form-input">权属人： </label>
                                  <label style="line-height:29px;" class="ownerName"></label>
                                  <input type="hidden"  id="ownerName" name="ownerName">
                                  <input type="hidden"  id="clientID" name="clientID">
                                  <input type="hidden"  id="apply_ID" name="apply_ID">
                                  <input type="hidden"  id="guarantyTypeID" name="guarantyTypeID">
                                  <input type="hidden"  id="guarantyTypeName" name="guarantyTypeName">
                                  <input type="hidden"  id="optTypeID" name="optTypeID">
                                  <input type="hidden"  id="optTypeName" name="optTypeName">
                              </div>
                              
                              <div class="form-group col-sm-6">
                                  <label class="col-sm-4 control-label no-padding-right clientType" for="form-input"><i class="icon-asterisk orange">身份证号或社会信用代码</i></label>
                                  <label style="line-height:29px;" class="codeNum"></label>
                              </div>
                              
                               <div class="form-group col-sm-6">
                                  <label class="col-sm-4 control-label no-padding-right" for="form-field-1"> <i class="icon-asterisk orange"></i>质物名称： </label>
                                  <div class="col-sm-8">
                                      <input type="text" id="optName" name="optName" class="col-sm-6 validate[required,maxSize[50]]" />
                                  </div>
                              </div>

                              <div class="form-group col-sm-6">
                                  <label class="col-sm-4 control-label no-padding-right" for="form-field-1"> <i class="icon-asterisk orange"></i>商标注册号： </label>
                                  <div class="col-sm-8">
                                      <input type="text" id="brandCode" name="brandCode" class="col-sm-6 validate[required,maxSize[50]]" />
                                  </div>
                              </div>
                              
                              <div class="form-group">
                                  <label class="col-sm-2 control-label no-padding-right" for="form-field-1">核定使用商品： </label>
                                  <div class="col-sm-10">
                                      <input type="text" id="useCommodity" name="useCommodity" class="col-sm-5 validate[maxSize[50]]" />
                                  </div>
                              </div>
                              
                              <div class="form-group col-sm-6">
                                  <label class="col-sm-4 control-label no-padding-right" for="form-field-1">注册人： </label>
                                  <div class="col-sm-8">
                                      <input type="text" id="landOwnerName" name="landOwnerName" class="col-sm-6 validate[maxSize[50]]" />
                                  </div>
                              </div>
							
							<div class="form-group col-sm-6">
                                  <label class="col-sm-4 control-label no-padding-right" for="form-field-1"> <i class="icon-asterisk orange"></i>注册有效期限： </label>
                                  <div class="col-sm-8">
										<div class="row">
											<div class="col-sm-5 no-padding-right">
												<div class="input-group">
													<input class="form-control date-picker validate[required,custom[date]]" type="text" id="insuranceBeginDate" data-date-format="yyyy-mm-dd" name="insuranceBeginDate" />
													<span class="input-group-addon">
														<i class="icon-calendar bigger-110"></i>
													</span>
												</div>
											</div>
											<span class="midden col-sm-1" style="line-height:28px;">~</span>
											<div class="col-sm-5 no-padding-left">
												<div class="input-group">
													<input class="form-control date-picker validate[required,custom[date]]" type="text" id="insuranceEndDate" data-date-format="yyyy-mm-dd" name="insuranceEndDate" />
													<span class="input-group-addon">
														<i class="icon-calendar bigger-110"></i>
													</span>
												</div>
											</div>
										</div>
                                  </div>
                              </div>

							 <div class="form-group">
                                  <label class="col-sm-2 control-label no-padding-right" for="form-field-1">注册地址： </label>
                                  <div class="col-sm-10">
                                      <input type="text" id="adderss" name="adderss" class="col-sm-5 validate[maxSize[50]]" />
                                  </div>
                              </div>
							
							
                             <div class="form-group col-sm-6">
                                  <label class="col-sm-4 control-label no-padding-right" for="form-field-1"> <i class="icon-asterisk orange"></i>评估价值： </label>
                                  <div class="col-sm-8">
                                      <input type="text" id="assessValue" placeholder="" name="assessValue" class="col-sm-4 validate[required,custom[number],maxSize[18]]" />
                                      <span style="line-height:28px;">&nbsp;万元</span>
                                  </div>
                              </div>
							
                              <div class="form-group col-sm-6">
                                  <label class="col-sm-4 control-label no-padding-right" for="form-field-1"> <i class="icon-asterisk orange"></i>质押率 ：</label>
                                  <div class="col-sm-8">
                                      <input type="text" id="coverageRatio" placeholder="" name="coverageRatio" class="col-sm-4 validate[required,custom[number],max[100],maxSize[5]]" />
                                      <span style="line-height:28px;">&nbsp;%</span>
                                  </div>
                              </div>

                              <div class="form-group col-sm-6">
                                  <label class="col-sm-4 control-label no-padding-right" for="form-field-1"> <i class="icon-asterisk orange"></i>质押价值： </label>
                                  <div class="col-sm-8">
                                      <input type="text" id="optValue" placeholder="" name="optValue" class="col-sm-4 validate[required,custom[number],maxSize[18]]" />
                                      <span style="line-height:28px;">&nbsp;万元</span>
                                  </div>
                              </div>
							
							 <div class="form-group col-sm-6 hidden-xs" style="height: 30px;"></div>
			
							  <div class="space-4"></div>		
                              <div class="form-group">
                                  <label class="col-sm-2 control-label no-padding-right" for="form-input">说明： </label>
                                  <div class="col-sm-10">
	                                  <div class="row">
	                                  	<div class="col-sm-12">
                                  			<textarea class="col-sm-5 limited validate[maxSize[2000]]" id="disposeRemark" rows="5" name="disposeRemark"></textarea>
                                  		</div>
	                                  </div>
                                  	
                                     <div class="col-sm-5 no-padding-right">
                                          <span class="light-grey" style="float:right;">限制2000个字符</span>
                                     </div>
                                  </div>
                              </div>

                              <div class="form-group col-sm-12">
                                  <label class="col-sm-2 control-label no-padding-right" for="form-input"><i class="icon-asterisk orange"></i>是否需要登记： </label>
                                      <div class="radio">
                                          <label style="margin-top:-10px;">
                                              <input type="radio" name="isRegister" value="1" class="ace" checked="checked">
                                              <span class="lbl">是</span>
                                          </label>
                                          <label style="margin-top:-10px;">
                                              <input type="radio" name="isRegister" value="0" class="ace">
                                              <span class="lbl">否</span>
                                          </label>
                                      </div>
                              </div>
                              
                              <!-- <div class="form-group col-sm-12">
                                  <label class="col-sm-2 control-label no-padding-right" for="form-input"><i class="icon-asterisk orange"></i>是否有最高保证额： </label>
                                      <div class="radio">
                                          <label style="margin-top:-10px;">
                                              <input type="radio" name="isGuarantyMaxSum" value="1" class="ace" checked="checked">
                                              <span class="lbl">是</span>
                                          </label>
                                          <label style="margin-top:-10px;">
                                              <input type="radio" name="isGuarantyMaxSum" value="0" class="ace">
                                              <span class="lbl">否</span>
                                          </label>
                                      </div>
                              </div> -->

                             
                                                        	 <%@ include file="/project/opt/optManager/opt_btn.jsp" %>
                          </form>

						</div>


	<script type="text/javascript">
		// 注册日期控件点击事件
		$('.date-picker').datepicker({autoclose:true}).next().on(ace.click_event, function(){
			$(this).prev().focus();
		});
	</script>
	
	<%@ include file="/common_timestamp.jsp" %>
	<script src="<%=path%>/project/opt/optManager/ZYbrandPledge.js?v=<%=vardate%>"></script>

		<%@ include file="/project/opt/imgUpload/optUpload.jsp" %>
	
	