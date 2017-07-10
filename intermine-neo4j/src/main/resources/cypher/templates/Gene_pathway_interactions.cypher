MATCH (gene :Gene),
(gene)-[]-(gene_interactions :interactions),
(gene_interactions)-[]-(gene_interactions_participant2 :participant2),
(gene_interactions_participant2)-[]-(gene_interactions_participant2_pathways :pathways),
(gene_interactions)-[]-(gene_interactions_details :details)

WHERE gene_interactions_participant2_pathways.name = 'ABC-family proteins mediated transport' AND ( ANY (key in keys(gene) WHERE gene[key]='Chc') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.primaryIdentifier,
gene.symbol,
gene_interactions_participant2.primaryIdentifier,
gene_interactions_participant2.symbol,
gene_interactions_details.type,
gene_interactions_participant2_pathways.name
ORDER BY gene.primaryIdentifier ASC
