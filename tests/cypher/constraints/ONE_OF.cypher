MATCH (gene :Gene)

    WHERE gene.symbol IN ['bib', 'zen', 'eve']
RETURN gene.symbol,
       gene.primaryIdentifier
