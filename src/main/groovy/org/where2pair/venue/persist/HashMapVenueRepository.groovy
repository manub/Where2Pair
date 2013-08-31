package org.where2pair.venue.persist

import java.util.List
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.where2pair.venue.Coordinates;
import org.where2pair.venue.Venue;
import org.where2pair.venue.VenueRepository;

class HashMapVenueRepository implements VenueRepository {

	ConcurrentHashMap venues = [:]
	AtomicInteger idGenerator = new AtomicInteger()
	
	@Override
	public List getAll() {
		venues.collect { id, venue ->
			venue.clone()
		}.asImmutable()
	}

	@Override
	public Venue get(long id) {
		find(id)?.clone()
	}

	@Override
	public long save(Venue venue) {
		venue.id = idGenerator.incrementAndGet()
		venues[venue.id] = venue.clone()
		venue.id
	}

	@Override
	public Venue findByNameAndCoordinates(String name, Coordinates coordinates) {
		venues.find { id, Venue venue -> 
			venue.name == name && venue.location == coordinates 
		}?.value
	}

	@Override
	public void update(Venue venue) {
		venues.replace(venue.id, venue.clone())
	}

	private Venue find(long id) {
		venues[id]
	}
}