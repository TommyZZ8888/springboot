package com.www.search.rediSearch.service;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class SampleDataService {
	@Autowired
	RedisService redisService;
	
	List<SseEmitter> emitterList;
	long previousTime;
	
	public SampleDataService() {
		emitterList = new ArrayList<SseEmitter>();
		previousTime = 0;
	}
	
	@Async
	public CompletableFuture<Long> PopulateSampleData(long numentries) throws InterruptedException {
		Long result = Long.valueOf(0);
		previousTime = System.currentTimeMillis();
		
		Faker faker = new Faker(Locale.US);
		
		Calendar cal = Calendar.getInstance();
		Date maxDate = cal.getTime();
		cal.add(Calendar.YEAR, -10);
		Date minDate = cal.getTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		// single thread
		for(int i = 0; i < numentries; i++) {
			Map<String, String> customerDetails = new HashMap<String, String>();
			Name name = faker.name();
			String cif = faker.finance().bic();
			customerDetails.put("First name", name.firstName());
			customerDetails.put("Last name", name.lastName());
			customerDetails.put("CIF", cif);
			customerDetails.put("Address", faker.address().fullAddress());
			customerDetails.put("Phone", faker.phoneNumber().phoneNumber());
			customerDetails.put("E-mail", faker.internet().emailAddress());
			customerDetails.put("Credit card 1", faker.finance().creditCard());
			customerDetails.put("Credit card 2", faker.finance().creditCard());
			
			Date regDate = faker.date().between(minDate, maxDate);
			customerDetails.put("Registration date epoch", String.valueOf(regDate.getTime()));
			customerDetails.put("Registration date", sdf.format(regDate));
			
			redisService.addCustomer("cust:" + cif, customerDetails);
			
			if((System.currentTimeMillis() - previousTime) > 500) {
				previousTime = System.currentTimeMillis();
				
				try {
					broadcastEvent(i, numentries);
				}
				catch(Exception ex) {
//					ex.printStackTrace();
				}
			}
		}
		
		broadcastEvent(numentries, numentries);
		
		return CompletableFuture.completedFuture(result);
	}
	
	public void addSseEmitter(SseEmitter emitter) {
		synchronized(emitterList) { emitterList.add(emitter); }
		previousTime = 0; // force status update as soon as possible
	}
	
	private void broadcastEvent(long currentProgress, long maxProgress) {
		List<SseEmitter> pendingRemoval = new ArrayList<SseEmitter>();

		String data = formatJsonData(currentProgress, maxProgress);
		synchronized(emitterList) {
			for(SseEmitter emitter: emitterList) {
				try { 
					emitter.send(SseEmitter.event().data(data)); 
				}
				catch(IOException ex) {
					pendingRemoval.add(emitter);
				}
			}
	
			emitterList.removeAll(pendingRemoval);
		}
	}
	
	private String formatJsonData(long currentProgress, long maxProgress) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("{\"progress\": ");
		sb.append(currentProgress);
		sb.append(", \"max\": ");
		sb.append(maxProgress);
		sb.append("}");
		
		return sb.toString();
	}
}
