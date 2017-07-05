MATCH (gene :Gene),
(gene)-[]-(gene_mrnaexpressionresults :mRNAExpressionResults),
(gene_mrnaexpressionresults)-[]-(gene_mrnaexpressionresults_stagerange :stageRange),
(gene_mrnaexpressionresults)-[]-(gene_mrnaexpressionresults_mrnaexpressionterms :mRNAExpressionTerms),
(gene_mrnaexpressionresults)-[]-(gene_mrnaexpressionresults_expressed :expressed),
(gene_mrnaexpressionresults)-[]-(gene_mrnaexpressionresults_dataset :dataSet),
(gene_mrnaexpressionresults_dataset)-[]-(gene_mrnaexpressionresults_dataset_datasource :dataSource),
(gene)-[]-(gene_organism :organism)

WHERE ANY (key in keys(gene) WHERE gene[key]='runt') AND gene_organism.name = 'Drosophila melanogaster' AND gene_mrnaexpressionresults_dataset.name = 'BDGP in situ data set'
RETURN gene.name,
gene_mrnaexpressionresults_mrnaexpressionterms.name,
gene_mrnaexpressionresults_dataset_datasource.name
ORDER BY gene.name ASC
