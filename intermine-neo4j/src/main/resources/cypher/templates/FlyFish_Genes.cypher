MATCH (gene :Gene),
(gene)-[:mRNAExpressionResults]-(gene_mrnaexpressionresults :MRNAExpressionResult),
(gene_mrnaexpressionresults)-[:mRNAExpressionTerms]-(gene_mrnaexpressionresults_mrnaexpressionterms :OntologyTerm),
(gene_mrnaexpressionresults)-[:gene]-(gene_mrnaexpressionresults_gene :Gene),
(gene_mrnaexpressionresults)-[:dataSet]-(gene_mrnaexpressionresults_dataset :DataSet)

WHERE gene_mrnaexpressionresults_dataset.name = 'fly-Fish data set' AND gene_mrnaexpressionresults.stageRange = 'stage 4-5 (fly-FISH)' AND gene_mrnaexpressionresults.expressed = 'true' AND gene_mrnaexpressionresults_mrnaexpressionterms.name = '*blastoderm nuclei*'
RETURN gene_mrnaexpressionresults.stageRange,
gene_mrnaexpressionresults_mrnaexpressionterms.name,
gene_mrnaexpressionresults_gene.primaryIdentifier,
gene_mrnaexpressionresults_gene.secondaryIdentifier,
gene_mrnaexpressionresults_gene.symbol,
gene_mrnaexpressionresults.expressed
ORDER BY gene_mrnaexpressionresults.stageRange ASC
