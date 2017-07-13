MATCH (rnaseqresult :RNASeqResult),
(rnaseqresult)-[:gene]-(rnaseqresult_gene :Gene)

WHERE rnaseqresult.stage = 'embryo 02-04hr' AND rnaseqresult.expressionLevel = 'High expression' AND rnaseqresult.expressionScore >= 4001
RETURN rnaseqresult.stage,
rnaseqresult.expressionLevel,
rnaseqresult.expressionScore,
rnaseqresult_gene.primaryIdentifier,
rnaseqresult_gene.symbol
ORDER BY rnaseqresult.stage ASC
