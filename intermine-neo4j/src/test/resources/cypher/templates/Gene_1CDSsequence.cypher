MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:gene]-(gene_cdss :CDS),
(gene_cdss)-[:sequence]-(gene_cdss_sequence :Sequence)
WHERE gene_organism.name = 'Drosophila melanogaster' AND gene_cdss.primaryIdentifier = '*PA*'
RETURN gene.primaryIdentifier,
gene.secondaryIdentifier,
gene.symbol,
gene_cdss.primaryIdentifier,
gene_cdss_sequence.residues
ORDER BY gene.primaryIdentifier ASC
