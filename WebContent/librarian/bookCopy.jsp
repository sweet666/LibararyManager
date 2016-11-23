<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="./css/librarian_bookCopy.css">
<script src="./js/application.js"></script>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<title>Библиотекарь - Экземпляры книг</title>
</head>
<body>
    <h1>Библиотекарь: экземпляры книг</h1>

    <table border="0" style="width:98%;">
    <tr>
        <td style="width: 70%;" valign="top">
            <div class="bookCopyContainer">
                <h3>Экземпляры книг</h3>

                <table class="datatable" cellpadding="0" cellspacing="0" border="0">
                <tr class="datatable_header">
                    <td class="serialNumber">#</td>
                    <td><b>Название книги</b></td>
                    <td><b>Автор</b></td>
                    <td><b>ISBN</b></td>
                    <td><b>Состояние</b></td>
                    <td><b>Последнее обновление</b></td>

                    <td style="width:10px;">&nbsp;</td>
                    <td style="width:10px;">&nbsp;</td>
                </tr>
                <%
                   int counter = 0;
                %>
                <c:forEach items="${requestScope['booksCopies']}" var="bookCopy">
                     <tr <% if (counter++ % 2 == 0) out.println("class=\"datatable_even\""); %>>
                         <td class="serialNumber">${bookCopy.getSerialNumber()}</td>
                         <td>${fn:escapeXml(bookCopy.getBook().getTitle())}</td>
                         <td>${fn:escapeXml(bookCopy.getBook().getAuthor().getFullName())}</td>
                         <td class="serialNumber">${fn:escapeXml(bookCopy.getBook().getIsbn())}</td>
                         <td>
                            <c:if test="${bookCopy.getState().toString().equals('AVAILABLE')}">
                                Доступна
                            </c:if>
                            <c:if test="${bookCopy.getState().toString().equals('BOOKED')}">
                                Читатель: ${fn:escapeXml(bookCopy.getUsername())}
                            </c:if>
                            <c:if test="${bookCopy.getState().toString().equals('INROOM')}">
                                Читатель в зале: ${fn:escapeXml(bookCopy.getUsername())}
                            </c:if>
                         </td>
                         <td class="serialNumber">${bookCopy.getLastStateChange()}</td>

                         <td>
                            <c:if test="${bookCopy.getState().toString().equals('BOOKED')||bookCopy.getState().toString().equals('INROOM')}">
                              <a href="#" onclick="return releaseBookCopy('${fn:escapeXml(bookCopy.getSerialNumber())}');">вернуть</a>
                            </c:if>
                         </td>
                         <td><a href="#" onclick="return removeBookCopy('${fn:escapeXml(bookCopy.getSerialNumber())}');">удал.</a></td>
                     </tr>
                </c:forEach>
                </table>
                <form action="./Controller?command=librarian_book_copy_remove" id="removeForm" method="POST">
                    <input type="hidden" name="removeSerialNumber" value="" id="removeSerialNumber" />
                </form>
                <form action="./Controller?command=librarian_book_copy_release" id="releaseForm" method="POST">
                    <input type="hidden" name="releaseSerialNumber" value="" id="releaseSerialNumber" />
                </form>

            </div>
        </td>
        <td style="width: 30%;" valign="top">
            <div style="padding: 5px 0 50px 40px;">


                <h3>Создание нового экземпляра книги: </h3>
                <form action="./Controller?command=librarian_book_copy_add" method="POST">

                    <c:if test="${requestScope['error'] != null}">
                      <p style="color: red;">${fn:escapeXml(requestScope['error'])}</p>
                    </c:if>

                    <p>
                        <span class="formItem">Серийный номер<sup>*</sup>:</span>
                        <input
                             type="text"
                             name="serialNumber"
                             required
                             pattern=".*\S+.*"
                             title="Это поле обязательно."
                             value="${fn:escapeXml(requestScope['serialNumber'])}"
                             style="width:250px;"
                             >
                    </p>

                    <p>
                        <span class="formItem">Книга<sup>*</sup>:</span>
                        <select name="isbn" required>
                          <option value="">--Выберите книгу--</option>
                          <c:forEach items="${requestScope['books']}" var="book">
                                 <option value="${fn:escapeXml(book.getIsbn())}">
                                 &quot;${fn:escapeXml(book.getTitle())}&quot;,
                                 ${fn:escapeXml(book.getAuthor().getLastName())}
                                 ${fn:escapeXml(author.getFirstName())}
                                 ${fn:escapeXml(author.getMiddleName())}
                              </option>
                          </c:forEach>
                        </select>
                    </p>

                    <input type="submit" name="submit" value="Создать">

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