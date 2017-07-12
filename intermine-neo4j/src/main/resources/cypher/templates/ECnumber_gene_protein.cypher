MATCH (protein :Protein),
(protein)-[]-(protein_ecnumbers :ecNumbers),
(protein)-[]-(protein_genes :genes),
(protein_genes)-[]-(protein_genes_organism :organism)

WHERE protein_genes_organism.name = 'Drosophila melanogaster' AND protein_ecnumbers.identifier = '1.2.1.*'
RETURN protein_ecnumbers.identifier,
protein_genes.primaryIdentifier,
protein_genes.symbol,
protein_genes.name,
protein.primaryAccession,
protein.primaryIdentifier,
protein_genes_organism.name
ORDER BY protein_genes.name ASC
