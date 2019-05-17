(function($,z){
	$.zjm_projectSuggest = {
			initDate:initDate,//初始化页面,隐藏相应的div
			projectSuggestEdit:projectSuggestEdit,//修改意见信息
			
	};
	
	
	function initDate(type){
		if('view' == type){
			$(".btn_projectSuggestEdit").hide();//查看时隐藏,意见输入框
		}
		
	}
	
	//修改意见信息
	function projectSuggestEdit(suggest_ID){
		var entityID = $("#entityID").val();
		var productInstanceID = $("#productInstanceID").val();
		var taskName = $("#taskName").val();
		var taskID = $("#taskID").val();
		var nodeID = $("#nodeID").val();
		var nodeNames = $("#nodeNames").val();
		var type = $("#type").val();
		var entityType = $("#entityType").val();
		var suggestType = $("#suggestType").val();
		
		$("#projectSuggest_page").load("/project/suggest/returnProjectSuggestEditPage",{"suggest_ID":suggest_ID,"entityID":entityID, "type":suggestType},function(response,status,xhr){				
			
			zjm.init();
			//$('#id-date-picker-1').attr("value",tool.parseDate(new Date().getTime()));//设置还款日期默认值
			$("#productInstanceID2").val(productInstanceID);
			$("#taskName2").val(taskName);
			$("#myModalLabel").text(taskName);
			$("#taskID2").val(taskID);
			$("#nodeID2").val(nodeID);
			$("#nodeNames2").val(nodeNames);
			$("#entityID3").val(entityID);
//			$("#suggest_ID2").val(suggest_ID);
			$("#type2").val(type);
			$("#entityType2").val(entityType);
			$("#projectSuggestEditPage").modal({keyboard:true});
			tool.undisabled(".btn_submit");
			$(".btn_submitUpdate").click(function(){
				tool.disabled(".btn_submitUpdate");
				if($("#projectSuggestEdit_form").validationEngine("validate")){
					$("#projectSuggestEditPage").modal("hide");
						var queryContainer_form = $("#projectSuggestEdit_form");
						$.ajax({type:'POST',url:'/project/suggest/insertProjectSuggest',data:JSON.stringify(queryContainer_form.serializeJson()),contentType:'application/json; charset=UTF-8',dataType:'json',success:function(data) {
							if(data.obj==true){
								$("#projectSuggestEditPage").modal("hide");
								 window.location.reload();
							}else{
								alert("修改失败！");
							}
						}
						});
						
				}else{
					tool.undisabled(".btn_submitUpdate");
				}
			});
			$("#projectSuggestEditPage").on("hidden.bs.modal", function (e) {//解除事件绑定
				$(".btn_submitUpdate").unbind("click");
			});
		})
	}
		
})(jQuery, this);

$(function () {
	var type = $("#type").val();
	var nodeNames = decodeURI(tool.getUrlParam("nodeNames"));
	$("#nodeNames").val(nodeNames);
	$.zjm_projectSuggest.initDate(type);
	$(".btn_projectSuggestEdit").click(function(){
		var  suggest_ID= $(this).attr("value");		
		$.zjm_projectSuggest.projectSuggestEdit(suggest_ID);
	});
	
	
	
});
