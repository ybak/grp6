(function($,z){
	$.zjm_userGroup = {
			initTable:initTable,//初始化列表
			addUserGroup:addUserGroup,//添加
			editUserGroup:editUserGroup,//修改
			viewUserGroup:viewUserGroup,//查看
			delUserGroup:delUserGroup//删除
	};
	/**初始化列表***/
	function initTable(){
		$("#userGroup-table").bootstrapTable('destroy');
		$('#userGroup-table').bootstrapTable({
			method: 'get',
			columns: [{title: '序号',align: 'center',formatter: function (value, row, index) {return index+1;}}, 
					{field:"userGroupName",title: '项目组名称',align: 'center',sortable:"true",formatter: function (value, row, index) { return row.userGroupName;}}, 
					{field:"user_names",title: '项目组人员',align: 'center',sortable:"true",formatter: function (value, row, index) { return row.user_names;}},
					{field:"userGroup_uuid",title: '操作',align: 'center',formatter:function(value,row,index){
							return ['<button class="btn_modules_view btn btn-xs btn-warning" href="javascript:void(0)"><i class="icon-zoom-in bigger-120"></i></button>',
								'<button class="btn_modules_edit btn btn-xs btn-info" href="javascript:void(0)" data-toggle="modal" data-target="#edit_Page"><i class="icon-pencil bigger-120"></i></button>',
								'<button class="btn_modules_del btn btn-xs btn-danger" href="javascript:void(0)"><i class="icon-trash bigger-120"></i></button>'].join('');
						},
						events:{
							'click .btn_modules_view': function(e, value, row, index) {
								$.zjm_userGroup.viewUserGroup(row);
							},
							'click .btn_modules_edit': function(e, value, row, index) {
								$.zjm_userGroup.editUserGroup(row);
							},
							'click .btn_modules_del': function(e, value, row, index) {
								$.zjm_userGroup.delUserGroup(row);
							}
						}
					}],
			detailView: true,
			detailFormatter:function(index, row){
			    var html = [];
			        html.push('<p><b>序号:</b> ' + (index+1) + '</p>');
			        html.push('<p><b>项目组名称:</b> ' + tool.isNull(row.userGroupName,"") + '</p>');
			        html.push('<p><b>项目组人员:</b> ' + tool.isNull(row.user_names,"") + '</p>');
			    return html;
			},
			toolbar: '#toolbar',    //工具按钮用哪个容器
			striped: true,      //是否显示行间隔色
			cache: false,      //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,     //设置为 true 会在表格底部显示分页条
			paginationLoop: true,//设置为 true 启用分页条无限循环的功能。
			sortable: true,      //是否启用排序
			sortOrder: "asc",     //排序方式
			pageNumber:1,      //初始化加载第一页，默认第一页
			pageSize: 30,      //每页的记录行数（*）
			pageList: [30,50, 100, 200, 500],  //可供选择的每页的行数（*）
			url: "/sys/selectUserGroupPageTables",//这个接口需要处理bootstrap table传递的固定参数
			queryParamsType:'', //默认值为 'limit' ,在默认情况下 传给服务端的参数为：offset,limit,sort
			// 设置为 ''  在这种情况下传给服务器的参数为：pageSize,pageNumber
			queryParams: function(params) {
				// $.extend(params, {"queryCondition":{"mod_uid":nodeid}});
				return params;
			},//前端调用服务时，会默认传递上边提到的参数，如果需要添加自定义参数，可以自定义一个函数返回请求参数
			sidePagination: "server",   //分页方式：client客户端分页，server服务端分页（*）
			search: true,      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,//设置为 true启用 全匹配搜索，否则为模糊搜索
			showColumns: true,     //是否显示所有的列
			showRefresh: true,     //是否显示刷新按钮
			minimumCountColumns: 2,    //最少允许的列数
			clickToSelect: false,    //是否启用点击选中行
			searchOnEnterKey: true,//设置为 true时，按回车触发搜索方法，否则自动触发搜索方法
			showToggle: true,//是否显示详细视图和列表视图的切换按钮
			showPaginationSwitch:true,
            showExport: true,                     //是否显示导出
            exportDataType: "basic"              //basic', 'all', 'selected'
			         
			});
	}


	/**添加*/
	function addUserGroup(){
		$("#userGroup_page").load(
				"/sys/selectUserGroupAddPage",{},function(response,status,xhr){
					$("#addmodule").modal({keyboard:true});
					$(".btn_submit").click(function(){
						tool.disabled(".btn_submit");
						if($("#add_form").validationEngine("validate")){
							var queryContainer_form = $("#add_form");
							if(zjm.ajaxValidata("userGroupName","/sys/selectAddUserGroupNameIsExist",JSON.stringify(queryContainer_form.serializeJson()),"项目组名称重复！")){
								$.ajax({type:'POST',url:'/sys/insertOneUserGroupInfo',data:JSON.stringify(queryContainer_form.serializeJson()),contentType:'application/json; charset=UTF-8',dataType:'json',success:function(data) {
							        	if(data.obj==1){
											$("#addmodule").modal("hide");
											$.zjm_userGroup.initTable();
										}else{
											alert("保存失败！");
										}
							        }
								});
							}else{
								tool.undisabled(".btn_submit");
							}
						}else{
							tool.undisabled(".btn_submit");
						}
					});
					
					
					$.ajax({type:'POST',url:'/sys/dic/selectDepartsUserTree',data:JSON.stringify({}),contentType:'application/json; charset=UTF-8',dataType:'json',success:function(data) {
						$("#select_user_tree_userGroup").selectTreeWidgets({
							width : "93%",//设置控件宽度
							multiple : true,//是否多选
							data : data.obj//数据源
						});
			        }
			        });
					
					
					
				}
		);
	}
	/**查看*/
	function viewUserGroup(row){
		$("#userGroup_page").load(
				"/sys/selectUserGroupViewPage",{"userGroup_uuid":row.userGroup_uuid},function(response,status,xhr){
					$("#viewmodule").modal({keyboard:true});
				}
		);
	}
	/**修改*/
	function editUserGroup(row){
		$("#userGroup_page").load(
				"/sys/selectUserGroupEditPage",{"userGroup_uuid":row.userGroup_uuid},function(response,status,xhr){
					$("#editmodule").modal({keyboard:true});
					$(".btn_submit").click(function(){
						tool.disabled(".btn_submit");
						if($("#edit_form").validationEngine("validate")){
							var queryContainer_form = $("#edit_form");
							if(zjm.ajaxValidata("userGroupName","/sys/selectEditUserGroupNameIsExist",JSON.stringify(queryContainer_form.serializeJson()),"项目组名称重复！")){
								$.ajax({type:'POST',url:'/sys/updateOneUserGroupInfo',data:JSON.stringify(queryContainer_form.serializeJson()),contentType:'application/json; charset=UTF-8',dataType:'json',success:function(data) {
							        	if(data.obj==1){
											$("#editmodule").modal("hide");
											$.zjm_userGroup.initTable();
										}else{
											alert("保存失败！");
										}
							        }
								});
							}else{
								tool.undisabled(".btn_submit");
							}
						}else{
							tool.undisabled(".btn_submit");
						}
					});
					
					$.ajax({type:'POST',url:'/sys/dic/selectDepartsUserTree',data:JSON.stringify({}),contentType:'application/json; charset=UTF-8',dataType:'json',success:function(data) {
						$("#select_user_tree_userGroup").selectTreeWidgets({
							width : "93%",//设置控件宽度
							multiple : true,//是否多选
							data : data.obj//数据源
						});
			        }
			        });
				}
		);
	}
	/**删除*/
	function delUserGroup(row){
		$("#userGroup_page").load(
				"/sys/selectUserGroupDelPage",{"userGroup_uuid":row.userGroup_uuid},function(response,status,xhr){
					$("#delmodule").modal({keyboard:true});
					$(".btn_submit").click(function(){
						tool.disabled(".btn_submit");
						$.ajax({type:'POST',url:'/sys/delectOneUserGroupInfo',data:JSON.stringify({"userGroup_uuid":row.userGroup_uuid}),contentType:'application/json; charset=UTF-8',dataType:'json',success:function(data) {
					        	if(data.obj==1){
					        		$('#userGroup-table').bootstrapTable('remove', {
										field: 'userGroup_uuid',
										values: [row.userGroup_uuid]
									});
									$("#delmodule").modal("hide");
								}else{
									alert("删除失败！");
								}
					        }
						});
					});
				}
		);
	}

})(jQuery, this);



