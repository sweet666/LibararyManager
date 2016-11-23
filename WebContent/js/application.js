$ = function(id) {
 return document.getElementById(id)
};

removeAuthor = function(id) {
  $('removeId').value = '' + id;
  $('removeForm').submit();
}

removeBook = function(id) {
  if (!confirm('Удалить книгу?')) {
    return false;
  }
  $('removeId').value = '' + id;
  $('removeForm').submit();
  return false;
}

removeBookCopy = function(id) {
  if (!confirm('Удалить экземпляр книги?')) {
    return false;
  }
  $('removeSerialNumber').value = id;
  $('removeForm').submit();
  return false;
}

releaseBookCopy = function(id) {
  if (!confirm('Вернуть экземпляр книги в библиотеку?')) {
    return false;
  }
  $('releaseSerialNumber').value = id;
  $('releaseForm').submit();
  return false;
}

retainBookCopy = function(id) {
  $('retainSerialNumber').value = id;
  $('retainForm').submit();
  return false;
}

retainBookCopyInRoom = function(id) {
 $('retainSerialNumber').value = id;
 $('inRoom').value = "true";
 $('retainForm').submit();
 return false;
}