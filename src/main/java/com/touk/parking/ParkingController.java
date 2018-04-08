package com.touk.parking;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.touk.parking.model.*;
@RestController
public class ParkingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
    	ParkingDB app=new ParkingDB();
    	app.connectionToDerby();
        app.createNewTables();
    	app.prepareTestValues();
    	return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    @RequestMapping("/0.1/initValuesForTests")
    public void initValuesForTests() {
    	//DRIVER 1
        final int driver1ID=100;
        final String driver1Name= new String("Karol");
        //CAR 1
        final int car1ID=1;
        final String car1PlateNumber=new String("WB1234");
        //CAR 2
        final int car2ID=2;
        final String car2PlateNumber=new String("KR4567");
        //DRIVER 2
        final int driver2ID=101;
        final String driver2Name= new String("Basia");
        //CAR 3
        final int car3ID=3;
        final String car3PlateNumber=new String("WB1256");
        //CAR 4
        final int car4ID=4;
        final String car4PlateNumber=new String("KR4590");
        //PARKING METER 1
        final int parkingMeterID1=10;
        //PARKING METER 2
        final int parkingMeterID2=11;
        //PARKING
        final int parking1ID=1000;
        //CURRENCY
        final String currencyPLN=new String("PLN");
        //Current date
        LocalDateTime currentDate=LocalDateTime.parse("2015-02-20T01:00:00");
        
        Driver d1=new Driver(driver1ID,driver1Name,true);
    	Car c1   =new Car(car1ID,driver1ID,car1PlateNumber);
    	Car c2   =new Car(car2ID,driver1ID,car2PlateNumber);
    	
    	Driver d2=new Driver(driver2ID,driver2Name,false);
    	Car c3   =new Car(car3ID,driver2ID,car3PlateNumber);
    	Car c4   =new Car(car4ID,driver2ID,car4PlateNumber);
    }
	
	@RequestMapping("/0.1/startParkingMeter")
    public Greeting startParkingMeter(@RequestParam(value="carID") int carID, 
    		@RequestParam(value="parkingMeterID") int parkingMeterID) {
        
        
        return new Greeting(counter.incrementAndGet(),
                String.format(template, carID));
    }
}
