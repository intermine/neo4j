MATCH (gene :Gene),
(gene)-[:gene]-(gene_alleles :Allele),
(gene_alleles)-[:PART_OF]-(gene_alleles_organism :Organism),
(gene_alleles)-[:phenotypeAnnotations]-(gene_alleles_phenotypeannotations :PhenotypeAnnotation)
WHERE ANY (key in keys(gene) WHERE gene[key]='Ubx')
RETURN gene.primaryIdentifier,
gene_alleles.primaryIdentifier,
gene_alleles.secondaryIdentifier,
gene_alleles.alleleClass,
gene_alleles_phenotypeannotations.annotationType,
gene_alleles_phenotypeannotations.description,
gene_alleles_organism.name
ORDER BY gene.primaryIdentifier ASC
