MATCH (gene :Gene),
(gene)-[]-(gene_microarrayresults :microArrayResults),
(gene_microarrayresults)-[]-(gene_microarrayresults_affycall :affyCall),
(gene_microarrayresults)-[]-(gene_microarrayresults_tissue :tissue)

WHERE gene_microarrayresults_tissue.name IN ['Crop', 'Larval CNS', 'Hindgut', 'Larvae hindgut', 'Tubule', 'Head', 'Larval trachea', 'Adult heart', 'Larval fat body', 'Male accessory glands', 'Larval midgut', 'Larval tubule', 'Brain', 'Whole Fly', 'Adult fat body', 'Virgin spermatheca', 'Adult eye', 'S2 cells', 'Adult carcass', 'Larval carcass', 'Larval salivary gland', 'Salivary gland', 'Midgut', 'Thoracicoabdominal ganglion', 'Mated spermatheca', 'Testis'] AND gene_microarrayresults.affyCall = 'Up'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_microarrayresults_tissue.name
ORDER BY gene.secondaryIdentifier ASC
