<!DOCTYPE html>
<html lang="de">

<head>
    <meta charset="UTF-8">
    <title>${abgeordneter.vorname} ${abgeordneter.nachname} - Übersicht</title>
    <!-- jQuery als javascript lib wie in aufgabe verlangt  -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

</head>

<#include "header.ftl">


<body>
<header>
    <h1>${abgeordneter.vorname} ${abgeordneter.nachname}</h1>
</header>

<!-- erstmal alles zum abgeordneten (eigentlich wie meine toHTML vom abgeordneten) -->
<section>
    <h2>Persönliche Informationen</h2>
    <img id="abgeordneten-bild" src="${abgeordneter.bildURL}" alt="Bild von ${abgeordneter.vorname}" style="max-width: 200px; height: auto;">
    <h3>Bild ändern</h3>
    <br>
    <label for="image-url"></label>
    <input type="text" id="image-url" placeholder="Neuen Bild-Link eingeben">
    <button id="update-image">Bild aktualisieren</button>


    <p><strong>ID:</strong> ${abgeordneter.abgeordnetenID}</p>
    <p><strong>Partei:</strong> ${abgeordneter.fraktion}</p>
    <p><strong>Wahlkreis:</strong> ${abgeordneter.wkrLand}</p>
    <p><strong>Mandats-Art:</strong> ${abgeordneter.mandatsArt}</p>
    <p><strong>Geschlecht:</strong> ${abgeordneter.geschlecht}</p>
    <p><strong>Anrede:</strong> ${abgeordneter.anrede}</p>
    <p><strong>Akademischer Titel:</strong> ${abgeordneter.akademTitel}</p>

    <h3>Funktionen</h3>
    <ul>
        <!-- hier überprüfen obs das gibt, sonst error bei gastrendern -->
        <#if abgeordneter.funktionen??>
            <#list abgeordneter.funktionen as funktion>
                <li>${funktion}</li>
            </#list>
        <#else>
            <li>Keine Funktionen verfügbar.</li>
        </#if>
    </ul>

    <h3>Weitere Details</h3>
        <p><strong>Geburtsdatum: </strong> ${abgeordneter.gebDatum}</p>
        <p><strong>Geburtsort: </strong> ${abgeordneter.gebOrt}</p>
        <p><strong>Familienstand: </strong> ${abgeordneter.familienstand}</p>
        <p><strong>Religion: </strong> ${abgeordneter.religion}</p>
        <p><strong>Beruf: </strong> ${abgeordneter.beruf}</p>


</section>

<!-- hier alle reden mit links (wieder vom toHTml von reden) -->
<section>
    <h2>Reden von ${abgeordneter.vorname} ${abgeordneter.nachname}</h2>
    <ul>
        <#list reden as rede>
            <li>
                <a href="#rede${rede.redID}">Rede ${rede.redID}</a>
            </li>
        </#list>
    </ul>
</section>

