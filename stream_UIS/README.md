
Language: Java

Created: 3/2019 (Java experience: 3 months)

Keywords: Java, .csv, stream, TUI

This is a solo project to practice reading data from a .csv file, mapping it to objects and to 
create a simple text-based UI to print the desired data from any available year. It is made 
when attending the Advanced Programming course (Helsinki University MOOC) to further practice reading larger files and using streams. The project can be run in the Java IDE of one's choice.

The .csv file is a random pick from the UIS Statistics website (http://data.uis.unesco.org/) and it shows 
the percentages of out-of-school rates for children of primary school age. Each country has either 
one or several rates for one or more years. Many countries have several rates for different 
stats (such as for two genders and one for both). 

The UI first lists the countries (short example below; user input is marked woth ">>"):

Locations available:

ALB VAT NOR CUB BWA ITA LSO KEN TCD SEN 

SWZ EST UKR GHA NPL VCT ECU BHR GIN ATG 

UGA CHE AUS LBR JPN PHL HND HKG COM BEN 

Insert location: (insert x to quit)

>> GHA

Years available:
2013 2014 2015 2016 2017 2018 

>>2013

Fetching data for location GHA, year 2013
{Rate of out-of-school children of primary school age, Ghana,  male (%), 2013: value: 14.29037}
{Rate of out-of-school children of primary school age, Ghana,  both sexes (%), 2013: value: 14.15155}
{Rate of out-of-school children of primary school age, Ghana,  female (%), 2013: value: 14.00703}


