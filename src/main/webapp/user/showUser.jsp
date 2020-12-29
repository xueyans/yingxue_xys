<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<script>
    //延迟加载
    $(function(){
        pageInit();
        //删除用户
        $("#deleteUser").click(function(){

            //getGridParam:返回请求的参数信息  id
            //selrow:只读属性，最后选择行的id
            var rowId= $("#userTable").jqGrid("getGridParam","selrow");

            //选择一行删除
            if(rowId!=null){
                $.post("${path}/user/edit",{"id":rowId,"oper":"del"},function(data){
                    //刷新表单
                    $("#userTable").trigger("reloadGrid");
                })


            }else{
                //向提示框设置内容
                $("#showMsgs").html("<span>请选择一行</span>");
                //展示提示框
                $("#showMessages").show();
                //设置倒计时关闭
                setTimeout(function () {
                    //关闭提示框
                    $("#showMessages").hide();
                },2000);
            }
        });

        //展示用户详情
        $("#showUserDetail").click(function(){

            //getGridParam:返回请求的参数信息  id
            //selrow:只读属性，最后选择行的id
            var rowId= $("#userTable").jqGrid("getGridParam","selrow");

            //选择一行删除
            if(rowId!=null){

                //根据行id返回行数据
                var row=$("#userTable").jqGrid("getRowData",rowId);

                //清空模态框
                //$("#showModal").modal().empty();

                //给表单设置内容
                $("#userId").val(row.id);
                $("#userPhone").val(row.phone);
                $("#userUserName").val(row.username);
                $("#showUserCover").html("<label for='headImg' class='control-label'>头像:</label>\n" +
                    "<input type='file' class='form-control' id='headImg' name='headImg' >"+row.headImg);

                $("#userBrief").val(row.brief);

                var statusVal= $("#"+row.phone).val();
                $("#userStatus").val(statusVal);

                $("#userCreateDate").val(row.createDate);
                $("#userScore").val(row.score);

                //模态框
                $("#showModal").modal("show");

            }else{
                //向提示框设置内容
                $("#showMsgs").html("<span>请选择一行</span>");
                //展示提示框
                $("#showMessages").show();
                //设置倒计时关闭
                setTimeout(function () {
                    //关闭提示框
                    $("#showMessages").hide();
                },2000);
            }
        });

        //提交表单
        $("#submitUserFrom").click(function(){
            alert();

        });
    });


    //创建表格
    function pageInit(){
        $("#userTable").jqGrid({
            url : "${path}/user/queryUserPage",  //接收  page=当前页   rows=每页展示条数   返回  page=当前页   rows=[User,User]数据    tolal=总页数   records=总条数
            editurl:"${path}/user/edit",  //增删改走的路径  oper:add ,edit,del
            datatype : "json", //数据格式
            rowNum : 2,  //每页展示条数
            rowList : [ 2,10, 20, 30 ],  //可选没夜战是条数
            pager : '#userPage',  //分页工具栏
            sortname : 'id', //排序
            type : "post",  //请求方式
            styleUI:"Bootstrap", //使用Bootstrap
            autowidth:true,  //宽度自动
            height:"auto",   //高度自动
            viewrecords : true, //是否展示总条数
            colNames : [ 'Id', '手机', '用户名', '头像', '简介','状态', '创建时间',"学分" ],
            colModel : [
                {name : 'id',width : 55},
                {name : 'phone',editable:true,width : 90},
                {name : 'username',editable:true,width : 100},
                {name : 'headImg',editable:true,width : 150,align : "center",edittype:"file",
                    //cellvalue:值,options:设置的参数，rowObject:行数据
                    formatter:function(cellvalue){
                        return "<img src='${path}/upload/cover/"+cellvalue+"' width='200px' height='100px' >";
                    }
                },
                {name : 'brief',editable:true,width : 80,align : "center"},
                {name : 'status',width : 80,align : "center",
                    formatter:function(cellvalue, options, rowObject){
                        if(cellvalue==1){
                          //正常  展示冻结（绿色）
                            return "<button class='btn btn-success' onclick='ss(\""+rowObject.id+"\",\""+cellvalue+"\")'>冻结</button>";
                        }else{
                            //冻结  展示解除冻结（红色）
                            return "<button class='btn btn-danger' onclick='ss(\""+rowObject.id+"\",\""+cellvalue+"\")' >解除冻结</button>";
                        }
                    }
                },
                {name : 'createDate',width : 150},
                {name : 'score',hidden:true,width : 150}
            ]
        });

        //分页工具栏
        $("#userTable").jqGrid('navGrid', '#userPage',
            {edit : true,add : true,del : true,addtext:"添加",edittext:"编辑",deltext:"删除"},
            {
                closeAfterEdit:true,  //关闭对话框
                afterSubmit:function(data){  //提交之后执行
                    //异步文件上传
                    $.ajaxFileUpload({
                        url: "${path}/user/uploadUserCover", //后台文件上传方法的路径
                        type: 'post',   //当要提交自定义参数时，这个参数要设置成post
                        //dataType: 'json',   //服务器返回的数据类型。可以为xml,script,json,html。如果不填写，jQuery会自动判断。
                        fileElementId: "headImg",    //需要上传的文件域的ID，即<input type="file" name="headImg" id="headImg" >的ID。
                        data:{"id":data.responseText},
                        success:function(data) {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
                            //刷新表单
                            $("#userTable").trigger("reloadGrid");
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
                        url: "${path}/user/uploadUserCover", //后台文件上传方法的路径
                        type: 'post',   //当要提交自定义参数时，这个参数要设置成post
                        //dataType: 'json',   //服务器返回的数据类型。可以为xml,script,json,html。如果不填写，jQuery会自动判断。
                        fileElementId: "headImg",    //需要上传的文件域的ID，即<input type="file" name="headImg" id="headImg" >的ID。
                        data:{"id":data.responseText},
                        success:function(data) {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
                            //刷新表单
                            $("#userTable").trigger("reloadGrid");
                        }
                    });
                    return "hello";
                }
            }, //添加之后的额外操作
            {}  //删除之后的额外操作
        );
    }
    function ss(id,status){
        $.ajax({
            url: "${path}/user/status", //后台文件上传方法的路径
            type: 'post',   //当要提交自定义参数时，这个参数要设置成post
            data:{"id":id,"status":status},
            dataType: "json",
            success:function(data) {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
                //刷新表单
                // $("#userTable").jqGrid("clearGridData");
                $("#userTable").trigger("reloadGrid");
            }
        });
    }
    function msg(){
        var phone=document.getElementById("nn").value;
        $.ajax({
            url: "${path}/admin/sendMsg", //后台文件上传方法的路径
            type: 'post',   //当要提交自定义参数时，这个参数要设置成post
            data:{"phone":phone},
            dataType: "json",
            success:function(data) {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
                alert(data);
            }
        });
    }


</script>

<%--创建一个面板--%>
<div class="panel panel-info">

    <%--面板头--%>
    <div class="panel panel-heading">
        <span>用户信息</span>
    </div>
        <!-- Nav tabs -->
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">用户信息</a></li>
        </ul><br>

        <%--警告框--%>
        <div id="showMessages" style="width:300px;display: none " class="alert alert-warning alert-dismissible" role="alert">
            <strong id="showMsgs" />
        </div>

        <div>
            <button class="btn btn-info">导出用户信息</button>&emsp;&emsp;
            <button class="btn btn-info" id="deleteUser">删除用户</button>&emsp;&emsp;
            <button class="btn btn-info" id="showUserDetail">展示用户详情</button>
            <div class="input-group">
                <input type="text" id="nn" class="form-control" placeholder="请输入手机号">
                <span class="input-group-btn">
                    <button class="btn btn-danger" type="button" onclick="msg()">发送验证码</button>
                </span>
            </div><!-- /input-group -->
        </div><br>

    <%--创建表格--%>
    <table id="userTable" />

    <%--分页工具栏--%>
    <div id="userPage"/>

     <%--模态框--%>
    <div id="showModal" class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <%--模态框标题--%>
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">展示用户信息</h4>
                    </div>
                    <%--模态框内容--%>
                    <div class="modal-body">
                        <form method="post" action="">
                            <div class="form-group">
                                <label for="userId" class="control-label">Id:</label>
                                <input type="text" class="form-control" id="userId" name="id">
                            </div>
                            <div class="form-group">
                                <label for="userPhone" class="control-label">手机:</label>
                                <input type="text" class="form-control" id="userPhone">
                            </div>
                            <div class="form-group">
                                <label for="userUserName" class="control-label">用户名:</label>
                                <input type="text" class="form-control" id="userUserName">
                            </div>
                            <div class="form-group" id="showUserCover">
                            </div>
                            <div class="form-group">
                                <label for="userBrief" class="control-label">简介:</label>
                                <input type="text" class="form-control" id="userBrief">
                            </div>
                            <div class="form-group">
                                <label for="userStatus" class="control-label">状态:</label>
                                <input type="text" class="form-control" id="userStatus">
                            </div>
                            <div class="form-group">
                                <label for="userCreateDate" class="control-label">创建时间:</label>
                                <input type="text" class="form-control" id="userCreateDate">
                            </div>
                            <div class="form-group">
                                <label for="userScore" class="control-label">学分:</label>
                                <input type="text" class="form-control" id="userScore">
                            </div>
                        </form>
                    </div>
                    <%--模态框底部--%>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn btn-primary" id="submitUserFrom" >提交</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
</div>