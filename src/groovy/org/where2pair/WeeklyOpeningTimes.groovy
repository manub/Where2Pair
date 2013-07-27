package org.where2pair

import groovy.transform.Immutable

import org.joda.time.DateTime

class WeeklyOpeningTimes {

	Map weeklyOpeningTimes
	
	boolean isOpen(DateTime dateTime) {
		int dayOfWeek = dateTime.getDayOfWeek()
		int hourOfDay = dateTime.getHourOfDay()
		int minuteOfHour = dateTime.getMinuteOfHour()
		
		weeklyOpeningTimes[dayOfWeek].isOpen(hourOfDay, minuteOfHour)
	}
	
}