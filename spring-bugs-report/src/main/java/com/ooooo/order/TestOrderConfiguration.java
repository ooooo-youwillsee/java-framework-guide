package com.ooooo.order;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.List;

/**
 * @author ooooo
 * @since 2021/06/09 17:59
 */
@Configuration
public class TestOrderConfiguration {


	public TestOrderConfiguration(ObjectProvider<List<IHello>> listObjectProvider) {
		List<IHello> IHellos = listObjectProvider.getIfAvailable();
		IHellos.forEach(System.out::println);
		System.out.println("=====================");
		AnnotationAwareOrderComparator.sort(IHellos);
		IHellos.forEach(System.out::println);
	}
}


