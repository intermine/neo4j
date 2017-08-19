MATCH (gene :Gene),
(gene)-[gene_chromosomelocation:LOCATED_ON]-(gene_chromosomelocation_locatedon :BioEntity)
RETURN gene.primaryIdentifier,
gene_chromosomelocation.end,
gene_chromosomelocation_locatedon.primaryIdentifier,
gene_chromosomelocation.start

