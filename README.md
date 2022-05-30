# Zoho-Interview
Hi Team,

Myself Rajesh Kumar, Sr. Application Developer
CONTUS Support Interactive (P) Ltd

As Interview scheduled I have developed the Weather App by given requirement
Requirement as below

APK and Downloadable APK details

1. Video prototype: https://youtu.be/uV5GSl--UPU
2. Downloading APK link: https://github.com/rajesh0104/Zoho-Interview/raw/master/Zoho%20interview%20apk.apk

Your App needs to contain 2 screens,

Phase 1:
1. Listing screen (list of the random users) - with a functional (locally searchable)
   search bar - first name + last name and profile image on the list.
2. Details screen (which appears on clicking on an item on the listing screen). Just
   4/5 important details are enough (beautifully designed!)

Phase 2:
Next, incorporate the current weather (including graphics) and/or air quality at the
phone's location on the first screen (use any API of your own) and the
respective randomUser's location's weather and/or air quality information on the
second screen (on onclick).
Implement the right way for seeking user's permission for location covering all cases.
Make it as whole an App as possible.


As the above requirement I have developed the app with basic design
API details:
1. https://randomuser.me/api/?results=20&page=1
2. https://api.weatherapi.com/v1/current.json?key=5d0fda94b91f49f7b7961010222905&q=12.8782909,80.168847 

Note: From above weather api, I have created a app id key

Database Details:
1. Room DB has bee integrated

App workflow
1. On home page, api randomuser has been integrated to list the user details
2. From that api, I have saved the basic user details in table. Table structure below

Table name: USER_DETAILS

user_id(primary key)
gender
user_name
user_age
user_mail_id
user_mobile_number
postal_code
user_profile_picture_medium
user_profile_picture_large
user_state
user_country
user_coordination_latitude
user_coordination_longitude
is_user_active

The above fields are used in the table

3. These details will be inserted in table from api details(Manually i have used my details and 
   filtered by active state to avoid in home page listing)
4. Parallel I have enabling the user permission for location and gps enabling programmatically
5. Once the location fetched I have requesting climate details from weather api
6. Once api success Im enabling the card with climatic details
7. To show the user location climate, I have using latitude and longitude from the user random api
8. Note(I have problem from latitude and longitude because it was not correct, I just swapped
   the data and working fine)
9. In second page, Again I have requesting the weather api to get the user weather details

Architecture and work Flow of DB
1. Saved user details
2. And statically added my details to show my details when clicking on climate section from home page
3. Pagination query has been added using offset and limits

Server request:
1. Server request has been made from Retrofit

About App development environment:
Language                --Kotlin
Compile SDK             --32
Minimum SDK             --21
Target SDK              --32

Databinding has been enabled
Project developed using MVVM structure