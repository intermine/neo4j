MATCH (gene :Gene),
      (gene)-[]-(gene_homologues :homologues),
      (gene_homologues)-[]-(gene_homologues_datasets :dataSets),
      (gene_homologues)-[]-(gene_homologues_homologue :homologue),
      (gene_homologues_homologue)-[]-(gene_homologues_homologue_goannotation :goAnnotation),
      (gene_homologues_homologue_goannotation)-[]-(gene_homologues_homologue_goannotation_evidence :evidence),
      (gene_homologues_homologue_goannotation_evidence)-[]-(gene_homologues_homologue_goannotation_evidence_code :code),
      (gene_homologues_homologue_goannotation_evidence_code)-[]-(gene_homologues_homologue_goannotation_evidence_code_code :code),
      (gene_homologues_homologue_goannotation)-[]-(gene_homologues_homologue_goannotation_ontologyterm :ontologyTerm),
      (gene_homologues_homologue)-[]-(gene_homologues_homologue_organism :organism)

    WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG6235') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_homologues_homologue_organism.name = 'Anopheles gambiae'
RETURN gene.secondaryIdentifier,
       gene.symbol,
       gene_homologues_homologue.primaryIdentifier,
       gene_homologues_homologue.symbol,
       gene_homologues.type,
       gene_homologues_homologue_organism.name,
       gene_homologues_datasets.name,
       gene_homologues_homologue_goannotation_ontologyterm.name
    ORDER BY gene.secondaryIdentifier ASC
