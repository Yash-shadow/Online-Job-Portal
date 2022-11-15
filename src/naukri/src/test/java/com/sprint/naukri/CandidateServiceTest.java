package com.sprint.naukri;

import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.dao.CandidateDAO;
import com.dao.EmployerDAO;
import com.dao.ProjectDAO;
import com.dao.SkillDAO;
import com.dto.CandidateDTO;
import com.dto.ProfileDTO;
import com.dto.ProjectDTO;
import com.dto.RatingFeedbackDTO;
import com.dto.SkillDTO;
import com.enums.PostInterviewStatus;
import com.enums.PreInterviewStatus;
import com.exception.AlreadySelectedBySameEmployerException;
import com.exception.CandidateNotFoundException;
import com.exception.CandidateValidationExceptioncheck;
import com.exception.MoonLightingException;
import com.exception.ProjectNotFoundException;
import com.exception.feedbackException;
import com.model.Candidate;
import com.model.Employer;
import com.model.Interview;
import com.model.Project;
import com.model.Skill;
import com.service.CandidateService;
import com.service.InterviewService;
import com.service.ProjectService;


@SpringBootTest

public class CandidateServiceTest {
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
	InterviewService interviewService;
	@Autowired
	EmployerDAO employerDao;
	
	
	 

	 Candidate cand1 = new Candidate();
	 List<Project> pl = new ArrayList<>();
	 Set<Skill> css = new HashSet<>() ;
	 List<ProjectDTO> pdtl = new ArrayList<>();
	 Set<SkillDTO> csdts = new HashSet<>() ;
	//rest controller test
	@BeforeEach
	public  void setUp() throws Exception {
	    candao.deleteAll();
	    sdao.deleteAll();
	         
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
		sdao.deleteAll();
	}
	
	@Test 
	void addProfileTest(){
	    
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
	  
	    
	
      candidateService.addProfile(dto);
      Candidate candexpec = candidateService.findCandidateByName("yashss");
      
      Candidate candactual = candao.findById(candexpec.getCandidateId()).get();
      Assertions.assertEquals(candexpec.getCandidateName(), candactual.getCandidateName());
      
	    
	}
	@Test 
    void addProfileTestfailed(){
        
        ProfileDTO dto = new ProfileDTO();
        dto.setAge(2);
      
        dto.setEducationQualification("extra");
        dto.setEmailId("emailid");
        dto.setExperience(2);
        dto.setLocation("here");
        dto.setPassword("password");
        
        dto.setProjectDTOList(pdtl);
        
        dto.setSkillDTOSet(csdts);
      
    
      
      Candidate candexpec = candidateService.findCandidateByName("yashss");
      Exception exception = Assertions.assertThrows(CandidateValidationExceptioncheck.class, 
              () -> {candidateService.addProfile(dto);
      });

      String expectedMessage = "hey there check the docs for validation errors";
      String actualMessage = exception.getMessage();

    //  Assertions.assertTrue(actualMessage.contains(expectedMessage));
        
    }
	
	
	 @Test

	 public void removeProjectByProjectIdTest() throws ProjectNotFoundException{
	      


	        candao.save(cand1);

	      int length = (int) projectdao.count();
	        
	        projectService.removeProjectById(cand1.getProjectList().get(0).getProjectId());
	       
	        int lengthafter = (int) projectdao.count();
	        
       
	        Assertions.assertNotEquals(length, lengthafter);
	  }
	 
	 @Test
	 public void removeProjectByProjectIdTestFailed(){
	     candao.save(cand1);
	     int length = (int) projectdao.count();
         
        
         int lengthafter = (int) projectdao.count();
         
         Exception exception = Assertions.assertThrows(ProjectNotFoundException.class, 
         () -> {projectService.removeProjectById(55);
         });

         String expectedMessage = "the provided id is not in this world";
         String actualMessage = exception.getMessage();

         Assertions.assertTrue(actualMessage.contains(expectedMessage));
         
   }
	 
	 @Test
     public void getallCandidates() throws ProjectNotFoundException{

	     candao.save(cand1);



	     int length = (int) candao.count();
         

        
         Assertions.assertEquals(length, 1);
   }
	 
	 
	 @Test

