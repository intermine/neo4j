MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_alleles :alleles),
(gene_alleles)-[]-(gene_alleles_phenotypeannotations :phenotypeAnnotations)

WHERE gene.symbol =~ 'ad*'
RETURN gene.symbol,
gene_organism.name,
gene_alleles.symbol,
gene_alleles_phenotypeannotations.annotationType,
gene_alleles_phenotypeannotations.description

