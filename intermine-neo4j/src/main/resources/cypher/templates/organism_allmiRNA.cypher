MATCH (mirna :MiRNA),
(mirna)-[:PART_OF]-(mirna_organism :Organism),
(mirna)-[:chromosomeLocation]-(mirna_chromosomelocation :Location),
(mirna)-[:chromosome]-(mirna_chromosome :Chromosome)

WHERE mirna_organism.name = 'Drosophila melanogaster'
RETURN mirna.primaryIdentifier,
mirna_chromosome.primaryIdentifier,
mirna_chromosomelocation.start,
mirna_chromosomelocation.end,
mirna_chromosomelocation.strand
ORDER BY mirna.primaryIdentifier ASC