	 public void provideCandidateFeedbackTestfailed2(){

        List<Interview> interviews = new ArrayList<>();
        Interview inter = new Interview();

        inter.setCandidateFeedback("ssome gibberish");
        inter.setPreInterviewStatus(PreInterviewStatus.SHORTLISTED);
        inter.setPostInterviewStatus(PostInterviewStatus.SELECTED);
        interviews.add(inter);
        cand1.setInterviewList(interviews);
        candao.save(cand1);
        RatingFeedbackDTO dtoshit = new RatingFeedbackDTO(22, "ssome gibberish");
        
        Exception exception = Assertions.assertThrows(feedbackException.class, 
                () -> {
                    interviewService.provideCandidateFeedback(inter.getInterviewId(),dtoshit);
                    });
        

       String expectedMessage = 
               "caught transaction exception might be an validation error";
       String actualMessage = exception.getMessage();

       Assertions.assertTrue(actualMessage.equals(expectedMessage));
	        
	     
	        
	       
	  } 
	   
	 
	 
	  @Test 
	    void addProfileTestForCoverage(){
	        
	        ProfileDTO dto = new ProfileDTO();
	        dto.setAge(22);
	        dto.setCandidateName("yashss");
	        dto.setEducationQualification("extra");
	        dto.setEmailId("emailid");
	        dto.setExperience(2);
	        dto.setLocation("here");
	        dto.setPassword("password");
	        
	        dto.setProjectDTOList(pdtl);
	        dto.setSkillDTOSet(new HashSet<>());
	        
	       
	      
	        
	    
	      candidateService.addProfile(dto);
	      Candidate candexpec = candidateService.findCandidateByName("yashss");
	      
	      Candidate candactual = candao.findById(candexpec.getCandidateId()).get();
	      Assertions.assertEquals(candexpec.getCandidateName(), candactual.getCandidateName());
	      
	        
	    }
	  @Test 
	  public void getAllCandidates() throws CandidateNotFoundException{
      candao.save(cand1);
      List<Candidate> candidateList = candidateService.getAllCandidates(); 
      Assertions.assertEquals(candidateList.size(), 1);
    
	    }
	 
	  @Test 
      public void getAllCandidatesFailed() throws CandidateNotFoundException{
      Exception exception= Assertions.assertThrows(CandidateNotFoundException.class, ()->{
      List<Candidate> candidateList = candidateService.getAllCandidates(); 
      });
      String expectedMessage = 
              "Data Base is Empty";
      String actualMessage = exception.getMessage();

      Assertions.assertTrue(actualMessage.equals(expectedMessage));
    
        }
	  
	  @Test 
	  @Transactional
      public void updateProfileTest() throws CandidateNotFoundException{
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
	      candidateService.addProfile(dto);
	      Candidate candexpec = candidateService.findCandidateByName("yashss");
          
//	      System.out.println(candexpec.getExperience());
	      
	       
	       ProfileDTO dto1 = new ProfileDTO();
           dto1.setAge(22);
           dto1.setCandidateName("yashss");
           dto1.setEducationQualification("extra");
           dto1.setEmailId("emailid");
           dto1.setExperience(45);
           dto1.setLocation("here");
           dto1.setPassword("password");
           
           dto1.setProjectDTOList(pdtl);
           
           dto1.setSkillDTOSet(csdts);
       
	      candidateService.updateProfile(candexpec.getCandidateId(),dto1);
	      
//	      System.out.println(candexpec.getExperience());
	        Candidate candactual = candao.findById(candexpec.getCandidateId()).get();

	      
	      Assertions.assertEquals(45, candactual.getExperience());
	      
    
        }
	  @Test 
      @Transactional
      public void updateProfileTestFailed() throws CandidateNotFoundException{
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
          candidateService.addProfile(dto);
          Candidate candexpec = candidateService.findCandidateByName("yashss");
          
//          System.out.println(candexpec.getExperience());
          
           
           ProfileDTO dto1 = new ProfileDTO();
           dto1.setAge(22);
           dto1.setCandidateName("yashss");
           dto1.setEducationQualification("extra");
           dto1.setEmailId("emailid");
           dto1.setExperience(45);
           dto1.setLocation("here");
           dto1.setPassword("password");
           
           dto1.setProjectDTOList(pdtl);
           
           dto1.setSkillDTOSet(csdts);
           Candidate candactual = candao.findById(candexpec.getCandidateId()).get();

          
//          System.out.println(candexpec.getExperience());

          
            Exception exception= Assertions.assertThrows(CandidateNotFoundException.class, ()->{
                candidateService.updateProfile(11321,dto1);
   
            });
                String expectedMessage = 
                        "Check the id u r enter the data into";
                String actualMessage = exception.getMessage();

                Assertions.assertTrue(actualMessage.equals(expectedMessage));
        }
	 
