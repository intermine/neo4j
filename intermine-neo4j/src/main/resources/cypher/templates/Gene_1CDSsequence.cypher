MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_cdss :CDSs),
(gene_cdss)-[]-(gene_cdss_sequence :sequence),
(gene_cdss_sequence)-[]-(gene_cdss_sequence_residues :residues)

WHERE gene_organism.name = 'Drosophila melanogaster' AND gene_cdss.primaryIdentifier = '*PA*'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_cdss.primaryIdentifier
ORDER BY gene.secondaryIdentifier ASC
