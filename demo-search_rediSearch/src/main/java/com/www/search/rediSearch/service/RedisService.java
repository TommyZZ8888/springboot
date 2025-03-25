package com.www.search.rediSearch.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.search.*;
import redis.clients.jedis.search.Schema.TextField;
import redis.clients.jedis.search.querybuilder.Node;
import redis.clients.jedis.search.querybuilder.QueryBuilders;
import redis.clients.jedis.search.querybuilder.Values;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Service
public class RedisService {
	private String INDEX_CUSTOMER = "idx_cust";
	
	@Value("${jedis.redis.host}")
	private String host;

	@Value("${jedis.redis.port}")
	private int port;

	@Value("${jedis.redis.username}")
	private String username;

	@Value("${jedis.redis.password}")
	private String password;
	
	private JedisPooled jedis = null;

	@PostConstruct
	public void postConstructRoutine() {
		jedis = new JedisPooled(host, port, username, password);
		
		initializeIndex();
	}
	
	public long initializeIndex() {
		RediSearchCommands rediSearch = (RediSearchCommands) jedis;
		
		// create index if it doesn't exist
		if(!rediSearch.ftList().contains(INDEX_CUSTOMER)) {
			// Disable stopwords so we can search for people with the name "An"
			// FT.CREATE idx_cust PREFIX 1 "cust:" STOPWORDS 0 SCHEMA "First Name" AS firstname TEXT NOSTEM "Last Name" AS lastname TEXT NOSTEM "CIF" AS cif TAG
			// "Credit card 1" as "cc1" TAG "Credit card 2" as "cc2" TAG
			FTCreateParams params = FTCreateParams.createParams();
			params.addPrefix("cust:");
			
			TextField firstName = new TextField("First name", 2.0, false, true); // no stemming
			TextField lastName = new TextField("Last name", 2.0, false, true); // no stemming
			
			Schema sc = new Schema()
	        .addField(firstName).as("firstname")
	        .addField(lastName).as("lastname")
	        .addTagField("CIF").as("cif")
	        .addTagField("Credit card 1").as("cc1")
	        .addTagField("Credit card 2").as("cc2")
	        .addNumericField("Registration date epoch").as("regdate");

			IndexDefinition def = new IndexDefinition()
			        .setPrefixes(new String[]{"cust:"});
			
			IndexOptions opt = IndexOptions.defaultOptions();
			opt.setNoStopwords();
			        
			rediSearch.ftCreate(INDEX_CUSTOMER, opt.setDefinition(def), sc);
		}
		
		return 0;
	}
	
	public long addCustomer(String key, Map<String, String> hash) {
		long result = jedis.hset(key, hash);
		
		return result;
	}
	
	public SearchResult searchByCif(String cif) {
		// FT.SEARCH idx_cust "@cif:{QYUFWYCW6RJ}"
		RediSearchCommands rediSearch = (RediSearchCommands) jedis;
		Query q = new Query("@cif:{" + RediSearchUtil.escape(cif) + "}").limit(0, 10);
		SearchResult result = rediSearch.ftSearch(INDEX_CUSTOMER, q);

		return result;
	}
	
	public SearchResult searchByCifWithQueryBuilders(String cif) {
		// FT.SEARCH idx_cust "@cif:{QYUFWYCW6RJ}"
		RediSearchCommands rediSearch = (RediSearchCommands) jedis;
		
		Node n = QueryBuilders.intersect().add("cif", Values.tags(RediSearchUtil.escape(cif)));
		Query q = new Query(n.toString());
		
		SearchResult result = rediSearch.ftSearch(INDEX_CUSTOMER, q);

		return result;
	}
	
	public SearchResult searchFullText(String text) {
		// FT.SEARCH idx_cust "Edi*"
		
		// Notes on VERBATIM option
		//		> FT.EXPLAINCLI "idx_cust" "@lastname:smiths"
		//		1) @lastname:UNION {
		//		2)  @lastname:smiths
		//		3)  @lastname:+smith(expanded)
		//		4)  @lastname:smith(expanded)
		//		5) }
		//		6) 
		//		> FT.EXPLAINCLI "idx_cust" "@lastname:smiths" VERBATIM
		//		1) @lastname:smiths
		//		2)
		RediSearchCommands rediSearch = (RediSearchCommands) jedis;
		
		Query q = new Query(text).setVerbatim().limit(0, 10); // not escaping to support special characters, verbatim to prevent stemming
		SearchResult result = rediSearch.ftSearch(INDEX_CUSTOMER, q);
		
		return result;
	}

