#!/bin/bash

echo "Running Online Banking Application Tests..."
echo

# Check if tests are compiled
if [ ! -d "out" ]; then
    echo "ERROR: Tests not compiled. Please run build.sh first."
    exit 1
fi

echo "Running JUnit tests..."
java -cp "out:lib/junit-jupiter-api-5.10.0.jar:lib/junit-jupiter-engine-5.10.0.jar:lib/junit-platform-console-standalone-1.10.0.jar" org.junit.platform.console.ConsoleLauncher --scan-class-path --class-path out

echo
echo "Tests completed!"
