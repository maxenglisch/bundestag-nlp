<!DOCTYPE html>
<html lang="de">


<head>
    <meta charset="UTF-8">
    <title>Änderungshistorie</title>
</head>

<#include "header.ftl">



<body>
<header>
    <h1>Änderungshistorie</h1>
</header>

<section>
    <h2>Alle Änderungen</h2>

    <ul>
        <#list logs as log>

            <li>
                <strong>Zeit:</strong> ${log.zeitstempel} <br>
                <strong>Typ:</strong> ${log.typ} <br>
                <strong>Beschreibung:</strong> ${log.beschreibung} <br>
                <strong>Betroffener:</strong> ${log.betroffenerID}
            </li>

        </#list>
    </ul>
</section>
</body>
</html>
