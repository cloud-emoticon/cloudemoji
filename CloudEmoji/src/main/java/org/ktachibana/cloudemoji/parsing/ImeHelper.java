package org.ktachibana.cloudemoji.parsing;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Build;
import android.provider.UserDictionary;
import android.util.Log;

import org.ktachibana.cloudemoji.Constants;
import org.ktachibana.cloudemoji.models.Entry;

import java.util.List;

public class ImeHelper implements Constants {
    public static int importAllFavoritesIntoIme(ContentResolver contentResolver) {
        List<Entry> favorites = FavoritesHelper.getFavoritesAsCategory().getEntries();

        // Add all favorites into user dictionary
        int counter = 0;
        for (Entry entry : favorites) {
            if (!entry.getDescription().equals("")) {
                ContentValues newValue = new ContentValues();
                newValue.put(UserDictionary.Words.APP_ID, USER_DICTIONARY_APP_ID);
                newValue.put(UserDictionary.Words.WORD, entry.getEmoticon());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    newValue.put(UserDictionary.Words.SHORTCUT, entry.getDescription());
                }
                contentResolver.insert(UserDictionary.Words.CONTENT_URI, newValue);
                counter++;
            }
        }

        return counter;
    }

    public static int revokeAllFavoritesFromIme(ContentResolver contentResolver) {
        String clause = UserDictionary.Words.APP_ID + "=?";
        String[] args = {USER_DICTIONARY_APP_ID};

        // TODO: Remove all entries belonging to this app
        return contentResolver.delete(UserDictionary.Words.CONTENT_URI, clause, args);
    }
}