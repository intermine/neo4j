MATCH (naturaltransposableelement :NaturalTransposableElement),
(naturaltransposableelement)-[:PART_OF]-(naturaltransposableelement_organism :Organism)

WHERE naturaltransposableelement_organism.name = 'Drosophila melanogaster'
RETURN naturaltransposableelement.primaryIdentifier,
naturaltransposableelement.symbol
ORDER BY naturaltransposableelement.primaryIdentifier ASC
