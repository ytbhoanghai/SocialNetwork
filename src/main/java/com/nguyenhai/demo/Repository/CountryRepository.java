package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.Country;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends MongoRepository<Country, String> {
}
