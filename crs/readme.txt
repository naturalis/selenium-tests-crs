the program expects the presence of a MySQL-database with the following tables 

	create table selenium_settings (
		`project` varchar(32) not null,
		`setting` varchar(32) not null,
		`value` varchar(255) not null,
		primary key key_1 (setting,project) 
	);
	
	create table selenium_test_parameters (
		`project` varchar(32) not null,
		`test` varchar(32) not null,
		`test_name` varchar(128) not null,
		`gdocs_url` varchar(1024),
		`author` varchar(128) not null,
		`settings` text,
		primary key key_1 (project,test) 
	); 
	
`selenium_settings.project` and `selenium_test_parameters.project`correspond to the variable projectID in the TestClasses 
`selenium_test_parameters.test` corresponds to the variable testID in the TestClasses

selenium_settings -  required settings:
	homepage: startpage of the CRS website to test (full URL)
	domain: domain of the website to test, is prepended to the relative addresses of the various pages
	crs_username: username for logging in to the CRS 
	crs_password: password for logging in to the CRS
	browser_path: local path to the browser to be used by the Selenium-driver 

example data:
	insert into selenium_settings values
		('CRS','homepage','https://this.domain.nl/AtlantisWeb/pages/publiek/Login.aspx'),
		('CRS','domain','https://this.domain.nl'),
		('CRS','crs_username','UserName'),
		('CRS','crs_password','secret')
		('CRS','browser_path','C:/Program Files/FireFox-4.21/firefox.exe')
	;



selenium_test_parameters - contains test test-specific data (title, author, link to corresponding design doc, specific parameters):  
data:
	insert into selenium_test_parameters values (
		'CRS',
		'Test 01',
		'1. Selenium scenario - in- en uitloggen',
		'https://docs.google.com/spreadsheets/[...]',
		'Maarten Schermer (maarten.schermer@naturalis.nl)',
		'{"menus":{"SearchMenu":{"items":["Employee","Search","Specimen","Vertebrates"],"end_point_sibling_count":1},"AddMenu":{"items":["Employee","Add","Specimen","Vertebrates"],"end_point_sibling_count":1}},"detail_page_query_strings":{"ok":"xmlbeschrijvingid=20250966","nok":"xmlbeschrijvingid=23838308"},"detail_page_ok_result_number":1,"url_logged_out":"\/AtlantisWeb\/pages\/publiek\/Login.aspx?restart=true&action=afmelden"}');


database access; JDBC-driver address, database name and credentials for access to database are stored in
	configuration\config.yaml
