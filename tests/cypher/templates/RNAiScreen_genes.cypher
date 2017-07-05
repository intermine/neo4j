MATCH (rnaiscreen :RNAiScreen),
(rnaiscreen)-[]-(rnaiscreen_rnairesults :rnaiResults),
(rnaiscreen_rnairesults)-[]-(rnaiscreen_rnairesults_phenotype :phenotype),
(rnaiscreen_rnairesults)-[]-(rnaiscreen_rnairesults_score :score),
(rnaiscreen_rnairesults)-[]-(rnaiscreen_rnairesults_gene :gene),
(rnaiscreen_rnairesults)-[]-(rnaiscreen_rnairesults_conditions :conditions),
(rnaiscreen_rnairesults)-[]-(rnaiscreen_rnairesults_reagentid :reagentId),
(rnaiscreen)-[]-(rnaiscreen_reagenttype :reagentType),
(rnaiscreen)-[]-(rnaiscreen_method :method),
(rnaiscreen)-[]-(rnaiscreen_assay :assay),
(rnaiscreen)-[]-(rnaiscreen_publication :publication),
(rnaiscreen_publication)-[]-(rnaiscreen_publication_pubmedid :pubMedId),
(rnaiscreen)-[]-(rnaiscreen_scoretype :scoreType)

WHERE rnaiscreen.name = 'RNAi pathway'
RETURN rnaiscreen_rnairesults_gene.primaryIdentifier,
rnaiscreen_rnairesults_gene.secondaryIdentifier,
rnaiscreen_rnairesults_gene.symbol,
rnaiscreen.name
ORDER BY rnaiscreen_rnairesults.phenotype ASC
