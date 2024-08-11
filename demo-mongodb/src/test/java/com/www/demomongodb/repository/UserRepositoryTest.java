package com.www.demomongodb.repository;

import com.www.demomongodb.DemoMongodbApplicationTests;
import com.www.demomongodb.model.UserAgg;
import com.www.demomongodb.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @Describtion: UserRepositoryTest
 * @Author: 张卫刚
 * @Date: 2024/8/4 10:46
 */

/**
 * https://docs.spring.io/spring-data/mongodb/reference/mongodb.html
 * insert/save  insert插入主键重复报错，save更新内容
 */
@Slf4j
public class UserRepositoryTest extends DemoMongodbApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MongoTemplate mongoTemplate;


	@Test
	void save() {
		Users users = Users.builder().age(23L).name("tommy").status("processed").id("5").build();
		mongoTemplate.save(users);
	}

	@Test
	void batchSave() {
		List<Users> users = new ArrayList<>();
		for (int i = 2; i < 5; i++) {
			Users user = Users.builder().age(18L).name("tommy").status("processed").id("" + i).build();
			users.add(user);
		}
		mongoTemplate.insertAll(users);
	}

	@Test
	void update() {
		Optional<Users> usersOptional = userRepository.findById("1");
		usersOptional.ifPresent(user -> {
			user.setName("update");
			user.setAge(20L);
			mongoTemplate.save(user);
		});
	}

	@Test
	public void testUpdateAge() {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is("1"));
		Update update = new Update();
		update.inc("age", 1L);
		mongoTemplate.updateFirst(query, update, "users");

		userRepository.findById("1").ifPresent(user -> log.info("【age】= {}", user.getName()));
	}

	@Test
	void delete() {
		userRepository.deleteById("66ae2e20c2310000ba0017e3");
	}

	@Test
	void query() {
		Sort sort = Sort.by("age").descending();
		PageRequest pageRequest = PageRequest.of(0, 3, sort);
		userRepository.findAll(pageRequest).forEach(user -> log.info("【age】= {}", user.getAge()));
	}

	@Test
	void query2() {
		Query query = new Query();
		query.addCriteria(Criteria.where("age").is(18)
				.orOperator(Criteria.where("name").is("tommy")));
		mongoTemplate.find(query, Users.class, "users").forEach(user -> log.info("【age】= {}", user));

		mongoTemplate.query(Users.class).matching(Query.query(Criteria.where("age").is(18)
				.orOperator(Criteria.where("name").is("tommy")))).all().forEach(user -> log.info("【age】= {}", user));
	}

	@Test
	void query3() {
		Query query = new Query();
		query.fields().include("name", "age");
		query.addCriteria(Criteria.where("age").is(18));
		mongoTemplate.find(query, Users.class, "users").forEach(user -> log.info("【user】= {}", user));
	}

	@Test
	void count() {
		Query query = new Query();
		query.fields().include("name", "age");
		query.addCriteria(Criteria.where("age").is(18));
		long users = mongoTemplate.count(query, Users.class, "users");
		log.info("【users】= {}", users);
	}

	@Test
	void fuzzyQuery() {
		userRepository.findByNameLike("tom").forEach(user -> log.info("【name】= {}", user.getName()));
	}

	@Test
	void aggregationQuery() {
		Aggregation agg = newAggregation(
				project("age"),
				unwind("age"),
				group("age").count().as("n"),
				project("n").and("age").previousOperation(),
				sort(DESC, "age")
		);
		AggregationResults<Users> results = mongoTemplate.aggregate(agg, "users", Users.class);
		List<Users> tagCount = results.getMappedResults();
//		tagCount.forEach(user -> log.info("【user】= {}", user));


		TypedAggregation<Users> agg2 = newAggregation(Users.class,
				project("name", "age")
						.and("age").plus(1).as("netPricePlus1")
						.and("age").minus(1).as("netPriceMinus1")
						.and("age").multiply(1.19).as("grossPrice")
						.and("age").divide(2).as("netPriceDiv2")
		);
		// 参数2 inputCollectionName必须，否则结果null
		AggregationResults<UserAgg> result = mongoTemplate.aggregate(agg2,"users" ,UserAgg.class);
		List<UserAgg> resultList = result.getMappedResults();
		resultList.forEach(document -> log.info("【document】= {}", document));
	}
}
