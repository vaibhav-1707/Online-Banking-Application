#!/bin/bash

echo "Building Online Banking Application..."
echo

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java JDK 17 or higher"
    exit 1
fi

# Check if lib directory exists
if [ ! -d "lib" ]; then
    echo "ERROR: lib directory not found"
    echo "Please ensure all required JAR files are in the lib/ directory"
    exit 1
fi

# Check required JAR files
if [ ! -f "lib/postgresql-42.7.3.jar" ]; then
    echo "ERROR: postgresql-42.7.3.jar not found in lib/ directory"
    exit 1
fi

if [ ! -f "lib/javax.mail-1.6.2.jar" ]; then
    echo "ERROR: javax.mail-1.6.2.jar not found in lib/ directory"
    exit 1
fi

echo "Dependencies check passed!"
echo

# Create output directory
mkdir -p out

echo "Compiling main Java files..."
javac -cp "lib/*" -d out src/main/java/com/banking/model/*.java src/main/java/com/banking/services/*.java src/main/java/com/banking/ui/*.java

if [ $? -ne 0 ]; then
    echo "ERROR: Main compilation failed"
    exit 1
fi

echo "Compiling test Java files..."
javac -cp "lib/*:out" -d out src/test/java/com/banking/model/*.java src/test/java/com/banking/services/*.java src/test/java/com/banking/*.java

if [ $? -ne 0 ]; then
    echo "ERROR: Test compilation failed"
    exit 1
fi

echo "Compilation successful!"
echo

echo "Testing database connection..."
java -cp "lib/*:out" com.banking.services.DatabaseTest

echo
echo "Starting application..."
java -cp "lib/*:out" com.banking.ui.MainWindow
