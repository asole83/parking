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

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.touk.parking.model.Car;
import com.touk.parking.model.Currency;
import com.touk.parking.model.Driver;
import com.touk.parking.model.Parking;
import com.touk.parking.model.ParkingMeter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ParkingControllerTests {

    @Autowired
    private MockMvc mockMvc;
    
    final long oneHour=3600000;
    Properties prop = new Properties();
    
    //DRIVER 1
    Driver d1;
    Car c1;
    Car c2;
    
    //DRIVER 2
    Driver d2;
    Car c3;
    Car c4;

    //PARKING
    Parking p1;
    ParkingMeter pm1;
    ParkingMeter pm2;
    
    //CURRENCY
    Currency cur1;
    
    //Current date
    DateTime currentDate= new DateTime(2015,2,1,1,0);
    
    
    public ParkingControllerTests() {
    	try {
    		InputStream input = new FileInputStream("src/resources/TestValues.properties");
    		prop.load(input);
  	
	    	//DRIVER 1
	        d1=new Driver(Integer.parseInt(prop.getProperty("driver1ID")),prop.getProperty("driver1Name"),Boolean.getBoolean(prop.getProperty("driver1IsVIP")));
	        c1=new Car(Integer.parseInt(prop.getProperty("car1ID")),Integer.parseInt(prop.getProperty("driver1ID")),prop.getProperty("car1PlateNumber"));
	        c2=new Car(Integer.parseInt(prop.getProperty("car2ID")),Integer.parseInt(prop.getProperty("driver1ID")),prop.getProperty("car2PlateNumber"));
	
	        //DRIVER 2
	        d2=new Driver(Integer.parseInt(prop.getProperty("driver2ID")),prop.getProperty("driver2Name"),Boolean.getBoolean(prop.getProperty("driver2IsVIP")));
	        c3=new Car(Integer.parseInt(prop.getProperty("car3ID")),Integer.parseInt(prop.getProperty("driver2ID")),prop.getProperty("car3PlateNumber"));
	        c4=new Car(Integer.parseInt(prop.getProperty("car4ID")),Integer.parseInt(prop.getProperty("driver2ID")),prop.getProperty("car4PlateNumber"));
	        
	        //PARKING
	        p1 =new Parking(Integer.parseInt(prop.getProperty("parking1ID")),prop.getProperty("parking1Name"),Integer.parseInt(prop.getProperty("parking1CurrencyID")));
	        pm1=new ParkingMeter(Integer.parseInt(prop.getProperty("parkingMeter1ID")),Integer.parseInt(prop.getProperty("parking1ID")));
	        pm2=new ParkingMeter(Integer.parseInt(prop.getProperty("parkingMeter2ID")),Integer.parseInt(prop.getProperty("parking1ID")));
	        
	        //CURRENCY
	        cur1=new Currency(Integer.parseInt(prop.getProperty("currency1ID")),prop.getProperty("currency1Name"));
    	} catch (Exception e) {
  	  		System.out.println(e);
  	  	}
    	
    }
    
    @Test
    public void startParkingMeterTest() throws Exception {
    	this.mockMvc.perform(get("/0.1/initValuesForTests"))
                .andDo(print()).andExpect(status().isOk());
    	
    	this.mockMvc.perform(get("/0.1/isCarInMyParking")
        		.param("plateNumber", c1.getPlateNumber())
        		.param("parkingID",Integer.toString(p1.getParkingID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("No"));
    	
        callWSStartParkingMeter(c1,pm1);
        
        this.mockMvc.perform(get("/0.1/isCarInMyParking")
        		.param("plateNumber", c1.getPlateNumber())
        		.param("parkingID",Integer.toString(p1.getParkingID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Yes"));
        
    }

	
    
    @Test
    public void stopParkingMeterTest() throws Exception {
    	startParkingMeterTest();
    	
        callWSStopParkingMeter(c1);
        
        this.mockMvc.perform(get("/0.1/isCarInMyParking")
        		.param("plateNumber", c1.getPlateNumber())
        		.param("parkingID",Integer.toString(pm1.getParkingID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("No"));
    }
    
    @Test
    public void seeLastReceiptsVIPTest() throws Exception {
    	DateTimeUtils.setCurrentMillisFixed(currentDate.getMillis());
    	
    	this.mockMvc.perform(get("/0.1/initValuesForTests"))
        .andDo(print()).andExpect(status().isOk());
    	
    	callWSStartParkingMeter(c1,pm1);        
        DateTimeUtils.setCurrentMillisFixed(DateTime.now().plusHours(2).getMillis());
        callWSStopParkingMeter(c1);
        
        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(d1.getDriverID()))
        		.param("numberOfReceipts",Integer.toString(1)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value("2.0"));
        
        callWSStartParkingMeter(c1,pm1);      
        DateTimeUtils.setCurrentMillisFixed(DateTime.now().plusHours(4).getMillis());
        callWSStopParkingMeter(c1);
        
        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(d1.getDriverID()))
        		.param("numberOfReceipts",Integer.toString(3)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[1].price").value("2.0"));
        
        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(d1.getDriverID()))
        		.param("numberOfReceipts",Integer.toString(3)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value("9.5"));
        
        callWSStartParkingMeter(c2,pm1);
        DateTimeUtils.setCurrentMillisFixed(DateTime.now().plusHours(1).getMillis());
        callWSStopParkingMeter(c2);
        
        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(d1.getDriverID()))
        		.param("numberOfReceipts",Integer.toString(3)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[2].price").value("2.0"));
        
        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(d1.getDriverID()))
        		.param("numberOfReceipts",Integer.toString(3)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[1].price").value("9.5"));

        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(d1.getDriverID()))
        		.param("numberOfReceipts",Integer.toString(3)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value("0.0"));
    }
    
    @Test
    public void seeLastReceiptsRegularTest() throws Exception {
    	DateTimeUtils.setCurrentMillisFixed(currentDate.getMillis());
    	
    	this.mockMvc.perform(get("/0.1/initValuesForTests"))
        .andDo(print()).andExpect(status().isOk());
    	
    	callWSStartParkingMeter(c3,pm1);        
        DateTimeUtils.setCurrentMillisFixed(DateTime.now().plusHours(2).getMillis());
        callWSStopParkingMeter(c3);
        
        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(d2.getDriverID()))
        		.param("numberOfReceipts",Integer.toString(1)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value("3.0"));
        
        callWSStartParkingMeter(c3,pm1);
        DateTimeUtils.setCurrentMillisFixed(DateTime.now().plusHours(4).getMillis());
        callWSStopParkingMeter(c3);
        
        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(d2.getDriverID()))
        		.param("numberOfReceipts",Integer.toString(3)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[1].price").value("3.0"));

        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(d2.getDriverID()))
        		.param("numberOfReceipts",Integer.toString(3)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value("15.0"));
        
        callWSStartParkingMeter(c4,pm1);        
        DateTimeUtils.setCurrentMillisFixed(DateTime.now().plusHours(1).getMillis());
        callWSStopParkingMeter(c4);
        
        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(d2.getDriverID()))
        		.param("numberOfReceipts",Integer.toString(3)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[2].price").value("3.0"));
        
        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(d2.getDriverID()))
        		.param("numberOfReceipts",Integer.toString(3)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[1].price").value("15.0"));

        this.mockMvc.perform(get("/0.1/seeLastReceipts")
        		.param("driverID", Integer.toString(d2.getDriverID()))
        		.param("numberOfReceipts",Integer.toString(3)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value("1.0"));
    }
    
    @Test
    public void howMuchMoneyDuringThisDayVIPTest() throws Exception {
    	DateTimeUtils.setCurrentMillisFixed(currentDate.getMillis());

    	this.mockMvc.perform(get("/0.1/initValuesForTests"))
        .andDo(print()).andExpect(status().isOk());
    	
    	callWSStartParkingMeter(c1,pm1);       
        DateTimeUtils.setCurrentMillisFixed(DateTime.now().plusHours(2).getMillis());
        callWSStopParkingMeter(c1);
        
        this.mockMvc.perform(get("/0.1/howMuchMoneyDuringThisDay")
        		.param("parkingID", Integer.toString(p1.getParkingID()))
        		.param("date",currentDate.getYear()+"-"+currentDate.getMonthOfYear()+"-"+currentDate.getDayOfMonth()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("2.0"));
        
        callWSStartParkingMeter(c1,pm1);        
        DateTimeUtils.setCurrentMillisFixed(DateTime.now().plusHours(4).getMillis());
        callWSStopParkingMeter(c1);
        
        this.mockMvc.perform(get("/0.1/howMuchMoneyDuringThisDay")
        		.param("parkingID", Integer.toString(p1.getParkingID()))
        		.param("date",currentDate.getYear()+"-"+currentDate.getMonthOfYear()+"-"+currentDate.getDayOfMonth()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("11.5"));
        
        callWSStartParkingMeter(c2,pm1);        
        DateTimeUtils.setCurrentMillisFixed(DateTime.now().plusHours(1).getMillis());
        callWSStopParkingMeter(c2);
        
        this.mockMvc.perform(get("/0.1/howMuchMoneyDuringThisDay")
        		.param("parkingID", Integer.toString(p1.getParkingID()))
        		.param("date",currentDate.getYear()+"-"+currentDate.getMonthOfYear()+"-"+currentDate.getDayOfMonth()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("11.5"));
    }
    
    @Test
    public void howMuchMoneyDuringThisDayRegularTest() throws Exception {
    	DateTimeUtils.setCurrentMillisFixed(currentDate.getMillis());
    	
    	this.mockMvc.perform(get("/0.1/initValuesForTests"))
        .andDo(print()).andExpect(status().isOk());
    	
    	callWSStartParkingMeter(c3,pm1);        
        DateTimeUtils.setCurrentMillisFixed(DateTime.now().plusHours(2).getMillis());
        callWSStopParkingMeter(c3);
        
        this.mockMvc.perform(get("/0.1/howMuchMoneyDuringThisDay")
        		.param("parkingID", Integer.toString(p1.getParkingID()))
        		.param("date",currentDate.getYear()+"-"+currentDate.getMonthOfYear()+"-"+currentDate.getDayOfMonth()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("3.0"));
        
        callWSStartParkingMeter(c3,pm1);        
        DateTimeUtils.setCurrentMillisFixed(DateTime.now().plusHours(4).getMillis());
        callWSStopParkingMeter(c3);
        
        this.mockMvc.perform(get("/0.1/howMuchMoneyDuringThisDay")
        		.param("parkingID", Integer.toString(p1.getParkingID()))
        		.param("date",currentDate.getYear()+"-"+currentDate.getMonthOfYear()+"-"+currentDate.getDayOfMonth()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("18.0"));
        
        callWSStartParkingMeter(c4,pm1);        
        DateTimeUtils.setCurrentMillisFixed(DateTime.now().plusHours(1).getMillis());        
        callWSStopParkingMeter(c4);
        
        this.mockMvc.perform(get("/0.1/howMuchMoneyDuringThisDay")
        		.param("parkingID", Integer.toString(p1.getParkingID()))
        		.param("date",currentDate.getYear()+"-"+currentDate.getMonthOfYear()+"-"+currentDate.getDayOfMonth()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("19.0"));
    }
    
    private void callWSStartParkingMeter(Car c,ParkingMeter pm) throws Exception {
		this.mockMvc.perform(get("/0.1/startParkingMeter")
        		.param("carID", Integer.toString(c.getCarID()))
        		.param("parkingMeterID",Integer.toString(pm.getParkingMeterID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
	}
    
    private void callWSStopParkingMeter(Car c) throws Exception {
		this.mockMvc.perform(get("/0.1/stopParkingMeter")
        		.param("carID", Integer.toString(c.getCarID())))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Ok"));
	}

}
