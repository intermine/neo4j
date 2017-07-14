MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:homologues]-(gene_homologues :Homologue),
(gene_homologues)-[:dataSets]-(gene_homologues_datasets :DataSet),
(gene_homologues)-[:PARTNER_OF]-(gene_homologues_homologue :Gene),
(gene_homologues_homologue)-[:PART_OF]-(gene_homologues_homologue_organism :Organism)

WHERE gene.primaryIdentifier = '*' AND gene_homologues.type = 'orthologue'
RETURN gene_organism.shortName,
gene_homologues_datasets.name,
gene_homologues_homologue_organism.shortName
ORDER BY gene_homologues_datasets.name ASC
