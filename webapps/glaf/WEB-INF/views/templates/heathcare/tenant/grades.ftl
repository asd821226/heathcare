<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>班级信息列表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
</head>
<body style="margin:0px;">
<div class="easyui-tabs" data-options="tools:'#tab-tools'" style="overflow:hidden; width:100%; height:auto">
  <div title="入园体检" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/medicalExamination/reviewlist?type=1&tenantId=${tenantId}" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="定期体检" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/medicalExamination/reviewlist?type=5&tenantId=${tenantId}" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="专项体检" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/medicalExamination/reviewlist?type=7&tenantId=${tenantId}" 
	        style="width:100%;height:680px;"></iframe>
  </div>
</div>
</body>
</html>