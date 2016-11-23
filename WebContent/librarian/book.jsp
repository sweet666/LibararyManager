<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="./css/librarian_book.css">
<script src="./js/application.js"></script>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<title>Библиотекарь - Книги</title>
</head>
<body>
    <h1>Библиотекарь: книги</h1>

    <table border="0" style="width:98%;">
    <tr>
        <td style="width: 70%;" valign="top">
            <div class="bookContainer">
                <h3>Книги</h3>

                <table class="datatable" cellpadding="0" cellspacing="0" border="0">
                <tr class="datatable_header">
                    <td class="isbn">ISBN</td>
                    <td><b>Название</b></td>
                    <td><b>Автор</b></td>
                    <td><b>Год</b></td>
                    <td><b>Издание</b></td>

                    <td style="width:10px;">&nbsp;</td>
                    <td style="width:10px;">&nbsp;</td>
                </tr>
                <%
                   int counter = 0;
                %>
                <c:forEach items="${requestScope['books']}" var="book">
                     <tr <% if (counter++ % 2 == 0) out.println("class=\"datatable_even\""); %>>
                         <td class="isbn">${fn:escapeXml(book.getIsbn())}</td>
                         <td>${fn:escapeXml(book.getTitle())}</td>
                         <td>${fn:escapeXml(book.getAuthor().getFullName())}</td>
                         <td>${book.getYear()}</td>
                         <td>${book.getEdition()}</td>

                         <td><a href="./Controller?command=librarian_book_list&isbn=${fn:escapeXml(book.getIsbn())}">редакт.</td>
                         <td><a href="#" onclick="return removeBook('${fn:escapeXml(book.getIsbn())}');">удал.</td>
                     </tr>
                </c:forEach>
                </table>
                <form action="./Controller?command=librarian_book_remove" id="removeForm" method="POST">
                    <input type="hidden" name="removeId" value="" id="removeId" />
                </form>

            </div>
        </td>
        <td style="width: 30%;" valign="top">
            <div style="padding: 5px 0 50px 40px;">
                <c:if test="${requestScope['isbn'] != null}">
                <h3>Редактирование книги: </h3>
                <form action="./Controller?command=librarian_book_add&isbn=${fn:escapeXml(requestScope['isbn'])}" method="POST">
                </c:if>
                <c:if test="${requestScope['isbn'] == null}">
                <h3>Создание новой книги: </h3>
                <form action="./Controller?command=librarian_book_add" method="POST">
                </c:if>

                    <c:if test="${requestScope['error'] != null}">
                      <p style="color: red;">${fn:escapeXml(requestScope['error'])}</p>
                    </c:if>

                    <p>
                        <span class="formItem">ISBN<sup>*</sup>:</span>
                        <input type="text" name="isbn" required
                             pattern="[0-9]{1,4}-(.*[0-9]+)"
                             title="Это поле обязательно. Формат ISBN: 123-123-1234-0"
                             <c:if test="${requestScope['isbn'] != null}">
                                readonly
                             </c:if>
                             value="${fn:escapeXml(requestScope['isbn'])}">
                    </p>

                    <p>
                        <span class="formItem">Название<sup>*</sup>:</span>
                        <input type="text" name="title" required
                             pattern=".*\S+.*" title="Это поле обязательно"
                             value="${fn:escapeXml(requestScope['title'])}">
                    </p>

                    <p>
                        <span class="formItem">Автор<sup>*</sup>:</span>
                        <select name="authorId" required>
                          <option value="">--Выберите автора--</option>
                          <c:forEach items="${requestScope['authors']}" var="author">
                              <c:if test="${author.getId() == requestScope['authorId']}">
                                 <option value="${author.getId()}" selected>
                              </c:if>
                              <c:if test="${author.getId() != requestScope['authorId']}">
                                 <option value="${author.getId()}">
                              </c:if>
                                 ${fn:escapeXml(author.getLastName())},
                                 ${fn:escapeXml(author.getFirstName())}
                                 ${fn:escapeXml(author.getMiddleName())}
                              </option>
                          </c:forEach>
                        </select>
                    </p>

                    <p>
                        <span class="formItem">Издание:</span>
                        <input type="text" name="edition"
                             pattern="[0-9]{1,2}"
                             value="${fn:escapeXml(requestScope['edition'])}">
                    </p>

                    <p>
                        <span class="formItem">Год:</span>
                        <input type="text" name="year"
                             value="${fn:escapeXml(requestScope['year'])}">
                    </p>

                    <p>
                        <span class="formItem">Описание:</span>
                        <textarea name="description">${fn:escapeXml(requestScope['description'])}</textarea>
                    </p>

                    <c:if test="${requestScope['isbn'] != null}">
                        <input type="submit" name="submit" value="Сохранить">
                        <input type="button"
                           onclick="if (confirm('Отменить?')) {window.location = './Controller?command=librarian_book_list'};"
                           value="Отмена">
                    </c:if>
                    <c:if test="${requestScope['isbn'] == null}">
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