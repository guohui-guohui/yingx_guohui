<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<script>
    $(function(){
        pageInit();
    });



    function pageInit(){
        $("#cateTable").jqGrid({
            url : "${path}/category/queryOneCategory",//接收  page=当前页   rows=每页展示条数   返回  page=当前页   rows=[User,User]数据    tolal=总页数   records=总条数
            //url : "${path}/category/oneCategory.json",
            editurl:"${path}/category/edit?levels="+1,  //增删改走的路径  oper:add ,edit,del
            datatype : "json",
            height : "auto",
            autowidth:true,
            styleUI:"Bootstrap",
            rowNum : 8,
            rowList : [ 8, 10, 20, 30 ],
            pager : '#catePage',
            viewrecords : true,
            colNames : [ 'Id', '类别名', '级别', '上级别id',"数据量"],
            colModel : [
                {name : 'id',index : 'id',  width : 55},
                {name : 'cateName',editable:true,index : 'invdate',width : 90},
                {name : 'levels',index : 'name',width : 100},
                {name : 'parentId',hidden:true,index : 'note',width : 150,sortable : false},
                {name : 'count',index : 'name',width : 100},
            ],
            subGrid : true, //开启子表格
            // subgrid_id是在创建表数据时创建的div标签的ID
            //点击二级类别按钮，动态创建一个div,此div来容纳子表格，subgrid_id就是这个div的id
            // row_id是该行的ID
            subGridRowExpanded : function(subgrid_id, row_id) {
                addSubGird(subgrid_id, row_id);
            }
        });
        //分页工具栏
        $("#cateTable").jqGrid('navGrid', '#catePage', {add : true,edit : true,del : true,addtext:"添加",edittext:"编辑",deltext:"删除"},
            {
                closeAfterEdit:true,
                //关闭添加窗口
            },//修改之后的额外操作
            {
                closeAfterAdd:true,
            },//添加之后的额外操作
            {
                closeAfterDel:true,  //关闭对话框
                afterSubmit:function(data){//判断有无子表格数据， === count数量
                    //向提示框设置内容
                    $("#showMsg").html("<span>"+data.responseJSON.message+"</span>");
                    //展示提示框
                    $("#showMessage").show();
                    //设置倒计时关闭
                    setTimeout(function () {
                        //关闭提示框
                        $("#showMessage").hide();
                    },2000);

                    return "aaa";
                }
            }//删除之后的额外操作

        );
    }

    //创建子表格
    function addSubGird(subgridId, rowId) {

        //声明两个变量
        // 子表格table的id
        var subgridTableId= subgridId + "Table";
        // 子表格分页工具栏的id
        var pagerId= subgridId + "Page";

        //在div中创建子表格和分页工具栏
        $("#"+subgridId).html("<table id="+subgridTableId+" /><div id="+pagerId+" />");

        //初始化子表格
        $("#" + subgridTableId).jqGrid({
            url : "${path}/category/queryTwoCategory?parentId="+rowId,
            //url:"${path}/category/TwoCategory.json",
            editurl:"${path}/category/edit?parentId="+rowId+"&levels="+2,  //增删改走的路径  oper:add ,edit,del
            datatype : "json",
            rowNum : 5,
            rowList : [ 1,2,3,4,5],
            pager : "#"+pagerId,
            height : "auto",
            autowidth:true,
            styleUI:"Bootstrap",
            colNames : [ 'Id', '类别名', '级别', '上级别id'],
            colModel : [
                {name : 'id',index : 'id',  width : 55},
                {name : 'cateName',editable:true,index : 'invdate',width : 90},
                {name : 'levels',index : 'name',width : 100},
                {name : 'parentId',index : 'note',width : 150,sortable : false}
            ]
        });

        //子表格的分页工具栏
        $("#" + subgridTableId).jqGrid('navGrid',"#" + pagerId, {add : true,edit : true,del : true,addtext:"添加",edittext:"编辑",deltext:"删除"},
            {
                closeAfterEdit:true,
            },//修改之后的额外操作
            {
                closeAfterAdd:true,
            },//添加之后的额外操作
            {
                closeAfterDel:true,  //关闭对话框
                afterSubmit:function(data){//判断有无子表格数据， === count数量
                    //向提示框设置内容
                    $("#showMsg").html("<span>"+data.responseJSON.message+"</span>");
                    //展示提示框
                    $("#showMessage").show();
                    //设置倒计时关闭
                    setTimeout(function () {
                        //关闭提示框
                        $("#showMessage").hide();
                    },2000);

                    return "aaa";
                }
            }//删除之后的操作
            );
    }

</script>

<%--
1.实现类别功能
   删除数据要给一个提示
2.手机验证码发送
--%>

<%--创建一个面板--%>
<div class="panel panel-success">

    <%--面板头--%>
    <div class="panel panel-heading">
        <span>类别信息</span>
    </div>

    <!-- Nav tabs -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">类别信息</a></li>
    </ul><br>

        <%--警告框--%>
        <div id="showMessage" style="width:300px;display: none " class="alert alert-warning alert-dismissible" role="alert">
            <strong id="showMsg" />
        </div>


    <%--创建表格--%>
    <table id="cateTable" />

    <%--分页工具栏--%>
    <div id="catePage"/>

</div>