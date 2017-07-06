MATCH (mirna :MiRNA),
(mirna)-[]-(mirna_organism :organism),
(mirna)-[]-(mirna_chromosomelocation :chromosomeLocation),
(mirna)-[]-(mirna_chromosome :chromosome)

WHERE mirna_organism.name = 'Drosophila melanogaster'
RETURN mirna.primaryIdentifier,
mirna_chromosome.primaryIdentifier,
mirna_chromosomelocation.start,
mirna_chromosomelocation.end,
mirna_chromosomelocation.strand
ORDER BY mirna.primaryIdentifier ASC
