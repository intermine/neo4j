MATCH (chromosome :Chromosome),
(chromosome)-[]-(chromosome_organism :organism),
(chromosome)-[]-(chromosome_locatedfeatures :locatedFeatures),
(chromosome_locatedfeatures)-[]-(chromosome_locatedfeatures_feature :feature),
(chromosome_locatedfeatures_feature)-[]-(chromosome_locatedfeatures_feature_chromosomelocation :chromosomeLocation)

WHERE chromosome.primaryIdentifier = '2L' AND chromosome_organism.name = 'Drosophila melanogaster' AND chromosome_locatedfeatures_feature_chromosomelocation.start > 1 AND chromosome_locatedfeatures_feature_chromosomelocation.end < 50000
RETURN chromosome_locatedfeatures_feature.primaryIdentifier,
chromosome_locatedfeatures_feature.symbol,
chromosome.primaryIdentifier,
chromosome_locatedfeatures_feature_chromosomelocation.start,
chromosome_locatedfeatures_feature_chromosomelocation.end,
chromosome_locatedfeatures_feature_chromosomelocation.strand
ORDER BY chromosome_locatedfeatures_feature.primaryIdentifier ASC
