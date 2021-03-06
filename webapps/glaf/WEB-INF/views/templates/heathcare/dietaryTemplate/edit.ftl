<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑食谱模板</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

   function templateCate(){
	    var link = '${contextPath}/heathcare/dietaryCategory';
		var x=120;
        var y=50;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 1190, 650);
   }
                
   function saveData(){
	    //alert("保存数据......");
        var form = document.getElementById("iForm");
	    var link = "${contextPath}/heathcare/dietaryTemplate/saveDietaryTemplate";
	    var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: link,
				   dataType: 'json',
				   data: params,
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

   function saveAsData(){
	    //alert("保存数据......");
		//document.getElementById("id").value="";
        var form = document.getElementById("iForm");
	    var link = "${contextPath}/heathcare/dietaryTemplate/saveAs";
	    var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: link,
				   dataType:  'json',
				   data: params,
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert('操作成功完成！');
					   }
					   //window.parent.location.reload();
				   }
			 });
   }

 </script>
</head>
<body style="margin-top:0px;">
<div id="main_content" class="k-content ">
<br>
<div class="x_content_title"><img
	src="${contextPath}/static/images/window.png" alt="编辑食谱模板">&nbsp;
编辑食谱模板</div>
<br>
<form id="iForm" name="iForm" method="post" >
<input type="hidden" id="id" name="id" value="${dietaryTemplate.id}"/>
<table width="95%" align="center" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td width="15%" align="left">名称&nbsp;</td>
    <td align="left" colspan="3">
        <input id="name" name="name" type="text" class="easyui-validatebox  x-text"  
	           data-bind="value: name" style=" width:425px;"
	           value="${dietaryTemplate.name}" validationMessage="请输入名称"/>
	    <span class="k-invalid-msg" data-for="name"></span>
    </td>
  </tr>
  <tr valign="top">
    <td width="15%" align="left"  valign="middle">描述&nbsp;</td>
    <td align="left" valign="top" colspan="3">
      <textarea  id="description" name="description" rows="6" cols="46" class="easyui-validatebox  x-text" 
	             style="height:90px; width:425px;" >${dietaryTemplate.description}</textarea>  
	  <span class="k-invalid-msg" data-for="description"></span>
    </td>
  </tr>
  <tr>
    <td width="15%" align="left">模板序号&nbsp;</td>
    <td align="left">
          <select id="suitNo" name="suitNo">
			<option value="0">----请选择----</option>
			<#list categories as category>
			<option value="${category.suitNo}">${category.name}</option>
			</#list> 
		  </select>
		  <script type="text/javascript">
			   document.getElementById("suitNo").value="${dietaryTemplate.suitNo}";
		  </script>
		  &nbsp;
	      <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-list'" 
	         onclick="javascript:templateCate();" >模板列表</a>
    </td>
    <td width="15%" align="left">日期&nbsp;&nbsp;</td>
    <td align="left">
          <select id="dayOfWeek" name="dayOfWeek">
			<option value="0">----请选择----</option>
			<option value="1">星期一</option>
			<option value="2">星期二</option>
			<option value="3">星期三</option>
			<option value="4">星期四</option>
			<option value="5">星期五</option>
			<option value="6">星期六</option>
			<option value="7">星期日</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("dayOfWeek").value="${dietaryTemplate.dayOfWeek}";
		  </script>
	      <span class="k-invalid-msg" data-for="dayOfWeek"></span>
    </td>
  </tr>
    <tr>
    <td width="15%" align="left">餐点&nbsp;&nbsp;</td>
    <td align="left">
      <select id="typeId" name="typeId">
		<option value="0">----请选择----</option>
		<#list dictoryList as d>
		<option value="${d.id}">${d.name}</option>
		</#list> 
	  </select>
	  <script type="text/javascript">
	       document.getElementById("typeId").value="${dietaryTemplate.typeId}";
	  </script>
	  <span class="k-invalid-msg" data-for="typeId"></span>
    </td>
    <td width="15%" align="left">是否有效&nbsp;</td>
    <td align="left">
      <select id="enableFlag" name="enableFlag">
		<option value="Y">是</option>
		<option value="N">否</option>
	  </select>
	  <script type="text/javascript">
	       document.getElementById("enableFlag").value="${dietaryTemplate.enableFlag}";
	  </script>    
	  <span class="k-invalid-msg" data-for="enableFlag"></span>
    </td>
  </tr>
 
 <#if heathcare_curd_perm == true>
    <tr>
        <td colspan="4" align="center" valign="bottom" height="30">&nbsp;
         <div>
          <input type="button" id="saveButton"  class="btnGray" style="width: 90px" 
		          onclick="javascript:saveData();" value="保存">
		  <#if dietaryTemplate?exists  >
		  &nbsp;
		  <input type="button" id="saveAsButton"  class="btnGray" style="width: 90px" 
		          onclick="javascript:saveAsData();" value="另存">
		  </#if>
	    </div>
	</td>
   </tr>
  </#if>
</table>   
</form>
</div>     
</body>
</html>