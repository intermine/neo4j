MATCH (protein :Protein),
(protein)-[]-(protein_organism :organism),
(protein)-[]-(protein_genes :genes),
(protein_genes)-[]-(protein_genes_publications :publications),
(protein_genes_publications)-[]-(protein_genes_publications_pubmedid :pubMedId),
(protein)-[]-(protein_uniprotaccession :uniprotAccession),
(protein)-[]-(protein_publications :publications),
(protein_publications)-[]-(protein_publications_pubmedid :pubMedId)

WHERE ANY (key in keys(protein) WHERE protein[key]='3-hydroxyacyl-CoA dehydrogenase type-2 ') AND protein_organism.name = 'Drosophila melanogaster'
RETURN protein.name,
protein_genes.symbol,
protein_genes.primaryIdentifier
ORDER BY protein.name ASC
