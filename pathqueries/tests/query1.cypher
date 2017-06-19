MATCH (a:Gene)-[]-(:Interaction)-[]-()-[:Interaction]-(b)
    WHERE a.name CONTAINS "phosphate"
RETURN a.symbol, b.name
