MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism)

WHERE Gene.primaryIdentifier IN ['FBgn0000008', 'FBgn0000153']
RETURN gene.secondaryIdentifier,
gene.symbol,
gene.primaryIdentifier,
gene_organism.name
ORDER BY gene.secondaryIdentifier ASC
