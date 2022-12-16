package com.chess.common;

public class LocationFactory {
    private static final File[] files = File.values(); // convert enum to an array of values but in datatype File (will not work to use usual int for Location)
    // THIS IS INTERESTING: the location is being BUILT together
    public static Location buildLocation(Location current, Integer fileOffset, Integer rankOffset){
        Integer currentFile = current.getFile().ordinal(); // ordinal returns the integer value belonging to the enum value
        return new Location(files[currentFile + fileOffset], current.getRank() + rankOffset); // for rank, unlike for file, the format is integer
    }
}
