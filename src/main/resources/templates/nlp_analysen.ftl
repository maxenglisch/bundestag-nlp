<!DOCTYPE html>
<html lang="de">

<head>
    <meta charset="UTF-8">
    <title>NLP Analysen</title>
    <script src="https://d3js.org/d3.v7.min.js"></script>
    <!-- bisschen Style für die sentences muss sein -->
    <style>
        .sentence {
            padding: 10px;
            margin: 5px 0;
            border-radius: 5px;
            color: black;
        }
    </style>
</head>

<body>

<section>
    <h2>Rede-Analysen</h2>
    <#list analysen as analyse>
        <article>
            <h3>Rede-ID: ${analyse.id}</h3>



            <!-- Video anzeigen lassen (über andere REST ROUTE) -->
            <video id="video-${analyse.id}" controls width="400">
                <source src="${analyse.videoPath}" type="video/mp4">
                Ihr Browser unterstützt dieses Videoformat nicht.
            </video>



            <h4>Sätze und Sentiments</h4>
            <div id="sentences-${analyse.id}">
                <#list analyse.sentences as sentence>
                    <div class="sentence" style="background-color: ${sentence.color};">
                        ${sentence.text} (Sentiment: ${sentence.sentiment})
                    </div>
                </#list>
            </div>

            <!-- hier das barchart unten generieren lassen -->
            <h4>POS-Typen</h4>
            <div id="pos-chart-${analyse.id}"></div>

            <!-- hier bubble chart machen lassen (keine lust mehr) -->
            <h4>Named Entities</h4>
            <div id="entities-chart-${analyse.id}"></div>
        </article>
    </#list>
</section>






<script>



    <#list analysen as analyse>


    // inspiriert von d3.js Seite: https://observablehq.com/@d3/bubble-chart/2 (und auch bar Chart)


    // pos daten aus übergabe map ziehen, nur keys erstmal
    const posData${analyse.id} = [
        <#list analyse.posCounts?keys as type>
        {type: "${type}", count: ${analyse.posCounts[type]}}<#if !type?is_last>,</#if>
        </#list>
    ];

    // höhe, breite etc. definieren
    const width${analyse.id} = 500;
    const height${analyse.id} = 300;
    const margin${analyse.id} = {top: 20, right: 20, bottom: 40, left: 50};

    // scales machen
    const x${analyse.id} = d3.scaleBand()
        .domain(posData${analyse.id}.map(d => d.type))
        .range([margin${analyse.id}.left, width${analyse.id} - margin${analyse.id}.right])
        .padding(0.1);

    const y${analyse.id} = d3.scaleLinear()
        .domain([0, d3.max(posData${analyse.id}, d => d.count)])
        .range([height${analyse.id} - margin${analyse.id}.bottom, margin${analyse.id}.top]);

    // vector graphics ding nehmen
    const svg${analyse.id} = d3.select("#pos-chart-${analyse.id}")
        .append("svg")
        .attr("width", width${analyse.id})
        .attr("height", height${analyse.id});

    // balken
    svg${analyse.id}.selectAll("rect")
        .data(posData${analyse.id})
        .enter()
        .append("rect")
        .attr("x", d => x${analyse.id}(d.type))
        .attr("y", d => y${analyse.id}(d.count))
        .attr("width", x${analyse.id}.bandwidth())
        .attr("height", d => y${analyse.id}(0) - y${analyse.id}(d.count))
        .attr("fill", "steelblue");

    // x achse
    svg${analyse.id}.append("g")
        .attr("transform", "translate(0," + (height${analyse.id} - margin${analyse.id}.bottom) + ")")
        .call(d3.axisBottom(x${analyse.id}))
        .selectAll("text")
        .attr("transform", "rotate(-45)")
        .attr("text-anchor", "end");

    // y achse
    svg${analyse.id}.append("g")
        .attr("transform", "translate(" + margin${analyse.id}.left + ",0)")
        .call(d3.axisLeft(y${analyse.id}));




    </#list>



</script>






</body>
</html>
