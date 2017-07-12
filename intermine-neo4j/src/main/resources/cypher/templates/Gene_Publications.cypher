MATCH (gene :Gene),
(gene)-[]-(gene_publications :publications)

WHERE ANY (key in keys(gene) WHERE gene[key]='brat')
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_publications.pubMedId,
gene_publications.title,
gene_publications.firstAuthor,
gene_publications.journal,
gene_publications.year
ORDER BY gene_publications.year DESC
