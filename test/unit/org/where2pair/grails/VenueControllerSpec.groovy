package org.where2pair.grails

import grails.converters.JSON
import org.skyscreamer.jsonassert.JSONAssert
import org.where2pair.Coordinates
import org.where2pair.Venue
import org.where2pair.VenueFinder
import org.where2pair.VenueWithDistance
import org.where2pair.WeeklyOpeningTimesBuilder

import spock.lang.Specification

@TestFor(VenueController)
class VenueControllerSpec extends Specification {

	VenueFinder venueFinder = Mock()
	GormVenueRepository gormVenueRepository = Mock()
	VenueConverter venueConverter = new VenueConverter()

	def "should show all venues"() {
		given:
		request.method = 'GET'
		gormVenueRepository.getAll() >> 100.venues()
		List venueDTOs = toVenueDTO(100.venues())
		
		when:
		controller.show()
		
		then:
		response.text.equalToJsonOf(venueDTOs)
		response.status == 200
	}

	def "should display search results for given coordinates"() {
		given:
		request.method = 'GET'
		controller.params.'location1' = '1.0,0.1'
		venueFinder.findNearestTo(new Coordinates(lat: 1.0, lng: 0.1)) >> 10.venuesWithDistance()
		List venueDTOs = toVenueWithDistanceDTO(10.venuesWithDistance())
		
		when:
		controller.findNearest()

		then:
		response.text.equalToJsonOf(venueDTOs)
		response.status == 200
	}

	def "should save new venues"() {
		given:
		VenueDTO venueDTO = new VenueDTO(
				latitude: 1.0,
				longitude: 0.1,
				openHours: [monday: [
						[openHour: 12, openMinute: 0, closeHour: 18, closeMinute: 30]
					],
					tuesday: [
						[openHour: 8, openMinute: 0, closeHour: 11, closeMinute: 0]
					]]
				)
		request.method = 'POST'
		request.json = venueDTO

		when:
		controller.save()

		then:
		1 * gormVenueRepository.save(venueDTO)
		response.text.equalToJsonOf(venueDTO)
		response.status == 200
	}

	def "test http actions"() {
	}

	def "should handle errors"() {
	}

	private def toVenueDTO(List venues) {
		venueConverter.asVenueDTOs(venues)
	}
	
	private def toVenueWithDistanceDTO(List venues) {
		venueConverter.asVenueWithDistanceDTOs(venues)
	}
	
	def setup() {
		controller.venueFinder = venueFinder
		controller.gormVenueRepository = gormVenueRepository
		controller.venueConverter = venueConverter
		String.mixin(JSONMatcher)
		Integer.mixin(VenuesMixin)
	}

	def cleanup() {
		String.metaClass = null
		Integer.metaClass = null
	}

	@Category(String)
	static class JSONMatcher {
		boolean equalToJsonOf(Object object) {
			JSONAssert.assertEquals(new JSON(object).toString(), this, false)
			true
		}
	}

	@Category(Integer)
	static class VenuesMixin {
		List venues() {
			(0..this).collect { new Venue(location: new Coordinates(1.0, 0.5),
				weeklyOpeningTimes: new WeeklyOpeningTimesBuilder().build()) }
		}
		
		List venuesWithDistance() {
			(0..this).collect { new VenueWithDistance(venue: new Venue(location: new Coordinates(1.0, 0.5),
				weeklyOpeningTimes: new WeeklyOpeningTimesBuilder().build()),
				distanceInKm: 10.5) }
		}
	}
}