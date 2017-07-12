MATCH (rnaiscreen :RNAiScreen),
(rnaiscreen)-[]-(rnaiscreen_rnairesults :rnaiResults),
(rnaiscreen_rnairesults)-[]-(rnaiscreen_rnairesults_gene :gene),
(rnaiscreen)-[]-(rnaiscreen_publication :publication)

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
