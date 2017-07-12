MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_publications :publications)

WHERE gene_organism.name = 'Drosophila melanogaster'
RETURN gene.secondaryIdentifier,
gene_publications.pubMedId,
gene_publications.firstAuthor,
gene_publications.journal,
gene_publications.year,
gene_organism.name
ORDER BY gene.secondaryIdentifier ASC
