JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		  functions.java \
		  p2pws.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
