package com.sprint.naukri;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.controller.CandidateController;
import com.dao.CandidateDAO;
import com.dao.EmployerDAO;
import com.dao.InterviewDAO;
import com.dto.EmployerDTO;
import com.dto.ProfileDTO;
import com.dto.ProjectDTO;
import com.dto.RatingFeedbackDTO;
import com.dto.SkillDTO;
import com.enums.PostInterviewStatus;
import com.enums.PreInterviewStatus;
import com.exception.CandidateNotFoundException;
import com.exception.CandidateValidationExceptioncheck;
import com.model.Candidate;
import com.model.Employer;
import com.model.Interview;
import com.model.JwtRequest;
import com.model.JwtResponse;
import com.model.Project;
import com.model.Skill;
import com.service.CandidateService;


@SpringBootTest
public class CandidateControllerTest {

    @Autowired
	CandidateDAO candao ;
    @Autowired
	CandidateService candidateService;
    @Autowired
    CandidateController candidateController;
    @Autowired
    InterviewDAO interviewdao;
    @Autowired
    EmployerDAO employerDao;

    String commonToken;
	
	 Candidate cand1=new Candidate();
	
	 Candidate cand2 = new Candidate();
	
	 List<ProjectDTO> pdtl = new ArrayList<>();
     Set<SkillDTO> csdts = new HashSet<>() ;
	
