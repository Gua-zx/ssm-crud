<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>员工页面</title>
	<%
		pageContext.setAttribute("PATH", request.getContextPath());
	%>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<!-- 引入样式和jq -->
	<script type="text/javascript" src="${PATH}/static/js/jquery-1.12.4.js"></script>
	<link href="${PATH}/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
	<script src="${PATH}/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
  </head>
  
  <body>
    <!-- 用Bootstrap编写页面 -->
    <div class="container">
    	<!-- 标题 -->
    	<div class="row">
    		<div class="col-md-12">
    			<h1>SSM-CRUD</h1>
    		</div>
    	</div>
    	<!-- 按钮 -->
    	<div class="row">
    		<div class="col-md-4 col-md-offset-8">
    			<button type="button" class="btn btn-info btn btn-default btn-lg">添加</button>
    			<button type="button" class="btn btn-danger btn btn-default btn-lg">删除</button>
    		</div>
    	</div>
    	<br/>
    	<!-- 表格信息 -->
    	<div class="row">
    		<div class="col-md-12">
    			<table class="table table-hover">
    				<tr>
    					<th>ID</th>
    					<th>姓名</th>
    					<th>性别</th>
    					<th>邮箱</th>
    					<th>部门</th>
    					<th>操作</th>
    				</tr>
    				<c:forEach items="${pageInfo.list }" var="emp">
    				<tr>
    					<th>${emp.empId}</th>
    					<th>${emp.empName}</th>
    					<th>${emp.gender=="M"?"男":"女"}</th>
    					<th>${emp.email}</th>
    					<th>${emp.department.deptName}</th>
    					<th>
    						<button type="button" class="btn btn-info btn-sm">
    						<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
    						编辑
    						</button>
    						<button type="button" class="btn btn-danger btn-sm">
    						<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
    						删除
    						</button>
    					</th>
    				</tr>
    				</c:forEach>
    			</table>
    		</div>
    	</div>
    	<!-- 分页 -->
    	<div class="row">
    		<!-- 分页信息 -->
    		<div class="col-md-4">
    			当前${pageInfo.pageNum }页，总${pageInfo.pages }页，总${pageInfo.total }条记录 
    		</div>
    		<!-- 分页条信息 -->
			<div class="col-md-6">
				<nav aria-label="Page navigation">
				<ul class="pagination">
					<li><a href="${PATH }/emps?pn=1">首页</a></li>
					<c:if test="${pageInfo.hasPreviousPage }">
						<li><a href="${PATH }/emps?pn=${pageInfo.pageNum-1}"
							aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
						</a></li>
					</c:if>


					<c:forEach items="${pageInfo.navigatepageNums }" var="page_Num">
						<c:if test="${page_Num == pageInfo.pageNum }">
							<li class="active"><a href="#">${page_Num }</a></li>
						</c:if>
						<c:if test="${page_Num != pageInfo.pageNum }">
							<li><a href="${PATH }/emps?pn=${page_Num }">${page_Num }</a></li>
						</c:if>

					</c:forEach>
					<c:if test="${pageInfo.hasNextPage }">
						<li><a href="${PATH }/emps?pn=${pageInfo.pageNum+1 }"
							aria-label="Next"> <span aria-hidden="true">&raquo;</span>
						</a></li>
					</c:if>
					<li><a href="${PATH }/emps?pn=${pageInfo.pages}">末页</a></li>
				</ul>
				</nav>
			</div>
		</div>
    </div>
  </body>
</html>
