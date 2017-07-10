MATCH (gene :Gene),
(gene)-[]-(gene_rnairesults :rnaiResults),
(gene_rnairesults)-[]-(gene_rnairesults_phenotype :phenotype),
(gene_rnairesults)-[]-(gene_rnairesults_score :score),
(gene_rnairesults)-[]-(gene_rnairesults_rnaiscreen :rnaiScreen),
(gene_rnairesults_rnaiscreen)-[]-(gene_rnairesults_rnaiscreen_biosourcename :bioSourceName),
(gene_rnairesults_rnaiscreen)-[]-(gene_rnairesults_rnaiscreen_method :method),
(gene_rnairesults_rnaiscreen)-[]-(gene_rnairesults_rnaiscreen_assay :assay),
(gene_rnairesults_rnaiscreen)-[]-(gene_rnairesults_rnaiscreen_publication :publication),
(gene_rnairesults_rnaiscreen_publication)-[]-(gene_rnairesults_rnaiscreen_publication_pubmedid :pubMedId),
(gene_rnairesults_rnaiscreen)-[]-(gene_rnairesults_rnaiscreen_scoretype :scoreType),
(gene_rnairesults_rnaiscreen)-[]-(gene_rnairesults_rnaiscreen_biosourcetype :bioSourceType),
(gene_rnairesults)-[]-(gene_rnairesults_conditions :conditions),
(gene_rnairesults)-[]-(gene_rnairesults_reagentid :reagentId)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='eve') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.primaryIdentifier,
gene.symbol,
gene_rnairesults_rnaiscreen.name
ORDER BY gene.primaryIdentifier ASC
