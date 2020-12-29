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
        $("#logTable").jqGrid({
            url : "${path}/log/queryLogPage",  //接收  page=当前页   rows=每页展示条数   返回  page=当前页   rows=[User,User]数据    tolal=总页数   records=总条数
            editurl:"${path}/log/edit",  //增删改走的路径  oper:add ,edit,del
            datatype : "json", //数据格式
            rowNum : 4,  //每页展示条数
            rowList : [ 4,10, 20, 30 ],  //可选没夜战是条数
            pager : '#logPage',  //分页工具栏
            sortname : 'id', //排序
            type : "post",  //请求方式
            styleUI:"Bootstrap", //使用Bootstrap
            autowidth:true,  //宽度自动
            height:"auto",   //高度自动
            viewrecords : true, //是否展示总条数
            colNames : [ 'Id', '管理员','操作时间', '操作内容', '是否成功'],
            colModel : [
                {name : 'id',width : 55,align: "center"},
                {name : 'daminname',width : 50,align: "center"},
                {name : 'optiontime',width : 50,align: "center"},
                {name : 'options',width : 50,align : "center"},
                {name: 'issuccess',width:50,align: "center"},
            ]
        });

        //分页工具栏
        $("#logTable").jqGrid('navGrid', '#logPage',
            {edit : true,add : false,del : true,addtext:"添加",edittext:"编辑",deltext:"删除"},
            {
                closeAfterEdit:true,  //关闭对话框
            },  //修改之后的额外操作
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
        <span>日志信息</span>
    </div>

    <!-- Nav tabs -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">日志信息</a></li>
    </ul><br>

    <div>
        <button class="btn btn-danger">导出日志信息</button>
        <button class="btn btn-danger">测试</button>
    </div><br>

    <%--创建表格--%>
    <table id="logTable" />

    <%--分页工具栏--%>
    <div id="logPage"/>

</div>