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
	<%@ include file="/common_timestamp.jsp" %>
				<div id="divTwo">	<!-- divTwo 开始-->

						<div class="page-header">
							<h5>填写其他保证方式详情</h5>
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
                              
                              
                              <div class="form-group">
                                  <label class="col-sm-2 control-label no-padding-right" for="form-input"><i class="icon-asterisk orange"></i>反担保方式： </label>
                                    <div class="col-sm-8">
                                    	<div class="row">
                                    		<div class="col-sm-6">
                                    			<input type="hidden" name="guaranteePattern">
			                                    <select class="col-sm-6 validate[required]" name="guaranteePatternID" id="guaranteePatternID">
			                                    	<option value="">请选择</option>
			                                    	<c:forEach var="gList" items="${guaranteeList }">
				                                    	<option value="${gList.dicTypeID }">${gList.dicTypeName }</option>
			                                    	</c:forEach>
			                                    </select>
			                                  </div>  
	                                    </div>
	            					</div>                        
                              </div>
	
                              <div class="form-group">
                                  <label class="col-sm-2 control-label no-padding-right" for="form-field-1">可提供反担保金额： </label>
                                  <div class="col-sm-8">
                                      <input type="text" id="guaranteeSum" name="guaranteeSum" class="col-sm-3 validate[custom[number],maxSize[18]]" />
                                      <span style="line-height:28px;">&nbsp;万元</span>
                                  </div>
                              </div>

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
                              
             <!--                  <div class="form-group col-sm-12">
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



	<script src="<%=path%>/project/opt/optManager/otherCredit.js?v=<%=vardate%>"></script>
	
		<%@ include file="/project/opt/imgUpload/optUpload.jsp" %>
	
