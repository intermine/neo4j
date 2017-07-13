MATCH (gene :Gene),
(gene)-[:microArrayResults]-(gene_microarrayresults :MicroArrayResult),
(gene_microarrayresults)-[:assays]-(gene_microarrayresults_assays :MicroArrayAssay),
(gene_microarrayresults_assays)-[:experiment]-(gene_microarrayresults_assays_experiment :MicroArrayExperiment),
(gene_microarrayresults_assays_experiment)-[:publication]-(gene_microarrayresults_assays_experiment_publication :Publication)

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
