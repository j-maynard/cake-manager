package com.punkyideas.cakemgr;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations="classpath:application-test.properties")
class CakemgrApplicationTests {

	@MockBean
	private CakeLoader cakeLoader;

	@Test
	void contextLoads() {
	}

}
