package org.project.manage.services.impl;

import org.project.manage.services.TestService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService{

	@Override
	@Async("taskExecutor")
	public void asyncMethod() throws InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("mothod"+ Thread.currentThread().getName());
		 Thread.sleep(1000L);
		 System.out.println("mothod"+ Thread.currentThread().getName());
	}
	
	@Override
	@Async("taskExecutor")
	public void asyncMethod1() throws InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("sad2"+ Thread.currentThread().getName());
		 Thread.sleep(1000L);
		 System.out.println("sad22"+ Thread.currentThread().getName());
	}

}
