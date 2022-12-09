package com.application.foggy.documentnumbering;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentNumberingRepository extends MongoRepository<DocumentNumbering,String> {
}
