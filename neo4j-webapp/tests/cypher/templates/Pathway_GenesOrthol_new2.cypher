MATCH (pathway :Pathway),
(pathway)-[]-(pathway_genes :genes),
(pathway_genes)-[]-(pathway_genes_organism :organism),
(pathway_genes)-[]-(pathway_genes_homologues :homologues),
(pathway_genes_homologues)-[]-(pathway_genes_homologues_homologue :homologue),
(pathway_genes_homologues_homologue)-[]-(pathway_genes_homologues_homologue_organism :organism),
(pathway)-[]-(pathway_datasets :dataSets)

WHERE pathway.name = 'Pentose phosphate pathway' AND pathway_genes_organism.name = 'Drosophila melanogaster' AND pathway_datasets.name = 'KEGG pathways data set' AND pathway_genes_homologues_homologue_organism.name = 'Anopheles gambiae'
RETURN pathway_genes.secondaryIdentifier,
pathway_genes.symbol,
pathway_genes_homologues_homologue.secondaryIdentifier,
pathway_genes_homologues_homologue.symbol,
pathway.identifier,
pathway.name
ORDER BY pathway_genes.secondaryIdentifier ASC
