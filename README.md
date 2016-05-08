#TwLocator - Android Project



TwLocator - app which provides posibility of searching Tweets by Location.

##Configuration:
To run TwLocator it's necessary change values for keys in the files:

* AndroidManifest - YOUR GOOGLE KEY
* TwitterPrivateKeyAndSecret - CONSUMER_KEY
* TwitterPrivateKeyAndSecret - CONSUMER_SECRET


##Functionality:
* searching of the Tweets by location city/adrress;
* app returns max 50 Tweets;
* all new results are saved in the local DB (which is used to returnes values in the offline mode);
* posibility of searching values by typing a new Location in the SearchView or by selecting some suggestion (list of queries used recenlty)
* cleaning history - removes data from the DB, removes old suggestions and cleans markers/clusters/infoWindows from the map.
* if Twitter doesn't provide an exact position about the Longitude, Latidude, but the Tweet is obtiened as a one from the searching Location - in this case it receives a default Location which is equal to a center of the searching place.
* Tweets from the same place (that have the same longitude and langitude) are gruped into Clustres and they are presented on the map as one Cluster with a number which marks a number of all Tweets from the region. The user has a posibility of checking all of them by clicking the into window showed under the Cluster and presenting all of them in the Table.
* If there is only one Tweet in the Location - it's presented as a Marker with InfoWindow.
