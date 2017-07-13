MATCH (gene :Gene),
(gene)-[:PARTICIPATES_IN]-(gene_interactions :Interaction),
(gene_interactions)-[:participant2]-(gene_interactions_participant2 :BioEntity),
(gene_interactions)-[:details]-(gene_interactions_details :InteractionDetail),
(gene_interactions_details)-[:experiment]-(gene_interactions_details_experiment :InteractionExperiment),
(gene_interactions_details_experiment)-[:interactionDetectionMethods]-(gene_interactions_details_experiment_interactiondetectionmethods :InteractionTerm),
(gene_interactions_details_experiment)-[:publication]-(gene_interactions_details_experiment_publication :Publication),
(gene_interactions_details)-[:dataSets]-(gene_interactions_details_datasets :DataSet)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='notch') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND ( ANY (key in keys(gene_interactions_participant2) WHERE gene_interactions_participant2[key]='Abl') AND (gene_interactions_participant2)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.primaryIdentifier,
gene.symbol,
gene_interactions_participant2.primaryIdentifier,
gene_interactions_participant2.symbol,
gene_interactions_details.type,
gene_interactions_details.role1,
gene_interactions_details.role2,
gene_interactions_details_experiment_interactiondetectionmethods.name,
gene_interactions_details_experiment_publication.pubMedId,
gene_interactions_details_datasets.name
ORDER BY gene.primaryIdentifier ASC