	//rest controller test
	@BeforeEach
	void setUp() throws Exception {
	    
	    
		candao.deleteAll();
		interviewdao.deleteAll();

	
		cand1.setAge(21);
		List<Project> projectList = new ArrayList<>();
		
		cand1.setProjectList(projectList);
		cand1.setCandidateName("Yash");
		cand1.setEmailId("ysvjnskvn");
		pdtl.add(new ProjectDTO("yjbabv","happened"));
        pdtl.add(new ProjectDTO("slnacncs","happened"));

      ////
        
        cand2.setAge(22);
        cand2.setCandidateName("yash");
        cand2.setEducationQualification("B.tech");
        cand2.setExperience(2);
        cand2.setLocation("sfs");
        cand2.setEmailId("suraj@gmail.com");
        cand2.setPassword("121aaa");
        cand2.setProjectList(projectList);
     
        
        //Register Candidate
        String url = "http://localhost:9989/registerCandidate";
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        
        HttpEntity<Candidate> request = new HttpEntity<>(cand2, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        
        //Get The Token to access all the APIS
        JwtRequest jwtRequest = new JwtRequest("suraj@gmail.com", "121aaa");
        
        String url1 = "http://localhost:9989/authenticate";
        RestTemplate restTemplate1 = new RestTemplate();
        HttpHeaders headers1 = new HttpHeaders();
        HttpEntity<JwtRequest> request1 = new HttpEntity<>(jwtRequest, headers1);
        
        ResponseEntity<JwtResponse> response1 = restTemplate1.postForEntity(url1, request1, JwtResponse.class);
        
        JwtResponse resp = response1.getBody();
       
        commonToken = "Bearer " + resp.getJwtToken();

	}
	@AfterEach
	void setdown() throws Exception {
		
		candao.deleteAll();
	}
	
    
	
	@Test
    public void addProfiletest() throws URISyntaxException 
    {
		
	    ProfileDTO dto = new ProfileDTO();
        dto.setAge(22);
        dto.setCandidateName("yashss");
        dto.setEducationQualification("extra");
        dto.setEmailId("emailid");
        dto.setExperience(2);
        dto.setLocation("here");
        dto.setPassword("password");
        
        dto.setProjectDTOList(pdtl);
        
        dto.setSkillDTOSet(csdts);
        
        RestTemplate restTemplate = new RestTemplate();
  
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization", commonToken);

         headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
         
         HttpEntity entity=new HttpEntity(dto,headers);
         
//     System.out.println(cand1);
        ResponseEntity<String> result = restTemplate.exchange("http://localhost:9989/addProfile",
                HttpMethod.POST,entity,String.class);
         
        //Verify request succeed
       Assertions.assertEquals(202, result.getStatusCodeValue());
       Assertions.assertEquals("Candidate added successfully", result.getBody());
    }
	
	@Test
    public void addProfiletestfailed() throws URISyntaxException 
    {
	    ProfileDTO dto = new ProfileDTO();
        dto.setAge(2);
        
        dto.setEducationQualification("extra");
        dto.setEmailId("emailid");
        dto.setExperience(2);
        dto.setLocation("here");
        dto.setPassword("password");
        dto.setProjectDTOList(pdtl);
        
        dto.setSkillDTOSet(csdts);
        
        RestTemplate restTemplate = new RestTemplate();
  
        HttpHeaders headers=new HttpHeaders();
         
         headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
         
         headers.add("Authorization", commonToken);
         HttpEntity entity=new HttpEntity(dto,headers);
    
        
         
 

        Exception exception = Assertions.assertThrows(HttpClientErrorException.Forbidden.class, () -> {
            ResponseEntity<String> result = restTemplate.exchange("http://localhost:9989/addProfile",
                    HttpMethod.POST,entity,String.class);
        });

        String expectedMessage = "hey there check the docs for validation errors";
        String actualMessage = exception.getMessage();
          //System.out.println(actualMessage);
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
	
	
	 
//	/*
//	 * suraj method without transactional
	@Test
    public void addProjectbyIdTest() throws URISyntaxException, CandidateNotFoundException{
      
	 ProfileDTO dto = new ProfileDTO();
	        dto.setAge(22);
	        dto.setCandidateName("yashss");
	        dto.setEducationQualification("extra");
	        dto.setEmailId("emailid");
	        dto.setExperience(2);
	        dto.setLocation("here");
	        dto.setPassword("password");
	        
	        dto.setProjectDTOList(pdtl);
	        
	        dto.setSkillDTOSet(csdts);
	        String url  = "http://localhost:9989/addProfile";
	          RestTemplate template = new RestTemplate();
	          
	          HttpHeaders headers = new HttpHeaders();
	          headers.add("Authorization", commonToken);
	          HttpEntity<ProfileDTO> entity=new HttpEntity(dto,headers);

	          String response = template.exchange(url,  HttpMethod.POST, entity,String.class).getBody();
	          Assertions.assertEquals("Candidate added successfully", response);
	             Candidate c= candao.findByCandidateName("yashss");

	          
	          //real test starts from here
	          
	       
	          String url2 = "http://localhost:9989/addProjectById";
	          
	         
	          
	          
	          List<ProjectDTO> pdt = new ArrayList<>();
	           pdt.add(new ProjectDTO("kjsnv","skjnvk"));
	           HttpEntity<List<ProjectDTO>> entity2=new HttpEntity(pdt,headers);

	                  
	              ResponseEntity<String> result = template.exchange(url2,  HttpMethod.POST, entity2,String.class);
	              

	         
	          Assertions.assertEquals("Project added successfully",result.getBody());
	     
	}

	
	 @Test
	   public void updateLocationById() throws URISyntaxException, CandidateNotFoundException{
	     
	    ProfileDTO dto = new ProfileDTO();
	           dto.setAge(22);
	           dto.setCandidateName("yashkmlwfes");
	           dto.setEducationQualification("extra");
	           dto.setEmailId("csvsdv");
	           dto.setExperience(2);
	           dto.setLocation("here");
	           dto.setPassword("password");
	           List<ProjectDTO> pdt21 = new ArrayList<>();
	           Set<SkillDTO> csdt21 = new HashSet<>() ;
	           csdt21.add(new SkillDTO("Java"));
	           csdt21.add(new SkillDTO("Python"));
	           pdt21.add(new ProjectDTO("yjbabv","happened"));
	           pdt21.add(new ProjectDTO("slnacncs","happened"));
	           dto.setProjectDTOList(pdt21);
	           
	           dto.setSkillDTOSet(csdt21);
	           
	         String url  = "http://localhost:9989/addProfile";
	         RestTemplate template = new RestTemplate();
	         
	         HttpHeaders headers = new HttpHeaders();
	         headers.add("Authorization", commonToken);
	         HttpEntity<ProfileDTO> entity=new HttpEntity(dto,headers);

	         String response = template.exchange(url,  HttpMethod.POST, entity,String.class).getBody();
	         Assertions.assertEquals("Candidate added successfully", response);
	          
	         
	         //real test starts from here
	         
	         Candidate c= candao.findByCandidateName("yashkmlwfes");
//	         List<Project> p = candao.findByCandidateName("yashkmlwfes").getProjectList();
//	         System.out.println(p.get(0).getProjectId());
//	         System.out.println(p.get(0).getCandidate());

	         
	         
	         String url2 = "http://localhost:9989/updateLocation";
	         
	        
	         
	     
	          HttpEntity entity2=new HttpEntity("Location",headers);

	          template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
	          
	             ResponseEntity<String> result = template.exchange(url2,  HttpMethod.POST, entity2,String.class);

	         String expectedMessage = "Candidate updated location";
	         
	         Assertions.assertEquals(200, result.getStatusCodeValue());
	          Assertions.assertEquals("Candidate updated location", result.getBody());  
	   }
	 @Test
     public void addSkillByCandidateId() throws URISyntaxException, CandidateNotFoundException{
       
      ProfileDTO dto = new ProfileDTO();
             dto.setAge(22);
             dto.setCandidateName("yashkmlwfes");
             dto.setEducationQualification("extra");
             dto.setEmailId("csvsdv");
             dto.setExperience(2);
             dto.setLocation("here");
             dto.setPassword("password");
             List<ProjectDTO> pdt21 = new ArrayList<>();
             Set<SkillDTO> csdt21 = new HashSet<>() ;
             csdt21.add(new SkillDTO("Java"));
             csdt21.add(new SkillDTO("Python"));
             pdt21.add(new ProjectDTO("yjbabv","happened"));
             pdt21.add(new ProjectDTO("slnacncs","happened"));
             dto.setProjectDTOList(pdt21);
             
             dto.setSkillDTOSet(csdt21);
             
           String url  = "http://localhost:9989/addProfile";
           RestTemplate template = new RestTemplate();
           
           HttpHeaders headers = new HttpHeaders();
           headers.add("Authorization", commonToken);
           HttpEntity<ProfileDTO> entity=new HttpEntity(dto,headers);

           String response = template.exchange(url,  HttpMethod.POST, entity,String.class).getBody();
           Assertions.assertEquals("Candidate added successfully", response);
            
           
           //real test starts from here
           
           Candidate c= candao.findByCandidateName("yashkmlwfes");

           
           String url2 = "http://localhost:9989/addSkillToCandidate";
           
          SkillDTO sk = new SkillDTO("Klingon");
           
       
            HttpEntity entity2=new HttpEntity(sk,headers);

            template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            
               ResponseEntity<String> result = template.exchange(url2,  HttpMethod.PATCH, entity2,String.class);
       

           
           Assertions.assertEquals(200, result.getStatusCodeValue());
            Assertions.assertEquals("Candidate skill added succefully ", result.getBody());  
     }
  
	 
	 @Test
     public void removeSkillbyCanidateIdAndSkillName() throws URISyntaxException, CandidateNotFoundException{
       
      ProfileDTO dto = new ProfileDTO();
             dto.setAge(22);
             dto.setCandidateName("yashkmlwfes");
             dto.setEducationQualification("extra");
             dto.setEmailId("csvsdv");
             dto.setExperience(2);
             dto.setLocation("here");
             dto.setPassword("password");
             List<ProjectDTO> pdt21 = new ArrayList<>();
             Set<SkillDTO> csdt21 = new HashSet<>() ;
             csdt21.add(new SkillDTO("Java"));
             csdt21.add(new SkillDTO("Python"));
             pdt21.add(new ProjectDTO("yjbabv","happened"));
             pdt21.add(new ProjectDTO("slnacncs","happened"));
             dto.setProjectDTOList(pdt21);
             
             dto.setSkillDTOSet(csdt21);
             
           String url  = "http://localhost:9989/addProfile";
           RestTemplate template = new RestTemplate();
           
           HttpHeaders headers = new HttpHeaders();
           headers.add("Authorization", commonToken);
           HttpEntity<ProfileDTO> entity=new HttpEntity(dto,headers);

           String response = template.exchange(url,  HttpMethod.POST, entity,String.class).getBody();
           Assertions.assertEquals("Candidate added successfully", response);
            
           
           //real test starts from here
           
           Candidate c= candao.findByCandidateName("yashkmlwfes");
//         List<Project> p = candao.findByCandidateName("yashkmlwfes").getProjectList();
//         System.out.println(p.get(0).getProjectId());
//         System.out.println(p.get(0).getCandidate());

           
           
           String url2 = "http://localhost:9989/removeskillbyCanidateIdAndSkillName"+"/Java";
           
          
           
       
            HttpEntity entity2=new HttpEntity(headers);

            template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            
               ResponseEntity<String> result = template.exchange(url2,  HttpMethod.PATCH, entity2,String.class);
       

           
           Assertions.assertEquals(200, result.getStatusCodeValue());
            Assertions.assertEquals("Candidate skill removed succefully ", result.getBody());  
     }
  
	
	 @Test
     public void findCandidateById() throws URISyntaxException, CandidateNotFoundException{
       
      ProfileDTO dto = new ProfileDTO();
             dto.setAge(22);
             dto.setCandidateName("yashkmlwfes");
             dto.setEducationQualification("extra");
             dto.setEmailId("csvsdv");
             dto.setExperience(2);
             dto.setLocation("here");
             dto.setPassword("password");
             List<ProjectDTO> pdt21 = new ArrayList<>();
             Set<SkillDTO> csdt21 = new HashSet<>() ;
             csdt21.add(new SkillDTO("Java"));
             csdt21.add(new SkillDTO("Python"));
             pdt21.add(new ProjectDTO("yjbabv","happened"));
             pdt21.add(new ProjectDTO("slnacncs","happened"));
             dto.setProjectDTOList(pdt21);
             
             dto.setSkillDTOSet(csdt21);
             
           String url  = "http://localhost:9989/addProfile";
           RestTemplate template = new RestTemplate();
           
           HttpHeaders headers = new HttpHeaders();
           headers.add("Authorization", commonToken);
           HttpEntity<ProfileDTO> entity=new HttpEntity(dto,headers);

           String response = template.exchange(url,  HttpMethod.POST, entity,String.class).getBody();
           Assertions.assertEquals("Candidate added successfully", response);
            
           
           //real test starts from here
           
           Candidate c= candao.findByCandidateName("yashkmlwfes");
//         List<Project> p = candao.findByCandidateName("yashkmlwfes").getProjectList();
//         System.out.println(p.get(0).getProjectId());
//         System.out.println(p.get(0).getCandidate());

           
           
           String url2 = "http://localhost:9989/findCandidateById/"+c.getCandidateId();
           
           Candidate c1; 
       
            HttpEntity<Candidate> entity2=new HttpEntity(headers);

            template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            
               ResponseEntity<Candidate> result = template.exchange(url2,  HttpMethod.GET, entity2,Candidate.class);
       

           
           Assertions.assertEquals(302, result.getStatusCodeValue());
          //  Assertions.assertEquals(c.toString(), result.getBody().toString());  
     }
	
	
	   

     
     @Test
     public void deletecandidate() throws URISyntaxException, CandidateNotFoundException{
       
      ProfileDTO dto = new ProfileDTO();
             dto.setAge(22);
             dto.setCandidateName("yashkmlwfes");
             dto.setEducationQualification("extra");
             dto.setEmailId("csvsdv");
             dto.setExperience(2);
             dto.setLocation("here");
             dto.setPassword("password");
             List<ProjectDTO> pdt21 = new ArrayList<>();
             Set<SkillDTO> csdt21 = new HashSet<>() ;
             csdt21.add(new SkillDTO("Java"));
             csdt21.add(new SkillDTO("Python"));
             pdt21.add(new ProjectDTO("yjbabv","happened"));
             pdt21.add(new ProjectDTO("slnacncs","happened"));
             dto.setProjectDTOList(pdt21);
             
             dto.setSkillDTOSet(csdt21);
             
           String url  = "http://localhost:9989/addProfile";
           RestTemplate template = new RestTemplate();
           
           HttpHeaders headers = new HttpHeaders();
           headers.add("Authorization", commonToken);
           HttpEntity<ProfileDTO> entity=new HttpEntity(dto,headers);

           String response = template.exchange(url,  HttpMethod.POST, entity,String.class).getBody();
           Assertions.assertEquals("Candidate added successfully", response);
            
           
           //real test starts from here
           
           Candidate c= candao.findByCandidateName("yashkmlwfes");
//     
           
          int Beforelength =(int) candao.count(); 
           
           
           String url2 = "http://localhost:9989/deletecandidate/"+c.getCandidateId();
           
         
            HttpEntity entity2=new HttpEntity(headers);

            template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            
               ResponseEntity<String> result = template.exchange(url2,  HttpMethod.DELETE, entity2,String.class);
               int Afterlength =(int) candao.count(); 

           
           Assertions.assertEquals(302, result.getStatusCodeValue());
           Assertions.assertEquals("Candidate delete successfully", result.getBody());  
           Assertions.assertNotEquals(Beforelength, Afterlength);
     }
     @Test
   public void feedbackRating() throws URISyntaxException, CandidateNotFoundException{
     
    ProfileDTO dto = new ProfileDTO();
           dto.setAge(22);
           dto.setCandidateName("yashkmlwfes");
           dto.setEducationQualification("extra");
           dto.setEmailId("csvsdv");
           dto.setExperience(2);
           dto.setLocation("here");
           dto.setPassword("password");
           List<ProjectDTO> pdt21 = new ArrayList<>();
           Set<SkillDTO> csdt21 = new HashSet<>() ;
           csdt21.add(new SkillDTO("Java"));
           csdt21.add(new SkillDTO("Python"));
           pdt21.add(new ProjectDTO("yjbabv","happened"));
           pdt21.add(new ProjectDTO("slnacncs","happened"));
           dto.setProjectDTOList(pdt21);
           
           dto.setSkillDTOSet(csdt21);
           
         String url  = "http://localhost:9989/addProfile";
         RestTemplate template = new RestTemplate();
         
         
         HttpHeaders headers = new HttpHeaders();
         headers.add("Authorization", commonToken);
         HttpEntity<ProfileDTO> entity=new HttpEntity(dto,headers);

         String response = template.exchange(url,  HttpMethod.POST, entity,String.class).getBody();
         Assertions.assertEquals("Candidate added successfully", response);
          
         
         //real test starts from here
         
         Candidate c= candao.findByCandidateName("yashkmlwfes");
//       List<Project> p = candao.findByCandidateName("yashkmlwfes").getProjectList();
//       System.out.println(p.get(0).getProjectId());
//       System.out.println(p.get(0).getCandidate());
         List<Interview> interviews = new ArrayList<>();
         Interview x = new Interview();
         x.setCandidate(c);
         x.setInterviewId(1);
         x.setPostInterviewStatus(PostInterviewStatus.SELECTED);
         x.setPreInterviewStatus(PreInterviewStatus.SHORTLISTED);
         interviews.add(x);
         candao.save(c);
         interviewdao.save(x);
         
         c.setInterviewList(interviews);
         
         candao.save(c);
         
         
         String url2 = "http://localhost:9989/candidateFeedbackRating/"+c.getInterviewList().get(0).getInterviewId();
         
         RatingFeedbackDTO rto =new RatingFeedbackDTO();
         rto.setFeedback("FeedBAck");
         rto.setRating(5);
     
          HttpEntity<RatingFeedbackDTO> entity2=new HttpEntity(rto,headers);

          template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
          
             ResponseEntity<String> result = template.exchange(url2,  HttpMethod.POST, entity2,String.class);
     

         
         Assertions.assertEquals(200, result.getStatusCodeValue());
         Assertions.assertEquals("Feedback and rating by candidate saved", result.getBody());  
         
         
         String url21 = "http://localhost:9989/feedbackRating/"+c.getInterviewList().get(0).getInterviewId();
         
    
          
         ResponseEntity<String> result1 = template.exchange(url21,  HttpMethod.POST, entity2,String.class);
     

         
         Assertions.assertEquals(200, result1.getStatusCodeValue());
         Assertions.assertEquals("Feedback and rating by employer saved", result1.getBody());  
         
         
         
   }
    

}

