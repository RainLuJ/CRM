<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script>
	$(function () {
		// 使login.jsp始终在顶层窗口中打开
		if (window.top !== window) {
			window.top.location = window.location;
		}


		//页面加载完毕之后，需要清空文本框中的内容
		$("#loginAct").val("");
		$("#loginPwd").val("");

		//页面加载完毕之后，让用户的文本框自动获得光标
		$("#loginAct").focus();

		//为登录按钮绑定单击事件（如果用户敲击了“button”，即表示提交用户信息，开始登录）
		$("#submitBtn").click(function () {
			login();
		});

		// 为当前窗口绑定敲键盘事件（如果用户敲击了“回车”，即表示提交用户信息，开始登录）
		$(window).keydown(function (event) {
			if (event.keyCode === 13) {
				login();
			}
		})

	});

	function login() {

		// alert("执行登录操作");   需要去掉首尾空格
		var loginAct = $.trim($("#loginAct").val());
		var loginPwd = $.trim($("#loginPwd").val());

		if (loginAct === "" || loginPwd === "") {
			$("#msg").html("用户名和密码不能为空");

			return false;
		}

		$.ajax({
			url: "settings/user/login.do",
			data: {"loginAct" : loginAct, "loginPwd" : loginPwd},
			type: "post",
			dataType: "json",
			success: function (data) {

				//如果登录成功，则跳转到工作台
				if (data.success) {
					window.location.href = "workbench/index.jsp";
				} else {
					//错误信息由后台发送，回显
					$("#msg").html(data.msg);
				}
			}
		});
	}
</script>

</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2022 CRM</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red"></span>
						
					</div>
					<!--

						注意：按钮写在form表单中，默认的行为就是提交表单
							一定要将按钮的类型设置为button
							按钮所触发的行为应该是由我们自己手动写js代码来决定

					-->
					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>