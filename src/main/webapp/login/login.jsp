<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>yingx Login</title>
    <!-- CSS -->
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
    <link rel="stylesheet" href="${path}/login/assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${path}/login/assets/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="${path}/login/assets/css/form-elements.css">
    <link rel="stylesheet" href="${path}/login/assets/css/style.css">
    <link rel="shortcut icon" href="${path}/login/assets/ico/favicon.png">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="${path}/login/assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="${path}/login/assets/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="${path}/login/assets/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="${path}/login/assets/ico/apple-touch-icon-57-precomposed.png">
    <script src="${path}/bootstrap/js/jquery.min.js"></script>
    <script src="${path}/login/assets/bootstrap/js/bootstrap.min.js"></script>
    <script src="${path}/login/assets/js/jquery.backstretch.min.js"></script>
    <script src="${path}/login/assets/js/scripts.js"></script>
    <script src="${path}/login/assets/js/jquery.validate.min.js"></script>
</head>
<script>
    $(function(){

        //点击切换验证码
        $("#captchaImage").click(function(){
            $("#captchaImage").prop("src","${path}/Img/code?d="+new Date().getTime());
        });

        //表单验证  设置验证规则
        $.extend($.validator.messages, {
            required: "<sapn style='color:red' >这是必填字段</span>",
            minlength: $.validator.format("<sapn style='color:red' >请正确输入</span>")
        });

        //点击提交表单
        $("#loginButtonId").click(function(){

            //表单验证   出发表单验证
            var isOk=$("#loginForm").valid();

            //判断表单验证是否通过
            if(isOk){
                //提交表单
                $.ajax({
                    url:"${path}/admin/login",
                    type:"post",
                    dataType:"json",
                    data:$("#loginForm").serialize(),  //表单序列化获取表单数据     根据input的name获取对应的值
                    success:function(data){
                        if(data.status=="200"){
                            //跳转页面
                            location.href="${path}/main/main.jsp";
                        }else{
                            //展示错误信息
                            $("#msgDiv").html("<sapn style='color:red' >"+data.message+"</span>");
                        }
                    }
                })
            }
        });
    });

</script>

<body>

<!-- Top content -->
<div class="top-content">

    <div class="inner-bg">
        <div class="container">
            <div class="row">
                <div class="col-sm-8 col-sm-offset-2 text">
                    <h1><strong>YINGX</strong> Login Form</h1>
                    <div class="description">
                        <p>
                            <a href="#"><strong>YINGX</strong></a>
                        </p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6 col-sm-offset-3 form-box">
                    <div class="form-top" style="width: 450px">
                        <div class="form-top-left">
                            <h3>Login to showAll</h3>
                            <p>Enter your username and password to log on:</p>
                        </div>
                        <div class="form-top-right">
                            <i class="fa fa-key"></i>
                        </div>
                    </div>
                    <div class="form-bottom" style="width: 450px">
                        <form role="form" action="" method="post" class="login-form" id="loginForm">
                            <span ></span>
                            <div class="form-group">
                                <label class="sr-only" for="form-username">Username</label>
                                <input type="text" name="username" placeholder="请输入用户名..." class="form-username form-control" required id="form-username"  minlength="3">
                            </div>
                            <div class="form-group">
                                <label class="sr-only" for="form-password">Password</label>
                                <input type="password" name="password" placeholder="请输入密码..." class="form-password form-control" required id="form-password"  minlength="6">
                            </div>
                            <div class="form-group">
                                <%--<label class="sr-only" for="form-code">Code</label>--%>
                                <img id="captchaImage" style="height: 48px" class="captchaImage" src="${path}/Img/code" onclick="javascript:var dt=new Date();document.getElementById('captchaImage').src='${path}/Img/code?dt='+dt.getTime();">
                                <input style="width: 289px;height: 50px;border:3px solid #ddd;border-radius: 4px;" required type="test" name="enCode" id="form-code"  minlength="6">
                            </div>
                            <p id="msg_pwd" style="margin-left: 150px" ><span style="color: orangered" id="msgDiv">${sessionScope.mesg}</span></p>
                            <c:remove var="mesg" scope="session"></c:remove>
                            <input type="button" style="width: 400px;border:1px solid #9d9d9d;border-radius: 4px;" id="loginButtonId" value="登录">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
<div class="copyrights">Collect from <a href="http://www.cssmoban.com/" title="网站模板">网站模板</a></div>


<!-- Javascript -->

<!--[if lt IE 10]>
<script src="assets/js/placeholder.js"></script>
<![endif]-->

</body>

</html>