MATCH (crm :CRM),
(crm)-[:chromosomeLocation]-(crm_chromosomelocation :Location),
(crm)-[:gene]-(crm_gene :Gene),
(crm_gene)-[:regulatoryRegions]-(crm_gene_regulatoryregions :RegulatoryRegion),
(crm_gene_regulatoryregions)-[:dataSets]-(crm_gene_regulatoryregions_datasets :DataSet),
(crm_gene_regulatoryregions_datasets)-[:dataSource]-(crm_gene_regulatoryregions_datasets_datasource :DataSource),
(crm)-[:chromosome]-(crm_chromosome :Chromosome),
(crm)-[:anatomyOntology]-(crm_anatomyontology :AnatomyTerm),
(crm)-[:MENTIONED_IN]-(crm_publications :Publication)

WHERE crm_anatomyontology.name = '*neuro*' AND crm_gene_regulatoryregions_datasets.name = 'REDfly Drosophila transcriptional cis-regulatory modules'
RETURN crm_anatomyontology.name,
crm.primaryIdentifier,
crm_chromosome.primaryIdentifier,
crm_chromosomelocation.start,
crm_chromosomelocation.end,
crm.length,
crm_publications.pubMedId,
crm_gene_regulatoryregions_datasets_datasource.name
ORDER BY crm_anatomyontology.name ASC
