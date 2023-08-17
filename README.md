# tb0823 - Tool Rental Application
This application can be started as a SpringBoot Web Application or a CLI application

	The Application is designed to be a scalable web app or a single useage cli call

	All Requiremnts have been implemented

	Lazy loading cache is used at the repository layer to ensure fast start up time, reset-cache endpoint avalible if new data is added on the fly via H2 DB console

	Interfaces have been implemented with default constructors to ensure safe dependency injection


##Usage:

-Start as Web Application: 

	-start up with 0 arguments
	
-Start as CLI Application: 
	
	-Proper Usage is: java_program_filename commandline toolCode[String type] rentalDayCount[number type] discountPercent[number type] checkoutDate[String ex:01/01/2000]
	
	-Ex: $java_program_filename commandline CHNS 31 25 01/01/23
	
	-Ex CLI Output:
		
		2023-08-17T15:34:27.207-04:00  INFO 30000 --- [  restartedMain] c.a.t.t.controller.ToolRentalController  : /checkout called with RentalRequest:
		Tool Code: CHNS
		Rental Day Count : 31
		Discount Percent: 25
		Checkout Date: 01/01/23
		
		Hibernate: select t1_0.tool_code,t1_0.brand,t1_0.tool_type from tools t1_0
		Hibernate: select t1_0.tool_type,t1_0.daily_charge,t1_0.holiday_charge,t1_0.weekday_charge,t1_0.weekend_charge from toolpurchasedata t1_0
		2023-08-17T15:34:27.337-04:00  INFO 30000 --- [  restartedMain] c.a.t.t.controller.ToolRentalController  : /checkout returning RentalAgreement: 
		Tool Code: CHNS
		Tool Type: Chainsaw
		Tool Brand: Stihl
		Rental Days: 31
		Checkout Date: 01/01/23
		Due Date: 02/01/23
		Daily Rental Charge: $1.49
		Charge Days: 23
		PreDiscount Charge: $34.27
		Discount Percent: 25%
		Discount Amount: $8.57
		Final Charge: $25.70
	-Note: Hibrinate logs can be disabled in application.config with property spring.jpa.show-sql=true
	
##Documentation:
	
-Generated Java Docs:
	
--Navigate to: tb0823\src\main\resources\docs\index.html
		
	![JavaDocs](https://github.com/TheodoreBelanger/tb0823/assets/5751051/c939be95-cab9-43d2-a563-24e4fa024711)

-Primary checkout Controller Method (shared by API and CLI)

	![checkoutJavaDoc](https://github.com/TheodoreBelanger/tb0823/assets/5751051/82a0e387-c4ae-487f-ad3c-3a4db3320f94)

##Swagger API Documentation:
	
-When app is started as web app navigate to: http://localhost:8080/swagger-ui/index.html#/

	![toolRentalServiceSwaggerApi](https://github.com/TheodoreBelanger/tb0823/assets/5751051/bcc2bab2-1b7b-48d0-9455-35111b600598)
	
##Data Base:

-Is implmented using an in memory H2 Database and is interfaced with via Spring JPA to stay data source agnostic

-An H2 database console is avalible on app startup at: http://localhost:8080/h2-console/login.jsp
	
	![h2DbConsole](https://github.com/TheodoreBelanger/tb0823/assets/5751051/5df9116f-78f3-450b-a429-4bdd2ba9c1e6)


##Extention

-Inorder to add Tools and associated Purchase Information, edit the data.sql file found in /main/src/main/resources/data.sql

	![data_sqlPNG](https://github.com/TheodoreBelanger/tb0823/assets/5751051/84b61e7a-ee4d-4124-9c43-553dcd1aa990)

##Tests

-Are avalible in /src/main/test/java/com/application.tb0823/toolrental/ToolRentalApplicationTets.java

-13 tests cover input validation, customInernalRuntimeException cases, and provided required 6 tests

	![requiredTestCases](https://github.com/TheodoreBelanger/tb0823/assets/5751051/9c1242e0-0ff1-488e-b407-e53e15e20750)


-Resuts:

-Required Test 1 output:
	![test1_postman_output](https://github.com/TheodoreBelanger/tb0823/assets/5751051/2b38d97f-1ac8-4cb4-80d7-3ce0e052388a)

-Required Test 2 output:
	![test2_postman_output](https://github.com/TheodoreBelanger/tb0823/assets/5751051/6e8deceb-d946-4ac7-a9b5-7710423661b2)

-Required Test 3 output:
	![test3_postman_output](https://github.com/TheodoreBelanger/tb0823/assets/5751051/afb014ec-1f5a-47d8-b0e7-233435904b24)

-Required Test 4 output:
	![test4_postman_output](https://github.com/TheodoreBelanger/tb0823/assets/5751051/52286127-e21a-4a14-8c2b-172f99c2f107)

-Required Test 5 output:
	![test5_postman_output](https://github.com/TheodoreBelanger/tb0823/assets/5751051/50748560-1fb5-4571-aeb8-868281345938)

-Required Test 6 output:
	![test6_postman_output](https://github.com/TheodoreBelanger/tb0823/assets/5751051/bda25a5d-2421-412a-bf47-d5a2af3321e7)