	  @Test 
     
      public void updateProfileTestFailed2() throws CandidateNotFoundException{
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
          candidateService.addProfile(dto);
          Candidate candexpec = candidateService.findCandidateByName("yashss");
          
//          System.out.println(candexpec.getExperience());
          
           
           ProfileDTO dto1 = new ProfileDTO();
           dto1.setAge(2);
           dto1.setCandidateName("yashss");
           dto1.setEducationQualification("extra");
           dto1.setEmailId("emailid");
           dto1.setExperience(45);
           dto1.setLocation("here");
           dto1.setPassword("password");
           
           dto1.setProjectDTOList(pdtl);
           
           dto1.setSkillDTOSet(csdts);
           Candidate candactual = candao.findById(candexpec.getCandidateId()).get();
           
//           System.out.println(candexpec.getExperience());

           
             Exception exception= Assertions.assertThrows(CandidateValidationExceptioncheck.class, ()->{
                 candidateService.updateProfile(candexpec.getCandidateId(),dto1);
                 Candidate candactual1 = candao.findById(candexpec.getCandidateId()).get();
//                 System.out.println(candactual1.getAge());
    
             });
                 String expectedMessage = 
                         "hey there check the docs for validation errors";
                 String actualMessage = exception.getMessage();

                 Assertions.assertTrue(actualMessage.equals(expectedMessage));
         }

	 
	 @Test
	 public void findByIdTest() throws CandidateNotFoundException {
	     candao.save(cand1);
	     Candidate c = candidateService.getCandidateById(cand1.getCandidateId());
	     
         Assertions.assertEquals("yashss", c.getCandidateName());
         
	 }
	  
	 @Test
     public void findByIdTestFailed() throws CandidateNotFoundException {
         candao.save(cand1);

         Exception exception= Assertions.assertThrows(CandidateNotFoundException.class, ()->{
             Candidate c = candidateService.getCandidateById(123);

         });
             String expectedMessage = 
                     "Candidate not found";
             String actualMessage = exception.getMessage();

             Assertions.assertTrue(actualMessage.equals(expectedMessage));
     }
	
	 @Test
	 public void deletebyid() throws CandidateNotFoundException {
	     candao.save(cand1);
	     Assertions.assertTrue(candidateService.deletebyid(cand1.getCandidateId()));
	 }
	 @Test
     public void deletebyidfailed() throws CandidateNotFoundException {
         candao.save(cand1);
         Exception exception= Assertions.assertThrows(CandidateNotFoundException.class, ()->{
             candidateService.deletebyid(465);

         });
             String expectedMessage = 
                     "no candidate found";
             String actualMessage = exception.getMessage();

             Assertions.assertTrue(actualMessage.equals(expectedMessage));
     }
     
	 
	 @Test
     public void deleteAllTest() throws CandidateNotFoundException {
         candao.save(cand1);
         String s = candidateService.deleteAllCandidate();
         Assertions.assertTrue(s.contains("Successfully deleted all the Candidate"));
     }
	 
