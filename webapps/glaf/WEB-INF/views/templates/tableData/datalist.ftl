<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据列表</title>
<#include "/inc/init_easyui_layer3_import.ftl"/>
<script type="text/javascript">
 
    var _height = jQuery(window).height();
	var _width = jQuery(window).width();
 
    var x_height = Math.floor(_height * 0.82);
	var x_width = Math.floor(_width);

	//alert(jQuery(window).height());

   <#if table.treeFlag == "Y">
   var setting = {
			async: {
				enable: true,
				url:"${contextPath}/tableData/treeJson?tableId=${tableId}",
				dataFilter: filter
			},
			callback: {
				onClick: zTreeOnClick
			}
		};
  
  	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			childNodes[i].icon="${contextPath}/static/images/basic.gif";
			if(childNodes[i].level == 2){
               //childNodes[i].icon="${contextPath}/static/images/bricks.png";
			}
		}
		return childNodes;
	}


    function zTreeOnClick(event, treeId, treeNode, clickFlag) {
		jQuery("#nodeId").val(treeNode.id);
		//var p = jQuery('#mydatagrid').datagrid('getPager');
		//alert(p.pageSize);
		//var link = "${contextPath}/tableData/data?tableId=${tableId}&parentId="+treeNode.id;
		//loadGridData(link);
		jQuery('#mydatagrid').datagrid({
			queryParams: {
				tableId: '${tableId}',
				parentId: treeNode.id
			}
		});

 	}

    jQuery(document).ready(function(){
		jQuery.fn.zTree.init(jQuery("#myTree"), setting);
	});

	</#if>

   var link_ = "${contextPath}/tableData/json?tableId=${tableId}&topId=${topId}";
   <#if sqlCriterias?exists>
	<#list sqlCriterias as col>
	  <#if col.value?exists>
        <#if col.columnType == "Integer">
			link_ = link_ + "&${col.paramName}=${col.value}";     
	    <#elseif col.columnType == "Long">
			link_ = link_ + "&${col.paramName}=${col.value}";  	 
		<#elseif col.columnType == "Double">
			link_ = link_ + "&${col.paramName}=${col.value}";  	 
		<#elseif col.columnType == "Date">
			link_ = link_ + "&${col.paramName}=${col.value}";  	 
		<#else>
			link_ = link_ + "&${col.paramName}_enc=${col.valueEnc}";  	 
		</#if>	   
	  </#if>
	</#list>
   </#if>
   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:x_width,
				height:x_height,
				fit: false,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: link_,
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:60, sortable:false},
                        <#list  columns as field>
                         <#if field.locked == 0 && field.displayType == 4 && field.columnName?exists>
						   {title:'${field.title}', field:'${field.lowerColumnName}', width:${field.listWidth}, sortable:true},
					     </#if>
                        </#list>
						<#if topId?exists>
						<#else>
						<#if table.auditFlag == 'Y'>
						{title:'审核状态', field:'business_status_', width:60, sortable:false, formatter: formatterStatus},
						</#if>
						</#if>
					    {field:'functionKey', title:'功能键', width:210, formatter:formatterKeys }
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 20,
				pageList: [10,15,20,25,30,40,50,100,500,1000],
				pagePosition: 'both'
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
                    alert('before refresh');
 				}
		    });

			$('#mydatagrid').datagrid({
				onDblClickRow: function(index, row){
					 editRow(row.uuid);
				}
			});

	});

		 
	function addNew(){
		link="${contextPath}/tableData/edit?tableId=${tableId}&topId=${topId}";
		layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "新增记录",
			  area: ['980px', (jQuery(window).height() - 30) +'px'],
			  shade: 0.85,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
			});
	}


	function formatterStatus(val, row){
		if(val == 9){
			return "<font color='green'>通过</font>";
		}else if(val == -1){
			return "<font color='red'>不通过</font>";
		} else {
            return "<font color='blue'>待审核</font>";
		}
	}


	function formatterKeys(val, row){
		var str = "";
		<#if canEdit == true>
		if(row.business_status_ != 9){
		    str = "<a href='javascript:editRow(\""+row.uuid+"\");'><img src='${contextPath}/static/images/edit.gif' border='0'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.uuid+"\");'><img src='${contextPath}/static/images/remove.png' border='0'>删除</a>";
		}
		</#if>
        <#if table.auditFlag == "Y">
		  if(row.business_status_ == -1 || row.business_status_ == 9){
		     str = str + "&nbsp;<a href='javascript:openAuditWin(\""+row.id+"\");'><img src='${contextPath}/static/images/comments.png' border='0'>审核意见</a>";
		  }
        </#if>
		<#list correlations as table>
		<#if table.tableCorrelation.relationshipType == "OneToMany">
           str = str + "&nbsp;<a href='javascript:openMxWin${table.tableId}(\""+row.id+"\");'><img src='${contextPath}/static/images/list.gif' border='0'>${table.title}</a>";
		</#if>
		</#list >
	    return str;
	}

    function openAuditWin(id){
        var link="${contextPath}/tableData/comments?tableId=${table.tableId}&topId="+id;
		layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "审核意见",
			  area: ['680px', (jQuery(window).height() - 30) +'px'],
			  shade: 0.85,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
			});
	}

  <#list correlations as table>
    <#if table.tableCorrelation.relationshipType == "OneToMany">
    function openMxWin${table.tableId}(topId){
			var link="${contextPath}/tableData/datalist?tableId=${table.tableId}&topId="+topId;
			layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "${table.title}",
			  area: ['1080px', (jQuery(window).height() - 30) +'px'],
			  shade: 0.85,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
			});
	}

   </#if>
 </#list >

	function editRow(uuid){
		link="${contextPath}/tableData/edit?tableId=${tableId}&topId=${topId}&uuid="+uuid;
		layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "编辑记录",
			  area: ['980px', (jQuery(window).height() - 30) +'px'],
			  shade: 0.85,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
			});
	}


	function reloadGrid(){
		jQuery('#mydatagrid').datagrid({
			queryParams: {
				tableId: '${tableId}',
				topId: '${topId}',
				parentId: jQuery("#nodeId").val()
			}
		});
	}


	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
	}

	function removeSelected(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		  alert("请选择其中一条记录。");
		  return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected ){
		   deleteRow(selected.uuid);
	    }
	}

	function deleteRow(uuid){
		if(confirm("删除数据不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/tableDataDelete/deleteById?tableId=${tableId}&topId=${topId}&uuid='+uuid,
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert('操作成功完成！');
					   }
					   if(data.statusCode == 200){
						   //window.location.reload();
						   jQuery('#mydatagrid').datagrid('reload');
					   }
				   }
			 });
		}
	}

	function editSelected(){
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		  alert("请选择其中一条记录。");
		  return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected ){
		   editRow(selected.uuid);
	    }
	}


	function getSelected(){
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected){
		    alert(selected.code+":"+selected.name+":"+selected.addr+":"+selected.col4);
	    }
	}

	function getSelections(){
	    var ids = [];
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    for(var i=0;i<rows.length;i++){
		    ids.push(rows[i].code);
	    }
	    alert(ids.join(':'));
	}

	function clearSelections(){
	    jQuery('#mydatagrid').datagrid('clearSelections');
	}

	function loadGridData(url){
	    jQuery.ajax({
			type: "POST",
			url:  url,
			dataType:  'json',
			error: function(data){
				alert('服务器处理错误！');
			},
			success: function(data){
				jQuery('#mydatagrid').datagrid('loadData', data);
			}
		});
	}

