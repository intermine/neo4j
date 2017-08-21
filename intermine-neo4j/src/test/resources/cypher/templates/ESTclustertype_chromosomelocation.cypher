MATCH (overlappingestset :OverlappingESTSet),
(overlappingestset)-[:PART_OF]-(overlappingestset_organism :Organism),
(overlappingestset)-[:chromosome]-(overlappingestset_chromosome :Chromosome)
WHERE overlappingestset_organism.name = 'Anopheles gambiae' AND overlappingestset.primaryIdentifier = 'TCLAG*'
RETURN overlappingestset.primaryIdentifier,
overlappingestset.length,
overlappingestset_chromosome.primaryIdentifier,
overlappingestset_chromosomelocation.start,
overlappingestset_chromosomelocation.end,
overlappingestset_chromosomelocation.strand
ORDER BY overlappingestset.primaryIdentifier ASC
