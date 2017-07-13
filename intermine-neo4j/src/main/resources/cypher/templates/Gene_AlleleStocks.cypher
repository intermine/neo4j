MATCH (gene :Gene),
(gene)-[:alleles]-(gene_alleles :Allele),
(gene_alleles)-[:phenotypeAnnotations]-(gene_alleles_phenotypeannotations :PhenotypeAnnotation),
(gene_alleles)-[:stocks]-(gene_alleles_stocks :Stock)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='eve') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_alleles_stocks.stockCenter = 'Bloomington Drosophila Stock Center' AND ANY (key in keys(gene_alleles) WHERE gene_alleles[key]='eve[1] ')
RETURN gene.symbol,
gene.primaryIdentifier,
gene_alleles.primaryIdentifier,
gene_alleles.symbol,
gene_alleles.alleleClass,
gene_alleles_phenotypeannotations.description,
gene_alleles_phenotypeannotations.annotationType,
gene_alleles_stocks.primaryIdentifier,
gene_alleles_stocks.stockCenter,
gene_alleles_stocks.type
ORDER BY gene.symbol ASC
