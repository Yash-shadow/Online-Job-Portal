package com.sprint.naukri;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.dao.CandidateDAO;
import com.dao.ProjectDAO;
import com.dao.SkillDAO;
import com.dto.ProfileDTO;
import com.dto.ProjectDTO;
import com.dto.RatingFeedbackDTO;
import com.dto.SkillDTO;
import com.enums.PostInterviewStatus;
import com.enums.PreInterviewStatus;
import com.exception.CandidateNotFoundException;
import com.exception.CandidateValidationExceptioncheck;
import com.exception.feedbackException;
import com.exception.skillNotFoundException;
import com.model.Candidate;
import com.model.Interview;
import com.model.JwtRequest;
import com.model.JwtResponse;
import com.model.Project;
import com.model.Skill;
import com.service.CandidateService;
import com.service.InterviewService;
import com.service.ProjectService;

@SpringBootTest
public class AllTransactionalCandidateServiceTest {
    @Autowired
    CandidateDAO candao ;
    @Autowired
    CandidateService candidateService;
    @Autowired
    SkillDAO sdao;
    @Autowired
    ProjectService projectService;
    @Autowired
    ProjectDAO projectdao;
    @Autowired
    SkillDAO skilldao; 
    @Autowired
    InterviewService interviewService;
    
    String commonToken;
    
     Candidate cand1 = new Candidate();
     List<Project> pl = new ArrayList<>();
     Set<Skill> css = new HashSet<>() ;
    
    @BeforeEach
    public  void setUp() throws Exception {
    
    candao.deleteAll();
    skilldao.deleteAll();
         
        cand1.setAge(22);
        cand1.setCandidateName("yashss");
        cand1.setEducationQualification("extra");
        cand1.setExperience(2);
        cand1.setLocation("here");
        cand1.setEmailId("emailid");
        cand1.setPassword("password");
        
        
        
        
        Project p1 = new Project("yjbabv","happened");
        p1.setCandidate(cand1);
        Project p2 = new Project("slnacncs","happened");
        p2.setCandidate(cand1);
        pl.add(p1);
        pl.add(p2);
        cand1.setProjectList(pl);
       
        Skill s1 = new Skill("Json");
        Skill s2 = new Skill("Java");
        Set<Candidate> dummySet = new HashSet<>();
        dummySet.add(cand1);
       
        css.add(s1);
        css.add(s2);
        cand1.setSkillSet(css);

        
    }
    @AfterEach
    void setdown() throws Exception {
    candao.deleteAll();
    }
 
    @Test
    @Transactional
    public void addProjectbyIdTest() throws CandidateNotFoundException{
       cand1.setSkillSet(css); 
       int length = cand1.getProjectList().size();
//       System.out.println(cand1);
       candao.save(cand1);
//       System.out.println(cand1);
       
       List<ProjectDTO> pdt = new ArrayList<>();
       pdt.add(new ProjectDTO("kjsnv","skjnvk"));
        
       candidateService.addProjectbyId(cand1.getCandidateId(), pdt);
       int lengthafter = cand1.getProjectList().size();
        
       Assertions.assertNotEquals(length, lengthafter);
  }

