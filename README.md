# Bloom filter

### TL;DR
This repo aims to be a PoC for use guava `bloom filter` using postgres to verify positives. 

### Postgres in docker

```
$ docker run --name local-postgres -p 5432:5432 -e POSTGRES_USER=bloom_filter -e POSTGRES_PASSWORD=bloom_filter -d postgres
```

### Operations

I implemented the next operations:
- Load from disk when start and when we want, considering that it is not possible delete from bloomfilter
- Save and load to/from file
- Add entry to bloom filter (and database)
- Verify entry
- Delete entry from database (need manual rebuild bloom filter to get this kind of changes)

### TODO
Implement using redis `redisson` lib

### References

- https://medium.com/movile-tech/uso-de-estruturas-de-dados-probabil%C3%ADsticas-na-movile-bloom-filters-9f38f5f34df9
- https://www.baeldung.com/guava-bloom-filter
- https://gvnix.medium.com/bloom-filters-in-action-ea7304463fc6
- https://gvnix.medium.com/bloom-filter-using-redis-9ceec7e85f86
 