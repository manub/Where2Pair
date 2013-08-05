package org.where2pair.grails

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import net.sf.json.JSON
import spock.lang.Ignore;
import spock.lang.Specification

class VenueIntegrationSpec extends Specification {
    public static final String VENUE_NAME = "my test venue"

	@Ignore
    def "store and retrieve venue though the REST api"() {
        given:
        VenueDto venueDto = new VenueDto(name: VENUE_NAME, latitude: 1.0, longitude: 0.5,
                openHours: ["monday": [[openHour: 12, openMinute: 0, closeHour: 18, closeMinute: 0]]])

        when:
        JSON retrievedVenue = storeAndRetrieve(venueDto)

        then:
        retrievedVenue.name == VENUE_NAME
    }

    private JSON storeAndRetrieve(venueDto) {
        def where2pair = new RESTClient("http://localhost:8080")

        JSON savedVenue = where2pair.post(path: "Where2Pair/venue", body: venueDto, requestContentType: ContentType.JSON).data
        where2pair.get(path: "Where2Pair/venue/${savedVenue.id}", requestContentType: ContentType.URLENC).data
    }
}
