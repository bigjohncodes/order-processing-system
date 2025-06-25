#!/usr/bin/env bash

# Script for creating InventoryOrder microservices project skeleton
# Compatible with Linux, macOS, and Windows (with Git Bash or WSL)

echo "Creating InventoryOrder project structure..."

# Create main project directory if not exists
mkdir -p InventoryOrder
cd InventoryOrder

# Create main project using Spring CLI
spring init \
  --boot-version=3.4.3 \
  --build=gradle \
  --java-version=17 \
  --packaging=jar \
  --name=InventoryOrder \
  --package-name=com.InventoryOrder \
  --groupId=com.InventoryOrder \
  --dependencies=web,actuator \
  --version=1.0.0-SNAPSHOT \
  .

# Create microservices module
mkdir -p microservices
cd microservices

# Create Order Service
spring init \
  --boot-version=3.4.3 \
  --build=gradle \
  --java-version=17 \
  --packaging=jar \
  --name=orderService \
  --package-name=com.InventoryOrder.microservices.order \
  --groupId=com.InventoryOrder.microservices \
  --dependencies=web,data-mongodb,validation,actuator \
  --version=1.0.0-SNAPSHOT \
  orderService

# Create Inventory Service
spring init \
  --boot-version=3.4.3 \
  --build=gradle \
  --java-version=17 \
  --packaging=jar \
  --name=InventoryService \
  --package-name=com.InventoryOrder.microservices.Inventory \
  --groupId=com.InventoryOrder.microservices \
  --dependencies=web,data-jpa,validation,actuator \
  --version=1.0.0-SNAPSHOT \
  InventoryService

cd ..

# Create proto-api module
mkdir -p proto-api
cd proto-api

spring init \
  --boot-version=3.4.3 \
  --build=gradle \
  --java-version=17 \
  --packaging=jar \
  --name=proto-api \
  --package-name=com.InventoryOrder.proto \
  --groupId=com.InventoryOrder.proto \
  --dependencies=web \
  --version=1.0.0-SNAPSHOT \
  .

# Create proto directory structure
mkdir -p src/main/proto

cd ..

# Create Utils module
mkdir -p Utils
cd Utils

spring init \
  --boot-version=3.4.3 \
  --build=gradle \
  --java-version=17 \
  --packaging=jar \
  --name=Utils \
  --package-name=com.InventoryOrder.utils \
  --groupId=com.InventoryOrder.utils \
  --dependencies=web \
  --version=1.0.0-SNAPSHOT \
  .

# Create utility directories
mkdir -p src/main/java/com/InventoryOrder/DTOs
mkdir -p src/main/java/com/InventoryOrder/Events
mkdir -p src/main/java/com/InventoryOrder/Exceptions

cd ..

# Create empty docker-compose.yml
touch docker-compose.yml

echo "InventoryOrder project skeleton structure created successfully!"
echo "Navigate to the InventoryOrder directory to begin developing your microservices."


# From the project root directory
./gradlew :microservices:orderService:bootRun
./gradlew :microservices:InventoryService:bootRun