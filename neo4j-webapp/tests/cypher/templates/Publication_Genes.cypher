MATCH (gene :Gene),
(gene)-[]-(gene_publications :publications),
(gene_publications)-[]-(gene_publications_year :year),
(gene_publications)-[]-(gene_publications_title :title),
(gene_publications)-[]-(gene_publications_pubmedid :pubMedId),
(gene_publications)-[]-(gene_publications_firstauthor :firstAuthor)

WHERE gene_publications.pubMedId = 11875036
RETURN gene.secondaryIdentifier,
gene.symbol
ORDER BY gene.secondaryIdentifier ASC
