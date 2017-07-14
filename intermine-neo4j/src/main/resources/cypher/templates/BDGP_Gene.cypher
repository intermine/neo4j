MATCH (gene :Gene),
(gene)-[:mRNAExpressionResults]-(gene_mrnaexpressionresults :MRNAExpressionResult),
(gene_mrnaexpressionresults)-[:mRNAExpressionTerms]-(gene_mrnaexpressionresults_mrnaexpressionterms :OntologyTerm),
(gene_mrnaexpressionresults)-[:dataSet]-(gene_mrnaexpressionresults_dataset :DataSet)

WHERE gene_mrnaexpressionresults.expressed = 'true' AND gene_mrnaexpressionresults_mrnaexpressionterms.name = '*neuron*' AND gene_mrnaexpressionresults_dataset.name = 'BDGP in situ data set' AND gene_mrnaexpressionresults.stageRange = 'stage 11-12 (BDGP in situ)'
RETURN gene_mrnaexpressionresults_mrnaexpressionterms.name,
gene.secondaryIdentifier,
gene.symbol,
gene_mrnaexpressionresults.stageRange,
gene_mrnaexpressionresults.expressed,
gene_mrnaexpressionresults_dataset.name
ORDER BY gene_mrnaexpressionresults_mrnaexpressionterms.name ASC
