package com.shellshellfish.aaas.repositories;


import com.shellshellfish.aaas.model.Car;
import org.springframework.data.repository.CrudRepository;

public interface CarMongoRepository extends CrudRepository<Car, String> {}