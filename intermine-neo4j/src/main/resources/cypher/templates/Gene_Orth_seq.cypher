MATCH (gene :Gene),
(gene)-[]-(gene_sequence :sequence),
(gene)-[]-(gene_homologues :homologues),
(gene_homologues)-[]-(gene_homologues_evidence :evidence),
(gene_homologues_evidence)-[]-(gene_homologues_evidence_evidencecode :evidenceCode),
(gene_homologues)-[]-(gene_homologues_datasets :dataSets),
(gene_homologues)-[]-(gene_homologues_homologue :homologue),
(gene_homologues_homologue)-[]-(gene_homologues_homologue_sequence :sequence),
(gene_homologues_homologue)-[]-(gene_homologues_homologue_organism :organism)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='cdc2') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_homologues_homologue_organism.shortName = 'C. elegans' AND gene_homologues_datasets.name = 'Panther data set'
RETURN gene.primaryIdentifier,
gene.symbol,
gene_homologues_homologue.primaryIdentifier,
gene_homologues_homologue.symbol,
gene_homologues_homologue_organism.shortName,
gene_homologues_evidence_evidencecode.name,
gene_homologues_datasets.name,
gene_sequence.residues,
gene_homologues_homologue_sequence.residues
ORDER BY gene.symbol ASC
