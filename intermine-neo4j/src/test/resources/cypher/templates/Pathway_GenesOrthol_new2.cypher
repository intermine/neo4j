MATCH (pathway :Pathway),
(pathway)-[:PARTICIPATES_IN]-(pathway_genes :Gene),
(pathway_genes)-[:PART_OF]-(pathway_genes_organism :Organism),
(pathway_genes)-[:PARTNER_OF]-(pathway_genes_homologues :Homologue),
(pathway_genes_homologues)-[:PARTNER_OF]-(pathway_genes_homologues_homologue :Gene),
(pathway_genes_homologues_homologue)-[:PART_OF]-(pathway_genes_homologues_homologue_organism :Organism),
(pathway)-[:dataSets]-(pathway_datasets :DataSet)
WHERE pathway.name = 'Pentose phosphate pathway' AND pathway_genes_organism.name = 'Drosophila melanogaster' AND pathway_datasets.name = 'KEGG pathways data set' AND pathway_genes_homologues_homologue_organism.name = 'Anopheles gambiae'
RETURN pathway_genes.primaryIdentifier,
pathway_genes.symbol,
pathway_genes_homologues_homologue.primaryIdentifier,
pathway_genes_homologues_homologue.symbol,
pathway.identifier,
pathway.name
ORDER BY pathway_genes.primaryIdentifier ASC
