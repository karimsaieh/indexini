package tn.insat.pfe.sparkmanagerservice.services;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import tn.insat.pfe.sparkmanagerservice.dtos.SparkStatsDto;
import tn.insat.pfe.sparkmanagerservice.entities.Job;
import tn.insat.pfe.sparkmanagerservice.repositories.IJobRepository;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SparkManagerServiceTests {


    @MockBean
    private IJobRepository jobRepository;

    @Autowired
    private ISparkManagerService sparkManagerService;

    @Test
    public void sparkStatsTest() {
        Date date  = new Date();
        given(this.jobRepository.findFirst1ByOrderByDateDesc()).willReturn(new Job(date, 20 , 30));
        given(this.jobRepository.count()).willReturn((long)60);
        SparkStatsDto sparkStatsDto = this.sparkManagerService.sparkStats();
        assertThat(sparkStatsDto).isEqualTo(new SparkStatsDto((long)60, date, 20, 30));
    }


}
