MATCH (gene :Gene),
(gene)-[:mRNAExpressionResults]-(gene_mrnaexpressionresults :MRNAExpressionResult),
(gene_mrnaexpressionresults)-[:mRNAExpressionTerms]-(gene_mrnaexpressionresults_mrnaexpressionterms :OntologyTerm),
(gene_mrnaexpressionresults)-[:dataSet]-(gene_mrnaexpressionresults_dataset :DataSet),
(gene_mrnaexpressionresults_dataset)-[:dataSource]-(gene_mrnaexpressionresults_dataset_datasource :DataSource),
(gene)-[:PART_OF]-(gene_organism :Organism)

WHERE ANY (key in keys(gene) WHERE gene[key]='runt') AND gene_organism.name = 'Drosophila melanogaster' AND gene_mrnaexpressionresults_dataset.name = 'BDGP in situ data set'
RETURN gene.name,
gene_mrnaexpressionresults.stageRange,
gene_mrnaexpressionresults.expressed,
gene_mrnaexpressionresults_mrnaexpressionterms.name,
gene_mrnaexpressionresults_dataset_datasource.name
ORDER BY gene.name ASC
