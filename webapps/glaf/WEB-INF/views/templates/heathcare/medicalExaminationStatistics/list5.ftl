<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>体格检查</title>
<#include "/inc/init_easyui_layer3_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">
  
	function exportXls(outputFormat){
		var gradeId = jQuery("#gradeId").val();
		var year = jQuery("#year").val();
        var month = jQuery("#month").val();
		var link="${contextPath}/heathcare/tenantReportMain/exportXls?reportId=MedicalExaminationSicknessPositiveSign&checkId=${checkId}&ts=${ts}&type=${type}&outputFormat="+outputFormat;
		if(gradeId != ""){
			link = link + "&gradeId=" + gradeId;
		}
		if(year != ""){
			link = link + "&year=" + year;
		}
		if(month != ""){
			link = link  + "&month=" + month;
		}
        var frm = document.getElementById("frm2");
		var iwindow = frm.contentWindow;
        var idoc = iwindow.document;
        frm.height = idoc.body.offsetHeight;
		//alert(link);
		frm.src=link;
    }

	function exportXls2(outputFormat){
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=Growth&personId=${personId}&checkId=${checkId}&ts=${ts}&outputFormat="+outputFormat;
        var frm = document.getElementById("frm2");
		frm.src=link;
    }
	
	function exportXls3(outputFormat){
		var personId = document.getElementById("personId").value;
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=Growth&personId="+personId+"&checkId=${checkId}&ts=${ts}&outputFormat="+outputFormat;
        var frm = document.getElementById("frm2");
		frm.src=link;
    }	

	function exportXls55(outputFormat){
		var gradeId = jQuery("#gradeId").val();
		var year = jQuery("#year").val();
        var month = jQuery("#month").val();
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalExaminationCount&checkId=${checkId}&ts=${ts}&outputFormat="+outputFormat;
		if(gradeId != ""){
			link = link + "&gradeId=" + gradeId;
		}
		if(year != ""){
			link = link + "&year=" + year;
		}
		if(month != ""){
			link = link  + "&month=" + month;
		}
        var frm = document.getElementById("frm2");
		frm.src=link;
    }

	function exportXls60(outputFormat){
		var gradeId = jQuery("#gradeId").val();
		var year = jQuery("#year").val();
        var month = jQuery("#month").val();
		if(gradeId == ""){
			alert("请选择班级！");
			return;
		}
        if(year == ""){
			alert("请选择年份！");
			return;
		}
		if(month == ""){
			alert("请选择月份！");
			return;
		}
		var link="${contextPath}/heathcare/tenantReportMain/exportXls?reportId=TenantMedicalExaminationGrade&ts=${ts}&type=${type}&outputFormat="+outputFormat;
		if(gradeId != ""){
			link = link + "&gradeId=" + gradeId;
		}
		if(year != ""){
			link = link + "&year=" + year;
		}
		if(month != ""){
			link = link  + "&month=" + month;
		}
        var frm = document.getElementById("frm2");
		frm.src=link;
    }

	function exportXls65(outputFormat){
		var year = jQuery("#year").val();
        var month = jQuery("#month").val();
        if(year == ""){
			alert("请选择年份！");
			return;
		}
		if(month == ""){
			alert("请选择月份！");
			return;
		}
		var link="${contextPath}/heathcare/tenantReportMain/exportXls?reportId=TenantMedicalExaminationGradeCount&ts=${ts}&type=${type}&outputFormat="+outputFormat;
		if(year != ""){
			link = link + "&year=" + year;
		}
		if(month != ""){
			link = link  + "&month=" + month;
		}
        var frm = document.getElementById("frm2");
		frm.src=link;
    }

	function exportXls70(outputFormat){
		var year = jQuery("#year").val();
        var month = jQuery("#month").val();
        if(year == ""){
			alert("请选择年份！");
			return;
		}
		if(month == ""){
			alert("请选择月份！");
			return;
		}
		var link="${contextPath}/heathcare/tenantReportMain/exportXls?reportId=MedicalExaminationPersonExport&ts=${ts}&type=${type}&outputFormat="+outputFormat;
		if(year != ""){
			link = link + "&year=" + year;
		}
		if(month != ""){
			link = link  + "&month=" + month;
		}
        var frm = document.getElementById("frm2");
		frm.src=link;
    }

