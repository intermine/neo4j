MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:probeSets]-(gene_probesets :ProbeSet)

WHERE gene_organism.name = 'Drosophila melanogaster' AND ANY (key in keys(gene_probesets) WHERE gene_probesets[key]='155099_at')
RETURN gene_probesets.primaryIdentifier,
gene.symbol

