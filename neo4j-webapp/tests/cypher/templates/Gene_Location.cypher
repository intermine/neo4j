MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_chromosome :chromosome),
(gene)-[]-(gene_locations :locations),
(gene_locations)-[]-(gene_locations_locatedon :locatedOn)

WHERE ANY (key in keys(gene) WHERE gene[key]='CG1046') AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene.name,
gene_chromosome.primaryIdentifier,
gene_locations.start,
gene_locations.end,
gene_locations.strand
ORDER BY gene.secondaryIdentifier ASC
