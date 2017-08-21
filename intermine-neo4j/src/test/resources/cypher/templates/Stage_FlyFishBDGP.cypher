MATCH (gene :Gene),
(gene)-[:gene]-(gene_mrnaexpressionresults :MRNAExpressionResult),
(gene_mrnaexpressionresults)-[:stages]-(gene_mrnaexpressionresults_stages :DevelopmentTerm)
WHERE gene_mrnaexpressionresults_stages.name = 'Stage 8'
RETURN gene.primaryIdentifier,
gene.secondaryIdentifier,
gene.symbol,
gene_mrnaexpressionresults.expressed,
gene_mrnaexpressionresults.stageRange
ORDER BY gene.primaryIdentifier ASC
