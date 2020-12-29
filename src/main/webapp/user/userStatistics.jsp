<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>用户统计</title>
    <script src="${path}/bootstrap/js/echarts.js"></script>
    <script src="${path}/bootstrap/js/jquery.min.js"></script>
    <script type="text/javascript" src="${path}/bootstrap/js/goeasy-1.2.0.js"></script>

    <script type="text/javascript">
        $(function(){
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('main'));

            $.get("${path}/user/getUserData",function(data){

                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: '用户注册数统计图',  //标题
                        show:true,
                        link:"${path}/main/main.jsp",
                        textStyle:{
                            color:'#ccaadd'
                        }
                    },
                    tooltip: {},  //鼠标提示
                    legend: {
                        data:['男',"女"]  //选项卡
                    },
                    xAxis: {  //横坐标展示内容
                        data: data.month
                    },
                    yAxis: {},//纵坐标展示的内容
                    series: [{
                        name: '男',
                        type: 'line',  //图表的类型  bar:柱状图  line:折线图
                        data: data.boys  //数值
                    },{
                        name: '女',
                        type: 'line',
                        data: data.girls
                    }]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            },"JSON");
        });
    </script>
    <script type="text/javascript">

        /*初始化GoEasy对象*/
        var goEasy = new GoEasy({
            host:'hangzhou.goeasy.io', //应用所在的区域地址: 【hangzhou.goeasy.io |singapore.goeasy.io】
            appkey:"BC-bc9baffe713945f18c8df013e5acff44", //替换为您的应用appkey
        });

        $(function(){
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('main'));

            /*接收消息*/
            goEasy.subscribe({
                channel: "my2006_channel",//替换为您自己的channel
                onMessage: function (message) {  //获取的消息

                    //获取内容
                    var datas=message.content;

                    //将json字符串转为javascript对象
                    var data=JSON.parse(datas);

                    // 指定图表的配置项和数据
                    var option = {
                        title: {
                            text: '用户注册数统计图',  //标题
                            show:true,
                            link:"${path}/main/main.jsp",
                            textStyle:{
                                color:'#ccaadd'
                            }
                        },
                        tooltip: {},  //鼠标提示
                        legend: {
                            data:['男',"女"]  //选项卡
                        },
                        xAxis: {  //横坐标展示内容
                            data: data.month
                        },
                        yAxis: {},//纵坐标展示的内容
                        series: [{
                            name: '男',
                            type: 'line',  //图表的类型  bar:柱状图  line:折线图
                            data: data.boys  //数值
                        },{
                            name: '女',
                            type: 'line',
                            data: data.girls
                        }]
                    };

                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);

                }
            });
        });
    </script>

</head>
<body>

<%--创建一个面板--%>
<div class="panel panel-info">

    <%--面板头--%>
    <div class="panel panel-heading">
        <span>用户统计</span>
    </div>

    <!-- Nav tabs -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">用户统计</a></li>
    </ul><br>

    <%--创建echarts的盒子--%>
    <div id="main" style="width: 600px;height: 400px"></div>

</div>

</body>
</html>

