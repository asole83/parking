package com.touk.parking;

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
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
	
	@RequestMapping("/0.1/greeting2")
    public Driver greeting2(@RequestParam(value="name", defaultValue="World") String name) {
        Driver d1=new Driver(true,1);
		return d1;
    }
}
