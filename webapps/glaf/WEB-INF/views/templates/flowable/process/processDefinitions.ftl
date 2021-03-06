<html>
<head>
<title>流程定义列表</title>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/plugins/yui/build/datatable/assets/skins/sam/datatable.css"> 
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/plugins/yui/build/paginator/assets/skins/sam/paginator.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/plugins/yui/build/button/assets/skins/sam/button.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/plugins/yui/build/menu/assets/skins/sam/menu.css" />
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/css/core.css"/>
<script type="text/javascript"	src="${request.contextPath}/static/scripts/jquery.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/plugins/yui/yui-2.8.1-min.js"></script>
</head>
<body  class="yui-skin-sam" leftmargin="0" topmargin="0" marginheight="0" marginwidth="0" style="padding-top:10px;padding-left:20px;"> 
 
<div class="x_content_title">
<img
	src="${request.contextPath}/static/images/window.png"
	alt="流程定义列表">&nbsp;流程定义列表
</div>
<br>

  <table id="screen" width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td>
			<table  border="0" cellspacing="0" cellpadding="0" >
				<tr> 
				 <td width="95%" height="25" align="left" > 
					<div id="paginationContainerTop"  width="100%"></div>
					<div id="menu_line" class="menu_line2" width="100%"></div>
					<div id="DataTableYUI"  width="100%">
					
					</div>
					<div id="paginationContainerBottom"  width="100%"></div>
				 </td>
			  </tr>
			  <tr> 
				 <td width="95%" height="25" align="left" > 
				 <div>
				  <!-- <span id="create-button-2"> </span> -->
  				 </div>
				</td>
			  </tr>
			</table>			    
		</td>
  </tr>
