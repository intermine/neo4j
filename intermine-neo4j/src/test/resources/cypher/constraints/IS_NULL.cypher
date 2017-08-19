MATCH (gene :Gene),
(gene)-[:PARENT_OF]-(gene_childfeatures :SequenceFeature)
WHERE gene_childfeatures IS NULL
RETURN gene.primaryIdentifier
ORDER BY gene.primaryIdentifier ASC
