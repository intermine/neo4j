MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:CDSs]-(gene_cdss :CDS),
(gene_cdss)-[:sequence]-(gene_cdss_sequence :Sequence)

WHERE gene_organism.name = 'Drosophila melanogaster' AND gene_cdss.primaryIdentifier = '*PA*'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_cdss.primaryIdentifier,
gene_cdss_sequence.residues
ORDER BY gene.secondaryIdentifier ASC
