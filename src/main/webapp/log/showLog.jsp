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
            datatype : "json",//数据格式
            rowNum : 5,//默认展示条数
            rowList : [2,3, 5,8,10, 16,20,24, 30 ],
            pager : '#logPage',//分页工具栏
            sortname : 'id',//排序
            type : "post",//请求方式
            styleUI:"Bootstrap",//选用boot样式
            autowidth:true,//开启宽度自动
            height:"auto",//高度自动
            viewrecords : true, //是否展示总条数
            colNames : [ 'Id', '操作者', '操作时间', '操作信息', '操作结果' ],
            colModel : [
                {name : 'id',index : 'id',width : 80,align : "center"},
                {name : 'adminname',align : "center",index : 'invdate',width : 85},
                {name : 'optiontime',align : "center",index : 'optiontime asc, invdate',width : 80},
                {name : 'options',index : 'amount',width : 80,align : "center"},
                {name : 'issuccess',align : "center",index : 'tax',width : 80,align : "center"}

            ]
        });
        //分页工具栏
        $("#logTable").jqGrid('navGrid', '#logPage',
            {edit : false,add : false,del : false,addtext:"添加",edittext:"编辑",deltext:"删除"},
            {
                closeAfterEdit:true,  //关闭对话框

            },  //修改之后的额外操作
            {
                closeAfterAdd:true,  //关闭对话框
             },//添加之后的操作
            {}//删除之后的操作

            );



    }
</script>

<%--创建一个面板--%>
<div class="panel panel-primary">

    <%--面板头--%>
    <div class="panel panel-heading">
        <span>日志信息</span>
    </div>

    <!-- Nav tabs -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">用户信息</a></li>
    </ul><br>

        <%--警告框--%>
        <div id="showMessages" style="width:300px;display: none " class="alert alert-warning alert-dismissible" role="alert">
            <strong id="showMsgs" />
        </div>

    <%--创建表格--%>
    <table id="logTable" />

    <%--分页工具栏--%>
    <div id="logPage"/>

</div>