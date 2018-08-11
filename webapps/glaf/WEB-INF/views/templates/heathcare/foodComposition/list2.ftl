<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食物成分</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1880,
				height:556,
				fit:false,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/heathcare/foodComposition/json?wordLike_enc=${wordLike_enc}&nodeId=${nodeId}&namePinyinLike=${namePinyinLike}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号',field:'startIndex', width:80, sortable:false},
						{title:'名称',field:'name', width:180, formatter:formatterName, sortable:true},
						{title:'别名',field:'alias', width:180, sortable:true},
						{title:'食部',field:'radical', width:80, align:"right"},
						{title:'热能(千卡)',field:'heatEnergy', width:90, align:"right",sortable:true},
						{title:'蛋白质(克)',field:'protein', width:90, align:"right",sortable:true},
						{title:'脂肪(克)',field:'fat', width:90, align:"right",sortable:true},
						{title:'碳水化合物(克)',field:'carbohydrate', width:120, align:"right",sortable:true},
						{title:'微生素A(μgRE)',field:'vitaminA', width:120, align:"right",sortable:true},
						{title:'微生素B1(毫克)',field:'vitaminB1', width:120, align:"right",sortable:true},
						{title:'微生素B2(毫克)',field:'vitaminB2', width:120, align:"right",sortable:true},
						{title:'微生素C(毫克)',field:'vitaminC', width:120, align:"right",sortable:true},
						{title:'胡萝卜素(微克)',field:'carotene', width:120, align:"right",sortable:true},
						{title:'视黄醇(微克)',field:'retinol', width:90, align:"right",sortable:true},
						{title:'尼克酸(毫克)',field:'nicotinicCid', width:90, align:"right",sortable:true},
						{title:'钙(毫克)',field:'calcium', width:90, align:"right",sortable:true},
						{title:'铁(毫克)',field:'iron', width:90, align:"right",sortable:true},
						{title:'锌(毫克)',field:'zinc', width:90, align:"right",sortable:true},
						{title:'碘(毫克)',field:'iodine', width:90, align:"right",sortable:true},
						{title:'磷(毫克)',field:'phosphorus', width:90, align:"right",sortable:true}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 100,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000,2000],
				pagePosition: 'bottom',
				onDblClickRow: onMyRowClick 
			});

	});

	function formatterName(val, row){
        var str = "<a href='javascript:viewRow(\""+row.id+"\");' title='"+row.description+"'>"+val+"</a>";
	    return str;
	}

	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/foodComposition/view?id='+row.id;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "查看记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function viewRow(id){
	    var link = '${contextPath}/heathcare/foodComposition/view?id='+id;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "查看记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}


	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','食物成分查询');
	    //jQuery('#searchForm').form('clear');
	}

	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
	}

	function editSelected(){
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		  alert("请选择其中一条记录。");
		  return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected ){
		  var link = '${contextPath}/heathcare/foodComposition/view?id='+selected.id;
		  jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "编辑记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	    }
	}

	function viewSelected(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
		    var link='${contextPath}/heathcare/foodComposition/view?readonly=true&id='+selected.id;
		    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "编辑记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		   });
		}
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

	function searchData(){
        document.iForm.submit();
	}

	function searchXY(nameLike){
		var nodeId = document.getElementById("nodeId").value;
        var link = "${contextPath}/heathcare/foodComposition/json?nodeId="+nodeId+"&namePinyinLike="+nameLike;
		loadGridData(link);
	}

</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:68px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<table>
			<tr>
				 <td>
					<img src="${contextPath}/static/images/window.png">
					&nbsp;<span class="x_content_title">食物成分列表</span>
					<#if heathcare_write_perm == true>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
					   onclick="javascript:addNew();">新增</a>  
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
					   onclick="javascript:editSelected();">修改</a>  
					</#if>
				 </td>
				 <td>
				  <form id="iForm" name="iForm" method="post" action="${contextPath}/heathcare/foodComposition/list2">
					&nbsp;大类&nbsp;
					<select id="nodeId" name="nodeId">
						<option value="">----请选择----</option>
						<#list foodCategories as tree>
						<option value="${tree.id}">${tree.name}</option>
						</#list> 
					</select>
					<script type="text/javascript">
						document.getElementById("nodeId").value="${nodeId}";
					</script>
					&nbsp;名称&nbsp;
					<input type="text" id="wordLike" name="wordLike" value="${wordLike}" size="30"  style="width:180px" 
						   class="easyui-validatebox  x-searchtext">
					&nbsp;&nbsp;
					<button type="button" id="searchButton"  class="btnGrayMini" style="width: 90px" 
							onclick="javascript:searchData();">查找</button>
					</form>
				 </td>
			 </tr>
			 <tr>
				<td colspan="2">
				    <input type="hidden" id="namePinyinLike" name="namePinyinLike" value="${namePinyinLike}">
					&nbsp;&nbsp;&nbsp;&nbsp;
					<#list charList as item>
					&nbsp;<span style="cursor: pointer;" onclick="javascript:searchXY('${item}');">${item}</span>&nbsp;
					</#list>
				</td>
			</tr>
		 </table>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</div>
</body>
</html>