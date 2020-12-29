<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<script>
    //延迟加载
    $(function(){
        pageInit();
    });
    function update(id,status){
        $.ajax({
            url: "${path}/admin/status", //后台文件上传方法的路径
            type: 'post',   //当要提交自定义参数时，这个参数要设置成post
            data:{"id":id,"status":status},
            dataType: "json",
            success:function(data) {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
                //刷新表单
                // $("#userTable").jqGrid("clearGridData");
                $("#adminTable").trigger("reloadGrid");
            }
        });
    }
    //创建表格
    function pageInit(){
        $("#adminTable").jqGrid({
            url : "${path}/admin/queryAdminPage",  //接收  page=当前页   rows=每页展示条数   返回  page=当前页   rows=[User,User]数据    tolal=总页数   records=总条数
            editurl:"${path}/admin/edit",  //增删改走的路径  oper:add ,edit,del
            datatype : "json", //数据格式
            rowNum : 4,  //每页展示条数
            rowList : [ 4,10, 20, 30 ],  //可选没夜战是条数
            pager : '#adminPage',  //分页工具栏
            sortname : 'id', //排序
            type : "post",  //请求方式
            styleUI:"Bootstrap", //使用Bootstrap
            autowidth:true,  //宽度自动
            height:"auto",   //高度自动
            viewrecords : true, //是否展示总条数
            colNames : [ 'Id', '用户名','密码', '状态', '盐'],
            colModel : [
                {name : 'id',width : 55,align: "center"},
                {name : 'username',editable:true,width : 50,align: "center"},
                {name : 'password',editable:true,width : 50,align: "center"},
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
                {name: 'salt',width:50,align: "center"},
            ]
        });

        //分页工具栏
        $("#adminTable").jqGrid('navGrid', '#adminPage',
            {edit : true,add : true,del : true,addtext:"添加",edittext:"编辑",deltext:"删除"},
            {
                closeAfterEdit:true,  //关闭对话框
            },  //修改之后的额外操作
            {
                closeAfterAdd:true,  //关闭对话框
            }, //添加之后的额外操作
            {
                closeAfterAdd:true,  //关闭对话框
            }  //删除之后的额外操作
        );
    }



</script>

<%--创建一个面板--%>
<div class="panel panel-danger">

    <%--面板头--%>
    <div class="panel panel-heading">
        <span>管理员信息</span>
    </div>

    <!-- Nav tabs -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">管理员信息</a></li>
    </ul><br>

    <div>
        <button class="btn btn-danger">导出管理员信息</button>
        <button class="btn btn-danger">测试</button>
    </div><br>

    <%--创建表格--%>
    <table id="adminTable" />

    <%--分页工具栏--%>
    <div id="adminPage"/>

</div>