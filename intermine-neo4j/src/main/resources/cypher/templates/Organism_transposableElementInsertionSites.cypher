MATCH (transposableelementinsertionsite :TransposableElementInsertionSite),
(transposableelementinsertionsite)-[:PART_OF]-(transposableelementinsertionsite_organism :Organism)
OPTIONAL MATCH (transposableelementinsertionsite)-[:chromosomeLocation]-(transposableelementinsertionsite_chromosomelocation :Location),
(transposableelementinsertionsite)-[:chromosome]-(transposableelementinsertionsite_chromosome :Chromosome)
WHERE transposableelementinsertionsite_organism.name = 'Drosophila melanogaster'
RETURN transposableelementinsertionsite.primaryIdentifier,
transposableelementinsertionsite.cytoLocation,
transposableelementinsertionsite_chromosome.primaryIdentifier,
transposableelementinsertionsite_chromosomelocation.start,
transposableelementinsertionsite_chromosomelocation.end,
transposableelementinsertionsite_chromosomelocation.strand
ORDER BY transposableelementinsertionsite.primaryIdentifier ASC
