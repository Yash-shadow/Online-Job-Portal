package com.sprint.naukri;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.controller.AuthenticationController;
import com.controller.CandidateController;
import com.dao.CandidateDAO;
import com.dto.ProfileDTO;
import com.dto.ProjectDTO;
import com.dto.RatingFeedbackDTO;
import com.dto.SkillDTO;
import com.enums.PostInterviewStatus;
import com.enums.PreInterviewStatus;
import com.exception.CandidateNotFoundException;
import com.exception.CandidateValidationExceptioncheck;
import com.exception.FormatException;
import com.exception.NoSuchInterviewFoundException;
import com.exception.NullValueException;
import com.exception.ProjectNotFoundException;
import com.exception.feedbackException;
import com.exception.skillNotFoundException;
import com.model.Candidate;
import com.model.Interview;
import com.model.JwtRequest;
import com.model.JwtResponse;
import com.model.Project;
import com.model.Skill;
import com.service.CandidateService;
import com.service.SkillService;


@SpringBootTest
public class CandidateControllerMethodTests {
    @Autowired
    CandidateDAO candao ;
    @Autowired
    CandidateService candidateService;
    @Autowired
    CandidateController candidateController;
    
    @Autowired
    SkillService skillService;
    
    @Autowired
    AuthenticationController authController;

   
    
    String commonToken;
    
    Candidate candidate = new Candidate();
    
     Candidate cand1=new Candidate();
     
     Candidate cand2 = new Candidate();
     
     ProfileDTO dto = new ProfileDTO();
     
     List<ProjectDTO> pdtl = new ArrayList<>();
     Set<SkillDTO> csdts = new HashSet<>() ;
    
    //rest controller test
    @BeforeEach
    void setUp() throws Exception {
    

        candao.deleteAll();
        
        //project dto
	    
        pdtl.add(new ProjectDTO("yjbabv","happened"));
        pdtl.add(new ProjectDTO("slnacncs","happened"));
        
        dto.setAge(22);
        dto.setCandidateName("yashs");
        dto.setEducationQualification("extra");
        dto.setEmailId("emailid");
        dto.setExperience(2);
        dto.setLocation("here");
        dto.setPassword("password");

//        csdts.add(new SkillDTO("Java"));
        dto.setProjectDTOList(pdtl);
        
        dto.setSkillDTOSet(csdts);
        
        candidateService.addProfile(dto);
       
    
        cand1.setAge(21);
        List<Project> projectList = new ArrayList<>();
        
        cand1.setProjectList(projectList);
        cand1.setCandidateName("Yash");
        cand1.setEmailId("ysvjnskvn");
        
      ////
        
        cand2.setAge(22);
        cand2.setCandidateName("yash");
        cand2.setEducationQualification("B.tech");
        cand2.setExperience(2);
        cand2.setLocation("sfs");
        cand2.setEmailId("suraj@gmail.com");
        cand2.setPassword("121aaa");
        cand2.setProjectList(projectList);
        

        JwtRequest jwtRequest = new JwtRequest(dto.getEmailId(), dto.getPassword());
      
            JwtResponse jwtResponse = authController.authenticateCand(jwtRequest);
            commonToken = "Bearer " + jwtResponse.getJwtToken();
//            System.out.println(commonToken);

        
    }
    @AfterEach
    void setdown() throws Exception {
        candao.deleteAll();
        
        
    }
    
