<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学生考勤信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<style>

.subject { font-size: 13px; text-decoration: none; font-weight:normal; font-family:"宋体"}
.table-border { background-color:#eaf2ff; height: 32px; font-family:"宋体"}
.table-head { background-color:#5cb1f8; height: 30px; font-weight:bold; font-size: 13px; font-family:"宋体"}
.table-content { background-color:#ffffff; height: 28px; font-size: 12px; font-family:"宋体"}


</style>
<script type="text/javascript">
  <#if privilege_write == true>
	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/personAbsence/saveAll?gradeId=${gradeId}',
				   data: params,
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
					       
					   } 
				   }
			 });
	}
  </#if>

    function submitReq(){
        document.getElementById("iForm").submit();
    }

    function changeXY(personId){
	    var status_ = document.getElementById("status_"+personId).value;
	    if(status_ == 0){
            jQuery("#div_remark_"+personId).hide();
	    } else {
		    jQuery("#div_remark_"+personId).show();
	    }
    }

    jQuery(function(){
        jQuery('#absenceDate').datebox().datebox('calendar').calendar({
                validator: function(date){
                    var now = new Date();
                    var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate() - 365);
                    var d2 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                    return d1<=date && date<=d2;
                }
        });
    });

</script>
</head>

<body>
<div style="margin:0px;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north', split:false, border:true" style="height:48px;margin-top:0px;" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">${gradeInfo.name}考勤信息</span>
		<#if privilege_write == true>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
		   onclick="javascript:saveData();" > 保 存 
		</a> 
		</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post" action="${contextPath}/heathcare/personAbsence">
  <input type="hidden" id="gradeId" name="gradeId" value="${gradeId}"/>
  <table cellspacing="1" cellpadding="4" width="95%" nowrap align="center">
    <tbody>
	  <tr>
	  <td width="20%" align="left">&nbsp;</td>
	  <td width="20%" align="left">&nbsp;</td>
	  <td width="60%" align="right">
	     日期&nbsp;
		 <input id="absenceDate" name="absenceDate" type="text"  
		        class="easyui-datebox x-text" style="width:120px;"
				data-options="onSelect:submitReq"
			    <#if absenceDate?exists>
				value="${absenceDate?string('yyyy-MM-dd')}"
				</#if>>
		 &nbsp;阶段&nbsp;
		 <select id="section" name="section" onchange="javascript:submitReq();">
		    <option value="day">全天</option>
			<option value="am">上午</option>
			<option value="pm">下午</option>
	     </select>
		 <script type="text/javascript">
		     document.getElementById("section").value="${section}";  
		 </script>
	  </td>
	  </tr>
	</tbody>
  </table>
 
  <table class="table-border" cellspacing="1" cellpadding="4" width="95%" nowrap align="center">
    <thead>
	  <tr>
	  <td class="table-head" width="20%" align="left">姓名</td>
	  <td class="table-head" width="20%" align="left">考勤情况</td>
	  <td class="table-head" width="60%" align="left">备注</td>
	  </tr>
	</thead>
    <tbody>
	<#list persons as person>
	<tr>
		<td  class="table-content" align="left"> ${person.name} </td>
		<td  class="table-content" align="left">
            <select id="status_${person.id}" name="status_${person.id}" onchange="javascript:changeXY('${person.id}');">
				<option value="1">正常出勤</option>
				<option value="2">病假</option>
				<option value="3">事假</option>
				<option value="4">其他</option>
            </select>
			<script type="text/javascript">
		         document.getElementById("status_${person.id}").value="${person.status}";  
		    </script>
		</td>
		<td  class="table-content" align="left">
		   <div id="div_remark_${person.id}"
		   <#if person.status == 1>
			style="display:none"
			<#else>
			style="display:block"
			</#if>
		    >
		    <input id="remark_${person.id}" name="remark_${person.id}" style="width:350px;" 
				   class="easyui-validatebox  x-small-text"
				   value="${person.memo}" > 
		   </div>
		</td>
	</tr>
	</#list>
    </tbody>
  </table>
  <br><br><br><br>
 </form>
</div>
</div>

</body>
</html>