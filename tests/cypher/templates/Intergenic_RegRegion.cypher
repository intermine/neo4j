MATCH (intergenicregion :IntergenicRegion),
(intergenicregion)-[]-(intergenicregion_organism :organism),
(intergenicregion)-[]-(intergenicregion_overlappingfeatures :overlappingFeatures),
(intergenicregion_overlappingfeatures)-[]-(intergenicregion_overlappingfeatures_chromosomelocation :chromosomeLocation),
(intergenicregion_overlappingfeatures)-[]-(intergenicregion_overlappingfeatures_datasets :dataSets)

WHERE intergenicregion_organism.name = 'Drosophila melanogaster' AND intergenicregion.primaryIdentifier = 'intergenic_region_chr2L_10295386..10295858'
RETURN intergenicregion.primaryIdentifier,
intergenicregion_overlappingfeatures.primaryIdentifier,
intergenicregion_overlappingfeatures_chromosomelocation.start,
intergenicregion_overlappingfeatures_chromosomelocation.end,
intergenicregion_overlappingfeatures.length,
intergenicregion_overlappingfeatures_datasets.name
ORDER BY intergenicregion.primaryIdentifier ASC
