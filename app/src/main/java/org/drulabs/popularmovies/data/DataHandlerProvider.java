package org.drulabs.popularmovies.data;

/**
 * Provides an instance of {@link DataHandler}. This is the decider class that decides whether to
 * pass real or mocked implementation
 */
public class DataHandlerProvider {

    public static DataHandler provide() {
        return new AppDataHandler();
    }

}
