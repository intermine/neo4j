MATCH (flyatlasresult :FlyAtlasResult),
(flyatlasresult)-[:material]-(flyatlasresult_material :ProbeSet),
(flyatlasresult)-[:genes]-(flyatlasresult_genes :Gene),
(flyatlasresult)-[:dataSets]-(flyatlasresult_datasets :DataSet),
(flyatlasresult)-[:tissue]-(flyatlasresult_tissue :Tissue)

WHERE flyatlasresult.presentCall >= 3 AND flyatlasresult.enrichment > 2.0 AND flyatlasresult.mRNASignal >= 100.0 AND flyatlasresult.affyCall = 'Up' AND flyatlasresult_tissue.name = 'Ovary'
RETURN flyatlasresult.mRNASignal,
flyatlasresult.mRNASignalSEM,
flyatlasresult.presentCall,
flyatlasresult.enrichment,
flyatlasresult.affyCall,
flyatlasresult_genes.secondaryIdentifier,
flyatlasresult_genes.symbol,
flyatlasresult_material.primaryIdentifier,
flyatlasresult_datasets.name
ORDER BY flyatlasresult.enrichment DESC
