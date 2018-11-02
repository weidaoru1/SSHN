<%@ page language="java" contentType="text/html; charset=UTF-8"  
        pageEncoding="UTF-8"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
    <html>  
    <head>  
    <meta content="text/html; charset=UTF-8" http-equiv="Content-Type">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap/jquery.2.2.4.min.js"></script>  
    <script type="text/javascript">
	function doFind(){
		
	   $.ajax( {
		   cache: true,
		   type: "POST",
		   url:"${pageContext.request.contextPath}/user/UserLogin",
		   data:$('#ajaxFrm').serialize(),
		   error:function(data){
				alert("出错了！！:"+data.msg);
		   },
		   success:function(data){
			   alert(data.msg.userName);
			  $("#ajaxDiv").html(data.msg.userName);
			  $("#ajaxDivss").html(data.userName);
		   }
		});
	}
</script>
</head>
<body>
<a href="${pageContext.request.contextPath}/user/addUer">SpringMvc第一个程序</a>
<form id="ajaxFrm" >
     <input type="text" name="userName">
     <input type="button" onClick="doFind();" value="调用一下ajax">
     <div id="ajaxDiv"></div>
     <div id="ajaxDivss"></div>
    </form>
</body>
</html>