    @Test
    @Transactional
    public void addProjectbyIdTestfalied(){
        
        int length = cand1.getProjectList().size();
      
      
      

       List<ProjectDTO> pdt = new ArrayList<>();
       pdt.add(new ProjectDTO("kjsnv","skjnvk"));
        
        
       
        Exception exception = Assertions.assertThrows(CandidateNotFoundException.class, 
                () -> {candidateService.addProjectbyId(cand1.getCandidateId(), pdt);
        });

        String expectedMessage = "Check the id entered";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
        
  }
    @Test
    @Transactional
    public void updateLocationByCandidateIdTest(){
        
      
        String before = cand1.getLocation();
      
       candao.save(cand1);


       candidateService.updateLocationByCandidateId(cand1.getCandidateId(), "Lexter");
      
       Candidate actual = candao.findById(cand1.getCandidateId()).get();
//       System.out.println(actual);
      
  
       Assertions.assertFalse(before.equals(actual.getLocation()));
       
 }
    @Test
    @Transactional
    public void updateLocationByCandidateIdTestFailed(){
        
        
        
       String before = cand1.getLocation();
      
       candao.save(cand1);


       Exception exception = Assertions.assertThrows(NoSuchElementException.class, 
       () -> {candidateService.updateLocationByCandidateId(40, "Lexter");
       });

  

       
 }
    @Test
    @Transactional
    public void addSkillByCandidateIdTest() throws CandidateNotFoundException{
        
       
       
        candao.save(cand1);
        
        int lengthBefore = cand1.getSkillSet().size();
        
        candidateService.addSkillDTOByCandidateId(cand1.getCandidateId(), new SkillDTO("Javasa"));

        int lengthAfter  = candao.findById(cand1.getCandidateId()).get().getSkillSet().size();
      
        Assertions.assertNotEquals(lengthBefore,lengthAfter);
        
        
  }
    @Test
    @Transactional
    public void addSkillByCandidateIdTestFailed(){
        
       
        candao.save(cand1);
        
       

        
        Exception exception = Assertions.assertThrows(CandidateNotFoundException.class, 
         () -> {
             candidateService.addSkillDTOByCandidateId(50, new SkillDTO("Java"));
                });

        String expectedMessage = "Candidate User Not Found";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
        
        
  }
    
    
    
    
    @Test
    @Transactional
    public void removeSkillbyCanidateIdAndSkillName() throws CandidateNotFoundException, skillNotFoundException{
        
        
         
       
        candao.save(cand1);
        
        int lengthBefore = cand1.getSkillSet().size();
        
        candidateService.removeSkillbyCanidateIdAndSkillName(cand1.getCandidateId(),"JSon" );

        int lengthAfter  = candao.findById(cand1.getCandidateId()).get().getSkillSet().size();
//        System.out.println(lengthBefore+" "+lengthAfter);

        Assertions.assertNotEquals(lengthBefore,lengthAfter);
        
        
  }
    
    
    @Test
    @Transactional
    public void removeSkillbyCanidateIdAndSkillNameFailed() throws CandidateNotFoundException, skillNotFoundException{
        
         
       
        candao.save(cand1);
        
      
        List<Exception> exceptionList= new ArrayList<>();
       
        
        Exception exception = Assertions.assertThrows(skillNotFoundException.class, 
                () -> {
                    candidateService.removeSkillbyCanidateIdAndSkillName(cand1.getCandidateId(),"klslm" );
                       });

               String expectedMessage = "No such skill is linked with candidate";
               String actualMessage = exception.getMessage();

               Assertions.assertTrue(actualMessage.contains(expectedMessage));
        
        
  }
   
    @Test
    @Transactional
    public void removeSkillbyCanidateIdAndSkillNameFailed2() throws CandidateNotFoundException, skillNotFoundException{
        
         
         
       
        candao.save(cand1);
        
      
        candidateService.removeSkillbyCanidateIdAndSkillName(cand1.getCandidateId(),"JSon" );
        List<Exception> exceptionList= new ArrayList<>();
       
        
        Exception exception = Assertions.assertThrows(CandidateNotFoundException.class, 
                () -> {
                    candidateService.removeSkillbyCanidateIdAndSkillName(12,"Json" );
                       });

               String expectedMessage = "No candidate is found while checking skill";
               String actualMessage = exception.getMessage();

               Assertions.assertTrue(actualMessage.contains(expectedMessage));
        
        
  }
    
    
    @Test
    @Transactional
    public void provideCandidateFeedbackTest() throws feedbackException{
        
        
         
         List<Interview> interviews = new ArrayList<>();
         Interview inter = new Interview();
         inter.setCandidate(cand1);
//         inter.setCandidateRating(5);
//         inter.setCandidateFeedback("ssome gibberish");
         inter.setPreInterviewStatus(PreInterviewStatus.SHORTLISTED);
         inter.setPostInterviewStatus(PostInterviewStatus.SELECTED);
         interviews.add(inter);
         cand1.setInterviewList(interviews);
         candao.save(cand1);
         RatingFeedbackDTO dtoshit = new RatingFeedbackDTO(5, "ssome gibberish");
       
         interviewService.provideCandidateFeedback(inter.getInterviewId(),dtoshit);
         
        
        
        Candidate cand3 = candao.findById(cand1.getCandidateId()).get();
        
        Assertions.assertTrue(cand3.getInterviewList().get(0).getCandidateRating()==5);


     
        
       
  } 

    @Test
    @Transactional
    public void provideCandidateFeedbackTestfailed(){
        
      
        
        List<Interview> interviews = new ArrayList<>();
        Interview inter = new Interview();
        inter.setCandidate(cand1);
//        inter.setCandidateRating(5);
//        inter.setCandidateFeedback("ssome gibberish");
        inter.setPreInterviewStatus(PreInterviewStatus.SHORTLISTED);
       //inter.setPostInterviewStatus(PostInterviewStatus.SELECTED);
        
        interviews.add(inter);
        cand1.setInterviewList(interviews);
        candao.save(cand1);
        RatingFeedbackDTO dtoshit = new RatingFeedbackDTO(5, "ssome gibberish");
      
        
       
       
       Candidate cand3 = candao.findById(cand1.getCandidateId()).get();
       
     
       
         
         
       
        
        Exception exception = Assertions.assertThrows(feedbackException.class, 
                () -> {
                    interviewService.provideCandidateFeedback(inter.getInterviewId(),dtoshit);
                    });
        

               String expectedMessage = 
                       "candidate need to complete his interview or data corrupted"
                     
               ;
               String actualMessage = exception.getMessage();

               Assertions.assertTrue(actualMessage.equals(expectedMessage));
        
     
        
       
  } 
    

    
    
    
    
}