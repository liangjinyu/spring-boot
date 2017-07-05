
function addPersonRequest(personName,personAge) {
	var person = new Object();
	person.name = personName;
	person.age = personAge;
	$.ajax({
		url : "http://localhost:8080/person/add",
		async : true, // 请求是否异步，默认为异步，这也是ajax重要特性
		dataType : 'json',
		contentType : 'application/json',
		type : 'POST',
		data : JSON.stringify(person),
		success : function(data) {
			if ("0" == data.code) {
				alert("success")
			} else {
				alert("error");
			}
		},
		error : function() {
			alert("error");
		}
	});
}
function addPerson() {

	var personName = document.getElementById("personName");
	var personAge = document.getElementById("personAge");
	if (personName.value == "") {
		alert("请输入姓名");
	}else if (personAge.value == "") {
		alert("请输入年龄");
	} else {
		addPersonRequest(personName.value,personAge.value);
	}
}

function selectPerson() {
	
	var person = new Object();
	person.name = personName.value;
	person.age = personAge.value;
	$.ajax({
		url : "http://localhost:8080/person/select",
		async : true, // 请求是否异步，默认为异步，这也是ajax重要特性
		dataType : 'json',
		contentType : 'application/json',
		type : 'POST',
		data : JSON.stringify(person),
		success : function(data) {
			if ("0" == data.code) {
				alert(data.content)
				
			} else {
				alert("error");
			}
		},
		error : function() {
			alert("error");
		}
	});
	
}








var reqObj = {};
reqObj.pageNum = 10;
reqObj.currentPage = 1;

var datatable =null;

function _initElemts(){
	datatable = $("#userTable").dataTable({
	    bServerSide: true,
	    bLengthChange: true,
	    bPaginate: true,
	    bProcessing: true,
	    sPaginationType: "full_numbers",
	    bFilter: false,
	    bDestroy: true,
	    bAutoWidth: false,
	    aoColumns: [
		               {"bSortable": false, "mData": "id"},//序号
		               {"bSortable": false, "mData": "name"},
		               {"bSortable": false, "mData": "age"}
		           ],
	    bSort : false, //是否启动各个字段的排序功能  
	    sAjaxSource: "http://localhost:8080/person/select",
	    sAjaxDataProp: "content",
	    fnServerData: function (sSource, aoData, fnCallback) {
	    	var page = {};
	    	for(var i=0;i<aoData.length;i++){
	    		if(aoData[i].name=='iDisplayLength'){
	    			page.pageNum = aoData[i].value; // iDisplayLength
	    		}
	    		if(aoData[i].name=='iDisplayStart'){
	    			page.currentPage = aoData[i].value;
	    		}
	    	}
	    	reqObj.pageNum = page.pageNum;
	        reqObj.currentPage = page.currentPage/page.pageNum + 1;
	        $.ajax({
	            dataType: 'json',
	            contentType: 'application/json',
	            type: 'POST',
	            async: false,
	            url: sSource,
	            data: JSON.stringify(reqObj),
	            success: function (data) {
	                if (!data.content) {
	                    data.content = [];
	                }
	                data.iTotalRecords = data.totalNum;
	                data.iTotalDisplayRecords = data.totalNum;
	                fnCallback(data);
	            }
	        });
	    },
	    language: {
	        sProcessing: "处理中, 请稍候...",
	        sLoadingRecords: "数据加载中...",
	        lengthMenu: "每页 _MENU_ 条记录",
	        zeroRecords: "无数据",
	        info: "第 _PAGE_ 页 ( 总共 _PAGES_ 页，共 _TOTAL_ 条记录 )",
	        infoEmpty: "无记录",
	        infoFiltered: "(从 _MAX_ 条记录过滤)",
	        oPaginate: {
	            sFirst: "首页",
	            sPrevious: "上一页",
	            sNext: "下一页",
	            sLast: "末页"
	        }
	    },
	    fnRowCallback: function(nRow, aData, iDisplayIndex) {	
//	  	  $("td:eq(0)", nRow).text(iDisplayIndex+1);
//	    	$("td:eq(0)", nRow).text(aData.id);
/*	    	  $("td:eq(0)", nRow).text(iDisplayIndex+1);
	    	  $("td:eq(3)", nRow).text(new Date(aData.updateTime).pattern("yyyy-MM-dd hh:mm:ss"));
	    	  $("td:eq(4)", nRow).text(new Date(aData.createTime).pattern("yyyy-MM-dd hh:mm:ss"));
	    	  $("td:eq(5)", nRow).text(aData.status == 1?"已上线":"未上线");
	    	  var operation = "<div>";
	    	  if(aData.status == 1){
	    		  operation += "<a class='operation offline'>下线</a>";
	    	  } else{
	    		  operation += "<a class='operation online'>上线</a>";
	    	  }
	    	  operation += "<a class='operation detail'>详情</a>";
	    	  operation += "<a class='operation edit'>编辑</a>";
	    //	  operation += "<a class='operation preview'>预览</a>";
	    	  operation += "</div>";
	    	  var $operation = $(operation);
	    	  $("td:eq(6)", nRow).empty().append($operation);
	    	  $operation.find(".offline").click(function(){
	    		  offline(aData);
	    	  })
	    	   $operation.find(".online").click(function(){
	    		  online(aData);
	    	  })
	    	   $operation.find(".detail").click(function(){
	    		  detail(aData);
	    	  })
	    	   $operation.find(".edit").click(function(){
	    		  edit(aData);
	    	  })
	    	   $operation.find(".preview").click(function(){
	    		   merchantPreview(aData);
	    	  })*/
			return nRow;
	    }
	});

}

function _initEvent(){
	$("#queryButton").click(function() {
		
		//selectPerson
//		var $personName = $("#personName");
//		var $personAge = $("#personAge");
		
		reqObj = {};
		reqObj.pageNum = 10;
		reqObj.currentPage = 1;
//		alert($personName.val());
//		if($personName.val() != ''){
//			reqObj.name = $personName.val().trim();
//		}
//		if($personAge.val() != ''){
//			reqObj.age= $personAge.val().trim();    
//		}
		
		
		var personName = document.getElementById("personName");
		var personAge = document.getElementById("personAge");
		if (personName.value != "") {
			reqObj.name =personName.value;
		}
		if (personAge.value != "") {
			reqObj.age =personAge.value;
		} 
		
		
	    datatable.fnDraw();
	});	
	$("#addButton").click(function(){
		addPerson();
	})
//	$(document).keypress(function(e) {
//		// 回车键事件
//		if (e.which == 13) {
//			$("#searchBtn").click();
//		}
//	}); 	
}



$(document).ready(function() {
	_initElemts();
	_initEvent();
})





























