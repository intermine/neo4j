MATCH (gene :Gene),
(gene)-[:alleles]-(gene_alleles :Allele),
(gene_alleles)-[:PART_OF]-(gene_alleles_organism :Organism),
(gene_alleles)-[:phenotypeAnnotations]-(gene_alleles_phenotypeannotations :PhenotypeAnnotation)

WHERE ANY (key in keys(gene) WHERE gene[key]='Ubx')
RETURN gene.secondaryIdentifier,
gene_alleles.primaryIdentifier,
gene_alleles.secondaryIdentifier,
gene_alleles.alleleClass,
gene_alleles_phenotypeannotations.annotationType,
gene_alleles_phenotypeannotations.description,
gene_alleles_organism.name
ORDER BY gene.secondaryIdentifier ASC
