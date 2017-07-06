MATCH (proteindomain :ProteinDomain)

WHERE proteindomain.name = '*leucine*'
RETURN proteindomain.primaryIdentifier,
proteindomain.name,
proteindomain.type
ORDER BY proteindomain.primaryIdentifier ASC
