MATCH (gene :Gene),
(gene)-[]-(gene_mrnaexpressionresults :mRNAExpressionResults),
(gene_mrnaexpressionresults)-[]-(gene_mrnaexpressionresults_mrnaexpressionterms :mRNAExpressionTerms),
(gene_mrnaexpressionresults)-[]-(gene_mrnaexpressionresults_dataset :dataSet),
(gene)-[]-(gene_organism :organism)

WHERE ANY (key in keys(gene) WHERE gene[key]='armadillo') AND gene_organism.name = 'Drosophila melanogaster' AND gene_mrnaexpressionresults_dataset.name = 'fly-Fish data set'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_mrnaexpressionresults.stageRange,
gene_mrnaexpressionresults.expressed,
gene_mrnaexpressionresults_mrnaexpressionterms.name,
gene_mrnaexpressionresults_dataset.name
ORDER BY gene.secondaryIdentifier ASC
