
How to install the House of Fun Application:
	⁃	Copy the HouseOfFun folder to the Desktop
	⁃	Open the HouseOfFun folder
	⁃	Double-click HouseOfFun.jar OR:
		-	Go to the command line
		-	Navigate to the directory of the JAR
		-	Run the command “java -jar HouseOfFun.jar”

Minimum Requirements:
	⁃	Windows XP or later
	⁃	Mac OS X 10.6 or later

How to use the House of Fun Application:



	NOTE: For detailed help, open the help.html file.



	⁃	Enter the default username and password.

		Username: admin
		Password: admin123

		The first time the program is run, the database tables are automatically
		created in a file called “FEC_Database.sqlite”. If moving the application
		JAR, move the database to the same directory to preserve data. To clear
		data, remove the file entirely.

	⁃	The main menu screen will appear.

	⁃	Employees
			-	Add will bring up the add employee panel.
			-	Edit will bring up the edit employee panel, 
				which allows for
		  		the editing of the selected employee.
			- 	Back will return to the main menu.

	-	Attendance

			- 	+1 Child adds the attendance record of a child.
			- 	+1 Adult adds the attendance record of an adult.
			- 	Generate creates a graph of attendance for a 
				specific day.
				This day can be changed with the calendar picker.
			- 	Back will return to the main menu.

	-	Schedule

			-	Edit allows for the editing of a single employee’s
				schedule.
			-	Print can print the schedule table.
			-	Back will return to the main menu.

Credit:
	-	Java SQL Library: https://www.sqlite.org
	- 	Graphing Library: http://www.jfree.org
	-	House Image: http://www.clipartbest.com/cliparts/dT7/yop/dT7yoprT9.jpeg