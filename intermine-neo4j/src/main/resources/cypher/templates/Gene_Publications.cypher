MATCH (gene :Gene),
(gene)-[]-(gene_publications :publications),
(gene_publications)-[]-(gene_publications_journal :journal),
(gene_publications)-[]-(gene_publications_year :year),
(gene_publications)-[]-(gene_publications_title :title),
(gene_publications)-[]-(gene_publications_pubmedid :pubMedId),
(gene_publications)-[]-(gene_publications_firstauthor :firstAuthor)

WHERE ANY (key in keys(gene) WHERE gene[key]='brat')
RETURN gene.secondaryIdentifier,
gene.symbol
ORDER BY gene_publications.year DESC
