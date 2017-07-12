MATCH (protein :Protein),
(protein)-[]-(protein_organism :organism),
(protein)-[]-(protein_genes :genes),
(protein_genes)-[]-(protein_genes_publications :publications),
(protein)-[]-(protein_publications :publications)

WHERE ANY (key in keys(protein) WHERE protein[key]='3-hydroxyacyl-CoA dehydrogenase type-2 ') AND protein_organism.name = 'Drosophila melanogaster'
RETURN protein.name,
protein.uniprotAccession,
protein_genes.symbol,
protein_genes.primaryIdentifier,
protein_genes_publications.pubMedId,
protein_publications.pubMedId
ORDER BY protein.name ASC
