MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_publications :publications),
(gene_publications)-[]-(gene_publications_journal :journal),
(gene_publications)-[]-(gene_publications_year :year),
(gene_publications)-[]-(gene_publications_pubmedid :pubMedId),
(gene_publications)-[]-(gene_publications_firstauthor :firstAuthor)

WHERE gene_organism.name = 'Drosophila pseudoobscura'
RETURN gene.secondaryIdentifier,
gene_organism.name
ORDER BY gene.secondaryIdentifier ASC
