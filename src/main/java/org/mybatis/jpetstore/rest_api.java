package org.mybatis.jpetstore;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class rest_api {
	
	 @RequestMapping(value="/news/{id}", method=RequestMethod.GET)
	    public String getNews(@PathVariable ("id") int newsId) {
	        String result = "조회된 "+newsId+"번 뉴스";

	        return result;
	    }

}
