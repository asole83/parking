This is an exercise for Company Touk.

Software used:
Eclipse Jee Oxygen.2 Release 4.7.2
Apache Maven 3.5.3
Git
Java 8 (jdk 1.8.0_111)
Java DB as database 
Properties to have the values for environment of tests
org.joda.time to manipulate time in tests

Examples of calls to webservices:
http://localhost:8080/0.1/isCarInMyParking?plateNumber=WB1234&parkingID=1000
http://localhost:8080/0.1/startParkingMeter?carID=1&parkingMeterID=10
http://localhost:8080/0.1/stopParkingMeter?carID=1
http://localhost:8080/0.1/seeLastReceipts?driverID=100&numberOfReceipts=1

Bibliography:
https://spring.io/guides/gs/rest-service/
http://shengwangi.blogspot.co.uk/2015/10/how-to-use-embedded-java-db-derby-in-maven.html
https://www.mkyong.com/java/java-properties-file-examples/