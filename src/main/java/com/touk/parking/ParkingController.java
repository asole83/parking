package com.touk.parking;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.touk.parking.model.*;
@RestController
public class ParkingController {
    ParkingDB DB=new ParkingDB();

    public ParkingController() {
    	DB.connectionToDerby();
    }
    
    @RequestMapping("/0.1/initValuesForTests")
    public void initValuesForTests() {
    	DB.createNewTables();
    	DB.prepareTestValues();
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
    public List<SendingReceipt> seeLastReceipts(@RequestParam(value="driverID") int driverID,
    		@RequestParam(value="numberOfReceipts") int numberOfReceipts) {
        return DB.seeLastReceipts(driverID,numberOfReceipts);
    }
}
