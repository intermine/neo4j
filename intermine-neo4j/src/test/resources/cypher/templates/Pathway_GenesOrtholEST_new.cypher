MATCH (pathway :Pathway),
(pathway)-[:PARTICIPATES_IN]-(pathway_genes :Gene),
(pathway_genes)-[:PARTNER_OF]-(pathway_genes_homologues :Homologue),
(pathway_genes_homologues)-[:PARTNER_OF]-(pathway_genes_homologues_homologue :Gene),
(pathway_genes_homologues_homologue)-[:PART_OF]-(pathway_genes_homologues_homologue_organism :Organism),
(pathway_genes_homologues_homologue)-[:OVERLAPS]-(pathway_genes_homologues_homologue_overlappingfeatures :SequenceFeature),
(pathway)-[:dataSets]-(pathway_datasets :DataSet)
WHERE pathway.name = 'Pentose phosphate pathway' AND pathway_genes_homologues_homologue_organism.name = 'Anopheles gambiae' AND pathway_datasets.name = 'KEGG pathways data set'
RETURN pathway.identifier,
pathway.name,
pathway_genes.secondaryIdentifier,
pathway_genes.symbol,
pathway_genes_homologues_homologue.secondaryIdentifier,
pathway_genes_homologues.type,
pathway_genes_homologues_homologue_overlappingfeatures.primaryIdentifier
ORDER BY pathway.identifier ASC
