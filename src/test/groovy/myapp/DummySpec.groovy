package myapp

import grails.test.hibernate.HibernateSpec
import org.springframework.dao.DataAccessException

import javax.persistence.PersistenceException

class DummySpec extends HibernateSpec {

	def "test exception wrapping"() {
		Dummy.withNewTransaction {
			new Dummy(name: "dummy").save()
		}

		when:
		Dummy dummy = Dummy.first()
		dummy.name = "first dummy"

		Dummy.withNewTransaction {
			Dummy.first().name = "concurrent dummy wins!"
		}

		dummy.save(flush: true)

		then:
		thrown DataAccessException
	}

}
