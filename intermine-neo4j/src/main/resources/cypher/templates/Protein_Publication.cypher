MATCH (protein :Protein),
(protein)-[:PART_OF]-(protein_organism :Organism),
(protein)-[:MENTIONED_IN]-(protein_publications :Publication)

WHERE ( ANY (key in keys(protein) WHERE protein[key]='3-hydroxyacyl-CoA dehydrogenase type-2 ') AND (protein)-[]-(Organism { shortName: 'D. melanogaster' } )) AND protein_organism.name = 'Drosophila melanogaster'
RETURN protein.name,
protein.uniprotAccession,
protein.uniprotName,
protein_publications.pubMedId,
protein_publications.firstAuthor,
protein_publications.year,
protein_publications.title,
protein.primaryAccession,
protein.primaryIdentifier
ORDER BY protein.name ASC
