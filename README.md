## Searchable Encryption for Genomes

## Requirements

Java 21, Maven, Docker (Compose).

## Running

Firstly a compilation is needed, with `mvn clean package` it should be done.

After it, the system should be turned on with `docker compose up`.

To end it, run a `Ctrl + C`.

## Usage

### Registering a genome

```
curl --location 'localhost:3000/genomes/register' \
--header 'Content-Type: application/json' \
--data '{
    "genome":"<Genome of ATCG>",
    "description":"Description"
}'
```

### Listing the genomes

```
curl --location 'localhost:3000/genomes'
```

### Searching the indexes

```
curl --location 'localhost:3000/genomes/indexes' \
--header 'Content-Type: application/json' \
--data '{
    "genomeId":"<Genome identifier>",
    "pattern":"<Pattern of ATCG with length of 30>"
}'
```

