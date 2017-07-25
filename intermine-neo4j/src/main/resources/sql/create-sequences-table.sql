-- Create the sequences table in a Postgres database for storing sequence large object oids.

CREATE TABLE sequences (
  id          int    PRIMARY KEY,
  length      int    NOT NULL,
  md5checksum text,
  residues    oid    NOT NULL
);
