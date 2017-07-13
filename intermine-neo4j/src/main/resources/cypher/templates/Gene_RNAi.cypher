MATCH (gene :Gene),
(gene)-[:rnaiResults]-(gene_rnairesults :RNAiResult),
(gene_rnairesults)-[:rnaiScreen]-(gene_rnairesults_rnaiscreen :RNAiScreen),
(gene_rnairesults_rnaiscreen)-[:publication]-(gene_rnairesults_rnaiscreen_publication :Publication)

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
