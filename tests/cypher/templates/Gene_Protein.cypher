MATCH (gene :Gene),
(gene)-[]-(gene_proteins :proteins),
(gene_proteins)-[]-(gene_proteins_isfragment :isFragment),
(gene_proteins)-[]-(gene_proteins_isuniprotcanonical :isUniprotCanonical),
(gene_proteins)-[]-(gene_proteins_primaryaccession :primaryAccession),
(gene_proteins)-[]-(gene_proteins_molecularweight :molecularWeight)

WHERE ANY (key in keys(gene) WHERE gene[key]='CG1046')
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_proteins.primaryIdentifier,
gene_proteins.length
ORDER BY gene.secondaryIdentifier ASC
