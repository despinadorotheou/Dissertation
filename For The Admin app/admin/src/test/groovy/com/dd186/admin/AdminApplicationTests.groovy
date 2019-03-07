package com.dd186.admin

import com.dd186.admin.Domain.Product
import com.dd186.admin.Repositories.CategoryRepository
import com.dd186.admin.Repositories.UserRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AdminApplication.class)
//@DataJpaTest
class AdminApplicationTests extends Specification{

	@Autowired
	WebApplicationContext wac

	MockMvc mockMvc
	ResultActions result

	@Autowired
	TestEntityManager entityManager

	@Autowired
	UserRepository userRepository

	@Autowired
	CategoryRepository categoryRepository

	@Test
	def "Response for HTTP request '/'"() {
		given: "the context of the controller is set up"
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "I do a get '/'"
		result = this.mockMvc.perform(get('/'))
		then: "I should see the view 'login'"
		result
				.andExpect(status().isOk())
				.andExpect(view().name('login'))
	}

	@Test
	def "Response for HTTP request '/login'"() {
		given: "the context of the controller is set up"
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "I do a get '/login'"
		result = this.mockMvc.perform(get('/login'))
		then: "I should see the view 'login'"
		result
				.andExpect(status().isOk())
				.andExpect(view().name('login'))
	}

//	@Test
//	def "Response for HTTP request '/main'"() {
//		given: "the context of the controller is set up"
//		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
//		and: "User"
//		when: "I do a get '/main'"
//		result = this.mockMvc.perform(get('/main'))
//		then: "I should see the view 'main'"
//		result
//				.andExpect(status().isOk())
//				.andExpect(view().name('main'))
//	}


	@Test
	def "1.0 Response for HTTP request '/main/edit'"() {
		given: "the context of the controller is set up"
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
//		entityManager.persist(new Product(1, "water", "500ml", 0.75, 100,categoryRepository.findByCategory("Drink")))
		when: "I do a get '/main/edit'"
		result = this.mockMvc.perform(get('/main/edit')
				.param("productId", "1" ))
		then: "I should see the view 'editProduct'"
		result
				.andExpect(status().isOk())
				.andExpect(view().name('editProduct'))
	}

	@Test
	def "1.1 Response for HTTP request '/main/edit'"() {
		given: "the context of the controller is set up"
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "I do a get '/main/edit'"
		result = this.mockMvc.perform(get('/main/edit')
				.param("productId", "-1" ))
		then: "I should see the view 'editProduct'"
		result
				.andExpect(status().isOk())
				.andExpect(view().name('editProduct'))
	}

	@Test
	def "1.0 Response for HTTP request '/main/add'"() {
		given: "the context of the controller is set up"
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
//		entityManager.persist(new Product(1, "water", "500ml", 0.75, 100,categoryRepository.findByCategory("Drink")))
		when: "I do a post '/main/add'"
		result = this.mockMvc.perform(post('/main/add')
				.param("id", "1" )
				.param("name", "Buxton Still Natural Water Bottle" )
				.param("description", "500ml bottle" )
				.param("price", "0.75" )
				.param("quantity", "100" )
				.param("category", "Drinks" )
				.param("image", "" )
		)
		then: "I should see the view 'main'"
		result
				.andExpect(status().isOk())
				.andExpect(view().name('main'))
	}


//	@Test
//	def "1.1 Response for HTTP request '/main/add'"() {
//		given: "the context of the controller is set up"
//		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
//		when: "I do a post '/main/add'"
//		result = this.mockMvc.perform(post('/main/add')
//				.param("id", "-1" )
//				.param("name", "Buxton Still Natural Water " )
//				.param("description", "500ml bottle" )
//				.param("price", "0.75" )
//				.param("quantity", "100" )
//				.param("category", "Drinks" ))
//		then: "I should see the view 'main'"
//		result
//				.andExpect(status().isOk())
//				.andExpect(view().name('main'))
//	}

	@Test
	def "1.0 Response for HTTP request '/main/delete'"() {
		given: "the context of the controller is set up"
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "I do a get '/main/delete'"
		result = this.mockMvc.perform(get('/main/delete')
				.param("productId", "" ))
		then: "I should see the view 'main'"
		result
				.andExpect(status().isOk())
				.andExpect(view().name('main'))
	}

	@Test
	def "1.1 Response for HTTP request '/main/delete'"() {
		given: "the context of the controller is set up"
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		entityManager.persist(new Product(1, "water", "500ml", 0.75, 100,categoryRepository.findByCategory("Drink")))
		when: "I do a get '/main/delete'"
		result = this.mockMvc.perform(get('/main/delete')
				.param("productId", "1" ))
		then: "I should see the view 'main'"
		result
				.andExpect(status().isOk())
				.andExpect(view().name('main'))
	}

//	@Test
//	def "1.0 Response for HTTP request '/main/image'"() {
//		given: "the context of the controller is set up"
//		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
//		when: "I do a get '/main/delete'"
//		result = this.mockMvc.perform(get('/main/delete')
//				.param("productId", "" ))
//		then: "I should see the view 'main'"
//		result
//				.andExpect(status().isOk())
//				.andExpect(view().name('main'))
//	}
//
//	@Test
//	def "1.1 Response for HTTP request '/main/image'"() {
//		given: "the context of the controller is set up"
//		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
//		when: "I do a get '/main/delete'"
//		result = this.mockMvc.perform(get('/main/delete')
//				.param("productId", "-1" ))
//		then: "I should see the view 'main'"
//		result
//				.andExpect(status().isOk())
//				.andExpect(view().name('main'))
//	}
//
//	@Test
//	def "1.2 Response for HTTP request '/main/image'"() {
//		given: "the context of the controller is set up"
//		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
//		when: "I do a get '/main/delete'"
//		result = this.mockMvc.perform(get('/main/delete')
//				.param("productId", "3" ))
//		then: "I should see the view 'main'"
//		result
//				.andExpect(status().isOk())
//				.andExpect(view().name('main'))
//	}

	@Test
	def "Response for HTTP request '/main/deals'"() {
		given: "the context of the controller is set up"
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "I do a get '/main/deals'"
		result = this.mockMvc.perform(get('/main/deals'))
		then: "I should see the view 'dealsPage'"
		result
				.andExpect(status().isOk())
				.andExpect(view().name('dealsPage'))
	}

	@Test
	def "Response for HTTP request '/main/offers'"() {
		given: "the context of the controller is set up"
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "I do a get '/main/offers'"
		result = this.mockMvc.perform(get('/main/offers'))
		then: "I should see the view 'offersPage'"
		result
				.andExpect(status().isOk())
				.andExpect(view().name('offersPage'))
	}






}
