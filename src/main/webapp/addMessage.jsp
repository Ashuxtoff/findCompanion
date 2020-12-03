<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
  <head>
      <meta charset="utf-8">
      <title>Add message</title>

      <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
      <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
  </head>

  <body>

    <div class="container">


        <form:form method="POST" modelAttribute="messageForm" class="form-signin">
            <h2 class="form-signin-heading">Add message</h2>
            <spring:bind path="text">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <form:input type="text" path="text" class="form-control" placeholder="Text"
                                autofocus="true"></form:input>
                    <form:errors path="text"></form:errors>
                </div>
            </spring:bind>


            <button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
        </form:form>

    </div>

    <h4><a href="${contextPath}/event${Id}/chat">Back to chat</a></h4>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="${contextPath}/resources/js/bootstrap.min.js"></script>
  </body>
</html>
