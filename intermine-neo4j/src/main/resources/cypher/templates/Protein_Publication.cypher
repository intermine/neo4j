MATCH (protein :Protein),
(protein)-[]-(protein_uniprotname :uniprotName),
(protein)-[]-(protein_primaryaccession :primaryAccession),
(protein)-[]-(protein_organism :organism),
(protein)-[]-(protein_uniprotaccession :uniprotAccession),
(protein)-[]-(protein_publications :publications),
(protein_publications)-[]-(protein_publications_year :year),
(protein_publications)-[]-(protein_publications_title :title),
(protein_publications)-[]-(protein_publications_pubmedid :pubMedId),
(protein_publications)-[]-(protein_publications_firstauthor :firstAuthor)

WHERE ( ANY (key in keys(protein) WHERE protein[key]='3-hydroxyacyl-CoA dehydrogenase type-2 ') AND (protein)-[]-(Organism { shortName: 'D. melanogaster' } )) AND protein_organism.name = 'Drosophila melanogaster'
RETURN protein.name,
protein.primaryIdentifier
ORDER BY protein.name ASC
