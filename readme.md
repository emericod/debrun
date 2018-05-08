{::nomarkdown}
<h1>Debrun Running Race Login Manager</h1>
This application is a java desktop application and made for the debrun running race as a login manager. 

<p style="text-align:justify;">The main plan was to make an client application, what has a simple graphical user interface with low trafic, because there aren't wifi and lan network in the near, so mainly used it by mobile net.</p>
<p>The other important thing was the multi clients usage and independent of the internet. The application based on two database, a local (SqlLite) and a remoted (Mysql) database. The users of client app fully can be managed by the remoted database admin, so the first step creating the mysql tables, and the admin has to be create the client users, passwords etc. At the first running you have to connect to the remoted database, because the application authenticate the user, if the authentication is succeed, this storing the users datas to the local database, so at the next login you can use it offline, too.</p>
<p>At first time if the login is succeed, syncronizing the applicants between the remoted database and local database. There is a network monitoring in the application, the checking period time is configurable in the preferences menu item. If the monitoring is detected the network disconnecting, the user can continue the work in the application, because every modifying is storing the local database, when the network is fixed, the user can synchronize with the remoted database automatic or manual sync. Te automatic sync is also configurable in the preferences menu item.</p>
<p>The multi client usage: It's possible for one side or both side synchronizing. In the database menu can selectable from local to remoted, remoted to local and both direction synchronizing. At the synchronizing every client get the newest applicant's datas, so the latest modifying storing in the database.</p>
<p>This version is demo, you can try this: at the login panel the user is "demo" and the password is "1234". This application is connected to a demo mysql database, but you can set an other mysql database in the preferences menu item in file menu.</p>

Installation:
1. Clone this reposittory on your computer
2. The cloned repository's debrun folder type "mvn package"
3. After the build type "java -jar debrun/target/debrun-1.0-jar-with-dependencies.jar"
4. The application is running, in the login panel type the user login datas (username: "demo", password: "1234")
5. You can test it without internet (disable the network adapters) or with network.
6. For generating reports and documentation first type "mvn clover:instrument"
7. After enter type "mvn site" and you can find it in the /target/site/ folder
8. Click the checkstyle.html file.

Requirements: Java 8 


{:/}