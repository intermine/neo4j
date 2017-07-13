MATCH (proteindomain :ProteinDomain),
(proteindomain)-[:proteinDomainRegions]-(proteindomain_proteindomainregions :ProteinDomainRegion),
(proteindomain_proteindomainregions)-[:protein]-(proteindomain_proteindomainregions_protein :Protein),
(proteindomain_proteindomainregions_protein)-[:genes]-(proteindomain_proteindomainregions_protein_genes :Gene)

WHERE ANY (key in keys(proteindomain) WHERE proteindomain[key]='*kinase*')
RETURN proteindomain.name,
proteindomain.primaryIdentifier,
proteindomain.type,
proteindomain_proteindomainregions.start,
proteindomain_proteindomainregions.end,
proteindomain_proteindomainregions.identifier,
proteindomain_proteindomainregions.database,
proteindomain_proteindomainregions_protein.primaryAccession,
proteindomain_proteindomainregions_protein_genes.primaryIdentifier
ORDER BY proteindomain.primaryIdentifier ASC
