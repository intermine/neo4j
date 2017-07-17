MATCH (gene :Gene),
(gene)-[:PARENT_OF]-(gene_childfeatures :SequenceFeature)

WHERE gene.SequenceFeature IS NULL
RETURN gene.primaryIdentifier
ORDER BY gene.primaryIdentifier ASC
