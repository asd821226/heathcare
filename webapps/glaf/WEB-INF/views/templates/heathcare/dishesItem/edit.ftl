<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑菜肴食物明细信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">
   
    function save(){

	    if(document.getElementById("nodeId").value==""){
			alert("食物大类不能为空。");
			document.getElementById("nodeId").focus();
			return;
		}

	    if(document.getElementById("foodId").value==""){
			alert("选用食物不能为空。");
			document.getElementById("foodId").focus();
			return;
	    }

		if(document.getElementById("name").value==""){
			alert("名称不能为空。");
			document.getElementById("name").focus();
			return;
		}

		if(document.getElementById("quantity").value=="" || document.getElementById("quantity").value * 1.0 <= 0){
			alert("请输入数量。");
			document.getElementById("quantity").focus();
			return;
	    }
  
	    var link = "${contextPath}/heathcare/dishesItem/saveItem";
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

   function switchFood(){
       var nodeId = document.getElementById("nodeId").value;
	   if(nodeId != ""){
		   var link = "${contextPath}/heathcare/foodComposition/jsonFavorites?nodeId="+nodeId+"&rows=1000&ts=${ts}";
		   //alert(link);
		   jQuery.getJSON(link, function(data){
			  var food = document.getElementById("foodId");
			  food.options.length=0;
			  jQuery.each(data.rows, function(i, item){
				 food.options.add(new Option(item.name, item.id));
			  });
			});
	   }
   }

   function switchFood2(selected){
       var nodeId = document.getElementById("nodeId").value;
	   if(nodeId != ""){
		   var link = "${contextPath}/heathcare/foodComposition/jsonFavorites?nodeId="+nodeId+"&rows=1000&ts=${ts}&selected="+selected;
		   //alert(link);
		   jQuery.getJSON(link, function(data){
			  var food = document.getElementById("foodId");
			  food.options.length=0;
			  var selectedIndex = 0;
			  jQuery.each(data.rows, function(i, item){
				 if(item.id == selected){
					 selectedIndex = i;
				 }
				 food.options.add(new Option(item.name, item.id));
			  });
             food.selectedIndex = selectedIndex;
			 //alert(selectedIndex);
			});
	   }
   }

   function changeName(){
	    var name = document.getElementById("name");
        var food = document.getElementById("foodId");
		//if(name.value == ""){
        name.value = food.options[food.selectedIndex].text;
		//}

		//var selectedItem = select.options(select.selectedIndex);//获取当前选中项 
		//selectedItem.text; //选中项的文本 
		//selectedItem.value; //选中项的值 
   }

   function searchGoods() {
	    var link = '${contextPath}/heathcare/foodComposition/searchlist';
        var x=80;
        var y=80;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 980, 500);
	}
 </script>
</head>
<body style="margin-top:0px;">
<div id="main_content" class="k-content ">
<br>
<div class="x_content_title">
<img src="${contextPath}/static/images/window.png" alt="编辑菜肴食物明细信息">&nbsp;
编辑菜肴食物明细信息
</div>
<br>
<form id="iForm" name="iForm" method="post" data-role="validator" novalidate="novalidate">
<input type="hidden" id="id" name="id" value="${dishesItem.id}">
<input type="hidden" id="dishesId" name="dishesId" value="${dishesId}">
<table width="95%" align="center" border="0" cellspacing="0" cellpadding="5" valign="top">
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="foodId" class="required">&nbsp;选用食物&nbsp;</label>
	    <select id="nodeId" name="nodeId" onchange="javascript:switchFood();">
			<option value="0">----请选择----</option>
			<#list foodCategories as cat>
			<option value="${cat.id}">${cat.name}</option>
			</#list> 
		</select>
		<script type="text/javascript">
	        document.getElementById("nodeId").value="${nodeId}";
	    </script>
		&nbsp;
		<select id="foodId" name="foodId" onchange="javascript:changeName();">
			<option value="">----请选择----</option>
			<#list foods as food>
			<option value="${food.id}">${food.name}</option>
			</#list> 
		</select>
		<script type="text/javascript">
	       document.getElementById("foodId").value="${dishesItem.foodId}";
	    </script>
		&nbsp;
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
	       onclick="javascript:searchGoods();">查找</a>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="name" class="required">&nbsp;名称&nbsp;</label>
        <input id="name" name="name" type="text" class="easyui-validatebox  x-text"  
	           style="width:385px;" size="40"
	           value="${dishesItem.name}" validationMessage="请输入名称"/>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left" valign="top">
	    <label for="description" class="required">&nbsp;描述&nbsp;</label>
        <textarea id="description" name="description" rows="4" cols="38" class="easyui-validatebox  x-text" 
		           style="height:90px;width:385px;" >${dishesItem.description}</textarea>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="quantity" class="required">&nbsp;用量(克)&nbsp;</label>
	    <input id="quantity" name="quantity" type="text" class="easyui-numberbox  x-text" 
	           style="width:50px;" maxlength="4" style="width:80px; text-align:right;"
	           value="${dishesItem.quantity}" validationMessage="请输入用量(克)"/>
    </td>
  </tr>

  <#if canEdit == true>
  <tr>
    <td colspan="2" align="center" valign="bottom" height="30">&nbsp;
      <div>
        <button type="button" id="saveButton"  class="btnGray" style="width: 90px" onclick="javascript:save();">保存</button>
	  </div>
	</td>
  </tr>
  </#if>
</table>   
</form>
<br>
<br>
</div>
</body>
</html>