    @Test
    public void addProfiletest() throws URISyntaxException 
    {
        ProfileDTO dto1 = new ProfileDTO("yasnjnda",22,22,"sdfsdf","sdfsd","adsfsd","sdfsdf",new ArrayList<>(),new HashSet<>());

       
        
    
      
         
   
        ResponseEntity<?> result = candidateController.addProfile(dto1);
         
        //Verify request succeed
       Assertions.assertEquals(202, result.getStatusCodeValue());
       Assertions.assertEquals("Candidate added successfully", result.getBody());
    }
    @Test
    public void addProfiletestfailed() throws URISyntaxException 
    {
        
        
        ProfileDTO dto1 = new ProfileDTO("yasnjnda",2,2,"sdfsdf","sdfsd","adsfsd","sdfsdf",new ArrayList<>(),new HashSet<>());
        
        

        Exception exception = Assertions.assertThrows(CandidateValidationExceptioncheck.class, () -> {
            
           
            ResponseEntity<?> result = candidateController.addProfile(dto1);
        });

        String expectedMessage = "hey there check the docs for validation errors";
        String actualMessage = exception.getMessage();
          //System.out.println(actualMessage);
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
    
    
    
     
//  /*
//   * suraj method without transactional
    @Transactional
    @Test
    public void addProjectbyIdTest() throws URISyntaxException, CandidateNotFoundException, FormatException, NullValueException{
 
      
             Candidate c= candao.findByCandidateName("yashs");

          
          //real test starts from here
          
       
          String url2 = "http://localhost:9989/addProjectById";
          
         
          
          
          List<ProjectDTO> pdt = new ArrayList<>();
           pdt.add(new ProjectDTO("kjsnv","skjnvk"));


           MockHttpServletRequest request = new MockHttpServletRequest();
           request.addHeader("Authorization", commonToken);

              ResponseEntity<String> result = candidateController.addProject(request, pdt);
         
          Assertions.assertEquals("Project added successfully",result.getBody());
     
    }
   

    



    @Test
    @Transactional
    public void removeProjectByProjectId() throws URISyntaxException, CandidateNotFoundException, ProjectNotFoundException, FormatException{
      
     
                   
        Candidate c= candao.findByCandidateName("yashs");
        
                
            ResponseEntity<String> result = candidateController.removeProjectByProjectId(c.getProjectList().get(0).getProjectId()+"");
         

        String expectedMessage = "Candidate project removed successfully";
       
          //System.out.println(actualMessage);
        Assertions.assertTrue(expectedMessage.contains(result.getBody()));
    }
    @Test
    @Transactional
    public void removeProjectByProjectIdfalied() throws URISyntaxException, CandidateNotFoundException, ProjectNotFoundException, FormatException{
      
     
                   
        Candidate c= candao.findByCandidateName("yashs");
        
                
         
            Exception exception = Assertions.assertThrows(FormatException.class, () -> {
                ResponseEntity<String> result = candidateController.removeProjectByProjectId(c.getProjectList().get(0).getProjectId()+"a");
                    });

            String expectedMessage = "Check the format of the input or wrong user id";
            String actualMessage = exception.getMessage();
              //System.out.println(actualMessage);
            Assertions.assertTrue(actualMessage.contains(expectedMessage));
            }
    
    @Test
    @Transactional
    public void removeProjectByProjectNameTEst() throws URISyntaxException, CandidateNotFoundException, ProjectNotFoundException, FormatException{
      
     
                   
        Candidate c= candao.findByCandidateName("yashs");
        

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", commonToken);
            ResponseEntity<String> result = candidateController.removeProjectByProjectName(request,"yjbabv");
         

        String expectedMessage = "Candidate project removed successfully";
       
          //System.out.println(actualMessage);
        Assertions.assertTrue(expectedMessage.contains(result.getBody()));
    }
    
    
    
    
 

 
    @Test
    public void updateLocationById() throws URISyntaxException, CandidateNotFoundException{
         
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization",commonToken );

             //real test starts from here
             
             Candidate c= candao.findByCandidateName("yashs");


              
             ResponseEntity result = candidateController.updateLocationById(request,"Location");
         

             String expectedMessage = "Candidate updated location";
             
             Assertions.assertEquals(200, result.getStatusCodeValue());
             Assertions.assertEquals("Candidate updated location", result.getBody());  
       }
     
     
     @Test
     @Transactional
     public void addSkillByCandidateId() throws URISyntaxException, CandidateNotFoundException{
       
      
           
           Candidate c= candao.findByCandidateName("yashs");
           
          SkillDTO sk = new SkillDTO("Klingon");
          MockHttpServletRequest request = new MockHttpServletRequest();
          request.addHeader("Authorization",commonToken );
       
          ResponseEntity<String> result = candidateController.addSkillByCandidateId(request,sk);
       

           
           Assertions.assertEquals(200, result.getStatusCodeValue());
            Assertions.assertEquals("Candidate skill added succefully ", result.getBody());  
     }
  
     

     @Test
     @Transactional
     public void removeSkillbyCanidateIdAndSkillName() throws URISyntaxException, CandidateNotFoundException, NumberFormatException, skillNotFoundException{
       
    
         MockHttpServletRequest request = new MockHttpServletRequest();
         request.addHeader("Authorization",commonToken );
         
           
           Candidate c= candao.findByCandidateName("yashs");
           Set<Skill> skillSet = new HashSet<>();
           skillSet.add(new Skill("JAVA"));
           c.setSkillSet(skillSet);
           candao.save(c);
           
           
          
           
  

            
         ResponseEntity<String> result = candidateController.removeSkillbyCanidateIdAndSkillName(request, "java");

           
           Assertions.assertEquals(200, result.getStatusCodeValue());
            Assertions.assertEquals("Candidate skill removed succefully ", result.getBody());  
     }

    
     @Test
     @Transactional
     public void findCandidateById() throws URISyntaxException, CandidateNotFoundException{
       
     
    
           
           Candidate c= candao.findByCandidateName("yashs");

           
    
       

            
               ResponseEntity<Candidate> result = candidateController.getCandidatebyid(c.getCandidateId());
       

           
           Assertions.assertEquals(302, result.getStatusCodeValue());
          //  Assertions.assertEquals(c.toString(), result.getBody().toString());  
     }
    
    

     
     @Test
     @Transactional
     public void deletecandidate() throws URISyntaxException, CandidateNotFoundException{
       
    
          
           Candidate c= candao.findByCandidateName("yashs");
//     
           
          int Beforelength =(int) candao.count(); 
           
           
           String url2 = "http://localhost:9989/deletecandidate/"+c.getCandidateId();
           
         
       
            
               ResponseEntity<String> result = candidateController.deleteCandidatebyid(c.getCandidateId());
               int Afterlength =(int) candao.count(); 

           
           Assertions.assertEquals(302, result.getStatusCodeValue());
           Assertions.assertEquals("Candidate delete successfully", result.getBody());  
           Assertions.assertNotEquals(Beforelength, Afterlength);
     }
    

   @Test
   @Transactional
   public void feedbackRating() throws URISyntaxException, CandidateNotFoundException, NumberFormatException, NoSuchInterviewFoundException, feedbackException{
     
    
         
         Candidate c= candao.findByCandidateName("yashs");

         List<Interview> interviews = new ArrayList<>();
         Interview x = new Interview();
         x.setCandidate(c);
         x.setInterviewId(1);
         x.setPostInterviewStatus(PostInterviewStatus.SELECTED);
         x.setPreInterviewStatus(PreInterviewStatus.SHORTLISTED);
         interviews.add(x);
         
         c.setInterviewList(interviews);
         
         candao.save(c);
         
         
         
         RatingFeedbackDTO rto =new RatingFeedbackDTO();
         rto.setFeedback("FeedBAck");
         rto.setRating(5);
     

          
        ResponseEntity<String> result = candidateController.feedbackRating(x.getInterviewId()+"", rto);
     

         
         Assertions.assertEquals(200, result.getStatusCodeValue());
         Assertions.assertEquals("Feedback and rating by candidate saved", result.getBody());  
   }
    

    

}

