<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<title>Библиотека - Библиотекарь</title>
</head>
<body>
    <h1>Библиотекарь</h1>

    <ul>
      <li> <a href="./Controller?command=librarian_author_list">Редактирование авторов</a>
      <li> <a href="./Controller?command=librarian_book_list">Редактирование книг</a>
      <li> <a href="./Controller?command=librarian_book_copy_list">Редактирование экземпляров книг</a>
    </ul>

    <br /><br /><br />
    <p>
      ${requestScope['date'].toString()} Библиотека. &nbsp;&nbsp;
       <a href="./Controller?command=index">На главную</a>
    </p>

</body>
</html>