</table>

 <script type="text/javascript"> 

  YAHOO.example.DynamicData = ( function() {
    
    var x_app = "&x_complex_query=" 

    var s_link = "${request.contextPath}/flowable/process/processDefinitionJson?q=1"+x_app;
	 
    function onViewButtonClick(e) {
    	var selectRows = myDataTable.getSelectedRows();
    	 if(selectRows.length!=null && selectRows.length==1){
          oRecord = myDataTable.getRecordSet().getRecord(selectRows[0]);
          var processDefinitionId = YAHOO.lang.dump(oRecord.getData("processDefinitionId"));
          window.open("${request.contextPath}/flowable/process/definition?processDefinitionId="+processDefinitionId);
        } else{
          alert('请选择其中一条记录');
    	}
    }
    
	 function onMyClickRow(oArgs) {
	    myDataTable.onEventSelectRow(oArgs);
		var elTarget = oArgs.target;
        var elTargetRow = myDataTable.getTrEl(elTarget);
        if(elTargetRow) {
              var oRecord = myDataTable.getRecord(elTargetRow);
              var status = YAHOO.lang.dump(oRecord.getData("status"));			 
              if(status <= 10 ){                  
				     editbutton.removeAttribute("disabled");
					 editbutton.removeStateCSSClasses("disabled");
               } else {
                     editbutton.setAttribute("disabled", "disabled");
					 editbutton.addStateCSSClasses("disabled");
			   }
	    }
	}

	function onMyDblClickRow(oArgs) {
	    myDataTable.onEventSelectRow(oArgs);
		var elTarget = oArgs.target;
        var elTargetRow = myDataTable.getTrEl(elTarget);
        if(elTargetRow) {
              var oRecord = myDataTable.getRecord(elTargetRow);
              var processDefinitionId = YAHOO.lang.dump(oRecord.getData("processDefinitionId"));
			  //window.open("${request.contextPath}/flowable/process/definition?processDefinitionId="+proces//sDefinitionId+x_app);
	    }
	}

	 var formatFunKey = function(elCell, oRecord, oColumn, oData) {
		var processDefinitionId = YAHOO.lang.dump(oRecord.getData("processDefinitionId"));
        var link ='${request.contextPath}/flowable/process/processDefinition?processDefinitionId='+processDefinitionId;
		var link2 ='${request.contextPath}/flowable/image/viewImage?processDefinitionId='+processDefinitionId;
		var link3 ='${request.contextPath}/flowable/process/processInstances?processDefinitionId='+processDefinitionId;
		var link4 ='${request.contextPath}/flowable/history/historyProcessInstances?processDefinitionId='+processDefinitionId;
		//var msg = '<a href=\" '+link+'  " target=_blank>查看</a>';
		var msg = '&nbsp;&nbsp;<a href=\" '+link2+'  " target=_blank><img src="${request.contextPath}/static/images/process.gif"/>&nbsp;流程图</a>';
		msg += '&nbsp;&nbsp;<a href=\" '+link3+'  "><img src="${request.contextPath}/static/images/lightbulb.png"/>&nbsp;流程实例</a>';
		msg += '&nbsp;&nbsp;<a href=\" '+link4+'  "><img src="${request.contextPath}/static/images/lightbulb_off.png"/>&nbsp;历史实例</a>';
        elCell.innerHTML = msg ;
      }

	  var formatRow = function(elCell, oRecord, oColumn, oData) {
        var processDefinitionId = YAHOO.lang.dump(oRecord.getData("processDefinitionId"));
        //var link = "${request.contextPath}/flowable/process/view?processDefinitionId="+processDefinitionId+x_app;
        //elCell.innerHTML = "<a href=\""+link+"\" target='_blank'>"+oData+"</a>";
		elCell.innerHTML = oData;
      }

	  var formatStatus = function(elCell, oRecord, oColumn, oData) {
        var status = YAHOO.lang.dump(oRecord.getData("isSuspended"));
    	var msg = "";
        if (status == true) {
            msg = "<span style='color: red; font-weight: bold; text-align: center;'> 已停用 </span>";
    	}  else {
            msg = "<span style='color: green; font-weight: bold; text-align: center;'> 使用中  </span>";
    	}
        elCell.innerHTML = msg;
      }


 	 var imgFormatter = function(elCell, oRecord, oColumn, oData) {
           var imginfo = oData; 
           elCell.innerHTML = "<img width=12 height=12 src=" + imginfo + "></img>";
        };

 	var myRowFormatter = function(elTr, oRecord) {
		if (oRecord.getData('status') == -1) {
			Dom.addClass(elTr, 'mark');
		} else if (oRecord.getData('status') == 50) {
			Dom.addClass(elTr, 'greenmark');
		}
		return true;
	}; 

      YAHOO.widget.DataTable.Formatter.imginfoFormatter = imgFormatter;
	  YAHOO.widget.DataTable.Formatter.formatFunKey = formatFunKey;
	  YAHOO.widget.DataTable.Formatter.formatStatus = formatStatus;
	  YAHOO.widget.DataTable.Formatter.formatRow = formatRow;

	   myDataSource = new YAHOO.util.DataSource(s_link);
       myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
       myDataSource.connXhrMode = "queueRequests";
       myDataSource.responseSchema = { 
	       resultsList : "records",  
	       fields : [ 
						{key: 'id'},
						{key: 'processDefinitionId'},
						{key: 'key'},
						{key: 'name'},
						{key: 'category'},
						{key: 'version'},
						{key: 'diagramResourceName'},
						{key: 'hasStartFormKey'} 
                     ],
           metaFields: {
                totalRecords: "totalRecords"
          }
	   }; 

	  var myColumnDefs = [ 
	    {label: '流程定义编号', key: 'id', width: 150, resizeable: true, formatter: formatRow},
        {label: 'Key', key: 'key',resizeable: true, width: 150},
	    {label: '流程名称', key: 'name', resizeable: true, width: 150},
		{label: '版本', key: 'version', resizeable: true, width: 60, align: 'center' },
		{label: '流程图', key: 'diagramResourceName', width: 180, resizeable: true, align: 'center'},
        {label: '功能键', key: 'functionKey', resizeable: true, width: 240, align: 'center', formatter: formatFunKey}
       ];

    var t0="{FirstPageLink}&nbsp;&nbsp;{PreviousPageLink}&nbsp;&nbsp;{NextPageLink}&nbsp;&nbsp;{LastPageLink}&nbsp;&nbsp; {RowsPerPageDropdown}&nbsp;&nbsp;{CurrentPageReport}";

    var t2 = '&nbsp;&nbsp;第&nbsp;{currentPage}&nbsp;页&nbsp;&nbsp;总页数:&nbsp;{totalPages}&nbsp;&nbsp;总记录数:&nbsp;{totalRecords}&nbsp;&nbsp;';

	var myConfigs = { 
     initialRequest:"sort=id&dir=asc&startIndex=0&results=10",
     dynamicData: true, 
     MSG_LOADING: '<center>数据加载中......</center>',
     MSG_ERROR: '数据加载错误。',
     MSG_EMPTY: '数据为空。',
     MSG_SORTASC: '升序',
     MSG_SORTDESC: '降序',
     paginator: new YAHOO.widget.Paginator({
            rowsPerPage: 10,
			rowsPerPageOptions: [10,15,20,25,50,100,200,500],
        	containers : 'paginationContainerBottom',
        	PageLinks : YAHOO.widget.Paginator.VALUE_UNLIMITED,
        	firstPageLinkLabel : '第一页', 
            lastPageLinkLabel : '最后一页', 
            previousPageLinkLabel:'前一页',
            nextPageLinkLabel:'后一页',
        	template: t0,
         	pageReportTemplate: t2
        })
	}; 

      
    this.myDataTable = new YAHOO.widget.DataTable("DataTableYUI", myColumnDefs, myDataSource, myConfigs);
	this.myDataTable.subscribe("rowMouseoverEvent", this.myDataTable.onEventHighlightRow);
    this.myDataTable.subscribe("rowMouseoutEvent", this.myDataTable.onEventUnhighlightRow);
    this.myDataTable.subscribe("rowClickEvent", onMyClickRow);
	this.myDataTable.subscribe("rowDblclickEvent", onMyDblClickRow);

 	myDataTable.selectRow(myDataTable.getTrEl(0));
 	myDataTable.focus();

    this.myDataTable.handleDataReturnPayload = function(oRequest, oResponse, oPayload) {
        oPayload.totalRecords = oResponse.meta.totalRecords;
        return oPayload;
    }    

	return {
    	ds: myDataSource,
    	dt: myDataTable
    };

   })();
 </script> 
 <br><br>
</body>
</html>