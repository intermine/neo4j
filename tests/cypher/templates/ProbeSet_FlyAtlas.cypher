MATCH (probeset :ProbeSet),
(probeset)-[]-(probeset_datasets :dataSets),
(probeset)-[]-(probeset_results :results),
(probeset_results)-[]-(probeset_results_enrichment :enrichment),
(probeset_results)-[]-(probeset_results_affycall :affyCall),
(probeset_results)-[]-(probeset_results_datasets :dataSets),
(probeset_results)-[]-(probeset_results_tissue :tissue),
(probeset_results)-[]-(probeset_results_presentcall :presentCall)

WHERE probeset.primaryIdentifier = '1622901_at' AND probeset_datasets.name = 'FlyAtlas'
RETURN probeset.primaryIdentifier,
probeset_datasets.name,
probeset_results_datasets.name,
probeset_results_tissue.name
ORDER BY probeset.primaryIdentifier ASC
