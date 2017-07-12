MATCH (gene :Gene),
(gene)-[]-(gene_chromosome :chromosome)
OPTIONAL MATCH (gene_chromosome)-[gene_chromosome_sequenceontologyterm:sequenceOntologyTerm]-(gene_chromosome_sequenceontologyterm_datasets :dataSets)

RETURN gene.primaryIdentifier,
gene_chromosome.primaryIdentifier,
gene_chromosome_sequenceontologyterm.identifier,
gene_chromosome_sequenceontologyterm_datasets.url,
gene_chromosome_sequenceontologyterm_datasets_publication.doi,
gene_chromosome_sequenceontologyterm_datasets_publication_authors.name
ORDER BY gene.primaryIdentifier ASC
