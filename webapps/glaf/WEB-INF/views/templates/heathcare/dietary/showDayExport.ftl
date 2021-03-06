<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食谱构成</title>
<#include "/inc/init_easyui_import.ftl"/>
<style>

.table-border { background-color:#0099CC; height: 32px; font-family:"微软雅黑"}
.table-content { background-color:#ffffff; height: 32px;font-size: 16px; font-family:"微软雅黑"}

.x_y_title {
	text-transform: uppercase;
	background-color: inherit;
	color: #000033;
	font-size: 15px;
	font-weight: bold;
	text-align: center;
}

.dietary_title {
	height: 20px;
	line-height: 20px;
	text-align: center;
	font: bold 16px 微软雅黑;
	color: #FF6600;
	cursor: pointer;
}

.dietary_item {
	height: 20px;
	line-height: 20px;
	text-align: center;
	font: bold 15px 微软雅黑;
	color: #0099CC;
}

.xz_input {
    background-color: #fff;
	border: 1px solid #fff;
	font: bold 15px 微软雅黑;
	color: #FF6600;
	padding: 2px 2px;
	line-height: 22px;
	width: 35px;
	height: 22px;
	font-size: 15px;
	text-align: right;
}

.xz_input:hover {
	color: rgb(255, 0, 0);
	font-weight: bold;
	box-shadow: 1px 1px 1px 1px #aaa;
	background-color: #ffff99;
	font-size: 15px;
	-moz-box-shadow: 0 1px 1px #aaa;
	-webkit-box-shadow: 0 1px 1px #aaa;
}

</style>
<script type="text/javascript">

 
	function doSubmit(){
		document.iForm.action="${contextPath}/heathcare/dietaryExport/showDayExport";
        document.iForm.submit();
	}

	function modifyItem(id){
		var quantity = jQuery("#item_"+id).val();
		//alert(id + "=" + quantity);
		var link="${contextPath}/heathcare/dietaryItem/updateQuantity?id="+id+"&quantity="+quantity;
        jQuery.ajax({
				   type: "POST",
				   url: link,
				   dataType: 'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					  
				   }
			 });
	}

    function editItems(dietaryId) {
		var link = "${contextPath}/heathcare/dietaryItem/datalist?dietaryId="+dietaryId;
		jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "食物构成",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['880px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
		});
	}
 
 	function execute(){
		var form = document.getElementById("iForm");
	    var link = "${contextPath}/heathcare/dietaryStatistics/execute";
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
					   window.location.reload();
				   }
			 });
	}

 	function exportNutrition(){
		var fullDay = jQuery("#fullDay").val();
		if(fullDay == ""){
            alert('请选择日期！');
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=DailyDietaryNutritionCount";
		if(fullDay != ""){
			link = link  + "&fullDay="+fullDay;
		}
        window.open(link);
	}

	function addDishes(id){
		var link = '${contextPath}/heathcare/dietary/showAddDishes?oDietaryId='+id;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "增加菜肴",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['1280px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function changeDishes(id){
		var link = '${contextPath}/heathcare/dietary/showChangeDishes?oDietaryId='+id;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "更换菜肴",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['1280px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function removeDishes(id){
	   if(confirm("数据删除后不能恢复，确定删除吗？")){
		  var link = '${contextPath}/heathcare/dietary/delete?id='+id;
	      jQuery.ajax({
				   type: "POST",
				   url: link,
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
					       window.location.reload();
					   }
				   }
			 });
	   }
	}

</script>
</head>
<body style="margin-left:5px;">
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north', split:false, border:false" style="height:45px"> 
    <div class="toolbar-backgroud">
	  <form id="iForm" name="iForm" method="post">
	   <table width="1050" align="left">
		<tbody>
		 <tr>
		    <td width="15%" align="left">
			<img src="${contextPath}/static/images/window.png"><span class="x_content_title">&nbsp;食谱构成</span>
			</td>
			<td width="45%" align="left">
			  &nbsp;年份&nbsp; 
			  <select id="year" name="year">
				<#list years as year>
				<option value="${year}">${year}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("year").value="${year}";
			  </script>
			  &nbsp;周次&nbsp;
			  <select id="week" name="week">
				<#list weeks as week>
				<option value="${week}">${week}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("week").value="${week}";
			  </script>
			  &nbsp;日期&nbsp;
			  <select id="fullDay" name="fullDay" onchange="javascript:switchDay();">
				<#list days as day>
				<option value="${day}">${day}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("fullDay").value="${fullDay}";
			  </script>
			</td>
			<td width="5%"></td>
			<td>
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-ok'" 
	             onclick="javascript:doSubmit();" >确定</a>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
	             onclick="javascript:exportNutrition();" >营养成分统计表</a>
			  <#if dietary_copy_add_perm == true>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-formula'" 
	             onclick="javascript:execute();" >统计成分</a>
			  </#if>
			</td>
		</tr>
	   </tbody>
	  </table>
	 </form>
    </div> 
   </div>
   <div data-options="region:'center',border:false,cache:true">
   <#if dayRptModel?exists>
   <table width="100%" height="99%">
    <tr>
    <td width="1050" valign="top">
     <table width="1050" height="99%" cellpadding='1' cellspacing='1' class="table-border" nowrap>
	  <tr>
		<td colspan="6" align="center" width="100%"  class="table-content">
		  <table border='0' cellpadding='0' cellspacing='0' width="100%">
		   <tr>
		     <td width="70%" align="center">
		       <span class="x_y_title">  ${fullDay}  帯  量  食  谱 </span>
		     </td>
		     <td width="30%" align="right">
		       <span >&nbsp;配餐均龄：4岁 &nbsp;一人可食均量：克&nbsp;</span>
		     </td>
		     </tr>
		   </table>
		</td>
	  </tr>
	  <tr>
		  <td width="80" class="table-content">
		  &nbsp;&nbsp;餐别
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0'   >
			  <tr>
				<td width="150">&nbsp;食谱&nbsp;</td>
				<td width="300">
					<table>
					<tr>
					  <td align="left"  width="200">&nbsp;食物&nbsp;</td>
					  <td align="right" width="100">&nbsp;重量&nbsp;</td>
					</tr>
					</table>
				</td>
				<td align="right" width="30">&nbsp;&nbsp;</td>
				<td align="right" width="100">&nbsp;热能(kcal)&nbsp;</td>
				<td align="right" width="100">&nbsp;碳水化合物(g)&nbsp;</td>
				<td align="right" width="100">&nbsp;蛋白质(g)&nbsp;</td>
				<td align="right" width="100">&nbsp;脂肪(g)&nbsp;</td>
				<td align="right" width="100">&nbsp;钙(mg)&nbsp;</td>
			  </tr>

			</table>
		  </td>
        </tr>
	  <#if breakfastList?exists>
	    <tr>
		  <td width="80" class="table-content">
		  &nbsp;&nbsp;早餐<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0'   >
			  <#list breakfastList as r1>
			  <tr>
				<td width="150">
				   <span class="dietary_title" onclick="javascript:editItems('${r1.dietary.id}');">${r1.name}</span>
				   <#if canChangeDishes == true>
				   &nbsp;
				   <span><img src="${contextPath}/static/images/add2.png" title="点击增加菜肴"
				              onclick="javascript:addDishes('${r1.dietary.id}');" style="cursor:pointer;"></span>
				   &nbsp;
				   <span><img src="${contextPath}/static/images/remove.png" title="点击删除菜肴"
				              onclick="javascript:removeDishes('${r1.dietary.id}');" style="cursor:pointer;"></span>
				   &nbsp;
				   <span><img src="${contextPath}/static/images/arrow_switch.png"  title="点击更换菜肴"
				              onclick="javascript:changeDishes('${r1.dietary.id}');" style="cursor:pointer;"></span>
				   </#if>
				</td>
				<td width="300">
					<table>
					<#list r1.items as item1>
					<tr>
					  <td align="left"  width="200"><span class="dietary_item">${item1.name}</span></td>
					  <td align="right" width="100">
					    <#if item1.name?exists>
					      <input type="text" id="item_${item1.id}" name="myInput" value="${item1.quantity2}"
						         onchange="javascript:modifyItem('${item1.id}');" size="3" class="xz_input">
						</#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
				<td align="right" width="30">&nbsp;&nbsp;</td>
				<td align="right" width="100">${r1.heatEnergy}</td>
				<td align="right" width="100">${r1.carbohydrate}</td>
				<td align="right" width="100">${r1.protein}</td>
				<td align="right" width="100">${r1.fat}</td>
				<td align="right" width="100">${r1.calcium}</td>
			  </tr>
			  </#list>
			</table>
		  </td>
        </tr>
	  </#if>
	  <#if breakfastMidList?exists>
	    <tr>
		  <td class="table-content">
		  &nbsp;&nbsp;早点
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0'  >
			  <#list breakfastMidList as r2>
			  <tr>
				<td width="150">
				   <span class="dietary_title" onclick="javascript:editItems('${r2.dietary.id}');">${r2.name}</span>
				   <#if canChangeDishes == true>
				   &nbsp;
				   <span><img src="${contextPath}/static/images/add2.png" title="点击增加菜肴"
				              onclick="javascript:addDishes('${r2.dietary.id}');" style="cursor:pointer;"></span>
				   &nbsp;
				   <span><img src="${contextPath}/static/images/remove.png" title="点击删除菜肴"
				              onclick="javascript:removeDishes('${r2.dietary.id}');" style="cursor:pointer;"></span>
				   &nbsp;
				   <span><img src="${contextPath}/static/images/arrow_switch.png"  title="点击更换菜肴"
				              onclick="javascript:changeDishes('${r2.dietary.id}');" style="cursor:pointer;"></span>
				   </#if>
				</td>
				<td width="300">
					<table>
					<#list r2.items as item2>
					<tr>
					  <td align="left"  width="200"><span class="dietary_item">${item2.name}</span></td>
					  <td align="right" width="100">
					    <#if item2.name?exists>
					      <input type="text" id="item_${item2.id}" name="myInput" value="${item2.quantity2}"
						         onchange="javascript:modifyItem('${item2.id}');" size="3" class="xz_input">
						</#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
				<td align="right" width="30">&nbsp;&nbsp;</td>
				<td align="right" width="100">${r2.heatEnergy}</td>
				<td align="right" width="100">${r2.carbohydrate}</td>
				<td align="right" width="100">${r2.protein}</td>
				<td align="right" width="100">${r2.fat}</td>
				<td align="right" width="100">${r2.calcium}</td>
			  </tr>
			  </#list>
			</table>
		  </td>
        </tr>
	  </#if>
	  <#if momingTotal?exists>
	  <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;小计<br>
           &nbsp;&nbsp;占比<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <tr>
				<td width="150"></td>
				<td align="left" width="300">&nbsp;</td>
				<td align="right" width="30">&nbsp;</td>
				<td align="right" width="100">
				 ${momingTotal.heatEnergy}
                 <br>${momingTotalPercent.heatEnergy}%
				</td>
				<td align="right" width="100">
				${momingTotal.carbohydrate}
                <br>${momingTotalPercent.carbohydrate}%
				</td>
				<td align="right" width="100">
				${momingTotal.protein}
				<br>${momingTotalPercent.protein}%
				</td>
				<td align="right" width="100">
				${momingTotal.fat}
				<br>${momingTotalPercent.fat}%
				</td>
				<td align="right" width="100">
				${momingTotal.calcium}
				<br>${momingTotalPercent.calcium}%
				</td>
			   </tr>
			  </table>
		  </td>
       </tr>
	  </#if>
	  <#if lunchList?exists>
	    <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;午餐<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <#list lunchList as r3>
			  <tr>
				<td width="150">
				   <span class="dietary_title" onclick="javascript:editItems('${r3.dietary.id}');">${r3.name}</span>
				   <#if canChangeDishes == true>
				   &nbsp;
				   <span><img src="${contextPath}/static/images/add2.png" title="点击增加菜肴"
				              onclick="javascript:addDishes('${r3.dietary.id}');" style="cursor:pointer;"></span>
				   &nbsp;
				   <span><img src="${contextPath}/static/images/remove.png" title="点击删除菜肴"
				              onclick="javascript:removeDishes('${r3.dietary.id}');" style="cursor:pointer;"></span>
				   &nbsp;
				   <span><img src="${contextPath}/static/images/arrow_switch.png"  title="点击更换菜肴"
				              onclick="javascript:changeDishes('${r3.dietary.id}');" style="cursor:pointer;"></span>
				   </#if>
				</td>
				<td width="300">
					<table>
					<#list r3.items as item3>
					<tr>
					  <td align="left"  width="200"><span class="dietary_item">${item3.name}</span></td>
					  <td align="right" width="100">
					    <#if item3.name?exists>
					      <input type="text" id="item_${item3.id}" name="myInput" value="${item3.quantity2}"
						         onchange="javascript:modifyItem('${item3.id}');" size="3" class="xz_input">
						</#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
				<td align="right" width="30">&nbsp;&nbsp;</td>
				<td align="right" width="100">${r3.heatEnergy}</td>
				<td align="right" width="100">${r3.carbohydrate}</td>
				<td align="right" width="100">${r3.protein}</td>
				<td align="right" width="100">${r3.fat}</td>
				<td align="right" width="100">${r3.calcium}</td>
			  </tr>
			  </#list>
			</table>
		  </td>
        </tr>
	  </#if>
	  <#if snackList?exists>
	    <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;午点<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <#list snackList as r4>
			  <tr>
				<td width="150">
				   <span class="dietary_title" onclick="javascript:editItems('${r4.dietary.id}');">${r4.name}</span>
				   <#if canChangeDishes == true>
				   &nbsp;
				   <span><img src="${contextPath}/static/images/add2.png" title="点击增加菜肴"
				              onclick="javascript:addDishes('${r4.dietary.id}');" style="cursor:pointer;"></span>
				   &nbsp;
				   <span><img src="${contextPath}/static/images/remove.png" title="点击删除菜肴"
				              onclick="javascript:removeDishes('${r4.dietary.id}');" style="cursor:pointer;"></span>
				   &nbsp;
				   <span><img src="${contextPath}/static/images/arrow_switch.png"  title="点击更换菜肴"
				              onclick="javascript:changeDishes('${r4.dietary.id}');" style="cursor:pointer;"></span>
				   </#if>
				</td>
				<td width="300">
					<table>
					<#list r4.items as item4>
					<tr>
					  <td align="left"  width="200"><span class="dietary_item">${item4.name}</span></td>
					  <td align="right" width="100">
					    <#if item4.name?exists>
					      <input type="text" id="item_${item4.id}" name="myInput" value="${item4.quantity2}"
						         onchange="javascript:modifyItem('${item4.id}');" size="3" class="xz_input">
						</#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
				<td align="right" width="30">&nbsp;&nbsp;</td>
				<td align="right" width="100">${r4.heatEnergy}</td>
				<td align="right" width="100">${r4.carbohydrate}</td>
				<td align="right" width="100">${r4.protein}</td>
				<td align="right" width="100">${r4.fat}</td>
				<td align="right" width="100">${r4.calcium}</td>
			  </tr>
			  </#list>
			</table>
		  </td>
        </tr>
	  </#if>
      <#if noonTotal?exists>
	  <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;小计<br>
           &nbsp;&nbsp;占比<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <tr>
				<td width="150"></td>
				<td align="left" width="300">&nbsp;</td>
				<td align="right" width="30">&nbsp;</td>
				<td align="right" width="100">
				 ${noonTotal.heatEnergy}
                 <br>${noonTotalPercent.heatEnergy}%
				</td>
				<td align="right" width="100">
				${noonTotal.carbohydrate}
                <br>${noonTotalPercent.carbohydrate}%
				</td>
				<td align="right" width="100">
				${noonTotal.protein}
				<br>${noonTotalPercent.protein}%
				</td>
				<td align="right" width="100">
				${noonTotal.fat}
				<br>${noonTotalPercent.fat}%
				</td>
				<td align="right" width="100">
				${noonTotal.calcium}
				<br>${noonTotalPercent.calcium}%
				</td>
			  </tr>
			</table>
		  </td>
       </tr>
      </#if>
	  <#if dinnerList?exists>
	    <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;晚餐<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <#list dinnerList as r5>
			  <tr>
				<td width="150">
				   <span class="dietary_title" onclick="javascript:editItems('${r5.dietary.id}');">${r5.name}</span>
				   <#if canChangeDishes == true>
				   &nbsp;
				   <span><img src="${contextPath}/static/images/add2.png" title="点击增加菜肴"
				              onclick="javascript:addDishes('${r5.dietary.id}');" style="cursor:pointer;"></span>
				   &nbsp;
				   <span><img src="${contextPath}/static/images/remove.png" title="点击删除菜肴"
				              onclick="javascript:removeDishes('${r5.dietary.id}');" style="cursor:pointer;"></span>
				   &nbsp;
				   <span><img src="${contextPath}/static/images/arrow_switch.png"  title="点击更换菜肴"
				              onclick="javascript:changeDishes('${r5.dietary.id}');" style="cursor:pointer;"></span>
				   </#if>
				</td>
				<td width="300">
					<table>
					<#list r5.items as item5>
					<tr>
					  <td align="left"  width="200"><span class="dietary_item">${item5.name}</span></td>
					  <td align="right" width="100">
					    <#if item5.name?exists>
					      <input type="text" id="item_${item5.id}" name="myInput" value="${item5.quantity2}"
						         onchange="javascript:modifyItem('${item5.id}');" size="3" class="xz_input">
						</#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
				<td align="right" width="30">&nbsp;&nbsp;</td>
				<td align="right" width="100">${r5.heatEnergy}</td>
				<td align="right" width="100">${r5.carbohydrate}</td>
				<td align="right" width="100">${r5.protein}</td>
				<td align="right" width="100">${r5.fat}</td>
				<td align="right" width="100">${r5.calcium}</td>
			  </tr>
			  </#list>
			</table>
		  </td>
       </tr>
	   <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;小计<br>
           &nbsp;&nbsp;占比<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <tr>
				<td width="150"></td>
				<td align="left" width="300">&nbsp;</td>
				<td align="right" width="30">&nbsp;</td>
				<td align="right" width="100">
				 ${dietaryCount_dinner.heatEnergy}
                 <br>${dietaryCountPercent_dinner.heatEnergy}%
				</td>
				<td align="right" width="100">
				${dietaryCount_dinner.carbohydrate}
                <br>${dietaryCountPercent_dinner.carbohydrate}%
				</td>
				<td align="right" width="100">
				${dietaryCount_dinner.protein}
				<br>${dietaryCountPercent_dinner.protein}%
				</td>
				<td align="right" width="100">
				${dietaryCount_dinner.fat}
				<br>${dietaryCountPercent_dinner.fat}%
				</td>
				<td align="right" width="100">
				${dietaryCount_dinner.calcium}
				<br>${dietaryCountPercent_dinner.calcium}%
				</td>
			  </tr>
			</table>
		  </td>
       </tr>
	 </#if>

	  <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;合计<br>
		   &nbsp;&nbsp;标准<br>
		   &nbsp;&nbsp;占比<br>
		   &nbsp;&nbsp;评价<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <tr>
				<td width="150"></td>
				<td align="left" width="300">&nbsp;</td>
				<td align="right" width="30">&nbsp;</td>
				<td align="right" width="100">
				${dietaryCountSum.heatEnergy}
                <br>
				${foodDRI.heatEnergy}
				<br>
				${dayRptModel.heatEnergyPercent}%
				<br>
				${dayRptModel.heatEnergyEvaluate}
				</td>
				<td align="right" width="100">
				${dietaryCountSum.carbohydrate}
				<br>
				${foodDRI.carbohydrate}
				<br>
				${dayRptModel.carbohydratePercent}%
				<br>
				${dayRptModel.carbohydrateEvaluate}
				</td>
				<td align="right" width="100">
				${dietaryCountSum.protein}
				<br>
				${foodDRI.protein}
				<br>
				${dayRptModel.proteinPercent}%
				<br>
				${dayRptModel.proteinEvaluate}
				</td>
				<td align="right" width="100">
				${dietaryCountSum.fat}
				<br>
				${foodDRI.fat}
				<br>
				${dayRptModel.fatPercent}%
				<br>
				${dayRptModel.fatEvaluate}
				</td>
				<td align="right" width="100">
				${dietaryCountSum.calcium}
				<br>
				${foodDRI.calcium}
				<br>
				${dayRptModel.calciumPercent}%
				<br>
				${dayRptModel.calciumEvaluate}
				</td>
			  </tr>
			</table>
		  </td>
       </tr>
    </table>
    </td>
	<td height="98%" width="100%">
	 <table height="100%" width="100%">
	  <tbody>
	   <tr height="45%">
		<td>
		  <iframe id="x1" name="x1" style="width:420px; height:390px" frameborder="0" ></iframe>		  
        </td>
	   </tr>
	   <tr height="55%">
		<td>
		  <iframe id="x2" name="x2" style="width:420px; height:380px" frameborder="0"></iframe>
		</td>
	   </tr>
	  </tbody>
	 </table>
	</td>
   </tr>
  </table>
  <table>
   <tr>
	<td width="45%">
	    <iframe id="x3" name="x3" style="width:420px; height:480px" frameborder="0" ></iframe>
	</td>
	<td width="30%">
	    <iframe id="x4" name="x4" style="width:520px; height:480px" frameborder="0" ></iframe>
	</td>
	<td width="25%">
	</td>
   </tr>
  </table>
  <table>
   <tr>
	<td width="45%">
	    <iframe id="x5" name="x5" style="width:420px; height:480px" frameborder="0" ></iframe>
	</td>
	<td width="30%">
	    <iframe id="x6" name="x6" style="width:520px; height:480px" frameborder="0" ></iframe>
	</td>
	<td width="25%">
	</td>
   </tr>
  </table>
  <table>
   <tr>
	<td width="45%">
	    <iframe id="x7" name="x7" style="width:420px; height:480px" frameborder="0" ></iframe>
	</td>
	<td width="30%">
	    <iframe id="x8" name="x8" style="width:520px; height:480px" frameborder="0" ></iframe>
	</td>
	<td width="25%">
	</td>
   </tr>
  </table>
  </#if>
  <br>&nbsp;
  <br>&nbsp;
  <br>&nbsp;
 </div>   
</div>
 <br>&nbsp;
 <br>&nbsp;
 <br>&nbsp;
 <script type="text/javascript">
	jQuery(document).ready(function() { 

        $('#x1').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135903&suitNo=${suitNo}&type=heatEnergyX1PerDayDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=true&sysFlag=N');

		$('#x2').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135902&suitNo=${suitNo}&type=heatEnergyX2PerDayDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=true&sysFlag=N');

		$('#x3').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135907&suitNo=${suitNo}&type=heatEnergyX3PercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=true&sysFlag=N');

		$('#x4').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135302&suitNo=${suitNo}&type=proteinPercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=true&sysFlag=N');

		$('#x5').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135904&suitNo=${suitNo}&type=vitaminAAnimalsX2PercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=true&sysFlag=N');

        $('#x6').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135905&suitNo=${suitNo}&type=ironAnimalsX2PercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=true&sysFlag=N');

		$('#x7').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135906&suitNo=${suitNo}&type=fatAnimalsX2PercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=true&sysFlag=N');

		$('#x8').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135908&suitNo=${suitNo}&type=calciumPhosphorusX2PercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=true&sysFlag=N');

        }); 
</script>	
</body>
</html>