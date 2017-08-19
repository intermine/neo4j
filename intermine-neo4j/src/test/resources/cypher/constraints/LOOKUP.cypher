MATCH (gene :Gene)
WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG6235') AND (gene)-[]-(Organism { shortName: 'D. willistoni' } ))
RETURN gene.primaryIdentifier
ORDER BY gene.primaryIdentifier ASC
