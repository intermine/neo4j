MATCH (gene :Gene),
(gene)-[]-(gene_rnaseqresults :rnaSeqResults),
(gene_rnaseqresults)-[]-(gene_rnaseqresults_expressionlevel :expressionLevel),
(gene_rnaseqresults)-[]-(gene_rnaseqresults_stage :stage),
(gene_rnaseqresults)-[]-(gene_rnaseqresults_expressionscore :expressionScore)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='eve') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_rnaseqresults.expressionLevel = 'High expression' AND gene_rnaseqresults.stage = 'embryo 02-04hr' AND gene_rnaseqresults.expressionScore >= 4001
RETURN gene.primaryIdentifier,
gene.symbol
ORDER BY gene_rnaseqresults.stage ASC
