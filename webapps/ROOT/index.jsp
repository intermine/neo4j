<!DOCTYPE html>
<html>
    
    <head>
        <title>NCGR Neo4j PathQuery Endpoint Development</title>
        <link rel="stylesheet" type="text/css" href="stylesheet.css" title="Style"/>
    </head>

    <body>

        <h1>Welcome to the NCGR Neo4j PathQuery service.</h1>

        <p>
            The concept here is to mimic an InterMine web service by reverse engineering PathQuery XML and querying either a Neo4j database with Cypher (for most bio objects) or perhaps
            the InterMine web service with the PathQuery for stuff that isn't in the Neo4j database.
        </p>

        <p>
            The syntax is identical to that used by InterMine. Simply aim your HTTP request at <b><%=request.getRequestURI()%></b> instead of /beanmine/.
        </p>

        <p>
            NOTE: the PathQuery&rightarrow;Cypher translation is under <em>very early development</em>. Many PathQueries will fail, especially those with JOINs.
        </p>

        <p>
            For your convenience, here's a place where you can paste PathQuery XML and test this out, or submit one of the sample queries provided below.
        </p>
        <form action="service/query/results" method="POST">
            <input type="hidden" name="format" value="tab"/>
            <table>
                <tr>
                    <td><textarea name="query"></textarea></td>
                    <td><input type="submit" value="SEND"/></td>
                </tr>
            </table>
        </form>

        <h2>Genes associated with a particular GO term</h2>
        <textarea class="sample"><query name="" model="genomic" view="Gene.id Gene.primaryIdentifier Gene.goAnnotation.ontologyTerm.identifier Gene.goAnnotation.ontologyTerm.name" longDescription="" sortOrder="Gene.id asc"><constraint path="Gene.goAnnotation.ontologyTerm.identifier" op="=" value="GO:0008270"/></query></textarea>

        <h2>Homologues of a given gene (via GeneFamily)</h2>
        <textarea class="sample"><query name="" model="genomic" view="Gene.primaryIdentifier Gene.geneFamily.primaryIdentifier Gene.geneFamily.genes.primaryIdentifier" longDescription="" sortOrder="Gene.primaryIdentifier asc"><constraint path="Gene.primaryIdentifier" op="=" value="Phvul.008G137900"/></query></textarea>

        <h2>A particular QTL and its associated genetic markers</h2>
        <textarea class="sample"><query name="" model="genomic" view="QTL.primaryIdentifier QTL.associatedGeneticMarkers.primaryIdentifier" longDescription="" sortOrder="QTL.primaryIdentifier asc"><constraint path="QTL.primaryIdentifier" op="=" value="Seed weight 3-2"/></query></textarea>

        <h2>All gene locations on chromosomes</h2>
        <textarea class="sample"><query name="" model="genomic" view="Gene.primaryIdentifier Gene.chromosomeLocation.start Gene.chromosomeLocation.end Gene.chromosomeLocation.strand Gene.chromosome.primaryIdentifier " longDescription="" sortOrder="Gene.primaryIdentifier asc"></query></textarea>

        <h2>Genes with mRNA expression > 10000 NOT CURRENTLY WORKING</h2>
        <textarea class="sample"><query name="" model="genomic" view="Gene.primaryIdentifier Gene.transcripts.expressionValues.value Gene.transcripts.expressionValues.expressionSample.primaryIdentifier" longDescription="" sortOrder="Gene.primaryIdentifier asc"><constraint path="Gene.transcripts" type="MRNA"/><constraint path="Gene.transcripts.expressionValues.value" op="&gt;" value="10000"/></query></textarea>


    </body>

</html>

