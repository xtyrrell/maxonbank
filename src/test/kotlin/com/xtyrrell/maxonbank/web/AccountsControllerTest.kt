package com.xtyrrell.maxonbank.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

// TODO: Write these tests

@SpringBootTest
@AutoConfigureMockMvc
internal class AccountsControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    val baseUrl = "/accounts"

    //val accountId

    @BeforeEach
    fun setUp() {

    }

    @Nested
    @DisplayName("GET /accounts")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetAccounts {

        @Test
        fun `should return all accounts`() {
            // when/then
            //MockMvcRequestBuilders.
            mockMvc
                .perform(
                    MockMvcRequestBuilders.get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                //.andDo { print() }
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().json("[]"))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.path").value(path))
            //{
            //        status { isOk() }
            //        //content { contentType(MediaType.APPLICATION_JSON) }
            //        //jsonPath("$[0].accountId") { exists() }
            //        content { string("[]") }
            //    }
        }
    }
}
