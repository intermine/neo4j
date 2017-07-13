MATCH (gene :Gene),
(gene)-[:sequence]-(gene_sequence :Sequence),
(gene)-[:homologues]-(gene_homologues :Homologue),
(gene_homologues)-[:EVIDENCED_BY]-(gene_homologues_evidence :OrthologueEvidence),
(gene_homologues_evidence)-[:evidenceCode]-(gene_homologues_evidence_evidencecode :OrthologueEvidenceCode),
(gene_homologues)-[:dataSets]-(gene_homologues_datasets :DataSet),
(gene_homologues)-[:PARTNER_OF]-(gene_homologues_homologue :Gene),
(gene_homologues_homologue)-[:sequence]-(gene_homologues_homologue_sequence :Sequence),
(gene_homologues_homologue)-[:PART_OF]-(gene_homologues_homologue_organism :Organism)

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
