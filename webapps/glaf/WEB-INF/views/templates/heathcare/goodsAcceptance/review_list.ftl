<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物品验收单</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

    function getLink(){
	    var link_ = "${contextPath}/heathcare/goodsAcceptance/reviewJson?tenantId=${tenantId}&startTime=${startTime}&endTime=${endTime}";
		//alert(link_);
	    return link_;
	}


   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: getLink(),
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				    {title:'序号', field:'startIndex', width:60, sortable:false},
					{title:'物品名称', field:'goodsName', width:220, align:'left', sortable:true},
					{title:'重量(千克)', field:'quantity', width:120, align:'right', sortable:true},
					{title:'计量单位', field:'unit', width:100, align:'center', formatter:formatterUnit},
					{title:'验收日期', field:'acceptanceTime', width:100, align:'center', sortable:true},
					{title:'状态', field:'businessStatus', width:100, align:'center', formatter:formatterStatus},
					{title:'验收人', field:'createByName', width:100, align:'center'},
					{title:'确认人', field:'confirmName', width:100, align:'center'},
					{title:'确认时间', field:'confirmTime', width:100, align:'center'}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 50,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000],
				pagePosition: 'both'
			});

			var pgx = $("#mydatagrid").datagrid("getPager");
			if(pgx){
			   $(pgx).pagination({
				   onBeforeRefresh:function(){
					   //alert('before refresh');
				   },
				   onRefresh:function(pageNumber,pageSize){
					   //alert(pageNumber);
					   //alert(pageSize);
					   loadGridData(getLink()+"&page="+pageNumber+"&rows="+pageSize);
					},
				   onChangePageSize:function(){
					   //alert('pagesize changed');
					   loadGridData(getLink());
					},
				   onSelectPage:function(pageNumber, pageSize){
					   //alert(pageNumber);
					   //alert(pageSize);
					   loadGridData(getLink()+"&page="+pageNumber+"&rows="+pageSize);
					}
			   });
			}
	});


	function formatterUnit(val, row){
		if(val == 'KG'){
			return "千克";
		} else if(val == 'G'){
			return "克"
		} else if(val == 'L'){
			return "升"
		} else if(val == 'ML'){
			return "毫升"
		}
		return "千克";
	}

   function formatterKey(value, row, index) {
		if(row.businessStatus == 0){
		  var s = '<input name="isCheck" type="checkbox" value="'+row.id+'" > ';
		  return s;
		}
		return "";
	}

	function formatterKeys(val, row){
		if(row.businessStatus == 9){
			return "已验收";
		}
		var str = "";
	    return str;
	}

	function formatterStatus(val, row){
		if(val == 9){
           return "<font color='green'>已验收</font>";   
		}
        return "未验收";   
	}
	

	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
	}

	function reloadGrid(){
	    jQuery('#mydatagrid').datagrid('reload');
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
 
	function doSearch(){
		var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime != ""){
			startTime = startTime + " 00:00:00";
		}
		if(endTime != ""){
			endTime = endTime + " 23:59:59";
			//jQuery("#endTime").val(endTime);
		}
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
		document.iForm.submit();
	}
	
</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:48px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<form id="iForm" name="iForm" method="post" action="">
		  <table valign="middle">
		   <tr>
			<td valign="middle" width="42%">
				<img src="${contextPath}/static/images/window.png">
				&nbsp;<span class="x_content_title">物品验收单列表</span>
			</td>
			<td valign="middle">
			  &nbsp;开始&nbsp;
			  <input id="startTime" name="startTime" type="text" class="easyui-datebox x-text" style="height:23px;width:120px"
					 <#if startTime?exists> value="${startTime}"</#if>>
			  &nbsp;结束&nbsp;
			  <input id="endTime" name="endTime" type="text" class="easyui-datebox x-text" style="height:23px;width:120px"
					 <#if endTime?exists> value="${endTime}"</#if>>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
				 onclick="javascript:doSearch();">查找</a>
			  &nbsp;
			</td>
		   </tr>
		  </table>
		</form>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</div>
</body>
</html>