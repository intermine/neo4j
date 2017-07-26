MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:gene]-(gene_alleles :Allele),
(gene_alleles)-[:phenotypeAnnotations]-(gene_alleles_phenotypeannotations :PhenotypeAnnotation)

WHERE gene.symbol =~ 'ad*'
RETURN gene.symbol,
gene_organism.name,
gene_alleles.symbol,
gene_alleles_phenotypeannotations.annotationType,
gene_alleles_phenotypeannotations.description

