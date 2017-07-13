MATCH (cdnaclone :CDNAClone)
OPTIONAL MATCH (cdnaclone)-[:gene]-(cdnaclone_gene :Gene)
WHERE ANY (key in keys(cdnaclone) WHERE cdnaclone[key]='LD14383')
RETURN cdnaclone.primaryIdentifier,
cdnaclone_gene.primaryIdentifier,
cdnaclone_gene.secondaryIdentifier,
cdnaclone_gene.symbol
ORDER BY cdnaclone.primaryIdentifier ASC
