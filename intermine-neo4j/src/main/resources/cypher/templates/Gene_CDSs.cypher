MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_cdss :CDSs)

WHERE gene_organism.name = 'Drosophila melanogaster'
RETURN gene.symbol,
gene.primaryIdentifier,
gene_cdss.primaryIdentifier
ORDER BY gene.symbol ASC
