package com.spring.wefit.course.service;

import java.util.List;

import com.spring.wefit.command.CourseBoardVO;

public interface ICourseBoardService {
	
	void regist(CourseBoardVO vo);
	
	List<CourseBoardVO> getList();

}