<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common_timestamp.jsp" %>
<%@ include file="/common_head.jsp" %>
<%@ include file="/common_foot.jsp" %>

	<!-- <input type="hidden" name="client_ID" class="client_ID" value="">
	<input type="hidden" name="clientTypeID" class="clientTypeID" value="02"> -->
	<div class="page-content">
		<div class="row" style="display:block" id="personClientPage">
			<div class="col-xs-12">
				<div class="page-header">
					
					<!-- <button class="btn btn-sm btn-info" id="btn_projectAfterAdd" >新增历史项目</button>
					<button class="btn btn-sm btn-info" id="btn_openFactPayRegister" >还款登记</button>
					<button class="btn btn-sm btn-info" id="btn_openProjectDelay" >展期</button>
					<button class="btn btn-sm btn-info" id="btn_openProjectOverRegister" >逾期确认</button>
					<button class="btn btn-sm btn-info" id="btn_openProjectReplaceAndReturn" >代偿与追偿</button>
					<button class="btn btn-sm btn-info" id="btn_openProjectFinish" >项目完结</button>
					<button class="btn btn-sm btn-info" id="btn_openProjectLawsuit" >项目诉讼登记</button>
					<button class="btn btn-sm btn-info" id="btn_openAssetSealUp" >资产查封信息</button>
					<button class="btn btn-sm btn-info" id="btn_hightSelect" >高级查询</button>
					 -->
					 <button class="btn btn-sm btn-info" id="btn_projectContinue" >申请续保续贷</button>
					 <button class="btn btn-sm btn-info" id="btn_refresh" >重置列表</button>
				</div>
                <div class="col-sm-12">
					<div class="table-responsive"  id="loadinfo">
						 <table id="projectFinish-table" style="font-size:13px !important;"></table>  
                    </div>
                </div>
			</div>
			<!-- PAGE CONTENT ENDS -->
		</div><!-- /.row -->
		<div id="projectFinish_page"></div>
	</div><!-- /.page-content -->
	<script type="text/javascript" src="<%=path %>/project/projectFinish/projectFinish.js?v=<%=vardate%>"></script>
	<%@ include file="/common_message.jsp"%>
	<%@ include file="/common_del.jsp" %>