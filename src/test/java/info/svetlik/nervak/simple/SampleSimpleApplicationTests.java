/*
 * Copyright 2012-2014 the original author or authors.
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

package info.svetlik.nervak.simple;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import info.svetlik.nervaak.simple.NervaakApplication;

/**
 * Tests for {@link NervaakApplication}.
 *
 * @author Dave Syer
 * @author Phillip Webb
 */
class SampleSimpleApplicationTests {

	private String profiles;

	@BeforeEach
	void init() {
		this.profiles = System.getProperty("spring.profiles.active");
	}

	@AfterEach
	void after() {
		if (this.profiles != null) {
			System.setProperty("spring.profiles.active", this.profiles);
		}
		else {
			System.clearProperty("spring.profiles.active");
		}
	}

	@Test
	void testDefaultSettings() throws Exception {
		NervaakApplication.main(new String[0]);
	}

	@Test
	void testCommandLineOverrides() throws Exception {
		NervaakApplication.main(new String[] { "--name=Gordon" });
	}

}
