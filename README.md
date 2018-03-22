# Challenge / Starzplay

## Building the service

From the command line, using maven, type:

```
mvn clean package
```

Note that the project requires at least java 8 to build.

## Running the service

The application contains an embedded Tomcat container, to run, being on the same directory as the jar file:

```
java -jar starzplay-1.0-SNAPSHOT.jar
```

The service will start listening on port 8080 by default, to change this either
add the parameter `-Dserver.port=8181` or whatever port is required, or create a properties
file at the same level as the service (either application.properties or application.yml) with same
property.

## As a service

The jar contains a script to be used as a service. To be enabled as such just create a link to the jar file,
like:

```
sudo ln -s /path/to/starzplay-1.0-SNAPSHOT.jar /etc/init.d/starzplay
```

## Framework

The base framework used is spring-mvc, managed by spring boot. Testing uses both AssertJ and Mockito, 
mainly to mock the response from an external server. Tests with the actual service at starzplay has been 
tested manually through PostMan.

To generate the http client it has been used the library 'feign' from spring-cloud. This library generates
the http clients from annotated interfaces.

To leave code cleaner the additional library 'lombok' has been used to autogenerate the 'getter/setter' 
methods, loggers and such. If the project is loaded on an IDE, like eclipse or IDEA, errors will be shown 
until the plugin is installed on the IDE. To do so, check the instructions at http://projectlombok.com

For example, the instructions for Eclipse: https://projectlombok.org/setup/eclipse

In most cases, it is only required to download the plugin, double click on it and point the installer to the
IDE. IDEA has a plugin that can be downloaded directly from the IDE plugin manager. 
