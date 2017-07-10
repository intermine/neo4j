MATCH (rnaseqresult :RNASeqResult),
(rnaseqresult)-[]-(rnaseqresult_expressionlevel :expressionLevel),
(rnaseqresult)-[]-(rnaseqresult_stage :stage),
(rnaseqresult)-[]-(rnaseqresult_gene :gene),
(rnaseqresult)-[]-(rnaseqresult_expressionscore :expressionScore)

WHERE rnaseqresult.stage = 'embryo 02-04hr' AND rnaseqresult.expressionLevel = 'High expression' AND rnaseqresult.expressionScore >= 4001
RETURN rnaseqresult_gene.primaryIdentifier,
rnaseqresult_gene.symbol
ORDER BY rnaseqresult.stage ASC
