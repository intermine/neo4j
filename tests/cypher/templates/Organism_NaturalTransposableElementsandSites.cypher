MATCH (transposableelement :TransposableElement),
(transposableelement)-[]-(transposableelement_organism :organism),
(transposableelement)-[]-(transposableelement_chromosomelocation :chromosomeLocation),
(transposableelement)-[]-(transposableelement_chromosome :chromosome),
(transposableelement)-[]-(transposableelement_insertedelement :insertedElement)

WHERE transposableelement_organism.name = 'Drosophila melanogaster'
RETURN transposableelement_insertedelement.primaryIdentifier,
transposableelement_insertedelement.symbol,
transposableelement.primaryIdentifier,
transposableelement.symbol,
transposableelement.length,
transposableelement_chromosome.primaryIdentifier,
transposableelement_chromosomelocation.start,
transposableelement_chromosomelocation.end,
transposableelement_chromosomelocation.strand
ORDER BY transposableelement_insertedelement.primaryIdentifier ASC
