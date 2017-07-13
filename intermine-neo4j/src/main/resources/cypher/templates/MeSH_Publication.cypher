MATCH (meshterm :MeshTerm),
(meshterm)-[:MENTIONED_IN]-(meshterm_publications :Publication)

WHERE meshterm.name CONTAINS 'Ataxia'
RETURN meshterm.name,
meshterm_publications.firstAuthor,
meshterm_publications.title,
meshterm_publications.year,
meshterm_publications.journal,
meshterm_publications.volume,
meshterm_publications.pages,
meshterm_publications.pubMedId
ORDER BY meshterm.name ASC
