MATCH (gene :Gene),
(gene)-[]-(gene_publications :publications)

WHERE gene_publications.pubMedId = 11875036
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_publications.pubMedId,
gene_publications.title,
gene_publications.firstAuthor,
gene_publications.year
ORDER BY gene.secondaryIdentifier ASC
