MATCH (gene :Gene),
(gene)-[]-(gene_rnaseqresults :rnaSeqResults)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='eve') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_rnaseqresults.expressionLevel = 'High expression' AND gene_rnaseqresults.stage = 'embryo 02-04hr' AND gene_rnaseqresults.expressionScore >= 4001
RETURN gene.primaryIdentifier,
gene.symbol,
gene_rnaseqresults.stage,
gene_rnaseqresults.expressionScore,
gene_rnaseqresults.expressionLevel
ORDER BY gene_rnaseqresults.stage ASC
