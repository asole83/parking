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
    ParkingDB DB=new ParkingDB();

    public ParkingController() {
    	DB.connectionToDerby();
    }
    
    @RequestMapping("/0.1/initValuesForTests")
    public void initValuesForTests() {
    	DB.createNewTables();
    	DB.prepareTestValues();
    }
    
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
    	
    	return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    @RequestMapping("/0.1/isCarInMyParking")
    public Response isCarInMyParking(@RequestParam(value="plateNumber") String plateNumber, 
    		@RequestParam(value="parkingID") int parkingID) {
        return DB.isCarInMyParking(plateNumber,parkingID);
    }
    
	@RequestMapping("/0.1/startParkingMeter")
    public Response startParkingMeter(@RequestParam(value="carID") int carID, 
    		@RequestParam(value="parkingMeterID") int parkingMeterID) {
        return DB.startParkingMeter(carID,parkingMeterID);
    }
	
	@RequestMapping("/0.1/stopParkingMeter")
    public Response stopParkingMeter(@RequestParam(value="carID") int carID) {
        return DB.stopParkingMeter(carID);
    }
	
	@RequestMapping("/0.1/howMuchMoneyDuringThisDay")
    public Response howMuchMoneyDuringThisDay(@RequestParam(value="parkingID") int parkingID,
    		@RequestParam(value="date") String date) {
        return DB.howMuchMoneyDuringThisDay (parkingID, date);
    }
	
	@RequestMapping("/0.1/seeLastReceipts")
    public Response seeLastReceipts(@RequestParam(value="driverID") int driverID,
    		@RequestParam(value="numberOfReceipts") int numberOfReceipts) {
        return DB.seeLastReceipts(driverID,numberOfReceipts);
    }
}
