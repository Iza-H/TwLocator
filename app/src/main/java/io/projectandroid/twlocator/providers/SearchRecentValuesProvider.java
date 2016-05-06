package io.projectandroid.twlocator.providers;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by izabela on 06/05/16.
 */
public class SearchRecentValuesProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = SearchRecentValuesProvider.class.getName();

    public static final int MODE = DATABASE_MODE_QUERIES;

    public SearchRecentValuesProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
