MATCH (transcript :Transcript),
(transcript)-[]-(transcript_microarrayoligos :microarrayOligos),
(transcript_microarrayoligos)-[]-(transcript_microarrayoligos_sequence :sequence),
(transcript_microarrayoligos_sequence)-[]-(transcript_microarrayoligos_sequence_residues :residues),
(transcript_microarrayoligos)-[]-(transcript_microarrayoligos_tm :tm)

WHERE ANY (key in keys(transcript) WHERE transcript[key]='CG10000-RA')
RETURN transcript.primaryIdentifier,
transcript_microarrayoligos.primaryIdentifier,
transcript_microarrayoligos.length
ORDER BY transcript.primaryIdentifier ASC
