#!/bin/sh

# Error Codes
NO_MAVEN=1
MAVEN_FAILED=2

# get the directory that this script is running in (./bin)
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# and use it to build the path to the fat jar
FATJAR="$(readlink -m $SCRIPT_DIR/target/anubis-3.2.0-SNAPSHOT.jar)"

# if the fat jar doesn't exist, we can try to compile it with maven
if [ ! -f "$FATJAR" ]; then

    # but we need to see if the maven command is available to us
	if [ $(which mvn 2>/dev/null) != "" ]; then

		mvn clean package -U

		if [ 0 != $? ]; then
		    echo "Failed to compile amp:anubis"
		    exit $MAVEN_FAILED;
		fi
	else
		echo "amp:anubis cannot be compiled because maven is not available on the command line"
		exit $NO_MAVEN;
	fi
fi

# if we're on cygwin, convert the path to windows-style
if [ $(uname -o) = "Cygwin" ]; then
    FATJAR=$(cygpath -w $FATJAR)
fi

echo "Anubis built successfully: $FATJAR"
