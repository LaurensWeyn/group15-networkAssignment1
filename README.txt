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

If you want to run it in a Unix terminal:
mvn compile exec:java -Dexec.mainClass="com.group15.server.Server"
mvn compile exec:java -Dexec.mainClass="com.group15.client.GUI"

If you want to run it in the Windows command line (not recommended but can be done):
mvn compile exec:java -D"exec.mainClass"="com.group15.server.Server"
mvn compile exec:java -D"exec.mainClass"="com.group15.client.GUI"

Some more Maven commands:
"mvn compile" just compiles.
"mvn test" will compile and run JUnit tests (only for HexCommand atm)
"mvn clean" cleans the output in the 'target' folder.
(Maven tip: you can combine commands, e.g. "mvn clean compile exec:java ...")

I've checked and 'mvn' is installed and functional on the Senior Lab computers,
but by default none of the plugins, not even core stuff, are downloaded yet,
so it'll be busy doing that for a while the first time you run some of the commands above.

If you really don't want to setup maven, the project doesn't really depend on it too much,
so you can always setup a build manually. I don't recommend you do though.