MATCH (gene :Gene),
(gene)-[]-(gene_interactions :interactions),
(gene_interactions)-[]-(gene_interactions_participant2 :participant2),
(gene_interactions)-[]-(gene_interactions_details :details),
(gene_interactions_details)-[]-(gene_interactions_details_role1 :role1),
(gene_interactions_details)-[]-(gene_interactions_details_experiment :experiment),
(gene_interactions_details_experiment)-[]-(gene_interactions_details_experiment_interactiondetectionmethods :interactionDetectionMethods),
(gene_interactions_details_experiment)-[]-(gene_interactions_details_experiment_publication :publication),
(gene_interactions_details_experiment_publication)-[]-(gene_interactions_details_experiment_publication_pubmedid :pubMedId),
(gene_interactions_details)-[]-(gene_interactions_details_role2 :role2),
(gene_interactions_details)-[]-(gene_interactions_details_datasets :dataSets)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='notch') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND ( ANY (key in keys(gene_interactions_participant2) WHERE gene_interactions_participant2[key]='Abl') AND (gene_interactions_participant2)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.primaryIdentifier,
gene.symbol,
gene_interactions_participant2.primaryIdentifier,
gene_interactions_participant2.symbol,
gene_interactions_details.type,
gene_interactions_details_experiment_interactiondetectionmethods.name,
gene_interactions_details_datasets.name
ORDER BY gene.primaryIdentifier ASC
