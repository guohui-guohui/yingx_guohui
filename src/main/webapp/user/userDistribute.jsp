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
    <title>用户分布</title>
    <script src="${path}/bootstrap/js/echarts.js"></script>
    <script type="text/javascript" src="${path}/bootstrap/js/china.js"></script>
    <script src="${path}/bootstrap/js/jquery.min.js"></script>
    <script type="text/javascript">
        $(function(){
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('main'));

            $.get("${path}/user/getUserChina",function(datas){

                //创建空数组
                var series=[];

                //遍历集合数据
                for(var i=0;i<datas.length;i++){

                    //获取集合中的对象
                    var data=datas[i];

                    series.push(
                        {
                            name: data.sex,
                            type: 'map',
                            mapType: 'china',
                            roam: false,
                            label: {
                                normal: {
                                    show: false
                                },
                                emphasis: {
                                    show: true
                                }
                            },
                            data:data.citys
                        }
                    )
                }

                // 指定图表的配置项和数据
                var option = {
                    title : {
                        text: '每月用户注册量',
                        subtext: '虚构',
                        left: 'center'
                    },
                    tooltip : {
                        trigger: 'item'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data:['男','女']
                    },
                    visualMap: {
                        min: 0,
                        max: 5,
                        left: 'left',
                        top: 'bottom',
                        text:['高','低'],           // 文本，默认为数值文本
                        calculable : true
                    },
                    toolbox: {
                        show: true,
                        orient : 'vertical',
                        left: 'right',
                        top: 'center',
                        feature : {
                            mark : {show: true},
                            dataView : {show: true, readOnly: false},
                            restore : {show: true},
                            saveAsImage : {show: true}
                        }
                    },
                    series : series
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
                channel: "channel",//替换为您自己的channel
                onMessage: function (message) {  //获取的消息

                    //获取内容
                    var data=message.content;

                    //将json字符串转为javascript对象
                    var datas=JSON.parse(data);

                    ///创建空数组
                    var series = [];

                    //遍历集合数据
                    for (var i = 0; i < datas.length; i++) {

                        //获取集合中的对象
                        var data = datas[i];

                        series.push(
                            {
                                name: data.sex,
                                type: 'map',
                                mapType: 'china',
                                roam: false,
                                label: {
                                    normal: {
                                        show: false
                                    },
                                    emphasis: {
                                        show: true
                                    }
                                },
                                data: data.citys
                            }
                        )
                    }

                    // 指定图表的配置项和数据
                    var option = {
                        title: {
                            text: '每月用户注册量',
                            subtext: '虚构',
                            left: 'center'
                        },
                        tooltip: {
                            trigger: 'item'
                        },
                        legend: {
                            orient: 'vertical',
                            left: 'left',
                            data: ['男', '女']
                        },
                        visualMap: {
                            min: 0,
                            max: 5,
                            left: 'left',
                            top: 'bottom',
                            text: ['高', '低'],           // 文本，默认为数值文本
                            calculable: true
                        },
                        toolbox: {
                            show: true,
                            orient: 'vertical',
                            left: 'right',
                            top: 'center',
                            feature: {
                                mark: {show: true},
                                dataView: {show: true, readOnly: false},
                                restore: {show: true},
                                saveAsImage: {show: true}
                            }
                        },
                        series: series
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
        <span>用户分布</span>
    </div>

    <!-- Nav tabs -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">用户分布</a></li>
    </ul><br>

        <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
        <div id="main" style="width: 600px;height:400px;"></div>


</div>

</body>
</html>
