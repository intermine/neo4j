MATCH (gene :Gene),
(gene)-[]-(gene_microarrayresults :microArrayResults),
(gene_microarrayresults)-[]-(gene_microarrayresults_assays :assays),
(gene_microarrayresults_assays)-[]-(gene_microarrayresults_assays_experiment :experiment),
(gene_microarrayresults_assays_experiment)-[]-(gene_microarrayresults_assays_experiment_publication :publication)

WHERE gene_microarrayresults_assays_experiment.name = 'Arbeitman M: Gene Expression During the Life Cycle of Drosophila melanogaster' AND gene_microarrayresults.value >= 2.0 AND gene_microarrayresults_assays.sample2 = '*Embryo*'
RETURN gene.secondaryIdentifier,
gene_microarrayresults.type,
gene_microarrayresults.value,
gene_microarrayresults.isControl,
gene_microarrayresults_assays.sample2,
gene_microarrayresults_assays.sample1,
gene_microarrayresults_assays_experiment.name,
gene_microarrayresults_assays_experiment_publication.pubMedId
ORDER BY gene.secondaryIdentifier ASC
