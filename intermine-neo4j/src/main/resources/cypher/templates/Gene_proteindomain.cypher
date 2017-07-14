MATCH (gene :Gene),
(gene)-[:proteins]-(gene_proteins :Protein),
(gene_proteins)-[:proteinDomainRegions]-(gene_proteins_proteindomainregions :ProteinDomainRegion),
(gene_proteins_proteindomainregions)-[:proteinDomain]-(gene_proteins_proteindomainregions_proteindomain :ProteinDomain)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='notch') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.primaryIdentifier,
gene.symbol,
gene_proteins.uniprotAccession,
gene_proteins_proteindomainregions_proteindomain.name,
gene_proteins_proteindomainregions_proteindomain.type,
gene_proteins_proteindomainregions.start,
gene_proteins_proteindomainregions.end,
gene_proteins_proteindomainregions.identifier,
gene_proteins_proteindomainregions.database
ORDER BY gene.primaryIdentifier ASC
