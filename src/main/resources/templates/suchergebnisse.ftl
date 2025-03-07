<!DOCTYPE html>
<html lang="de">


<head>
    <meta charset="UTF-8">
    <title>Suchergebnisse für ${query} </title>
</head>

<#include "header.ftl">


<body>

<h1> Suchergebnisse für ${query} </h1>

<#if abgeordnete?? >

    <ul>
        <#list abgeordnete as abgeordneter>

            <li>
                <strong>${abgeordneter.vorname} ${abgeordneter.nachname}</strong>
                <p>Fraktion: ${abgeordneter.fraktion}</p>
                <a href="/abgeordnete/${abgeordneter.abgeordnetenID}">Zum Abgeordneten</a>
            </li>

        </#list>
    </ul>


<#else>
    <p>Keine Ergebnisse gefunden.</p>
</#if>

<a href="/index">Zurück zur Übersicht</a>

</body>
</html>
