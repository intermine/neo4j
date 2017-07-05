MATCH (gene :Gene),
(gene)-[]-(gene_microarrayresults :microArrayResults),
(gene_microarrayresults)-[]-(gene_microarrayresults_assays :assays),
(gene_microarrayresults_assays)-[]-(gene_microarrayresults_assays_sample1 :sample1),
(gene_microarrayresults_assays)-[]-(gene_microarrayresults_assays_experiment :experiment),
(gene_microarrayresults_assays_experiment)-[]-(gene_microarrayresults_assays_experiment_publication :publication),
(gene_microarrayresults_assays_experiment_publication)-[]-(gene_microarrayresults_assays_experiment_publication_pubmedid :pubMedId),
(gene_microarrayresults_assays)-[]-(gene_microarrayresults_assays_sample2 :sample2),
(gene_microarrayresults)-[]-(gene_microarrayresults_iscontrol :isControl)

WHERE gene_microarrayresults_assays_experiment.name = 'Arbeitman M: Gene Expression During the Life Cycle of Drosophila melanogaster' AND gene_microarrayresults.value >= 2.0 AND gene_microarrayresults_assays.sample2 = '*Embryo*'
RETURN gene.secondaryIdentifier,
gene_microarrayresults.type,
gene_microarrayresults.value,
gene_microarrayresults_assays_experiment.name
ORDER BY gene.secondaryIdentifier ASC
