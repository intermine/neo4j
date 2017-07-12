MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_cdss :CDSs),
(gene_cdss)-[]-(gene_cdss_sequence :sequence)

WHERE gene_organism.name = 'Drosophila melanogaster' AND gene_cdss.primaryIdentifier = '*PA*'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_cdss.primaryIdentifier,
gene_cdss_sequence.residues
ORDER BY gene.secondaryIdentifier ASC
