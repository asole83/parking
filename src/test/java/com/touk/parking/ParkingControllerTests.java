/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.touk.parking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.touk.parking.model.Car;
import com.touk.parking.model.Driver;
import com.touk.parking.model.ParkingMeter;

import org.joda.time.DateTimeUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ParkingControllerTests {

    @Autowired
    private MockMvc mockMvc;
    
    final long oneHour=3600000;
    
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
	Car c3   =new Car(car1ID,driver2ID,car1PlateNumber);
	Car c4   =new Car(car2ID,driver2ID,car2PlateNumber);
    
    @Test
    public void startParkingMeterTest() throws Exception {
    	ParkingMeter pm1=new ParkingMeter(parking1ID,parkingMeterID1);
    	
    	this.mockMvc.perform(get("/0.1/isCarInMyParking")
        		.param("plateNumber", car1PlateNumber)
        		.param("parkingID",Integer.toString(pm1.getParkingID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("No"));
    	
        this.mockMvc.perform(get("/0.1/startParkingMeter")
        		.param("carID", Integer.toString(car1ID))
        		.param("parkingMeterID",Integer.toString(pm1.getParkingMeterID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        this.mockMvc.perform(get("/0.1/isCarInMyParking")
        		.param("plateNumber", car1PlateNumber)
        		.param("parkingID",Integer.toString(pm1.getParkingID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Yes"));
    }
    
    @Test
    public void stopParkingMeterTest() throws Exception {
    	ParkingMeter pm1=new ParkingMeter(parking1ID,parkingMeterID1);
    	
    	this.mockMvc.perform(get("/0.1/isCarInMyParking")
        		.param("plateNumber", car1PlateNumber)
        		.param("parkingID",Integer.toString(pm1.getParkingID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("No"));
    	
        this.mockMvc.perform(get("/0.1/startParkingMeter")
        		.param("carID", Integer.toString(car1ID))
        		.param("parkingMeterID",Integer.toString(pm1.getParkingMeterID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        this.mockMvc.perform(get("/0.1/isCarInMyParking")
        		.param("plateNumber", car1PlateNumber)
        		.param("parkingID",Integer.toString(pm1.getParkingID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Yes"));
        
        this.mockMvc.perform(get("/0.1/stopParkingMeter")
        		.param("carID", Integer.toString(car1ID))
        		.param("currency",currencyPLN))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        this.mockMvc.perform(get("/0.1/isCarInMyParking")
        		.param("plateNumber", car1PlateNumber)
        		.param("parkingID",Integer.toString(pm1.getParkingID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("No"));
    }
    
    @Test
    public void seeLastReceiptsVIPTest() throws Exception {
    	ParkingMeter pm1=new ParkingMeter(parking1ID,parkingMeterID1);
    	
        DateTimeUtils.setCurrentMillisFixed(currentDate.getLong(ChronoField.MILLI_OF_SECOND));
    	
        this.mockMvc.perform(get("/0.1/startParkingMeter")
        		.param("carID", Integer.toString(car1ID))
        		.param("parkingMeterID",Integer.toString(pm1.getParkingMeterID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        //2 hours later
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        
        this.mockMvc.perform(get("/0.1/stopParkingMeter")
        		.param("carID", Integer.toString(car1ID))
        		.param("currency",currencyPLN))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(driver1ID))
        		.param("howManyReceipts",Integer.toString(1)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("2"));
        
        this.mockMvc.perform(get("/0.1/startParkingMeter")
        		.param("carID", Integer.toString(car1ID))
        		.param("parkingMeterID",Integer.toString(pm1.getParkingMeterID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        //4 hours later
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        
        this.mockMvc.perform(get("/0.1/stopParkingMeter")
        		.param("carID", Integer.toString(car1ID))
        		.param("currency",currencyPLN))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(driver1ID))
        		.param("howManyReceipts",Integer.toString(3)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("2,9.5"));
        
        this.mockMvc.perform(get("/0.1/startParkingMeter")
        		.param("carID", Integer.toString(car2ID))
        		.param("parkingMeterID",Integer.toString(pm1.getParkingMeterID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        //1 hour later
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        
        this.mockMvc.perform(get("/0.1/stopParkingMeter")
        		.param("carID", Integer.toString(car2ID))
        		.param("currency",currencyPLN))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(driver1ID))
        		.param("howManyReceipts",Integer.toString(3)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("2,9.5,0"));
    }
    
    @Test
    public void seeLastReceiptsRegularTest() throws Exception {
    	ParkingMeter pm1=new ParkingMeter(parking1ID,parkingMeterID1);
    	
        DateTimeUtils.setCurrentMillisFixed(currentDate.getLong(ChronoField.MILLI_OF_SECOND));
    	
        this.mockMvc.perform(get("/0.1/startParkingMeter")
        		.param("carID", Integer.toString(car1ID))
        		.param("parkingMeterID",Integer.toString(pm1.getParkingMeterID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        //2 hours later
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        
        this.mockMvc.perform(get("/0.1/stopParkingMeter")
        		.param("carID", Integer.toString(car1ID))
        		.param("currency",currencyPLN))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(driver1ID))
        		.param("howManyReceipts",Integer.toString(1)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("3"));
        
        this.mockMvc.perform(get("/0.1/startParkingMeter")
        		.param("carID", Integer.toString(car1ID))
        		.param("parkingMeterID",Integer.toString(pm1.getParkingMeterID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        //4 hours later
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        
        this.mockMvc.perform(get("/0.1/stopParkingMeter")
        		.param("carID", Integer.toString(car1ID))
        		.param("currency",currencyPLN))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(driver1ID))
        		.param("howManyReceipts",Integer.toString(3)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("3,15"));
        
        this.mockMvc.perform(get("/0.1/startParkingMeter")
        		.param("carID", Integer.toString(car2ID))
        		.param("parkingMeterID",Integer.toString(pm1.getParkingMeterID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        //1 hour later
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        
        this.mockMvc.perform(get("/0.1/stopParkingMeter")
        		.param("carID", Integer.toString(car2ID))
        		.param("currency",currencyPLN))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(driver1ID))
        		.param("howManyReceipts",Integer.toString(3)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("3,15,1"));
    }
    
    @Test
    public void howMuchMoneyDuringThisDayVIPTest() throws Exception {
    	ParkingMeter pm1=new ParkingMeter(parking1ID,parkingMeterID1);
    	ParkingMeter pm2=new ParkingMeter(parking1ID,parkingMeterID2);
    	
    	//Setting initial hour at 1a.m.
    	DateTimeUtils.setCurrentMillisFixed(currentDate.getLong(ChronoField.MILLI_OF_SECOND));
    	
        this.mockMvc.perform(get("/0.1/startParkingMeter")
        		.param("carID", Integer.toString(car1ID))
        		.param("parkingMeterID",Integer.toString(pm1.getParkingMeterID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        //2 hours later
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        
        this.mockMvc.perform(get("/0.1/stopParkingMeter")
        		.param("carID", Integer.toString(car1ID))
        		.param("currency",currencyPLN))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        this.mockMvc.perform(get("/0.1/howMuchMoneyDuringThisDayTest")
        		.param("parkingID", Integer.toString(parking1ID))
        		.param("date",currentDate.getYear()+"-"+currentDate.getMonthValue()+"-"+currentDate.getDayOfMonth()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("2"));
        
        this.mockMvc.perform(get("/0.1/startParkingMeter")
        		.param("carID", Integer.toString(car1ID))
        		.param("parkingMeterID",Integer.toString(pm1.getParkingMeterID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        //4 hours later
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        
        this.mockMvc.perform(get("/0.1/stopParkingMeter")
        		.param("carID", Integer.toString(car1ID))
        		.param("currency",currencyPLN))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        this.mockMvc.perform(get("/0.1/howMuchMoneyDuringThisDayTest")
        		.param("parkingID", Integer.toString(parking1ID))
        		.param("date",currentDate.getYear()+"-"+currentDate.getMonthValue()+"-"+currentDate.getDayOfMonth()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("11.5"));
        
        this.mockMvc.perform(get("/0.1/startParkingMeter")
        		.param("carID", Integer.toString(car2ID))
        		.param("parkingMeterID",Integer.toString(pm2.getParkingMeterID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        //1 hour later
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        
        this.mockMvc.perform(get("/0.1/stopParkingMeter")
        		.param("carID", Integer.toString(car2ID))
        		.param("currency",currencyPLN))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        this.mockMvc.perform(get("/0.1/howMuchMoneyDuringThisDayTest")
        		.param("parkingID", Integer.toString(parking1ID))
        		.param("date",currentDate.getYear()+"-"+currentDate.getMonthValue()+"-"+currentDate.getDayOfMonth()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("11.5"));
    }
    
    @Test
    public void howMuchMoneyDuringThisDayRegularTest() throws Exception {
    	ParkingMeter pm1=new ParkingMeter(parking1ID,parkingMeterID1);
    	ParkingMeter pm2=new ParkingMeter(parking1ID,parkingMeterID2);
    	
    	//Setting initial hour at 1a.m.
    	DateTimeUtils.setCurrentMillisFixed(currentDate.getLong(ChronoField.MILLI_OF_SECOND));
    	
        this.mockMvc.perform(get("/0.1/startParkingMeter")
        		.param("carID", Integer.toString(car3ID))
        		.param("parkingMeterID",Integer.toString(pm1.getParkingMeterID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        //2 hours later
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        
        this.mockMvc.perform(get("/0.1/stopParkingMeter")
        		.param("carID", Integer.toString(car3ID))
        		.param("currency",currencyPLN))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        this.mockMvc.perform(get("/0.1/howMuchMoneyDuringThisDayTest")
        		.param("parkingID", Integer.toString(parking1ID))
        		.param("date",currentDate.getYear()+"-"+currentDate.getMonthValue()+"-"+currentDate.getDayOfMonth()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("3"));
        
        this.mockMvc.perform(get("/0.1/startParkingMeter")
        		.param("carID", Integer.toString(car3ID))
        		.param("parkingMeterID",Integer.toString(pm1.getParkingMeterID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        //4 hours later
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        
        this.mockMvc.perform(get("/0.1/stopParkingMeter")
        		.param("carID", Integer.toString(car3ID))
        		.param("currency",currencyPLN))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        this.mockMvc.perform(get("/0.1/howMuchMoneyDuringThisDayTest")
        		.param("parkingID", Integer.toString(parking1ID))
        		.param("date",currentDate.getYear()+"-"+currentDate.getMonthValue()+"-"+currentDate.getDayOfMonth()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("18"));
        
        this.mockMvc.perform(get("/0.1/startParkingMeter")
        		.param("carID", Integer.toString(car3ID))
        		.param("parkingMeterID",Integer.toString(pm2.getParkingMeterID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        //1 hour later
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis()+oneHour);
        
        this.mockMvc.perform(get("/0.1/stopParkingMeter")
        		.param("carID", Integer.toString(car4ID))
        		.param("currency",currencyPLN))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
        
        this.mockMvc.perform(get("/0.1/howMuchMoneyDuringThisDayTest")
        		.param("parkingID", Integer.toString(parking1ID))
        		.param("date",currentDate.getYear()+"-"+currentDate.getMonthValue()+"-"+currentDate.getDayOfMonth()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("19"));
    }

}
