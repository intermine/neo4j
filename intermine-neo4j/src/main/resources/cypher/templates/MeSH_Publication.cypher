MATCH (meshterm :MeshTerm),
(meshterm)-[]-(meshterm_publications :publications),
(meshterm_publications)-[]-(meshterm_publications_volume :volume),
(meshterm_publications)-[]-(meshterm_publications_pages :pages),
(meshterm_publications)-[]-(meshterm_publications_journal :journal),
(meshterm_publications)-[]-(meshterm_publications_year :year),
(meshterm_publications)-[]-(meshterm_publications_title :title),
(meshterm_publications)-[]-(meshterm_publications_pubmedid :pubMedId),
(meshterm_publications)-[]-(meshterm_publications_firstauthor :firstAuthor)

WHERE meshterm.name CONTAINS 'Ataxia'
RETURN meshterm.name
ORDER BY meshterm.name ASC
