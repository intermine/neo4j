MATCH (gene :Gene),
(gene)-[:proteins]-(gene_proteins :Protein)

WHERE ANY (key in keys(gene) WHERE gene[key]='CG1046')
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_proteins.primaryIdentifier,
gene_proteins.primaryAccession,
gene_proteins.isUniprotCanonical,
gene_proteins.molecularWeight,
gene_proteins.length,
gene_proteins.isFragment
ORDER BY gene.secondaryIdentifier ASC