<!-- hier jetzt die reden anzeigen mit abschnitten, kommis und so -->
<section>
    <#list reden as rede>
        <article id="rede${rede.redID}">
            <h3>Rede ID: ${rede.redID}</h3>
            <p><strong>Top ID:</strong> ${rede.topID}</p>
            <p><strong>Inhalt:</strong></p>
            <#list rede.abschnitte as abschnitt>

                <#if abschnitt.isKommentar()>

                    <div>
                        <!-- erstmal kommi mit ID anzeigen -->
                        <p><strong>Kommentar-ID:</strong> ${abschnitt.kommentarID}</p>
                        <p><strong>Inhalt:</strong> ${abschnitt.inhalt}</p>

                        <!-- hier jetzt die zuweisungen per kommentar anzeigen -->
                        <!-- kommi mit getKommentar aus dem redeabschnitt holen immer, und dann zuweisung anzeigen falls existent -->
                        <#assign kommentar = abschnitt.getKommentar(factory.getDbHandler())>


                        <p><strong>Zugewiesen an Abgeordnete:</strong></p>
                        <ul>
                            <#if kommentar.abgeordnetenIDs?size==0>
                                <li>Keine Abgeordneten zugewiesen</li>


                            <#else>
                                <#list kommentar.abgeordnetenIDs as id>
                                    <li>
                                        ${id}
                                    </li>
                                </#list>
                            </#if>
                        </ul>

                        <p><strong>Zugewiesen an Fraktionen:</strong></p>
                        <ul>

                            <#if kommentar.fraktionen?size==0>
                                <li>Keine Fraktionen zugewiesen</li>

                            <#else>
                                <#list kommentar.fraktionen as fraktion>
                                    <li>
                                        ${fraktion}
                                    </li>
                                </#list>

                            </#if>
                        </ul>


                        <!-- hier dann zuweisungs formular erstmal vorauswahl mit frak oder abge (dass nicht alles auf einmal geladen wird) -->
                        <form action="/kommentar/${abschnitt.kommentarID}/zuweisen" method="POST">

                            <!-- hier hidden abgeordneten ID für redirect später -->
                            <input type="hidden" name="abgeordnetenID" value="${abgeordneter.abgeordnetenID}">

                            <label for="zuweisungsart-${abschnitt.kommentarID}">Zuweisen an:</label>
                            <select name="zuweisungsart" id="zuweisungsart-${abschnitt.kommentarID}">
                                <option value="abgeordneter">Abgeordneter</option>
                                <option value="fraktion">Fraktion</option>
                            </select>
                            <br>

                            <!-- alle abges in select  -->
                            <div>
                                <label for="abgeordnetenID-${abschnitt.kommentarID}">Abgeordneter wählen:</label>
                                <select name="abgeordnetenID" id="abgeordnetenID-${abschnitt.kommentarID}">
                                    <#list abgeordnete as abgeordneter>
                                        <option value="${abgeordneter.abgeordnetenID}">
                                            ${abgeordneter.vorname} ${abgeordneter.nachname} (ID: ${abgeordneter.abgeordnetenID})
                                        </option>
                                    </#list>
                                </select>
                            </div>

                            <!-- alles fraks ins selects -->
                            <div>
                                <label for="fraktion-${abschnitt.kommentarID}">Fraktion wählen:</label>
                                <select name="fraktion" id="fraktion-${abschnitt.kommentarID}">
                                    <#list fraktionen as fraktion>
                                        <option value="${fraktion.frakName}">
                                            ${fraktion.frakName}
                                        </option>
                                    </#list>
                                </select>
                            </div>

                            <br>
                            <button type="submit">Zuweisen</button>
                        </form>
                    </div>


                <#else>
                    <p>${abschnitt.inhalt}</p>
                </#if>
            </#list>
        </article>
    </#list>
</section>



<script>

    // erst machen wenn dokument komplett geladen ist (dom)
    $(document).ready(function() {


        // erstmal bild aktualisieren mit event listener auf click
        $("#update-image").click(function(){

            // hier neue url aus der eingabe mit .val()
            const updatedBildURL = $("#image-url").val();

            // mit ajax das bild dynamisch ändern (ohne reload)
            $.ajax({
                // route für aktualisiern (im resthandler)
                url: "/abgeordneter/${id}/update-bild",
                // PUT als type für REST (auch route im handler)
                type: "PUT",
                contentType: "application/json",
                // url in json dann klapps besser
                data: JSON.stringify({ bildURL: updatedBildURL }),

                // wenn geklappt dann bild anzeigen und nachricht ausgeben
                success: function() {
                    $("#abgeordneten-bild").attr("src", updatedBildURL);
                    alert("Bild url wurde aktualisiert");
                },
                // bei fehler nur meldung ausgeben
                error: function() {
                    alert("Fehler beim aktualisieren der url");
                }
            });})
    });



</script>



</body>


</html>
