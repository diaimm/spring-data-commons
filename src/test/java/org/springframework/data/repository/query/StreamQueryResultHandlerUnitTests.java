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
package org.springframework.data.repository.query;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import lombok.Value;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.query.ResultProcessor.StreamQueryResultHandler;

/**
 * Unit tests for {@link StreamQueryResultHandler}.
 *
 * @author John Blum
 * @author Oliver Gierke
 */
public class StreamQueryResultHandlerUnitTests {

	StreamQueryResultHandler handler;

	@Before
	public void setUp() {

		ReturnedType returnedType = ReturnedType.of(String.class, Person.class, mock(ProjectionFactory.class));

		this.handler = new StreamQueryResultHandler(returnedType, new Converter<Object, Object>() {

			@Override
			public Object convert(Object source) {
				return source.toString();
			}
		});
	}

	/**
	 * @see DATACMNS-868
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void mapsStreamUsingConverter() {

		Stream<Person> people = Arrays.asList(Person.of("Dave", "Matthews")).stream();

		Object result = this.handler.handle(people);

		assertThat(result).isEqualTo(instanceOf(Stream.class));

		Stream<Object> stream = (Stream<Object>) result;

		assertThat(stream.allMatch(new Predicate<Object>() {

			@Override
			public boolean test(Object t) {

				assertThat(t).isEqualTo(instanceOf(String.class));

				String string = (String) t;

				assertThat(string).contains("Dave");
				assertThat(string).contains("Matthews");

				return true;
			}
		}), is(true));

		stream.close();
	}

	/**
	 * @see DATACMNS-868
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullSource() {
		handler.handle(null);
	}

	@Value(staticConstructor = "of")
	static class Person {
		String firstName, lastName;
	}
}
