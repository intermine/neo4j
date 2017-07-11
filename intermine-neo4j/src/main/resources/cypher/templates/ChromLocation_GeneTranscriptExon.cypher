MATCH (chromosome :Chromosome),
(chromosome)-[]-(chromosome_organism :organism),
(chromosome)-[]-(chromosome_locatedfeatures :locatedFeatures),
(chromosome_locatedfeatures)-[]-(chromosome_locatedfeatures_feature :feature),
(chromosome_locatedfeatures_feature)-[]-(chromosome_locatedfeatures_feature_gene :gene),
(chromosome_locatedfeatures_feature)-[]-(chromosome_locatedfeatures_feature_transcripts :transcripts)

WHERE chromosome_organism.name = 'Drosophila melanogaster' AND chromosome.primaryIdentifier = '2L' AND chromosome_locatedfeatures.start >= 1 AND chromosome_locatedfeatures.end < 10000
RETURN chromosome.primaryIdentifier,
chromosome_organism.name,
chromosome_locatedfeatures_feature_gene.primaryIdentifier,
chromosome_locatedfeatures_feature_transcripts.primaryIdentifier,
chromosome_locatedfeatures_feature.primaryIdentifier
ORDER BY chromosome.primaryIdentifier ASC,
chromosome.primaryIdentifier ASC
