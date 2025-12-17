package com.ieval.mockTool.controller.impl.travel;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/travel-plans/booking")
public class TravelPlanBookingController extends BasedController {

    // 31. 预订酒店
    @PostMapping("/{planId}/hotels")
    public String bookHotel(@PathVariable Long planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute book hotel...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (!requestBody.containsKey("hotelName") || !requestBody.containsKey("checkInDate")) {
            result.put("code", 400);
            result.put("message", "缺少酒店名称或入住日期");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 201);
            result.put("bookingId", generateRandomNum(10));
            result.put("planId", planId);
            result.put("hotelName", requestBody.getString("hotelName"));
            result.put("hotelAddress", requestBody.getString("hotelAddress"));
            result.put("checkInDate", requestBody.getString("checkInDate"));
            result.put("checkOutDate", requestBody.getString("checkOutDate"));
            result.put("roomType", requestBody.getString("roomType"));
            result.put("guests", requestBody.getInteger("guests"));
            result.put("totalPrice", requestBody.getDouble("totalPrice"));
            result.put("status", "confirmed");
            result.put("bookedAt", Instant.now());
            logger.info("Success to book hotel...");
        }
        return result.toString();
    }

    // 32. 获取酒店预订列表
    @GetMapping("/{planId}/hotels")
    public String getHotelBookings(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get hotel bookings...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray hotels = new JSONArray();
            String[] names = {"巴黎丽思酒店", "东京帝国酒店", "纽约华尔道夫酒店", "伦敦萨伏依酒店", "悉尼歌剧院酒店"};
            String[] addresses = {"巴黎香榭丽舍大街", "东京丸之内", "纽约曼哈顿", "伦敦斯特兰德", "悉尼环形码头"};
            
            for (int i = 0; i < 5; i++) {
                JSONObject hotel = new JSONObject();
                hotel.put("bookingId", generateRandomNum(10));
                hotel.put("planId", planId);
                hotel.put("hotelName", names[i]);
                hotel.put("hotelAddress", addresses[i]);
                hotel.put("checkInDate", "2023-07-0" + (1 + i));
                hotel.put("checkOutDate", "2023-07-0" + (3 + i));
                hotel.put("roomType", "豪华双人间");
                hotel.put("guests", 2);
                hotel.put("totalPrice", 500 + i * 100);
                hotel.put("status", "confirmed");
                hotels.add(hotel);
            }
            
            result.put("code", 200);
            result.put("data", hotels);
            result.put("planId", planId);
            logger.info("Success to get hotel bookings...");
        }
        return result.toString();
    }

    // 33. 取消酒店预订
    @PostMapping("/{planId}/hotels/cancel")
    public String cancelHotelBooking(@PathVariable Integer planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute cancel hotel booking...");
        JSONObject result = new JSONObject();
        
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        logger.info(String.valueOf(planId));
        logger.info(requestBody.getString("bookingId"));
        if (planId == null || planId <= 0 || !requestBody.containsKey("bookingId")) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或预订ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("message", "酒店预订已取消");
            result.put("bookingId", requestBody.getString("bookingId"));
            result.put("planId", planId);
            result.put("cancelledAt", Instant.now());
            logger.info("Success to cancel hotel booking...");
        }
        return result.toString();
    }

    // 34. 预订航班
    @PostMapping("/{planId}/flights")
    public String bookFlight(@PathVariable Long planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute book flight...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (!requestBody.containsKey("flightNumber") || !requestBody.containsKey("departureDate")) {
            result.put("code", 400);
            result.put("message", "缺少航班号或出发日期");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 201);
            result.put("bookingId", generateRandomNum(10));
            result.put("planId", planId);
            result.put("flightNumber", requestBody.getString("flightNumber"));
            result.put("airline", requestBody.getString("airline"));
            result.put("departureAirport", requestBody.getString("departureAirport"));
            result.put("arrivalAirport", requestBody.getString("arrivalAirport"));
            result.put("departureDate", requestBody.getString("departureDate"));
            result.put("arrivalDate", requestBody.getString("arrivalDate"));
            result.put("departureTime", requestBody.getString("departureTime"));
            result.put("arrivalTime", requestBody.getString("arrivalTime"));
            result.put("passengers", requestBody.getInteger("passengers"));
            result.put("totalPrice", requestBody.getDouble("totalPrice"));
            result.put("status", "confirmed");
            result.put("bookedAt", Instant.now());
            logger.info("Success to book flight...");
        }
        return result.toString();
    }

    // 35. 获取航班预订列表
    @GetMapping("/{planId}/flights")
    public String getFlightBookings(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get flight bookings...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray flights = new JSONArray();
            String[] numbers = {"CA1234", "MU5678", "CZ9012", "HU3456", "MF7890"};
            String[] airlines = {"中国国航", "东方航空", "南方航空", "海南航空", "厦门航空"};
            
            for (int i = 0; i < 5; i++) {
                JSONObject flight = new JSONObject();
                flight.put("bookingId", generateRandomNum(10));
                flight.put("planId", planId);
                flight.put("flightNumber", numbers[i]);
                flight.put("airline", airlines[i]);
                flight.put("departureAirport", "PEK");
                flight.put("arrivalAirport", "CDG");
                flight.put("departureDate", "2023-07-0" + (1 + i));
                flight.put("arrivalDate", "2023-07-0" + (1 + i));
                flight.put("departureTime", "08:00");
                flight.put("arrivalTime", "14:00");
                flight.put("passengers", 2);
                flight.put("totalPrice", 3000 + i * 500);
                flight.put("status", "confirmed");
                flights.add(flight);
            }
            
            result.put("code", 200);
            result.put("data", flights);
            result.put("planId", planId);
            logger.info("Success to get flight bookings...");
        }
        return result.toString();
    }

    // 36. 取消航班预订
    @PostMapping("/{planId}/flights/cancel")
    public String cancelFlightBooking(@PathVariable Long planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute cancel flight booking...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0 || !requestBody.containsKey("bookingId")) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或预订ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("message", "航班预订已取消");
            result.put("bookingId", requestBody.getString("bookingId"));
            result.put("planId", planId);
            result.put("cancelledAt", Instant.now());
            logger.info("Success to cancel flight booking...");
        }
        return result.toString();
    }

    // 37. 预订租车
    @PostMapping("/{planId}/cars")
    public String bookCar(@PathVariable Long planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute book car...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (!requestBody.containsKey("carModel") || !requestBody.containsKey("pickupDate")) {
            result.put("code", 400);
            result.put("message", "缺少车型或取车日期");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 201);
            result.put("bookingId", generateRandomNum(10));
            result.put("planId", planId);
            result.put("carModel", requestBody.getString("carModel"));
            result.put("carType", requestBody.getString("carType"));
            result.put("pickupLocation", requestBody.getString("pickupLocation"));
            result.put("returnLocation", requestBody.getString("returnLocation"));
            result.put("pickupDate", requestBody.getString("pickupDate"));
            result.put("returnDate", requestBody.getString("returnDate"));
            result.put("pickupTime", requestBody.getString("pickupTime"));
            result.put("returnTime", requestBody.getString("returnTime"));
            result.put("rentalDays", requestBody.getInteger("rentalDays"));
            result.put("totalPrice", requestBody.getDouble("totalPrice"));
            result.put("status", "confirmed");
            result.put("bookedAt", Instant.now());
            logger.info("Success to book car...");
        }
        return result.toString();
    }

    // 38. 获取租车预订列表
    @GetMapping("/{planId}/cars")
    public String getCarBookings(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get car bookings...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray cars = new JSONArray();
            String[] models = {"丰田凯美瑞", "本田雅阁", "大众帕萨特", "奔驰C级", "宝马3系"};
            String[] types = {"中型车", "中型车", "中型车", "豪华车", "豪华车"};
            
            for (int i = 0; i < 5; i++) {
                JSONObject car = new JSONObject();
                car.put("bookingId", generateRandomNum(10));
                car.put("planId", planId);
                car.put("carModel", models[i]);
                car.put("carType", types[i]);
                car.put("pickupLocation", "机场");
                car.put("returnLocation", "机场");
                car.put("pickupDate", "2023-07-0" + (1 + i));
                car.put("returnDate", "2023-07-0" + (3 + i));
                car.put("pickupTime", "10:00");
                car.put("returnTime", "10:00");
                car.put("rentalDays", 2);
                car.put("totalPrice", 200 + i * 50);
                car.put("status", "confirmed");
                cars.add(car);
            }
            
            result.put("code", 200);
            result.put("data", cars);
            result.put("planId", planId);
            logger.info("Success to get car bookings...");
        }
        return result.toString();
    }

    // 39. 取消租车预订
    @PostMapping("/{planId}/cars/cancel")
    public String cancelCarBooking(@PathVariable Long planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute cancel car booking...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        logger.info(String.valueOf(planId));
        logger.info(requestBody.toJSONString());
        logger.info(requestBody.getString("bookingId"));
        if (planId == null || planId <= 0 || !requestBody.containsKey("bookingId")) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或预订ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("message", "租车预订已取消");
            result.put("bookingId", requestBody.getString("bookingId"));
            result.put("planId", planId);
            result.put("cancelledAt", Instant.now());
            logger.info("Success to cancel car booking...");
        }
        return result.toString();
    }

    // 40. 预订门票
    @PostMapping("/{planId}/tickets")
    public String bookTicket(@PathVariable Long planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute book ticket...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (!requestBody.containsKey("attractionName") || !requestBody.containsKey("visitDate")) {
            result.put("code", 400);
            result.put("message", "缺少景点名称或参观日期");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 201);
            result.put("bookingId", generateRandomNum(10));
            result.put("planId", planId);
            result.put("attractionName", requestBody.getString("attractionName"));
            result.put("attractionAddress", requestBody.getString("attractionAddress"));
            result.put("visitDate", requestBody.getString("visitDate"));
            result.put("visitTime", requestBody.getString("visitTime"));
            result.put("ticketType", requestBody.getString("ticketType"));
            result.put("quantity", requestBody.getInteger("quantity"));
            result.put("unitPrice", requestBody.getDouble("unitPrice"));
            result.put("totalPrice", requestBody.getDouble("totalPrice"));
            result.put("status", "confirmed");
            result.put("bookedAt", Instant.now());
            logger.info("Success to book ticket...");
        }
        return result.toString();
    }
}
