<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>process</title>
<style>
.tip-top {
  color: #000;
  width: 130px;
  z-index: 13000;
}
.tip{
}
.tip .tip-title {
  font-weight: bold;
  font-size: 14px;
  margin: 0;
  color: #3E4F14;
  padding: 8px 8px 4px;
  background: #C3DF7D;
  border-bottom: 1px solid #B5CF74;
}

.tip .tip-text {
  font-size: 12px;
  padding: 4px 8px 8px;
  background: #CFDFA7;
}
.tip-bottom{
}
.processed{
  position:absolute;
  border:3px yellow solid;
}

.processed:hover{
  border-color:blue;
}

.active{
  position:absolute;
  border:3px red solid;
}
.active:hover{
  border-color: green;
}
 
.processed,.active{
  background-image:url(${contextPath}/images/s.gif);
}
</style>
 <script type="text/javascript"
      src="${contextPath}/scripts/mootools/mootools-core.js"></script>
 <script type="text/javascript"
      src="${contextPath}/scripts/mootools/mootools-more.js"></script>
</head>
<body>
  <div class="processContainer" align="center">
    <img style="position: absolute; left: 0px; top: 0px;"
        src="${contextPath}/rs/flowable/process/image/${processInstanceId}"/>
          ${x_script}
  </div>
  <script>
	try{
	    new Tips('.tip');
	 }catch(exe){}
 </script>
</body>
</html>