MATCH (protein :Protein),
(protein)-[:proteinDomainRegions]-(protein_proteindomainregions :ProteinDomainRegion),
(protein_proteindomainregions)-[:proteinDomain]-(protein_proteindomainregions_proteindomain :ProteinDomain)

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
