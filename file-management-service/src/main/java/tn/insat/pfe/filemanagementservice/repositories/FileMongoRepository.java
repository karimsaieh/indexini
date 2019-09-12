package tn.insat.pfe.filemanagementservice.repositories;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Repository;
import java.util.List;

import tn.insat.pfe.filemanagementservice.dtos.FileTypesGetDto;
import tn.insat.pfe.filemanagementservice.entities.File;


@Repository
public class FileMongoRepository implements IFileMongoRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<FileTypesGetDto> countFileTypes() {
        Aggregation agg = newAggregation(
                group("contentType").count().as("count"),
                project("count").and("contentType").previousOperation()
        );

        //Convert the aggregation result into a List
        AggregationResults<FileTypesGetDto> groupResults
                = mongoTemplate.aggregate(agg, File.class, FileTypesGetDto.class);
        return  groupResults.getMappedResults();
    }
}
