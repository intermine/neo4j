MATCH (gene :Gene),
(gene)-[]-(gene_alleles :alleles),
(gene_alleles)-[]-(gene_alleles_organism :organism),
(gene_alleles)-[]-(gene_alleles_phenotypeannotations :phenotypeAnnotations)

WHERE gene_alleles_organism.name = 'Drosophila melanogaster' AND gene_alleles.alleleClass =~ '*loss of function*' AND gene_alleles_phenotypeannotations.description = '*eye disc*'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_alleles.alleleClass,
gene_alleles.primaryIdentifier,
gene_alleles.secondaryIdentifier,
gene_alleles_phenotypeannotations.description
ORDER BY gene.secondaryIdentifier ASC
