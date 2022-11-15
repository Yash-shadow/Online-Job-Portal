package com.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dao.CandidateDAO;
import com.dao.EmployerDAO;
import com.model.Candidate;
import com.model.Employer;

@Service
public class CandidateJwtService implements UserDetailsService {

	@Autowired
	CandidateDAO canDao;
	
	@Autowired
	EmployerDAO emplDao;

	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException{
		// TODO Auto-generated method stub
		Candidate can = canDao.findByEmailId(emailId);
		
		if(can == null) {
			Employer empl = emplDao.findByEmailId(emailId);
			if(empl == null) {
				throw new UsernameNotFoundException("EmailId does not exists");
			}
			else {
				return new User(empl.getEmailId(),  empl.getPassword(), new ArrayList<>());
			}
		}
		
		//return new User(can.getEmailId(), can.getPassword(), new ArrayList<>());
		//System.out.println(passwordEncoder().encode("121aaa"));
		return new User(can.getEmailId(),  can.getPassword(), new ArrayList<>());
	}
	
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		//return NoOpPasswordEncoder.getInstance();
//		return new BCryptPasswordEncoder();
// 	}
	
	

}
