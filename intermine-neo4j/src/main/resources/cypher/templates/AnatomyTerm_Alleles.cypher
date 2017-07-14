MATCH (gene :Gene),
(gene)-[:alleles]-(gene_alleles :Allele),
(gene_alleles)-[:PART_OF]-(gene_alleles_organism :Organism),
(gene_alleles)-[:phenotypeAnnotations]-(gene_alleles_phenotypeannotations :PhenotypeAnnotation)

WHERE gene_alleles_organism.name = 'Drosophila melanogaster' AND gene_alleles.alleleClass =~ '*loss of function*' AND gene_alleles_phenotypeannotations.description = '*eye disc*'
RETURN gene.primaryIdentifier,
gene.symbol,
gene_alleles.alleleClass,
gene_alleles.primaryIdentifier,
gene_alleles.secondaryIdentifier,
gene_alleles_phenotypeannotations.description
ORDER BY gene.primaryIdentifier ASC
