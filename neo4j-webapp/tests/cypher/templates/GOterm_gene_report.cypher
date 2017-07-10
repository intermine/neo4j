MATCH (goterm :GOTerm),
(goterm)-[]-(goterm_ontologyannotations :ontologyAnnotations),
(goterm_ontologyannotations)-[]-(goterm_ontologyannotations_subject :subject)

WHERE goterm.identifier = 'GO:0001965'
RETURN goterm.name,
goterm_ontologyannotations_subject.primaryIdentifier,
goterm_ontologyannotations_subject.symbol
ORDER BY goterm.name ASC
