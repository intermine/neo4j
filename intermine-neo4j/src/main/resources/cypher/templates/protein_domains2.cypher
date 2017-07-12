MATCH (protein :Protein),
(protein)-[]-(protein_proteindomainregions :proteinDomainRegions),
(protein_proteindomainregions)-[]-(protein_proteindomainregions_proteindomain :proteinDomain)

WHERE ( ANY (key in keys(protein) WHERE protein[key]='notch') AND (protein)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN protein.uniprotAccession,
protein.uniprotName,
protein_proteindomainregions_proteindomain.name,
protein_proteindomainregions_proteindomain.primaryIdentifier,
protein_proteindomainregions_proteindomain.type,
protein_proteindomainregions.start,
protein_proteindomainregions.end,
protein_proteindomainregions.identifier,
protein_proteindomainregions.database
ORDER BY protein.uniprotAccession ASC
