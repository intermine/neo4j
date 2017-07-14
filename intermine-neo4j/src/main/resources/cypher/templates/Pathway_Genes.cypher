MATCH (pathway :Pathway),
(pathway)-[:genes]-(pathway_genes :Gene),
(pathway_genes)-[:PART_OF]-(pathway_genes_organism :Organism),
(pathway)-[:dataSets]-(pathway_datasets :DataSet)

WHERE pathway_datasets.name = 'KEGG pathways data set' AND pathway_genes_organism.name = 'Drosophila melanogaster' AND pathway.name = 'Pentose phosphate pathway'
RETURN pathway.identifier,
pathway.name,
pathway_genes.primaryIdentifier,
pathway_genes.symbol
ORDER BY pathway.identifier ASC
