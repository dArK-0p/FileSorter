package com.fileSorter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.util.Date;

class Helper {
    /**
     * Formats a legacy Date object into a specific "yyyy/MMMM" pattern (UTC timezone)
     * or returns null if the input date corresponds to a specific 'zero' date (1904-01-01).
     *
     * @param legacyDate The legacy Date object to be formatted.
     * @return A formatted date string in the "yyyy/MMMM" pattern (UTC timezone), or null if the input date is the 'zero' date.
     */
    String getFormattedDate(Date legacyDate) {
        LocalDate inputDate = LocalDate.ofInstant(legacyDate.toInstant(), ZoneId.systemDefault());
        LocalDate zeroDate = LocalDate.of(1904, 1, 1);

        if (inputDate.equals(zeroDate)) return null;

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MMMM").withZone(ZoneId.of("UTC"));

        return outputFormatter.format(inputDate);
    }
}