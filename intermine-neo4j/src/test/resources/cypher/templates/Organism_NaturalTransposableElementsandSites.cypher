MATCH (transposableelement :TransposableElement),
(transposableelement)-[:PART_OF]-(transposableelement_organism :Organism),
(transposableelement)-[:insertedElement]-(transposableelement_insertedelement :NaturalTransposableElement)
OPTIONAL MATCH (transposableelement)-[:chromosome]-(transposableelement_chromosome :Chromosome)
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