$(function () {
	/**
	 * 文档加载的时候 读取 菜单信息
	 */
	window.onload = floaddata;
	function floaddata() {
		$.zjm_userGroup.initTable();
	};

	$("#btn_add").click(function(){
		$.zjm_userGroup.addUserGroup();
	});
	$("#btn_sort").click(function(){
		$("#sortop").modal({keyboard:true});
		zjm.dataSortVal("/sys/selectUserGroupListJSON",{});
		tool.sort();
		tool.undisabled(".btn_submit");
		$(".btn_submit").click(function () {
			tool.disabled(".btn_submit");
			$.ajax({type:'POST',url:'/updateSortOrder',data:JSON.stringify({"tableName":"sys_usergroup","tableFileName":"userGroup_uuid","tableFileIds":$("#tableFileIds").val()}),contentType:'application/json; charset=UTF-8',dataType:'json',success:function(data) {
				if(data.obj==1){
					$("#sortop").modal("hide");
					$.zjm_userGroup.initTable();
				}else{
					alert("保存失败！");
					$.zjm_userGroup.initTable();
				}
		        }
			});
		});
		$(".btn_reset").click(function () {
			zjm.dataSortVal("/sys/selectUserGroupListJSON",{});
		});
		$("#sortop").on("hidden.bs.modal", function (e) {//解除事件绑定
			 $(".btn_reset").unbind("click");
			 $(".btn_submit").unbind("click");
			 $("#btn_moveUp").unbind("click");
			 $("#btn_moveDown").unbind("click");
			 $("#btn_moveTop").unbind("click");
			 $("#btn_moveBottom").unbind("click");
		});
	});
});
