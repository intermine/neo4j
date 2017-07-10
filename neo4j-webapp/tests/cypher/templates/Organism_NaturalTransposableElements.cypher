MATCH (naturaltransposableelement :NaturalTransposableElement),
(naturaltransposableelement)-[]-(naturaltransposableelement_organism :organism)

WHERE naturaltransposableelement_organism.name = 'Drosophila melanogaster'
RETURN naturaltransposableelement.primaryIdentifier,
naturaltransposableelement.symbol
ORDER BY naturaltransposableelement.primaryIdentifier ASC
