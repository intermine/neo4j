MATCH (gene :Gene),
(gene)-[:proteins]-(gene_proteins :Protein)

WHERE ANY (key in keys(gene) WHERE gene[key]='CG1046')
RETURN gene.primaryIdentifier,
gene.symbol,
gene_proteins.primaryIdentifier,
gene_proteins.primaryAccession
ORDER BY gene.primaryIdentifier ASC
