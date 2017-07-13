MATCH (rnaiscreen :RNAiScreen),
(rnaiscreen)-[:rnaiResults]-(rnaiscreen_rnairesults :RNAiResult),
(rnaiscreen_rnairesults)-[:gene]-(rnaiscreen_rnairesults_gene :Gene),
(rnaiscreen)-[:publication]-(rnaiscreen_publication :Publication)

WHERE rnaiscreen.name = 'RNAi pathway'
RETURN rnaiscreen_rnairesults_gene.primaryIdentifier,
rnaiscreen_rnairesults_gene.secondaryIdentifier,
rnaiscreen_rnairesults.phenotype,
rnaiscreen_rnairesults_gene.symbol,
rnaiscreen_rnairesults.score,
rnaiscreen.scoreType,
rnaiscreen_rnairesults.reagentId,
rnaiscreen.reagentType,
rnaiscreen.name,
rnaiscreen.assay,
rnaiscreen.method,
rnaiscreen_publication.pubMedId,
rnaiscreen_rnairesults.conditions
ORDER BY rnaiscreen_rnairesults.phenotype ASC
