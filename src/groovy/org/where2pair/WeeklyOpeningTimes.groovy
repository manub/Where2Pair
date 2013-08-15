package org.where2pair

import groovy.transform.EqualsAndHashCode;
import groovy.transform.Immutable
import groovy.transform.ToString;


@EqualsAndHashCode
@ToString
class WeeklyOpeningTimes {

	Map weeklyOpeningTimes
	
	boolean isOpen(OpenTimesCriteria openTimesCriteria) {
		weeklyOpeningTimes[openTimesCriteria.dayOfWeek]
			.isOpen(openTimesCriteria.openFrom, openTimesCriteria.openUntil)
	}
	
	def getAt(key) {
		weeklyOpeningTimes[key]
	}
	
	@Override
	void each(Closure c) {
		weeklyOpeningTimes.each(c)
	}
	
}
