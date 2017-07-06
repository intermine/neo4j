MATCH (gene :Gene),
(gene)-[]-(gene_mrnaexpressionresults :mRNAExpressionResults),
(gene_mrnaexpressionresults)-[]-(gene_mrnaexpressionresults_stagerange :stageRange),
(gene_mrnaexpressionresults)-[]-(gene_mrnaexpressionresults_mrnaexpressionterms :mRNAExpressionTerms),
(gene_mrnaexpressionresults)-[]-(gene_mrnaexpressionresults_gene :gene),
(gene_mrnaexpressionresults)-[]-(gene_mrnaexpressionresults_expressed :expressed),
(gene_mrnaexpressionresults)-[]-(gene_mrnaexpressionresults_dataset :dataSet)

WHERE gene_mrnaexpressionresults_dataset.name = 'fly-Fish data set' AND gene_mrnaexpressionresults.stageRange = 'stage 4-5 (fly-FISH)' AND gene_mrnaexpressionresults.expressed = 'true' AND gene_mrnaexpressionresults_mrnaexpressionterms.name = '*blastoderm nuclei*'
RETURN gene_mrnaexpressionresults_mrnaexpressionterms.name,
gene_mrnaexpressionresults_gene.primaryIdentifier,
gene_mrnaexpressionresults_gene.secondaryIdentifier,
gene_mrnaexpressionresults_gene.symbol
ORDER BY gene_mrnaexpressionresults.stageRange ASC
