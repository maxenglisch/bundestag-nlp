<!DOCTYPE html>
<html lang="de">

<head>
    <meta charset="UTF-8">
    <title>Übersicht der Abgeordneten und Redner nach Fraktion</title>
</head>

<#include "header.ftl">


<body>
<h1>Übersicht der Abgeordneten und Redner nach Fraktion</h1>

<!-- Hier noch schön die Suche mit knopf und so -->
<form action="/abgeordnete/suche" method="get">
    <label for="suche">Abgeordneten suchen:</label>
    <input type="text" id="suche" name="q" placeholder="Name, Fraktion, Beruf, GebOrt etc.">
    <button type="submit">Suchen</button>
</form>



<#list fraktionen as fraktion>
    <div>
        <h2>Fraktion: ${fraktion.frakName}</h2>
        <ul>
            <#list redner as redner>
                <#if redner.fraktion == fraktion.frakName>
                    <li>
                        <a href="/abgeordnete/${redner.rednerID}">
                            ${redner.vorname} ${redner.nachname}
                        </a>
                    </li>
                </#if>
            </#list>
        </ul>
    </div>
</#list>
</body>
</html>
