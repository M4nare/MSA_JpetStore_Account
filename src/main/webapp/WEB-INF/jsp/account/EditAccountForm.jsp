<%--

       Copyright 2010-2016 the original author or authors.

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.

--%>
<%@ include file="../common/IncludeTop.jsp"%>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript">
	function accountservice(){
		//alert("유저서비스작동 ");
		var formData = $("#editAccount").serialize();
		

		$.ajax({

			type:"POST",
			url:"http://localhost:8081/jpetstore/actions/Account.action?editAccount",
			data: formData,
			success : function(data){
				$.each(data,function(key,value) {
					alert(value);
					window.location.href = "http://localhost:8080/jpetstore/actions/Catalog.action";
				});
			},
			error : function(request,status,error){
      		  alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
      		}
			
		});
		
		}
</script>
<div id="Catalog">

<form name="editAccount" id="editAccount" method="post">

	<h3>User Information</h3>

	<table>
		<tr>
			<td>User ID:</td>
			<td><input type="text" name="username" value = "${actionBean.username}" readonly></td>
		</tr>
		<tr>
			<td>New password:</td>
			<td><input type="password" name="password"/></td>
		</tr>
		
		<tr>
			<td>Repeat password:</td>
			<td><input type="password" name="repeatedPassword"/></td>
		</tr>
		
	</table>
	<%@ include file="IncludeAccountFields.jsp"%>
<input type =button value="SUBMIT"onclick='accountservice()'>
</form> 

<a href="http://localhost:8082/jpetstore/actions/Order.action?listOrders&id=${actionBean.username}">My Orders</a>
</div>

<%@ include file="../common/IncludeBottom.jsp"%>
