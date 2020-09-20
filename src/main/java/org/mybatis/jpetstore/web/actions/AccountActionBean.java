/**
 *    Copyright 2010-2018 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore.web.actions;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SessionScope;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.AccountService;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;


/**
 * The Class AccountActionBean.
 *
 * @author Eduardo Macarron
 */
@SessionScope

public class AccountActionBean extends AbstractActionBean {

  private static final long serialVersionUID = 5499663666155758178L;

  private static final String NEW_ACCOUNT = "/WEB-INF/jsp/account/NewAccountForm.jsp";
  private static final String EDIT_ACCOUNT = "/WEB-INF/jsp/account/EditAccountForm.jsp";
  private static final String SIGNON = "/WEB-INF/jsp/account/SignonForm.jsp";

  private static final List<String> LANGUAGE_LIST;
  private static final List<String> CATEGORY_LIST;

  @SpringBean
  private transient AccountService accountService;
  

  private Account account = new Account();
  

  private List<Product> myList;
  private boolean authenticated;

  static {
    LANGUAGE_LIST = Collections.unmodifiableList(Arrays.asList("english", "japanese"));
    CATEGORY_LIST = Collections.unmodifiableList(Arrays.asList("FISH", "DOGS", "REPTILES", "CATS", "BIRDS"));
  }

  public Account getAccount() {
    return this.account;
  }

  public String getUsername() {
    return account.getUsername();
  }

  @Validate(required = true, on = { "signon", "newAccount", "editAccount" })
  public void setUsername(String username) {
    account.setUsername(username);
  }

  public String getPassword() {
    return account.getPassword();
  }

  @Validate(required = true, on = { "signon", "newAccount", "editAccount" })
  public void setPassword(String password) {
    account.setPassword(password);
  }

  public List<Product> getMyList() {
    return myList;
  }

  public void setMyList(List<Product> myList) {
    this.myList = myList;
  }

  public List<String> getLanguages() {
    return LANGUAGE_LIST;
  }

  public List<String> getCategories() {
    return CATEGORY_LIST;
  }

  public Resolution newAccountForm() {
    return new ForwardResolution(NEW_ACCOUNT);
  }

  /**
   * New account.
   *
   * @return the resolution
 * @throws ParseException 
   */
public Resolution newAccount() throws ParseException {
    accountService.insertAccount(account);
    
    
    String url="http://catalog:8080/jpetstore/actions/Catalog.action?getProductListByCategory&id="+account.getFavouriteCategoryId();
	RestTemplate restTemplate = new RestTemplate();
	String resp = restTemplate.getForObject(url, String.class);
	JSONParser parser = new JSONParser();
	@SuppressWarnings("unchecked")
	List<Product> product = (List<Product>)parser.parse(resp);
	myList = product;
	//User info
    authenticated = true;
    // return new RedirectResolution(CatalogActionBean.class);
    
    return new Resolution()
	{

		@Override
		public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			
			// TODO Auto-generated method stub
			
			response.setCharacterEncoding("utf-8");
	        response.setContentType("application/json");

			Gson gson = new Gson();
			Map<String, String> json_result= new HashMap();
			json_result.put("result", "success");
			
			String jsonlist = gson.toJson(json_result);
			PrintWriter out = response.getWriter();
			out.write(jsonlist);
	        out.flush();
	        out.close();
			
		}

	};
  }

  
  public Resolution getUserinfo()
  {
	  
	  return new Resolution()
		{
		  Account account2 = new Account();

			@Override
			public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
				// TODO Auto-generated method stub
				String userId = request.getParameter("id");
				account2= accountService.getAccount(userId);
				response.setCharacterEncoding("utf-8");
		        response.setContentType("application/json");

				Gson gson = new Gson();
				
				String jsonlist = gson.toJson(account2);
				PrintWriter out = response.getWriter();
				out.write(jsonlist);
		        out.flush();
		        out.close();
		        account2 = null;

			}

		};
  }
  
  


 

/**
   * Edits the account.
   *
   * @return the resolution
 * @throws ParseException 
   */
  public Resolution editAccount() throws ParseException {

    accountService.updateAccount(account);
    
    String url="http://catalog:8080/jpetstore/actions/Catalog.action?getProductListByCategory&id="+account.getFavouriteCategoryId();
   	RestTemplate restTemplate = new RestTemplate();
   	String resp = restTemplate.getForObject(url, String.class);
   	JSONParser parser = new JSONParser();
   	@SuppressWarnings("unchecked")
   	List<Product> product = (List<Product>)parser.parse(resp);
   	
   	myList = product;
   	//User info

    
    return new Resolution()
	{

		@Override
		public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			// TODO Auto-generated method stub
			response.setCharacterEncoding("utf-8");
	        response.setContentType("application/json");

			Gson gson = new Gson();
			Map<String, String> json_result= new HashMap();
			json_result.put("result", "success");
			String jsonlist = gson.toJson(json_result);
			PrintWriter out = response.getWriter();
			out.write(jsonlist);
	        out.flush();
	        out.close();
		}
	};
    
  }
 
 
  
  


  /**
   * Signon.
   *
   * @return the resolution
 * @throws UnsupportedEncodingException 
   */
  
  public Resolution signon() throws UnsupportedEncodingException {

    account = accountService.getAccount(getUsername(), getPassword());

    if (account == null) {
     
      clear();
      return new Resolution()
		{
  	 
			@Override
			public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
				// TODO Auto-generated method stub
				response.sendRedirect("http://catalog:8080/jpetstore/actions/Catalog.action?test&result=error");
				
			}
	
		};
    } else {
      account.setPassword(null);
      myList = null;      
      authenticated = true;

      
      String id = account.getUsername();
      
      return new Resolution()
		{
    	 
			@Override
			public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
				// TODO Auto-generated method stub
				 
				//json 작업
				response.setCharacterEncoding("utf-8");
		        response.setContentType("application/json");

				Gson gson = new Gson();
				Map<String, String> json_result= new HashMap();
				json_result.put("username", id);
				
				String jsonlist = gson.toJson(json_result);
				PrintWriter out = response.getWriter();
				out.write(jsonlist);
		        out.flush();
		        out.close();
			
				
			}
	
		};
      }
  }


  



  /**
   * Checks if is authenticated.
   *
   * @return true, if is authenticated
   */
  public boolean isAuthenticated() {
    return authenticated && account != null && account.getUsername() != null;
  }

  /**
   * Clear.
   */
  public void clear() {
    account = new Account();
    myList = null;
    authenticated = false;
  }

}
