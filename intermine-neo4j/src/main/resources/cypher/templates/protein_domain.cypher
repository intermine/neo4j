MATCH (proteindomain :ProteinDomain),
(proteindomain)-[]-(proteindomain_proteindomainregions :proteinDomainRegions),
(proteindomain_proteindomainregions)-[]-(proteindomain_proteindomainregions_protein :protein),
(proteindomain_proteindomainregions_protein)-[]-(proteindomain_proteindomainregions_protein_genes :genes)

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
