MATCH (gene :Gene),
(gene)-[:chromosome]-(gene_chromosome :Chromosome),
(gene_chromosome)-[:HAS_TERM]-(gene_chromosome_sequenceontologyterm :SOTerm),
(gene_chromosome_sequenceontologyterm_datasets)-[:publication]-(gene_chromosome_sequenceontologyterm_datasets_publication :Publication)
OPTIONAL MATCH (gene_chromosome_sequenceontologyterm)-[:dataSets]-(gene_chromosome_sequenceontologyterm_datasets :DataSet),
(gene_chromosome_sequenceontologyterm_datasets_publication)-[:AUTHOR_OF]-(gene_chromosome_sequenceontologyterm_datasets_publication_authors :Author)

RETURN gene.primaryIdentifier,
gene_chromosome.primaryIdentifier,
gene_chromosome_sequenceontologyterm.identifier,
gene_chromosome_sequenceontologyterm_datasets.url,
gene_chromosome_sequenceontologyterm_datasets_publication.doi,
gene_chromosome_sequenceontologyterm_datasets_publication_authors.name
ORDER BY gene.primaryIdentifier ASC
