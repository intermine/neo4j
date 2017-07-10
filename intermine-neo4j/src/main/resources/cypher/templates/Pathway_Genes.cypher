MATCH (pathway :Pathway),
(pathway)-[]-(pathway_genes :genes),
(pathway_genes)-[]-(pathway_genes_organism :organism),
(pathway)-[]-(pathway_datasets :dataSets)

WHERE pathway_datasets.name = 'KEGG pathways data set' AND pathway_genes_organism.name = 'Drosophila melanogaster' AND pathway.name = 'Pentose phosphate pathway'
RETURN pathway.identifier,
pathway.name,
pathway_genes.secondaryIdentifier,
pathway_genes.symbol,
pathway_datasets.name
ORDER BY pathway.identifier ASC
