<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="./css/librarian_author.css">
<script src="./js/application.js"></script>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<title>Библиотекарь - Авторы</title>
</head>
<body>
    <h1>Библиотекарь: авторы</h1>

    <table border="0" style="width:90%;">
    <tr>
        <td style="width: 70%;" valign="top">
            <div class="authorContainer">
                <h3>Авторы</h3>

                <table class="datatable" cellpadding="0" cellspacing="0" border="0">
                <tr class="datatable_header">
                    <td style="width:10px;">ID</td>
                    <td><b>Имя</b></td>
                    <td><b>Отчество</b></td>
                    <td><b>Фамилия</b></td>
                    <td style="width:10px;">&nbsp;</td>
                    <td style="width:10px;">&nbsp;</td>
                </tr>
                <%
                   int counter = 0;
                %>
                <c:forEach items="${requestScope['authors']}" var="author">
                     <tr <% if (counter++ % 2 == 0) out.println("class=\"datatable_even\""); %>>
                         <td>${author.getId()}</td>
                         <td>${author.getFirstName()}</td>
                         <td>${author.getMiddleName()}</td>
                         <td>${author.getLastName()}</td>
                         <td><a href="./Controller?command=librarian_author_list&id=${author.getId()}">редакт.</td>
                         <td><a href="#" onclick="if (confirm('Удалить?')) removeAuthor(${author.getId()});return false;">удал.</td>
                     </tr>
                </c:forEach>
                </table>
                <form action="./Controller?command=librarian_author_remove" id="removeForm" method="POST">
                    <input type="hidden" name="removeId" value="" id="removeId" />
                </form>

            </div>
        </td>
        <td style="width: 30%;" valign="top">
            <div style="padding: 5px 0 50px 40px;">
                <c:if test="${requestScope['id'] != null}">
                    <h3>Редактирование автора: </h3>
                    <form action="./Controller?command=librarian_author_add?id=${fn:escapeXml(requestScope['id'])}" method="POST">
                </c:if>
                <c:if test="${requestScope['id'] == null}">
                    <h3>Создание нового автора: </h3>
                    <form action="./Controller?command=librarian_author_add" method="POST">
                </c:if>

                    <c:if test="${requestScope['error'] != null}">
                      <p style="color: red;">${fn:escapeXml(requestScope['error'])}</p>
                    </c:if>

                    <p>
                        <span class="formItem">Имя<sup>*</sup>:</span>
                        <input type="text" name="firstName" required value="${fn:escapeXml(requestScope['firstName'])}">
                    </p>
                    <p>
                        <span class="formItem">Отчество:</span>
                        <input type="text" name="middleName" value="${fn:escapeXml(requestScope['middleName'])}">
                    </p>
                    <p>
                        <span class="formItem">Фамилия<sup>*</sup>:</span>
                        <input type="text" name="lastName" required value="${fn:escapeXml(requestScope['lastName'])}">
                    </p>

                    <c:if test="${requestScope['id'] != null}">
                        <input type="submit" name="submit" value="Сохранить">
                        <input type="button"
                           onclick="if (confirm('Отменить?')) {window.location = './Controller?command=librarian_author_list'};" value="Отмена">
                    </c:if>
                    <c:if test="${requestScope['id'] == null}">
                        <input type="submit" name="submit" value="Создать">
                    </c:if>

                </form>
            </div>
        </td>
    </tr>
    </table>




    <br /><br /><br />
    <p>
      ${requestScope['date'].toString()} Библиотека. &nbsp;&nbsp;
      <a href="./Controller?command=librarian_index">На страницу библиотекаря</a>
      |
      <a href="./Controller?command=index">На главную</a>
      (${requestScope['loggedInRole']} / ${requestScope['loggedInUser']})
    </p>
</body>
</html>