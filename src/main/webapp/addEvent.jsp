<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
  <head>
      <meta charset="utf-8">
      <title>Add event</title>

      <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
      <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
  </head>

  <body>

    <div class="container">


        <form:form method="POST" modelAttribute="eventForm" class="form-signin">
            <h2 class="form-signin-heading">Add event</h2>
            <spring:bind path="title">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <form:input type="text" path="title" class="form-control" placeholder="Title"
                                autofocus="true"></form:input>
                    <form:errors path="title"></form:errors>
                </div>
            </spring:bind>

            <spring:bind path="description">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <form:input type="text" path="description" class="form-control" placeholder="Description"></form:input>
                    <form:errors path="description"></form:errors>
                </div>
            </spring:bind>

            <spring:bind path="datetime">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    Datetime:
                    <form:input type="datetime-local" label="datetime" path="datetime" placeholder=""></form:input>
                    <form:errors path="datetime"></form:errors>
                </div>
            </spring:bind>

            <spring:bind path="address">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <form:input type="text" path="address" class="form-control" placeholder="Address"></form:input>
                    <form:errors path="address"></form:errors>
                </div>
            </spring:bind>


            <button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
        </form:form>

    </div>

    <h4><a href="${contextPath}/menu">Back to menu</a></h4>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="${contextPath}/resources/js/bootstrap.min.js"></script>
  </body>
</html>
