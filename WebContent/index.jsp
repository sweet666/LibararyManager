<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css" href="./css/index.css">

<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<title>Библиотека</title>
</head>
<body>
    <h1>Библиотека</h1>

    <c:if test="${'none' eq(requestScope['loggedInRole'])}">
    <table style="width:50%;margin: 0px auto;" cellpadding="0" cellspacing="0" border="0">
    <tr>
       <td style="width:50%;" valign="top">

           <form action="./Controller?command=reader_login" method="POST">
               <c:if test="${'bad_username' eq(requestScope['readerError'])}">
                  <p class="error">Неверное имя пользователя или пароль</p>
               </c:if>
               <p>
                   <span class="formItem">Имя читателя<sup>*</sup>:</span>
                   <input type="text" name="username" required>
               </p>
               <p>
                    <span class="formItem">Пароль<sup>*</sup>:</span>
                    <input type="password" name="password">
               </p>
               <p style="text-align:center;">
                  <input type="submit" name="submit" value="Войти как читатель">
               </p>
               <input type="hidden" name="action" value="readerLogin">
           </form>

            <p style="padding-left:30px;">
                <a href="./Controller?command=reader_signup_page">Регистрация читателя</а>
            </p>
       </td>
       <td style="width:50%;" valign="top">

           <form action="./Controller?command=librarian_login" method="POST">
                <c:if test="${'bad_username' eq(requestScope['librarianError'])}">
                  <p class="error">Неверное имя пользователя или пароль</p>
               </c:if>
               <p>
                   <span class="formItem">Имя библиотекаря<sup>*</sup>:</span>
                   <input type="text" name="username" required>
               </p>
               <p>
                    <span class="formItem">Пароль<sup>*</sup>:</span>
                    <input type="password" name="password">
               </p>
               <p style="text-align:center;">
                    <input type="submit" name="submit" value="Войти как библиотекарь">
               </p>
               <input type="hidden" name="action" value="librarianLogin">
           </form>

       </td>
     </tr>
    </table>
    </c:if>

    <c:if test="${!('none' eq(requestScope['loggedInRole']))}">
        <p style="width:30%;margin: 0 auto;">
            Вы залогинены как: <br/>

            Пользователь: <b>${requestScope['loggedInUser']}</b> <br />
            Роль: <b>${requestScope['loggedInRole']}</b>

        </p>
        <form action="./Controller?command=logout" method="POST" style="width:10%;margin: 0 auto;">
          <input type="submit" value="Выйти" />
          <input type="hidden" name="action" value="logout">
        </form>
    </c:if>

        <br /><br /><br />
        <p>
          ${requestScope['date'].toString()} Библиотека.
        </p>
</body>
</html>