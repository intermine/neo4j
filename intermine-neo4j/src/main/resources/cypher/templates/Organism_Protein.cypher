MATCH (protein :Protein),
(protein)-[:PART_OF]-(protein_organism :Organism)

WHERE protein_organism.name = 'Drosophila melanogaster'
RETURN protein.primaryIdentifier,
protein.primaryAccession
ORDER BY protein.primaryIdentifier ASC
