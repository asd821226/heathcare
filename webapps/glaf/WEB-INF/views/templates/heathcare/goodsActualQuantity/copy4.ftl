<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>复制入库量</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	function saveData(){
		var dateTime = document.getElementById("dateTime").value;
		if(document.getElementById("dateTime").value == ""){
			alert("请选择日期。");
			document.getElementById("dateTime").focus();
			return;
		}
	    if(confirm("确定将"+dateTime+"的入库量复制到实际用量表中吗？")){
		  var params = jQuery("#iForm").formSerialize();
		  jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsActualQuantity/copyInStock',
				   data: params,
				   dataType: 'json',
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
					       window.parent.location.reload();
					   } 
				   }
			 });
		}
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false,border:true" style="height:45px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">复制入库量到实际用量</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >确定</a>
    </div> 
  </div>
  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <br><br>
  <table class="easyui-form" style="width:650px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">&nbsp;</td>
		<td width="30%" align="left">
            日期&nbsp;&nbsp;<input id="dateTime" name="dateTime" type="text" class="easyui-datebox x-text" style="width:120px">
		</td>
		<td width="20%" align="left">是否关联到计划用量</td>
		<td width="30%" align="left">
             <select id="includePlan" name="includePlan">
			    <option value="false">不关联</option>
			    <option value="true">关联</option>
			 </select>
		</td>
	</tr>
	<tr>
	  <td colspan="4" align="center">
	     <div style="font-size: 14px; color:blue; font-weight:bold; font-family: 微软雅黑; margin-top:20px;">
		   （提示：已经在实际用量表的食品不会再加入。）
		 </div>
	  </td>
	</tr>
   </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>