MATCH (protein :Protein),
(protein)-[:PART_OF]-(protein_organism :Organism),
(protein)-[:genes]-(protein_genes :Gene),
(protein_genes)-[:MENTIONED_IN]-(protein_genes_publications :Publication),
(protein)-[:MENTIONED_IN]-(protein_publications :Publication)

WHERE ANY (key in keys(protein) WHERE protein[key]='3-hydroxyacyl-CoA dehydrogenase type-2 ') AND protein_organism.name = 'Drosophila melanogaster'
RETURN protein.name,
protein.uniprotAccession,
protein_genes.symbol,
protein_genes.primaryIdentifier,
protein_genes_publications.pubMedId,
protein_publications.pubMedId
ORDER BY protein.name ASC
