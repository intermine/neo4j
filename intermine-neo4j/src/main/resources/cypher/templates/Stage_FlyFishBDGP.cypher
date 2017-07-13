MATCH (gene :Gene),
(gene)-[:mRNAExpressionResults]-(gene_mrnaexpressionresults :MRNAExpressionResult),
(gene_mrnaexpressionresults)-[:stages]-(gene_mrnaexpressionresults_stages :DevelopmentTerm),
(gene_mrnaexpressionresults)-[:dataSet]-(gene_mrnaexpressionresults_dataset :DataSet)

WHERE gene_mrnaexpressionresults_stages.name = 'embryonic stage 7'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_mrnaexpressionresults.expressed,
gene_mrnaexpressionresults.stageRange,
gene_mrnaexpressionresults_dataset.name