	public SearchResult searchLastNameOnly(String text) {
		// FT.SEARCH idx_cust "@lastname:Pau*"
		RediSearchCommands rediSearch = (RediSearchCommands) jedis;
		
		Query q = new Query("@lastname:" + text).setVerbatim().limit(0, 10); // not escaping to support special characters, verbatim to prevent stemming
		SearchResult result = rediSearch.ftSearch(INDEX_CUSTOMER, q);
		
		return result;
	}

	public SearchResult searchLastNameOnlyWithQueryBuilders(String text) {
		// FT.SEARCH idx_cust "@lastname:Pau*"
		RediSearchCommands rediSearch = (RediSearchCommands) jedis;

		Node n = QueryBuilders.intersect().add("lastname", text);
		Query q = new Query(n.toString()).setVerbatim().limit(0, 10); // not escaping to support special characters, verbatim to prevent stemming
		SearchResult result = rediSearch.ftSearch(INDEX_CUSTOMER, q);
		
		return result;
	}

	public SearchResult searchCardNumber(String number) {
		// FT.SEARCH idx_cust "(@cc1:{6759\\-6040\\-5042\\-5701\\-836})|(@cc2:{6759\\-6040\\-5042\\-5701\\-836})"
		RediSearchCommands rediSearch = (RediSearchCommands) jedis;
		
		String formattedNumber = RediSearchUtil.escape(number);
		Query q = new Query("(@cc1:{" + formattedNumber + "})|(@cc2:{" + formattedNumber + "})").limit(0, 10);
		SearchResult result = rediSearch.ftSearch(INDEX_CUSTOMER, q);
		
		return result;
	}
	
	public SearchResult searchCardNumberWithQueryBuilders(String number) {
		// FT.SEARCH idx_cust "(@cc1:{6759\\-6040\\-5042\\-5701\\-836})|(@cc2:{6759\\-6040\\-5042\\-5701\\-836})"
		RediSearchCommands rediSearch = (RediSearchCommands) jedis;
		
		String formattedNumber = RediSearchUtil.escape(number);
		Node n = QueryBuilders.union().add("cc1", Values.tags(formattedNumber)).add("cc2", Values.tags(formattedNumber));
		Query q = new Query(n.toString(Node.Parenthesize.ALWAYS)).limit(0, 10);
        
        SearchResult result = rediSearch.ftSearch(INDEX_CUSTOMER, q);
		
		return result;
	}
	
	public SearchResult searchRegistrationBetweenDate(Date from, Date to) {
		// FT.SEARCH idx_cust "@regdate:[147609299246 1476092992469]" SORTBY regdate ASC
		RediSearchCommands rediSearch = (RediSearchCommands) jedis;
		
		// allow search within the current day. i.e.g [day1time00 day2time00]
		if(from.getTime() == to.getTime()) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(to);
			cal.add(Calendar.DATE, 1);
			to = cal.getTime();
		}
		
		Query q = new Query("@regdate:[" + String.valueOf(from.getTime()) + " " + String.valueOf(to.getTime()) + "]").setSortBy("regdate", true).limit(0, 10);
		SearchResult result = rediSearch.ftSearch(INDEX_CUSTOMER, q);
		return result;
	}

	public SearchResult searchRegistrationBetweenDateWithQueryBuilders(Date from, Date to) {
		// FT.SEARCH idx_cust "@regdate:[1603082217190 1603082217195]" SORTBY regdate ASC
		RediSearchCommands rediSearch = (RediSearchCommands) jedis;
		
		// allow search within the current day. i.e.g [day1time00..day2time00]
		if(from.getTime() == to.getTime()) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(to);
			cal.add(Calendar.DATE, 1);
			to = cal.getTime();
		}
		
		Node n = QueryBuilders.intersect().add("regdate", Values.between(from.getTime(), to.getTime()));
		Query q = new Query(n.toString()).setSortBy("regdate", true).limit(0, 10);
		SearchResult result = rediSearch.ftSearch(INDEX_CUSTOMER, q);
		return result;
	}
}
