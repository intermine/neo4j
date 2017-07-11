MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_probesets :probeSets)

WHERE gene_organism.name = 'Drosophila melanogaster' AND ANY (key in keys(gene_probesets) WHERE gene_probesets[key]='155099_at')
RETURN gene_probesets.primaryIdentifier,
gene.symbol

