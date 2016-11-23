<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css" href="./css/reader_signup.css">

<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<title>Библиотека</title>
</head>
<body>
    <h1>Библиотека: регистрация читателя</h1>

    <div class="signup">
    <c:if test="${'none' eq(requestScope['loggedInRole'])}">

           <form action="./Controller?command=reader_signup" method="POST">
               <c:if test="${'bad_username' eq(requestScope['error'])}">
                  <p class="error">Введите имя пользователя</p>
               </c:if>
               <c:if test="${'bad_email' eq(requestScope['error'])}">
                  <p class="error">Введите правильный email</p>
               </c:if>
               <c:if test="${'bad_password' eq(requestScope['error'])}">
                  <p class="error">Пароль пуст или не совпадает</p>
               </c:if>
               <c:if test="${requestScope['signup_error'] != null}">
                  <p class="error">
                    Ошибка создания: ${fn:escapeXml(requestScope['signup_error'])}
                    <br/>
                    Обратитесь к администратору.
                  </p>
               </c:if>
               <p>
                   <span class="formItem">Имя читателя<sup>*</sup>:</span>
                   <input type="text" name="username" required
                     value="${fn:escapeXml(requestScope['username'])}">
               </p>
                <p>
                   <span class="formItem">Email читателя<sup>*</sup>:</span>
                   <input type="email" name="email" required
                     value="${fn:escapeXml(requestScope['email'])}">
               </p>
               <p>
                    <span class="formItem">Пароль<sup>*</sup>:</span>
                    <input type="password" name="password">
               </p>
               <p>
                    <span class="formItem">Повторите пароль<sup>*</sup>:</span>
                    <input type="password" name="password_repeat">
               </p>
               <p style="text-align:center;">
                  <input type="submit" name="submit" value="Зарегистрироваться">
               </p>
           </form>

    </c:if>

    <c:if test="${!('none' eq(requestScope['loggedInRole']))}">
            Вы залогинены как: <br/>

            Пользователь: <b>${requestScope['loggedInUser']}</b> <br />
            Роль: <b>${requestScope['loggedInRole']}</b>

        <form action="./Controller?command=logout" method="POST" style="width:10%;margin: 0 auto;">
          <input type="submit" value="Выйти" />
          <input type="hidden" name="action" value="logout">
        </form>
    </c:if>
    </div>

        <br /><br /><br />
        <p>
          ${requestScope['date'].toString()} Библиотека.
        </p>
</body>
</html>