MATCH (protein :Protein),
(protein)-[:genes]-(protein_genes :Gene),
(protein_genes)-[:chromosomeLocation]-(protein_genes_chromosomelocation :Location),
(protein_genes)-[:chromosome]-(protein_genes_chromosome :Chromosome),
(protein)-[:PARTICIPATES_IN]-(protein_pathways :Pathway),
(protein_pathways)-[:dataSets]-(protein_pathways_datasets :DataSet)

WHERE ANY (key in keys(protein) WHERE protein[key]='P19107') AND protein_pathways_datasets.name = 'Affymetrix array: DrosGenome1'
RETURN protein.primaryAccession,
protein_genes.primaryIdentifier,
protein_genes.symbol,
protein_genes_chromosome.primaryIdentifier,
protein_genes_chromosomelocation.start,
protein_genes_chromosomelocation.end,
protein_genes_chromosomelocation.strand,
protein_genes.length
ORDER BY protein.primaryAccession ASC
