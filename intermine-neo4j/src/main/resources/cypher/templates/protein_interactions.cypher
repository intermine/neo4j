MATCH (gene :Gene),
(gene)-[]-(gene_proteins :proteins),
(gene)-[]-(gene_interactions :interactions),
(gene_interactions)-[]-(gene_interactions_participant2 :participant2),
(gene_interactions)-[]-(gene_interactions_details :details),
(gene_interactions_details)-[]-(gene_interactions_details_experiment :experiment),
(gene_interactions_details_experiment)-[]-(gene_interactions_details_experiment_interactiondetectionmethods :interactionDetectionMethods),
(gene_interactions_details_experiment)-[]-(gene_interactions_details_experiment_publication :publication),
(gene_interactions_details)-[]-(gene_interactions_details_datasets :dataSets)

WHERE gene_interactions_details.type = 'physical' AND gene_interactions_details_experiment_interactiondetectionmethods.name = 'two hybrid' AND gene_proteins.uniprotName = 'zen1_drome'
RETURN gene_proteins.uniprotAccession,
gene_proteins.uniprotName,
gene.primaryIdentifier,
gene.symbol,
gene_interactions_participant2.primaryIdentifier,
gene_interactions_participant2.symbol,
gene_interactions_details.type,
gene_interactions_details.name,
gene_interactions_details.role1,
gene_interactions_details.role2,
gene_interactions_details_experiment_interactiondetectionmethods.name,
gene_interactions_details_experiment.name,
gene_interactions_details_experiment_publication.pubMedId,
gene_interactions_details_datasets.name
ORDER BY gene_proteins.uniprotAccession ASC
