MATCH (gene :Gene)

WHERE NOT gene.symbol IN ['bib', 'zen', 'eve']
RETURN gene.symbol,
gene.primaryIdentifier

