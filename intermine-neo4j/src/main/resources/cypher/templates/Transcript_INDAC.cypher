MATCH (transcript :Transcript),
(transcript)-[]-(transcript_microarrayoligos :microarrayOligos),
(transcript_microarrayoligos)-[]-(transcript_microarrayoligos_sequence :sequence)

WHERE ANY (key in keys(transcript) WHERE transcript[key]='CG10000-RA')
RETURN transcript.primaryIdentifier,
transcript_microarrayoligos.primaryIdentifier,
transcript_microarrayoligos.tm,
transcript_microarrayoligos.length,
transcript_microarrayoligos_sequence.residues
ORDER BY transcript.primaryIdentifier ASC
