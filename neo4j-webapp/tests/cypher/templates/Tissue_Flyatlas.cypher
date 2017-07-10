MATCH (flyatlasresult :FlyAtlasResult),
(flyatlasresult)-[]-(flyatlasresult_enrichment :enrichment),
(flyatlasresult)-[]-(flyatlasresult_affycall :affyCall),
(flyatlasresult)-[]-(flyatlasresult_material :material),
(flyatlasresult)-[]-(flyatlasresult_genes :genes),
(flyatlasresult)-[]-(flyatlasresult_datasets :dataSets),
(flyatlasresult)-[]-(flyatlasresult_tissue :tissue),
(flyatlasresult)-[]-(flyatlasresult_mrnasignal :mRNASignal),
(flyatlasresult)-[]-(flyatlasresult_mrnasignalsem :mRNASignalSEM),
(flyatlasresult)-[]-(flyatlasresult_presentcall :presentCall)

WHERE flyatlasresult.presentCall >= 3 AND flyatlasresult.enrichment > 2.0 AND flyatlasresult.mRNASignal >= 100.0 AND flyatlasresult.affyCall = 'Up' AND flyatlasresult_tissue.name = 'Ovary'
RETURN flyatlasresult_genes.secondaryIdentifier,
flyatlasresult_genes.symbol,
flyatlasresult_material.primaryIdentifier,
flyatlasresult_datasets.name
ORDER BY flyatlasresult.enrichment DESC
