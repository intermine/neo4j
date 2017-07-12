MATCH (gene :Gene),
(gene)-[]-(gene_alleles :alleles),
(gene_alleles)-[]-(gene_alleles_organism :organism),
(gene_alleles)-[]-(gene_alleles_phenotypeannotations :phenotypeAnnotations)

WHERE ANY (key in keys(gene) WHERE gene[key]='Ubx')
RETURN gene.secondaryIdentifier,
gene_alleles.primaryIdentifier,
gene_alleles.secondaryIdentifier,
gene_alleles.alleleClass,
gene_alleles_phenotypeannotations.annotationType,
gene_alleles_phenotypeannotations.description,
gene_alleles_organism.name
ORDER BY gene.secondaryIdentifier ASC