	 @Test
	 @Transactional
	 public void checkIfAlreadySelectedByEmployerTest () throws AlreadySelectedBySameEmployerException, MoonLightingException {
	     candao.save(cand1);
	      List<Interview> interviewList = new ArrayList<>();
	      Interview i = new Interview();
	      i.setCandidate(cand1);
	      i.setPostInterviewStatus(PostInterviewStatus.SELECTED);
	      
	      
	      interviewList.add(i);
	      Employer e = new Employer();
          e.setEmployerName("kbml");
          e.setInterviewList(interviewList);
          employerDao.save(e);
          i.setEmployer(e);
	      cand1.setInterviewList(interviewList);
	      
	      candao.save(cand1);
	      Exception exception= Assertions.assertThrows(AlreadySelectedBySameEmployerException.class, ()->{
	          candidateService.checkIfAlreadySelectedByEmployer(cand1,e);
	         });
	             String expectedMessage = 
	                     "You have already been hired by "+e.getEmployerName();
	             String actualMessage = exception.getMessage();

	             Assertions.assertTrue(actualMessage.equals(expectedMessage));
	     }

	      
	 @Test
     @Transactional
     public void checkIfAlreadySelectedByEmployerTestfailed () throws AlreadySelectedBySameEmployerException, MoonLightingException {
         candao.save(cand1);
          List<Interview> interviewList = new ArrayList<>();
          Interview i = new Interview();
          i.setCandidate(cand1);
          i.setPostInterviewStatus(PostInterviewStatus.INVALID);
          
          
          interviewList.add(i);
          Employer e = new Employer();
          e.setEmployerName("kbml");
          e.setInterviewList(interviewList);
          employerDao.save(e);
          i.setEmployer(e);
          cand1.setInterviewList(interviewList);
          
         
              candidateService.checkIfAlreadySelectedByEmployer(cand1,e);
        
              

                 Assertions.assertTrue(true);
         }

	  //ask naman here   
	 @Test
     @Transactional
     public void checkIfAlreadySelectedByEmployerTestfailed2 () throws AlreadySelectedBySameEmployerException, MoonLightingException {
         candao.save(cand1);
          List<Interview> interviewList = new ArrayList<>();
          Interview i = new Interview();
          i.setCandidate(cand1);
          i.setPostInterviewStatus(PostInterviewStatus.SELECTED);
          
          
          interviewList.add(i);
          Employer e = new Employer();
          e.setEmployerName("kbml");
          e.setInterviewList(interviewList);
          employerDao.save(e);
          i.setEmployer(new Employer());
          cand1.setInterviewList(interviewList);
          
          candao.save(cand1);
          Exception exception= Assertions.assertThrows(MoonLightingException.class, ()->{
              candidateService.checkIfAlreadySelectedByEmployer(cand1,e);
             });
                 String expectedMessage = 
                         "This is bad practice :(";
                 String actualMessage = exception.getMessage();

                 Assertions.assertTrue(actualMessage.equals(expectedMessage));
         }

	 @Test
	 public void getAllCandidatesByExperienceTest() {
	     candao.save(cand1);
	     List<CandidateDTO> experience = candidateService.getAllCandidatesByExperience(0,cand1.getExperience());
	     Assertions.assertTrue(1==experience.size());
	 }
	 
     
	 @Test
	 public void getAllCandidatesByExperienceTestfailed() {
         
         List<CandidateDTO> experience = candidateService.getAllCandidatesByExperience(5665,6513);
//         System.out.println(experience);
         Assertions.assertTrue(experience.size()==0);
         
     }
	
	
	 @Test
     public void getAllCandidatesByQualification() {
         candao.save(cand1);
         List<CandidateDTO> qualification = candidateService.getAllCandidatesByQualification(cand1.getEducationQualification());
         Assertions.assertTrue(1==qualification.size());
     }
	
	 @Test
     public void getAllCandidatesByQualificationfalied() {
         candao.save(cand1);
         List<CandidateDTO> qualification = candidateService.getAllCandidatesByQualification("jknsdkvj");
         Assertions.assertTrue(0==qualification.size());
     }

	 @Test
	 @Transactional
	 public void getAllCandidatesBySkillSetTest() {
	     candao.save(cand1);
	     String skills  = "Json,Javsvds,Jksnks ";
	     List <CandidateDTO> candidates = candidateService.getAllCandidatesBySkillSet(skills);
         Assertions.assertTrue(1==candidates.size());

	     
	 }
	 @Test
     @Transactional
     public void getAllCandidatesBySkillSetTestFailed() {
         candao.save(cand1);
         String skills  = "Json ,Javsvds,Jksnks ";
         List <CandidateDTO> candidates = candidateService.getAllCandidatesBySkillSet(skills);
         Assertions.assertTrue(1==candidates.size());

         
     }
	 @Test
     @Transactional
     public void getAllCandidatesBySkillSetTestFailed2() {
         candao.save(cand1);
         String skills  = "Jsoasdn ,Javsvds,Jksnks ";
         List<CandidateDTO> candidates = candidateService.getAllCandidatesBySkillSet(skills);
         Assertions.assertTrue(0==candidates.size());

         
     }
	 
	 
	
}
