MATCH (gene :Gene),
(gene)-[]-(gene_chromosome :chromosome),
(gene_chromosome)-[]-(gene_chromosome_sequenceontologyterm :sequenceOntologyTerm)
OPTIONAL MATCH (gene_chromosome_sequenceontologyterm)-[]-(gene_chromosome_sequenceontologyterm_datasets :dataSets),
(gene_chromosome_sequenceontologyterm_datasets)-[]-(gene_chromosome_sequenceontologyterm_datasets_publication :publication),
(gene_chromosome_sequenceontologyterm_datasets_publication)-[]-(gene_chromosome_sequenceontologyterm_datasets_publication_doi :doi),
(gene_chromosome_sequenceontologyterm_datasets_publication)-[]-(gene_chromosome_sequenceontologyterm_datasets_publication_authors :authors),
(gene_chromosome_sequenceontologyterm_datasets)-[]-(gene_chromosome_sequenceontologyterm_datasets_url :url)

RETURN gene.primaryIdentifier,
gene_chromosome.primaryIdentifier,
gene_chromosome_sequenceontologyterm.identifier,
gene_chromosome_sequenceontologyterm_datasets_publication_authors.name
ORDER BY gene.primaryIdentifier ASC
