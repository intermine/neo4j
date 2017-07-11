MATCH (protein :Protein),
(protein)-[]-(protein_uniprotname :uniprotName),
(protein)-[]-(protein_uniprotaccession :uniprotAccession),
(protein)-[]-(protein_proteindomainregions :proteinDomainRegions),
(protein_proteindomainregions)-[]-(protein_proteindomainregions_database :database),
(protein_proteindomainregions)-[]-(protein_proteindomainregions_proteindomain :proteinDomain)

WHERE ( ANY (key in keys(protein) WHERE protein[key]='notch') AND (protein)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN protein_proteindomainregions_proteindomain.name,
protein_proteindomainregions_proteindomain.primaryIdentifier,
protein_proteindomainregions_proteindomain.type,
protein_proteindomainregions.start,
protein_proteindomainregions.end,
protein_proteindomainregions.identifier
ORDER BY protein.uniprotAccession ASC
