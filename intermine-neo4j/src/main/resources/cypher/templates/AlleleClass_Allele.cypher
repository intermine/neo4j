MATCH (gene :Gene),
(gene)-[:alleles]-(gene_alleles :Allele),
(gene_alleles)-[:PART_OF]-(gene_alleles_organism :Organism)

WHERE gene_alleles_organism.name = 'Drosophila melanogaster' AND gene_alleles.alleleClass = '*hypomorph*'
RETURN gene_alleles.primaryIdentifier,
gene_alleles.secondaryIdentifier,
gene_alleles.alleleClass,
gene.symbol
ORDER BY gene_alleles.secondaryIdentifier DESC
