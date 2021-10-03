all: compile start

start:
	rmiregistry &
	java MainServer localhost

stop:
	@echo "Finalizando el servidor\n"
	- killall rmiregistry

compile:
	javac *.java

clean:
	- rm *.class