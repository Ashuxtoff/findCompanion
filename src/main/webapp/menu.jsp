<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
  <head>
      <meta charset="utf-8">
      <title>Log in with your account</title>

      <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
      <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
  </head>

  <body>
            <h4 class="text-center"><a href="${contextPath}/editProfile">Edit profile</a></h4>
            <h4 class="text-center"><a href="${contextPath}/myEvents">My Events</a></h4>
            <h4 class="text-center"><a href="${contextPath}/lookForEvent">Look for event</a></h4>
            <h4 class="text-center"><a href="${contextPath}/addEvent">Add Event</a></h4>
            <h4 class="text-center"><a href="${contextPath}/logout">Logout</a></h4>
        </div>
      </form>
    </div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="${contextPath}/resources/js/bootstrap.min.js"></script>
  </body>
</html>
