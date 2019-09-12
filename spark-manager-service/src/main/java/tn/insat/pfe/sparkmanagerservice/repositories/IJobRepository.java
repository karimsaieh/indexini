package tn.insat.pfe.sparkmanagerservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.insat.pfe.sparkmanagerservice.entities.Job;

public interface IJobRepository extends MongoRepository<Job, String> {

    Job findFirst1ByOrderByDateDesc();

}
