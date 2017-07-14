MATCH (protein :Protein),
(protein)-[:genes]-(protein_genes :Gene),
(protein_genes)-[:PART_OF]-(protein_genes_organism :Organism)

WHERE protein.ecNumber = '1.2.1.*' AND protein_genes_organism.name = 'Drosophila melanogaster'
RETURN protein.ecNumber,
protein_genes_organism.name,
protein_genes.name,
protein_genes.primaryIdentifier,
protein_genes.symbol,
protein.primaryAccession,
protein.primaryIdentifier
ORDER BY protein_genes.name ASC
