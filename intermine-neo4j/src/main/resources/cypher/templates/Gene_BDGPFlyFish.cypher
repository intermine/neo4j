MATCH (gene :Gene),
(gene)-[]-(gene_mrnaexpressionresults :mRNAExpressionResults),
(gene_mrnaexpressionresults)-[]-(gene_mrnaexpressionresults_mrnaexpressionterms :mRNAExpressionTerms),
(gene_mrnaexpressionresults)-[]-(gene_mrnaexpressionresults_dataset :dataSet),
(gene_mrnaexpressionresults_dataset)-[]-(gene_mrnaexpressionresults_dataset_datasource :dataSource),
(gene)-[]-(gene_organism :organism)

WHERE ANY (key in keys(gene) WHERE gene[key]='hunchback') AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_mrnaexpressionresults.stageRange,
gene_mrnaexpressionresults_mrnaexpressionterms.name,
gene_mrnaexpressionresults_dataset_datasource.name
ORDER BY gene.secondaryIdentifier ASC
