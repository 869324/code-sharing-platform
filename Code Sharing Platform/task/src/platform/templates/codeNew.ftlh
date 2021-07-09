<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Create</title>
</head>
<body>
<form method="POST">
    <textarea id="code_snippet" placeholder="Type your code here"></textarea>
    <button id="send_snippet" type="submit" onclick="send()">Submit</button>
</form>
<script>function send() {
    let object = {
        "code": document.getElementById("code_snippet").value
    };
    
    let json = JSON.stringify(object);
    
    let xhr = new XMLHttpRequest();
    xhr.open("POST", '/api/code/new', false)
    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    xhr.send(json);
    
    if (xhr.status == 200) {
      alert("Success!");
    }
</script>
</body>
</html>