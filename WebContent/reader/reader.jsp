<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="./js/application.js"></script>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- Styles by condition -->
<c:if test="${requestScope['booksCopies'] != null}">
  <link rel="stylesheet" type="text/css" href="./css/reader_search.css">
</c:if>
<c:if test="${requestScope['booksCopies'] == null}">
  <link rel="stylesheet" type="text/css" href="./css/reader_index.css">
</c:if>

<title>Читатель - Книги</title>
</head>
<body>
    <h1 class="title">
        <span class="b1">Б</span><span class="b2">и</span><span
        class="b3">б</span><span class="b4">л</span><span
        class="b8">и</span><span class="b5">о</span><span
        class="b9">т</span><span class="b6">е</span><span
        class="b10">к</span><span class="b7">а</span>
    </h1>

        <form action="./Controller" method="GET" class="searchForm">
            <input type="text" name="q" value="${fn:escapeXml(requestScope['query'])}" />
            <input type="hidden" name="command" value="reader_book_search" />
            <input type="submit" value="Искать">
        </form>


    <c:if test="${requestScope['booksCopies'] != null}">
                <table class="datatable" cellpadding="0" cellspacing="0" border="0">
                <tr class="datatable_header">
                    <td><b>Название книги</b></td>
                    <td><b>Автор</b></td>
                    <td><b>ISBN</b></td>
                    <td><b>#</b></td>

                    <td style="width:10px;"><b>Год</b></td>
                    <td style="width:10px;">&nbsp;</td>
                     <td style="width:10px;">&nbsp;</td>
                </tr>
                <%
                   int counter = 0;
                %>
                <c:forEach items="${requestScope['booksCopies']}" var="bookCopy">
                     <tr <% if (counter++ % 2 == 0) out.println("class=\"datatable_even\""); %>>
                         <td>${fn:escapeXml(bookCopy.getBook().getTitle())}</td>
                         <td>${fn:escapeXml(bookCopy.getBook().getAuthor().getFullName())}</td>

                         <td class="serialNumber">${fn:escapeXml(bookCopy.getBook().getIsbn())}</td>
                         <td><small>${fn:escapeXml(bookCopy.getSerialNumber())}</small></td>
                         <td class="serialNumber">${bookCopy.getBook().getYear()}</td>
                      

                         <td>
                            <c:if test="${bookCopy.getState().toString().equals('BOOKED')||bookCopy.getState().toString().equals('INROOM')}">
                              недоступна
                            </c:if>
                            <c:if test="${bookCopy.getState().toString().equals('AVAILABLE')}">
                              <a href="#" onclick="return retainBookCopy('${fn:escapeXml(bookCopy.getSerialNumber())}');">взять</a>
                            </c:if>
                         </td>
                         <td>
                         <c:if test="${bookCopy.getState().toString().equals('BOOKED')||bookCopy.getState().toString().equals('INROOM')}">
                              недоступна
                            </c:if>
                         <c:if test="${bookCopy.getState().toString().equals('AVAILABLE')}">
                              <a href="#" onclick="return retainBookCopyInRoom('${fn:escapeXml(bookCopy.getSerialNumber())}');">читать в зале</a>
                            </c:if>
                            </td>
                     </tr>
                </c:forEach>
                </table>
    <</c:if>

        <form action="./Controller?command=reader_book_lease" id="retainForm" method="POST">
            <input type="hidden"
                   name="retainSerialNumber"
                   id="retainSerialNumber"
                   value="" />
            <input type="hidden"
                   name="inRoom"
                   id="inRoom"
                   value="" />
            <input type="hidden" name="q" value="${fn:escapeXml(requestScope['query'])}" />
        </form>

    <br /><br /><br />
    <p
      <c:if test="${requestScope['booksCopies'] == null}">
          style="text-align: center;"
      </c:if>
    >
      ${requestScope['date'].toString()} &nbsp;&nbsp; Библиотека. &nbsp;&nbsp;
      <a href="./index.jsp">На главную</a>
      (${requestScope['loggedInRole']} / ${requestScope['loggedInUser']})
    </p>
</body>
</html>