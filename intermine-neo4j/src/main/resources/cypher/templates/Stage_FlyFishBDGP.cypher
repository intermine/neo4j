MATCH (gene :Gene),
(gene)-[]-(gene_mrnaexpressionresults :mRNAExpressionResults),
(gene_mrnaexpressionresults)-[]-(gene_mrnaexpressionresults_stages :stages),
(gene_mrnaexpressionresults)-[]-(gene_mrnaexpressionresults_dataset :dataSet)

WHERE gene_mrnaexpressionresults_stages.name = 'embryonic stage 7'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_mrnaexpressionresults.expressed,
gene_mrnaexpressionresults.stageRange,
gene_mrnaexpressionresults_dataset.name

