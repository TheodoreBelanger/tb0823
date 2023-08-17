# tb0823 - Tool Rental Application
This application can be started as a SpringBoot Web Application or a CLI application

Usage:

	-Start as Web Application: start up with 0 arguments
	
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
	
Documentation:
	
	-Generated Java Docs:
		-Navigate to: tb0823\src\main\resources\docs\index.html
		
![JavaDocs](https://github.com/TheodoreBelanger/tb0823/assets/5751051/c939be95-cab9-43d2-a563-24e4fa024711)

	-Swagger API Documentation:
		-When app is started as web app navigate to: http://localhost:8080/swagger-ui/index.html#/

![toolRentalServiceSwaggerApi](https://github.com/TheodoreBelanger/tb0823/assets/5751051/bcc2bab2-1b7b-48d0-9455-35111b600598)
	
Extention

	-Inorder to add Tools and associated Purchase Information edit the data.sql file found in https://github.com/TheodoreBelanger/tb0823/blob/main/src/main/resources/data.sql

![data_sqlPNG](https://github.com/TheodoreBelanger/tb0823/assets/5751051/84b61e7a-ee4d-4124-9c43-553dcd1aa990)

