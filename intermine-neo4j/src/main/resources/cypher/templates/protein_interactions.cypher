MATCH (gene :Gene),
(gene)-[:proteins]-(gene_proteins :Protein),
(gene)-[:PARTICIPATES_IN]-(gene_interactions :Interaction),
(gene_interactions)-[:participant2]-(gene_interactions_participant2 :BioEntity),
(gene_interactions)-[:details]-(gene_interactions_details :InteractionDetail),
(gene_interactions_details)-[:experiment]-(gene_interactions_details_experiment :InteractionExperiment),
(gene_interactions_details_experiment)-[:interactionDetectionMethods]-(gene_interactions_details_experiment_interactiondetectionmethods :InteractionTerm),
(gene_interactions_details_experiment)-[:publication]-(gene_interactions_details_experiment_publication :Publication),
(gene_interactions_details)-[:dataSets]-(gene_interactions_details_datasets :DataSet)

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
