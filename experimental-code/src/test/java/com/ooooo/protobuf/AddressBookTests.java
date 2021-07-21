package com.ooooo.protobuf;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.Descriptors.FieldDescriptor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author leizhijie
 * @since 2021/6/5 12:37
 */
public class AddressBookTests {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AddressBookTests.class);
	
	/**
	 * 测试getAllFields, 这个方法可以用来反射， 通过key.getName()方法获取字段的名称，调用field。invoke来设置值
	 */
	@Test
	public void testGetAllFields() {
		AddressBook addressBook = AddressBook.newBuilder()
		                                     .addPerson(Person.newBuilder()
		                                                      .setName("tom")
		                                                      .setId(1)
		                                                      .setEmail("xxx").build())
		                                     .addPerson(Person.newBuilder()
		                                                      .setName("jack")
		                                                      .setId(2)
		                                                      .setEmail("yyy").build())
		                                     .build();
		Map<FieldDescriptor, Object> allFields = addressBook.getAllFields();
		for (Entry<FieldDescriptor, Object> entry : allFields.entrySet()) {
			FieldDescriptor key = entry.getKey();
			Object value = entry.getValue();
			
			//  key.getIndex()  这个key 是第几个,
			LOGGER.info("\nname: {}, key: {}, containingType: {}, javaType: {}, jsonName: {}, value: {}", key.getName(), key.getIndex(), key.getContainingType(), key.getJavaType(), key.getJsonName(), value.toString());
		}
	}
	
	
	/**
	 * 这里是比较了 json 和 protobuf 的序列化速度， 在实际使用中，要考虑传输的字节大小
	 */
	@Test
	public void testProtobufVSJson() throws Exception {
		int N = 1000;
		StopWatch stopWatch = new StopWatch("json");
		stopWatch.start();
		for (int i = 0; i < N; i++) {
			testJson();
		}
		stopWatch.stop();
		
		StopWatch stopWatch2 = new StopWatch("protobuf");
		stopWatch2.start();
		for (int i = 0; i < N; i++) {
			testProtobuf();
		}
		stopWatch2.stop();
		LOGGER.info("testJson: {}", stopWatch.getTotalTimeMillis() / 1000.0);
		LOGGER.info("testProtobuf: {}", stopWatch2.getTotalTimeMillis() / 1000.0);
		
		File jsonFile = new File(System.getProperty("user.dir") + "/src/main/resources/protobuf/person.json");
		File protobufFile = new File(System.getProperty("user.dir") + "/src/main/resources/protobuf/person.protobuf");
		LOGGER.info("jsonFile: {}", jsonFile.length());
		LOGGER.info("protobufFile: {}", protobufFile.length());
	}
	
	
	/**
	 * 测试读取json
	 *
	 * @throws Exception
	 */
	private void testJson() throws Exception {
		PersonJsonObject person = new PersonJsonObject();
		person.setName("tom");
		person.setId(1);
		person.setEmail("xxx");
		
		File file = new File(System.getProperty("user.dir") + "/src/main/resources/protobuf/person.json");
		if (file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		JSON.writeJSONString(fileOutputStream, person);
		fileOutputStream.close();
		
		FileInputStream fileInputStream = new FileInputStream(file);
		person = JSON.parseObject(fileInputStream, PersonJsonObject.class);
		fileInputStream.close();
		//LOGGER.info("{},{},{}", person.getId(), person.getName(), person.getEmail());
	}
	
	private void testProtobuf() throws Exception {
		Person person = Person.newBuilder()
		                      .setId(1)
		                      .setEmail("xxx")
		                      .setName("tom")
		                      .build();
		File file = new File(System.getProperty("user.dir") + "/src/main/resources/protobuf/person.protobuf");
		if (file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		person.writeTo(fileOutputStream);
		fileOutputStream.close();
		
		FileInputStream fileInputStream = new FileInputStream(file);
		person = Person.parseFrom(fileInputStream);
		fileInputStream.close();
		//LOGGER.info("{},{},{}", person.getId(), person.getName(), person.getEmail());
	}
}
