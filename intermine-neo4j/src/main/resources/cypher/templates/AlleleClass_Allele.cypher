MATCH (gene :Gene),
(gene)-[]-(gene_alleles :alleles),
(gene_alleles)-[]-(gene_alleles_organism :organism)

WHERE gene_alleles_organism.name = 'Drosophila melanogaster' AND gene_alleles.alleleClass = '*hypomorph*'
RETURN gene_alleles.primaryIdentifier,
gene_alleles.secondaryIdentifier,
gene_alleles.alleleClass,
gene.symbol
ORDER BY gene_alleles.secondaryIdentifier DESC
