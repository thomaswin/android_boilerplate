package com.bluepixel.android.sgpool.ui.parse.sample;

import android.content.Context;
import com.parse.*;

import java.util.List;

/**
 * Created by thomas_win on 12/3/2014.
 */
public class ParseSubclassSample {

    @ParseClassName("Armor")
    public class Armor extends ParseObject {

        public String getDisplayName() {
            return getString("displayName");
        }
        public void setDisplayName(String value) {
            put("displayName", value);
        }

        public void takeDamage(int amount) {
            // Decrease the armor's durability and determine whether it has broken
            increment("durability", -amount);
            if (getDurability() < 0) {
                setBroken(true);
            }
        }

        private void setBroken(boolean b) {
        }

        private int getDurability() {
            return 1;
        }

    }

    private void doSample_parseObject() {
        ParseObject shield = new ParseObject("Armor");
        shield.put("displayName", "Wooden Shield");
        shield.put("fireproof", false);
        shield.put("rupees", 50);


        // need to register class before parse initialize
        Context context = null;
        ParseObject.registerSubclass(Armor.class);
        Parse.initialize(context, "PARSE_APPLICATION_ID", "PARSE_CLIENT_KEY");

        Armor armor = new Armor();
        armor.setDisplayName("real_steal");

        // creating reference to existing object
        Armor armorReference = ParseObject.createWithoutData(Armor.class, armor.getObjectId());


    }

    private void doSample_query() {
        ParseQuery<Armor> query = ParseQuery.getQuery(Armor.class);
        query.whereLessThanOrEqualTo("rupees", ParseUser.getCurrentUser().get("rupees"));
        query.findInBackground(new FindCallback<Armor>() {
            @Override
            public void done(List<Armor> results, ParseException e) {
                for (Armor a : results) {
                    // ...
                }
            }
        });
    }

}
