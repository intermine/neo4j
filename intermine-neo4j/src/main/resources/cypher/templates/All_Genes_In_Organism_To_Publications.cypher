MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:MENTIONED_IN]-(gene_publications :Publication)

WHERE gene_organism.name = 'Drosophila melanogaster'
RETURN gene.secondaryIdentifier,
gene_publications.pubMedId,
gene_publications.firstAuthor,
gene_publications.journal,
gene_publications.year,
gene_organism.name
ORDER BY gene.secondaryIdentifier ASC
