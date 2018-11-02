<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${pageContext.request.contextPath}/css/bootstrap/bootstrap.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/bootstrap/font-awesome_new.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/bootstrap/font-awesome.min.new.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap/jquery.2.2.4.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap/bootstrap.min.js"></script>
<title>Insert title here</title>
<script type="text/javascript"> 
	$().ready(function (){
		 $("#uname").blur(function(){
			 $("#infor").empty();
	         var userName=$("#uname").val();
	         if(userName.length==0){
	        	 $("#infor").append("账号不能为空");
		     }
		 });
	});
	function loadCheck(){
		var uname=document.getElementById('uname').value;
		var pword=document.getElementById('pword').value;
		alert(uname);
		alert(pword);
		if (uname.length==0||pword.length==0){
			alert("账号或密码为空");
			return;
		}
	}
</script>  
</head>
<body>  
<div class="container" style="width: 940px;">
		<div>
			<h3>用户登录</h3>
			<form action="" method="post" class="form-horizontal">
				<div class="form-group">
					<label for="userName" class="col-sm-2 text-right">用户名：</label>
					<div class="col-sm-10">
						<input type="text" id="uname" class="form-control col-sm-8" /><div id="infor"></div>
					</div>
				</div>
				<div class="form-group">
					<label for="password" class="col-sm-2 text-right">密&nbsp;&nbsp;&nbsp; 码：</label>
					<div class="col-sm-10">
						<input type="password" id="pword" class="form-control col-sm-8" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-10 col-sm-offset-1">
						<button type="button" class="btn_login"  onclick="loadCheck()">登录</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>  
</html>