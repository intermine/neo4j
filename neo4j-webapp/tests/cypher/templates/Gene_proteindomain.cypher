MATCH (gene :Gene),
(gene)-[]-(gene_proteins :proteins),
(gene_proteins)-[]-(gene_proteins_proteindomainregions :proteinDomainRegions),
(gene_proteins_proteindomainregions)-[]-(gene_proteins_proteindomainregions_database :database),
(gene_proteins_proteindomainregions)-[]-(gene_proteins_proteindomainregions_proteindomain :proteinDomain),
(gene_proteins)-[]-(gene_proteins_uniprotaccession :uniprotAccession)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='notch') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.primaryIdentifier,
gene.symbol,
gene_proteins_proteindomainregions_proteindomain.name,
gene_proteins_proteindomainregions_proteindomain.type,
gene_proteins_proteindomainregions.start,
gene_proteins_proteindomainregions.end,
gene_proteins_proteindomainregions.identifier
ORDER BY gene.primaryIdentifier ASC
