<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>复制采购计划</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	function saveData(){
		var dateTime = document.getElementById("purchaseTime").value;
		if(document.getElementById("purchaseTime").value==""){
			alert("请选择日期。");
			document.getElementById("purchaseTime").focus();
			return;
		}
	    if(confirm("确定将"+dateTime+"的采购计划复制到采购登记表中吗？")){
		  var params = jQuery("#iForm").formSerialize();
		  jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsPurchase/copyPurchasePlan',
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
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">复制采购计划</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >确定</a>
    </div> 
  </div>
  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <br><br>
  <table class="easyui-form" style="width:480px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">计划采购日期</td>
		<td align="left">
            <input id="purchaseTime" name="purchaseTime" type="text" 
			       class="easyui-datebox x-text" style="width:120px">
		</td>
	</tr>
   </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>