package com.sprint.naukri;

import static org.junit.jupiter.api.Assertions.*;



import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.controller.CandidateController;
import com.controller.JobController;
import com.dao.JobDAO;
import com.dto.EmployerDTO;
import com.dto.JobDTO;
import com.exception.NoSuchEmployerFoundException;
import com.model.Employer;
import com.model.Job;
import com.service.EmployerService;
import com.service.JobService;

@SpringBootTest
class JobControllerMethodTest {

	@Autowired
	JobService jobService;
	
	@Autowired
	JobDAO jobDao;
	
	@Autowired
	JobController jobControl;
	
	@Autowired
	CandidateController candidateController;
	
	@Autowired
	EmployerService employerService;
	
	EmployerDTO emplDTO;
	Employer emploCurr;
	
	JobDTO jobDTO;
	Job job;
	
	
	@BeforeEach
	void intializeAll() throws NoSuchEmployerFoundException {
		
		employerService.deleteAllEmployer();
		
		emplDTO = new EmployerDTO("Pankaj Patil", "Mumbai", "pankaj121@gmail.com", "121aaa");
		
		employerService.addEmployer(emplDTO);
		
		emploCurr = employerService.getEmployerByEmailId(emplDTO.getEmailId());
	}
	
	@Test
	public void testGetAllJobs() throws NoSuchEmployerFoundException {
		
		//Create a Job
		
		jobDTO = new JobDTO(emploCurr.getEmployerId(), "Need a Skill Java, Web Development and Angular", "Capgemini", "Pune", 120030.45f);
		
		Job job = new Job(jobDTO.getJobDescription(), jobDTO.getLocation(), jobDTO.getLocation(), jobDTO.getSalaryPackage());
		job.setCreatedBy(emploCurr);
		jobDao.save(job);
		
		List<Job> actual = candidateController.getAllJobs().getBody();
		
		List<Job> expected = jobService.getAllJob();
		
		assertEquals(expected.toString(), actual.toString());
		
	}
	
	
	@Test
	public void testAddJobManually() {
		
		try {
			
			jobService.deleteAllJob();
		
			jobDTO = new JobDTO(emploCurr.getEmployerId(), "Angular and Java", "Wipro", "Mumbai", 1213430.45f);
			//jobService.addJobByEmployer(jobDTO);
			
			ResponseEntity<String> actual = jobControl.addJobManually(jobDTO);
		
			Job currentJob = jobService.getAllJob().get(0);
			
			assertEquals("Job Added Successfully", actual.getBody().toString());
			assertEquals(jobDTO.getJobDescription(), currentJob.getJobDescription());
			assertEquals(jobDTO.getSalaryPackage(), currentJob.getSalaryPackage());
		}
		catch (Exception e) {
			assertTrue(false);
		}
		
	}
	
	@Test
	void testUpdateJobDescription() {
		
		jobService.deleteAllJob();
		
		jobDTO = new JobDTO(emploCurr.getEmployerId(), "Angular and Java", "Wipro", "Mumbai", 1213430.45f);
		jobService.addJobByEmployer(jobDTO);
		
		Job currentJob = jobService.getAllJob().get(0);
		
		String expected = "Angular and Presentation Skills";
		ResponseEntity<String> response = jobControl.updateJob(String.valueOf(currentJob.getJobId()), expected);
		
		Job actual = jobService.getAllJob().get(0);
		
		assertEquals("Job updated Successfuly", response.getBody().toString());
		assertEquals(expected, actual.getJobDescription());
	}
	
	
	@Test
	void testDeleteJob() {
		
		jobService.deleteAllJob();
		
		jobDTO = new JobDTO(emploCurr.getEmployerId(), "Angular and Java", "Wipro", "Mumbai", 1213430.45f);
		jobService.addJobByEmployer(jobDTO);
		
		jobDTO = new JobDTO(emploCurr.getEmployerId(), "Presentation Skills", "TCS", "Chennai", 2456655.45f);
		jobService.addJobByEmployer(jobDTO);
		
		Job currentJob = jobService.getAllJob().get(0);
		
		ResponseEntity<String> response = jobControl.deleteJobById(String.valueOf(currentJob.getJobId()));
		
		int expected = 1;
		int actual = jobService.getAllJob().size();
		
		assertEquals("Job deleted successfully", response.getBody().toString());
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetJobsByIndustry() {
		
		jobService.deleteAllJob();
		
		jobDTO = new JobDTO(emploCurr.getEmployerId(), "Angular and Java", "Wipro", "Mumbai", 1213430.45f);
		jobService.addJobByEmployer(jobDTO);
		
		JobDTO expect = new JobDTO(emploCurr.getEmployerId(), "Presentation Skills", "TCS", "Chennai", 2456655.45f);
		jobService.addJobByEmployer(expect);
		
		ResponseEntity<List<Job>> actual = candidateController.getJobsByIndustry("TCS");
		
		assertEquals(200, actual.getStatusCodeValue());
		assertEquals(expect.getIndustry(), actual.getBody().get(0).getIndustry());
		
	}
	
	@Test
	void testGetJobsByLocation() {
		
		jobService.deleteAllJob();
		
		jobDTO = new JobDTO(emploCurr.getEmployerId(), "Angular and Java", "Wipro", "Mumbai", 1213430.45f);
		jobService.addJobByEmployer(jobDTO);
		
		JobDTO expect = new JobDTO(emploCurr.getEmployerId(), "Presentation Skills", "TCS", "Chennai", 2456655.45f);
		jobService.addJobByEmployer(expect);
		
		ResponseEntity<List<Job>> actual = candidateController.getJobsByLocation("Chennai");
		
		assertEquals(200, actual.getStatusCodeValue());
		assertEquals(expect.getLocation(), actual.getBody().get(0).getLocation());
		
	}
	
	@Test
	void testGetAllJobBySkill() {
		
		jobService.deleteAllJob();
		
		jobDTO = new JobDTO(emploCurr.getEmployerId(), "Angular and Java", "Wipro", "Mumbai", 1213430.45f);
		jobService.addJobByEmployer(jobDTO);
		
		JobDTO expect = new JobDTO(emploCurr.getEmployerId(), "Presentation Skills and Public Speaking", "TCS", "Chennai", 2456655.45f);
		jobService.addJobByEmployer(expect);
		
		List<Job> actual = candidateController.getAllJobBySkill("Public Speaking");
		
		assertEquals(expect.getJobDescription(), actual.get(0).getJobDescription());
		
	}

}
