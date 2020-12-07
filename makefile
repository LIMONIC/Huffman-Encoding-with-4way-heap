#
# A simple makefile for compiling three java classes
#

# define a makefile variable for the java compiler
#
JC = javac

# define a makefile variable for compilation flags
# the -g flag compiles with debugging information
#
JFLAGS = -g

# typing 'make' will invoke the first target entry in the makefile 
# (the default one in this case)
#
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		encoder.java \
		decoder.java 

default: classes

classes: $(CLASSES:.java=.class)
clean: 
	$(RM) *.class