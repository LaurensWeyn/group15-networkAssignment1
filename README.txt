Made by:
Group 15
Laurens Weyn       WYNLAU003
Winfreda Mazvidza  MZVWIN001
Fatimah Dhalla     DHLFAT001

This project is based on Maven. For tutors who don't know what that is,
it's basically makefiles if makefiles were actually good. It's the industry standard for automated
compilation of Java, alongside Gradle.

If you have IntelliJ or Android Studio (Even Eclipse and Netbeans should support this),
just open the project or import from the pom.xml file and run either the Server or GUI main class.

If you want to run it in a terminal:
mvn exec:java -Dexec.mainClass="com.group15.server.Server"
mvn exec:java -Dexec.mainClass="com.group15.client.GUI"

If you want to run it in the Windows command line:
mvn exec:java -D"exec.mainClass"="com.group15.server.Server"
mvn exec:java -D"exec.mainClass"="com.group15.client.GUI"

"mvn compile" just compiles.
"mvn test" will compile and run JUnit tests.
"mvn clean" cleans the output in the 'target' folder.

If you've never used maven before on your computer, it will download dependencies and plugins the first time,
so give it a bit.

If you really don't want to setup maven, the project doesn't really depend on it too much,
so you can always setup a build manually. I don't recommend you do though.