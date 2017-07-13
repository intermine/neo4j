MATCH (gene :Gene),
(gene)-[:rnaSeqResults]-(gene_rnaseqresults :RNASeqResult)

WHERE gene_rnaseqresults.stage IN ['L3 CNS', 'A MateF 4d head', 'A MateF 1d head', 'A MateM 1d head', 'L3 Wand saliv', 'CNS ML-DmBG1-c1', 'A VirF 20d head', 'A MateM 4d testis', 'L3 Wand dig sys', 'L3 Wand carcass', 'A MateM 4d acc gland', 'A MateM 4d head', 'CNS ML-DmBG2-c2', 'L3 Wand fat', 'L3 Wand imag disc', 'A VirF 4d head', 'A MateM 20d head', 'A MateF 20d head', 'A VirF 1d head'] AND gene_rnaseqresults.expressionLevel IN ['Extremely high expression', 'Moderately high expression', 'High level expression', 'Very high expression', 'High expression']
RETURN gene.symbol,
gene_rnaseqresults.stage,
gene_rnaseqresults.expressionLevel,
gene_rnaseqresults.expressionScore
ORDER BY gene.symbol ASC
