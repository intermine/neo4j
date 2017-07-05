MATCH (protein :Protein),
(protein)-[]-(protein_primaryaccession :primaryAccession),
(protein)-[]-(protein_genes :genes),
(protein_genes)-[]-(protein_genes_chromosomelocation :chromosomeLocation),
(protein_genes)-[]-(protein_genes_chromosome :chromosome)

WHERE ANY (key in keys(protein) WHERE protein[key]='P19107')
RETURN protein_genes.secondaryIdentifier,
protein_genes.symbol,
protein_genes_chromosome.primaryIdentifier,
protein_genes_chromosomelocation.start,
protein_genes_chromosomelocation.end,
protein_genes_chromosomelocation.strand,
protein_genes.length
ORDER BY protein.primaryAccession ASC
