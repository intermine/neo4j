MATCH (protein :Protein),
(protein)-[]-(protein_primaryaccession :primaryAccession),
(protein)-[]-(protein_organism :organism)

WHERE protein_organism.name = 'Drosophila melanogaster'
RETURN protein.primaryIdentifier
ORDER BY protein.primaryIdentifier ASC
