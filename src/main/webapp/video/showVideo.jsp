<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<script>
    //延迟加载
    $(function(){
        pageInit();
    });

    //创建表格
    function pageInit(){
        $("#videoTable").jqGrid({
            url : "${path}/video/queryVideoPage",  //接收  page=当前页   rows=每页展示条数   返回  page=当前页   rows=[User,User]数据    tolal=总页数   records=总条数
            editurl:"${path}/video/edit",  //增删改走的路径  oper:add ,edit,del
            datatype : "json", //数据格式
            rowNum : 4,  //每页展示条数
            rowList : [ 4,10, 20, 30 ],  //可选没夜战是条数
            pager : '#videoPage',  //分页工具栏
            sortname : 'id', //排序
            type : "post",  //请求方式
            styleUI:"Bootstrap", //使用Bootstrap
            autowidth:true,  //宽度自动
            height:"auto",   //高度自动
            viewrecords : true, //是否展示总条数
            colNames : [ 'Id', '标题', '视频', '上传时间', '描述', '类别ID',"状态","用户ID" ],
            colModel : [
                {name : 'id',width : 55},
                {name : 'title',editable:true,width : 90},
                {name : 'videoPath',editable:true,width : 150,align : "center",edittype:"file",
                    //cellvalue:值,options:设置的参数，rowObject:行数据
                    formatter:function(cellvalue){
                        return "<video src='"+cellvalue+"' width='200px' height='100px'  controls='controls' >"
                    }
                },
                {name : 'uploadTime',width : 80,align : "center"},
                {name : 'description',editable:true,width : 80,align : "center"},
                {name : 'categoryId',editable:true,width : 150},
                {name : 'status',width : 50,align : "center",
                    formatter:function(cellvalue, options, rowObject){
                        if(cellvalue==1){
                            //正常  展示冻结（绿色）
                            return "<button class='btn btn-success' onclick='update(\""+rowObject.id+"\",\""+cellvalue+"\")'>冻结</button>";
                        }else{
                            //冻结  展示解除冻结（红色）
                            return "<button class='btn btn-danger' onclick='update(\""+rowObject.id+"\",\""+cellvalue+"\")' >解除冻结</button>";
                        }
                    }
                },
                {name : 'userId',editable:true,width : 150}
            ]
        });

        //分页工具栏
        $("#videoTable").jqGrid('navGrid', '#videoPage',
            {edit : true,add : true,del : true,addtext:"添加",edittext:"编辑",deltext:"删除"},
            {
                closeAfterEdit:true,  //关闭对话框
                afterSubmit:function(data){  //提交之后执行
                    //异步文件上传
                    $.ajaxFileUpload({
                        url: "${path}/video/uploadVideoCover", //后台文件上传方法的路径
                        type: 'post',   //当要提交自定义参数时，这个参数要设置成post
                        //dataType: 'json',   //服务器返回的数据类型。可以为xml,script,json,html。如果不填写，jQuery会自动判断。
                        fileElementId: "videoPath",    //需要上传的文件域的ID，即<input type="file" name="headImg" id="headImg" >的ID。
                        data:{"id":data.responseText},
                        success:function(data) {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
                            //刷新表单
                            $("#videoTable").trigger("reloadGrid");
                        }
                    });
                    return "hello";
                }
            },  //修改之后的额外操作
            {
                closeAfterAdd:true,  //关闭对话框
                afterSubmit:function(data){  //提交之后执行
                    //异步文件上传
                    $.ajaxFileUpload({
                        url: "${path}/video/uploadVideoCover", //后台文件上传方法的路径
                        type: 'post',   //当要提交自定义参数时，这个参数要设置成post
                        //dataType: 'json',   //服务器返回的数据类型。可以为xml,script,json,html。如果不填写，jQuery会自动判断。
                        fileElementId: "videoPath",    //需要上传的文件域的ID，即<input type="file" name="headImg" id="headImg" >的ID。
                        data:{"id":data.responseText},
                        success:function(data) {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
                            //刷新表单
                            $("#videoTable").trigger("reloadGrid");
                        }
                    });
                    return "hello";
                }
            }, //添加之后的额外操作
            {}  //删除之后的额外操作
        );
    }
    function update(id,status){
        $.ajax({
            url: "${path}/video/status", //后台文件上传方法的路径
            type: 'post',   //当要提交自定义参数时，这个参数要设置成post
            data:{"id":id,"status":status},
            dataType: "json",
            success:function(data) {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
                //刷新表单
                // $("#userTable").jqGrid("clearGridData");
                $("#videoTable").trigger("reloadGrid");
            }
        });
    }


</script>

<%--创建一个面板--%>
<div class="panel panel-info">

    <%--面板头--%>
    <div class="panel panel-heading">
        <span>视频信息</span>
    </div>

        <!-- Nav tabs -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">视频信息</a></li>
    </ul><br>
        <div class="row">
            <div class="col-lg-6" style="width: 550px;">
                <button class="btn btn-info">导出视频信息</button>
                <button class="btn btn-info">测试</button>
            </div><!-- /.col-lg-6 -->
        </div><br>
    <%--创建表格--%>
    <table id="videoTable" />

    <%--分页工具栏--%>
    <div id="videoPage"/>

</div>