</script>
</head>
<body>  
<input type="hidden" id="nodeId" name="nodeId" value="" > 
 <div class="easyui-layout" data-options="fit:true">  
	<div data-options="region:'north', split:false, border:true" style="height:62px" class="toolbar-backgroud"> 
	  <div style="margin-top:4px;">
		<form id="iForm" name="iForm" method="post" action="">
			<input type="hidden" id="personId" name="personId">
			<input type="hidden" id="type" name="type" value="${type}">
			<table>
			  <tr>
				<td width="40%" valign="top" align="right">
					&nbsp;&nbsp;班级&nbsp;&nbsp;
					<select id="gradeId" name="gradeId" onchange="switchXY();">
						<option value="">--请选择--</option>
						<#list gradeInfos as grade>
						 <#if grade.deleteFlag == 0>
						  <option value="${grade.id}">${grade.name}</option>
						 </#if>
						</#list> 
					</select>
					<script type="text/javascript">
						document.getElementById("gradeId").value="${gradeId}";
					</script>
				    &nbsp;&nbsp;年份&nbsp;&nbsp;
					<select id="year" name="year" onchange="switchXY();">
						<option value="">--请选择--</option>
						<option value="2015">2015</option>
						<option value="2016">2016</option>
						<option value="2017">2017</option>
						<option value="2018">2018</option>
					</select>
					<script type="text/javascript">
						document.getElementById("year").value="${year}";
					</script>
				    &nbsp;&nbsp;月份&nbsp;&nbsp;
					<select id="month" name="month" onchange="switchXY();">
						<option value="">--请选择--</option>
						<#list months as month>
						<option value="${month}">${month}</option>
						</#list> 
					</select>
					<script type="text/javascript">
						document.getElementById("month").value="${month}";
					</script>
				</td>

				<td width="60%">
				    &nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-list'" 
					   onclick="javascript:exportXls('html');">统计结果</a>&nbsp;
					<img src="${contextPath}/static/images/excel.gif" style="cursor:pointer;margin-top:3px;width:14px;height:14px;"  
					     onclick="javascript:exportXls('xls');">
					&nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-list'" 
					   onclick="javascript:exportXls55('html');">体格统计</a>&nbsp;
					<img src="${contextPath}/static/images/excel.gif" style="cursor:pointer;margin-top:3px;width:14px;height:14px;"  
					     onclick="javascript:exportXls55('xls');">
					&nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-list'" 
					   onclick="javascript:exportXls60('html');">班级</a>&nbsp;
					<img src="${contextPath}/static/images/excel.gif" style="cursor:pointer;margin-top:3px;width:14px;height:14px;"  
					     onclick="javascript:exportXls60('xls');">
					&nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-list'" 
					   onclick="javascript:exportXls65('html');">全园</a>&nbsp;
					<img src="${contextPath}/static/images/excel.gif" style="cursor:pointer;margin-top:3px;width:14px;height:14px;"  
					     onclick="javascript:exportXls65('xls');">
					&nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-list'" 
					   onclick="javascript:exportXls70('html');">超重与肥胖</a>&nbsp;
					<img src="${contextPath}/static/images/excel.gif" style="cursor:pointer;margin-top:3px;width:14px;height:14px;"  
					     onclick="javascript:exportXls70('xls');">
				    &nbsp;
				</td>
			 </tr>
			</table>
			</form>
			</div>
		   </div> 
		   <div data-options="region:'center', border:true" data-options="fit:true">
			  <iframe id="frm2" name="frm2" scrolling="yes" frameborder="0"  src="" style="width:100%;height:680px;"></iframe>
		   </div>  
      </div>  
    </div>  
</body>  
</html>