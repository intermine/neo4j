CREATE (n :Gene :SequenceFeature {primaryIdentifier: "ABC"}),
       (n)-[:ANNOTATED_BY]->(m:GOAnnotation {annotationExtension: "DEF"}),
       (m)-[:EVIDENCED_BY {dataset:"New Dataset"}]->(e:GOEvidence {code: "GHI"}),
       (n)-[:PARTICIPATES_IN]->(p:Pathway {shortName: "JKL"})
;