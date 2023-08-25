<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form method="post" class="form-signin" action="findid.do" name="findform">
		<div class="form-label-group">
			<label for="name">name</label><br/>
			<input type="text" id="name" name="name" class="form-control"/>
		</div>
		
		<div class="form-label-group">
			<label for="email">email</label><br/>
			<input type="text" id="email" name="email" class="form-control"/>
		</div>

		<div class="form-label-group">
			<input class="btn btn-lg btn-secondary btn-block text-uppercase"
				type="submit" value="check">
		</div>

		<!-- 이름과 email 일치하지 않을 때-->
		<c:if test="${check == 1}">
			<script>
				opener.document.findform.name.value = "";
				opener.document.findform.email.value = "";
			</script>
			<label>일치하는 정보가 존재하지 않습니다.</label>
		</c:if>

		<!-- 이름과 비밀번호가  일치할때 -->
		<c:if test="${check == 0 }">
		<label>찾으시는 아이디는' ${id}' 입니다.</label>
		<div class="form-label-group">
			<input class="btn btn-lg btn-secondary btn-block text-uppercase" type="button" value="OK" onclick="closethewindow()">
		</div>
		</c:if>

	</form>
	
</body>
</html>