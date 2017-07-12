MATCH (protein :Protein),
(protein)-[]-(protein_organism :organism)

WHERE protein_organism.name = 'Drosophila melanogaster'
RETURN protein.primaryIdentifier,
protein.primaryAccession
ORDER BY protein.primaryIdentifier ASC
