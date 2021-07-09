<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Latest</title>
</head>
<body>
    <#list latest>
    <ul>
    <#items as record>
    <li>
       <span id="load_date">${record.date}</span>
       <pre id="code_snippet">${record.code}</pre>
    </li>
    </#items>
    </ul>
    </#list>
</body>
</html>