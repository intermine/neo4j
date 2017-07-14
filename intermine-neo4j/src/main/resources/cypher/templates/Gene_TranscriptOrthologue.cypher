MATCH (gene :Gene),
(gene)-[:transcripts]-(gene_transcripts :Transcript),
(gene)-[:homologues]-(gene_homologues :Homologue),
(gene_homologues)-[:dataSets]-(gene_homologues_datasets :DataSet),
(gene_homologues)-[:PARTNER_OF]-(gene_homologues_homologue :Gene),
(gene_homologues_homologue)-[:PART_OF]-(gene_homologues_homologue_organism :Organism),
(gene_homologues_homologue)-[:transcripts]-(gene_homologues_homologue_transcripts :Transcript)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG12399') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_transcripts.primaryIdentifier,
gene_homologues_homologue.symbol,
gene_homologues.type,
gene_homologues_homologue_organism.name,
gene_homologues_homologue_transcripts.primaryIdentifier,
gene_homologues_datasets.name
ORDER BY gene.secondaryIdentifier ASC
