MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_microarrayresults :microArrayResults),
(gene_microarrayresults)-[]-(gene_microarrayresults_standarderror :standardError),
(gene_microarrayresults)-[]-(gene_microarrayresults_material :material),
(gene_microarrayresults)-[]-(gene_microarrayresults_assays :assays),
(gene_microarrayresults_assays)-[]-(gene_microarrayresults_assays_sample1 :sample1),
(gene_microarrayresults_assays)-[]-(gene_microarrayresults_assays_experiment :experiment),
(gene_microarrayresults_assays_experiment)-[]-(gene_microarrayresults_assays_experiment_publication :publication),
(gene_microarrayresults_assays_experiment_publication)-[]-(gene_microarrayresults_assays_experiment_publication_pubmedid :pubMedId),
(gene_microarrayresults_assays_experiment_publication)-[]-(gene_microarrayresults_assays_experiment_publication_firstauthor :firstAuthor),
(gene_microarrayresults_assays)-[]-(gene_microarrayresults_assays_sample2 :sample2)

WHERE gene_organism.name = 'Anopheles gambiae' AND ANY (key in keys(gene) WHERE gene[key]='AGAP000008') AND gene_microarrayresults_assays.sample2 = 'stage: 12-14_h_embryo_mixed sex'
RETURN gene.primaryIdentifier,
gene_microarrayresults.value,
gene_microarrayresults.type,
gene_microarrayresults_material.primaryIdentifier,
gene_microarrayresults_assays_experiment.name

