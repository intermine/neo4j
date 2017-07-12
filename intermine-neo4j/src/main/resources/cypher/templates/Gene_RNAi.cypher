MATCH (gene :Gene),
(gene)-[]-(gene_rnairesults :rnaiResults),
(gene_rnairesults)-[]-(gene_rnairesults_rnaiscreen :rnaiScreen),
(gene_rnairesults_rnaiscreen)-[]-(gene_rnairesults_rnaiscreen_publication :publication)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='eve') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.primaryIdentifier,
gene.symbol,
gene_rnairesults.phenotype,
gene_rnairesults.score,
gene_rnairesults_rnaiscreen.scoreType,
gene_rnairesults.reagentId,
gene_rnairesults.conditions,
gene_rnairesults_rnaiscreen_publication.pubMedId,
gene_rnairesults_rnaiscreen.name,
gene_rnairesults_rnaiscreen.assay,
gene_rnairesults_rnaiscreen.bioSourceName,
gene_rnairesults_rnaiscreen.bioSourceType,
gene_rnairesults_rnaiscreen.method
ORDER BY gene.primaryIdentifier ASC
