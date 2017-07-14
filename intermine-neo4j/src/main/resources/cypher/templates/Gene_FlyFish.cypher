MATCH (gene :Gene),
(gene)-[:mRNAExpressionResults]-(gene_mrnaexpressionresults :MRNAExpressionResult),
(gene_mrnaexpressionresults)-[:mRNAExpressionTerms]-(gene_mrnaexpressionresults_mrnaexpressionterms :OntologyTerm),
(gene_mrnaexpressionresults)-[:dataSet]-(gene_mrnaexpressionresults_dataset :DataSet),
(gene)-[:PART_OF]-(gene_organism :Organism)

WHERE ANY (key in keys(gene) WHERE gene[key]='armadillo') AND gene_organism.name = 'Drosophila melanogaster' AND gene_mrnaexpressionresults_dataset.name = 'fly-Fish data set'
RETURN gene.primaryIdentifier,
gene.symbol,
gene_mrnaexpressionresults.stageRange,
gene_mrnaexpressionresults.expressed,
gene_mrnaexpressionresults_mrnaexpressionterms.name,
gene_mrnaexpressionresults_dataset.name
ORDER BY gene.primaryIdentifier ASC
