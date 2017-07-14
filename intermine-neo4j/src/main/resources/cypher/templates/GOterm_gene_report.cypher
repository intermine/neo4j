MATCH (goterm :GOTerm),
(goterm)-[:ANNOTATES]-(goterm_ontologyannotations :OntologyAnnotation),
(goterm_ontologyannotations)-[:subject]-(goterm_ontologyannotations_subject :BioEntity)

WHERE goterm.identifier = 'GO:0001965'
RETURN goterm.name,
goterm_ontologyannotations_subject.primaryIdentifier,
goterm_ontologyannotations_subject.symbol
ORDER BY goterm.name ASC