<#list correlations as table>
  <#if table.tableCorrelation.relationshipType == "OneToMany">
    function openWin${table.tableId}(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		  alert("请选择其中一条记录。");
		  return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected){
			var link="${contextPath}/tableData/datalist?tableId=${table.tableId}&topId="+selected.id;
			layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "${table.title}",
			  area: ['1080px', (jQuery(window).height() - 30) +'px'],
			  shade: 0.85,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
			});
		}
	}
  </#if>
</#list >

    function exportXls(){
        window.open("${contextPath}/tableData/exportXls?tableId=${tableId}&topId=${topId}");
    }

	function searchData(){
       document.iForm.submit();
	}

</script>
</head>
<body>
<input type="hidden" id="tableId" name="tableId" value="${tableId}" >
<input type="hidden" id="parentId" name="parentId" value="${parentId}" >
<input type="hidden" id="nodeId" name="nodeId" value="${nodeId}" > 
<input type="hidden" id="topId" name="topId" value="${topId}" >
<div class="easyui-layout" data-options="fit:true"> 
   <#if table.treeFlag == "Y">
    <div data-options="region:'west',split:true" style="width:240px;">
	  <div class="easyui-layout" data-options="fit:true">  
			 <div data-options="region:'center',border:false">
			    <ul id="myTree" class="ztree"></ul>  
			 </div> 
        </div>  
	</div> 
   </#if>
    <div data-options="region:'center'"> 
	 <div class="easyui-layout" data-options="fit:true">  
	  <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
	    <div> 
		  <form id="iForm" name="iForm" method="post" action="">
			<table>
			 <tr>
			   <td colspan="10">
				<div style="margin-top:2px;height:30px"> 
				&nbsp;<img src="${contextPath}/static/images/window.png">
				&nbsp;<span class="x_content_title">${table.title}</span>
					<#if canEdit == true>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
					   onclick="javascript:addNew();">新增</a>  
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
					   onclick="javascript:editSelected();">修改</a>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
					   onclick="javascript:removeSelected();">删除</a>
					</#if>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'"
					   onclick="javascript:exportXls();">导出Excel</a>
					<#list correlations as table>
					<#if table.tableCorrelation.relationshipType == "OneToMany">
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-list'"
					   onclick="javascript:openWin${table.tableId}();">${table.title}</a>
					</#if>
					</#list>
				</div> 
			   </td>
			   </tr>
			   <#if sqlCriterias?exists>
			   <tr>
				 <#list sqlCriterias as col>
				 <td>
				   &nbsp;${col.paramTitle}&nbsp;
				   <#if col.columnType == "Integer">
					<input id="${col.paramName}" name="${col.paramName}" type="text" 
					   class="easyui-numberbox easyui-validatebox x-searchtext" style="width:90px;" precision="0"
					   <#if col.requiredFlag == "Y"> required="true" data-options="required:true" </#if>
					   value="${col.value}" size="10"/>
					<#elseif col.columnType == "Long">
					<input id="${col.paramName}" name="${col.paramName}" type="text" 
						   class="easyui-numberbox easyui-validatebox x-searchtext" style="width:90px;" precision="0" 
						   <#if col.requiredFlag == "Y"> required="true" data-options="required:true" </#if>
						   value="${col.value}" size="10"/>
					<#elseif col.columnType == "Double">
					<input id="${col.paramName}" name="${col.paramName}" type="text" 
						   class="easyui-numberbox easyui-validatebox x-searchtext" style="width:90px;" precision="${col.scale}"
						   <#if col.requiredFlag == "Y"> required="true" data-options="required:true" </#if>
						   value="${col.value}" size="10"/>
					<#elseif col.columnType == "Date">
					<input id="${col.paramName}" name="${col.paramName}" type="text" 
						   class="easyui-datebox easyui-validatebox x-searchtext" style="width:120px;"  
						   <#if col.requiredFlag == "Y"> required="true" data-options="required:true" </#if>
						   value="${col.value}" size="30"/>
					<#else>
					<input id="${col.paramName}" name="${col.paramName}" type="text" 
					   class="easyui-validatebox  x-searchtext" style="width:120px;"
					   <#if col.requiredFlag == "Y"> required="true" data-options="required:true" </#if>
					   value="${col.value}" size="50"/>
					</#if>
				 </td>
				 </#list>
				 <td align="left">
					<button type="button" id="searchButton" class="btn btnGrayMini" style="width: 60px" 
						onclick="javascript:searchData();">查找</button>
				</td>
			   </tr>
			   </#if>
			 </table>
			</form>
		  </div> 
	    </div> 
        <div data-options="region:'center',border:true">
		  <table id="mydatagrid"></table>
	  </div>  
	</div>
  </div>
</div>
</body>
</html>
