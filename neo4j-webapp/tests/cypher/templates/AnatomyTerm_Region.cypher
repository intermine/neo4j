MATCH (crm :CRM),
(crm)-[]-(crm_chromosomelocation :chromosomeLocation),
(crm)-[]-(crm_gene :gene),
(crm_gene)-[]-(crm_gene_regulatoryregions :regulatoryRegions),
(crm_gene_regulatoryregions)-[]-(crm_gene_regulatoryregions_datasets :dataSets),
(crm_gene_regulatoryregions_datasets)-[]-(crm_gene_regulatoryregions_datasets_datasource :dataSource),
(crm)-[]-(crm_chromosome :chromosome),
(crm)-[]-(crm_anatomyontology :anatomyOntology),
(crm)-[]-(crm_publications :publications),
(crm_publications)-[]-(crm_publications_pubmedid :pubMedId)

WHERE crm_anatomyontology.name = '*neuro*' AND crm_gene_regulatoryregions_datasets.name = 'REDfly Drosophila transcriptional cis-regulatory modules'
RETURN crm_anatomyontology.name,
crm.primaryIdentifier,
crm_chromosome.primaryIdentifier,
crm_chromosomelocation.start,
crm_chromosomelocation.end,
crm.length,
crm_gene_regulatoryregions_datasets_datasource.name
ORDER BY crm_anatomyontology.name ASC
