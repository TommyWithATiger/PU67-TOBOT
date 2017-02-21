The project is built using maven. When running it will spawn a server on the port given in SocketHandler, currently set to 5032 on any know address of you network interfaces.

## How to build

#### Setup once

1. Install maven with a package manager (apt, brew, etc.)

	* `$ sudo apt-get install maven`
	* `$ brew install maven`

#### Everytime

In the `tobot/java` folder use the following commands:

1. Use `$ mvn clean install` to cleanly compile to source code
2. Use `$ mvn exec:java -D exec.mainClass=main.ServerInitializer` to run the code
