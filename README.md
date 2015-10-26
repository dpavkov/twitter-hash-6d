# twitter-hash-6d

## Overview

This clojure library creates graphs from the latest twitter feeds, related to and startig from a single term. It may be used in order to find and follow the latest sentiment on the twitter on the subject they are interested in, or maybe to investigate the structure of the graphs created in this way. 

## How it works

The library searches for the latest tweets with the hash of the term that user entered, and browses the results searching for other hashes in them. Then it searches for tweets containing those other hashes. Then again, each round searching for any hashes that were found in the previous round, but not before it. In the end, search results are grouped by the round in which they were found and scored according to the configurable scoring chart. 

## Usage

First, config.edn has to be added to the root of the project. Example content:
```
{
 :api-consumer-key "api_consumer_key_value"
 :api-consumer-secret "api_consumer_secret"
 :user-access-token "user_access_token"
 :user-access-token-secret "user_access_token_secret"
 :tweets-per-search 5
 :max-minutes-old 5
 :degrees-deep 3
 :scoring-chart {
                 1 10,
                 2 3,
                 3 1
                 }
 }
```

 * The first four values contain the data necesary for application to authenticate to twitter api. You can create twitter application and thus receive the credentials here: https://apps.twitter.com/
 * The next three values determine boundaries of your searches. While choosing those values, please be aware that if application does the search on too many terms, you may encounter this limitation: https://dev.twitter.com/rest/public/rate-limiting
 * Final value determines how highly results will be graded according to the degree of the distance from the first term.

To run it, simply run 

```
lein run your_term
```

Example output:

```
lein run magic

{goddess 41, uk 40, wicca 33, pagan 31, morrighan 31, morrigan 31, hekate 31, hecate 31, candle 31, hrtechworld 22, hrtech 20, usa 18, seeyouinparis 16, france 16, carers 15, nurses 14, iran 13, middleeast 12, dementia 12, un 11, syrianrefugee 11, syria 11, iraq 11, canada 11, norway 10, findingwinnie 10, obama 9, i 9, germany 9, denmark 8, change 7, austria 7, taeyeon8thwin 6, taeyeon 6, pretty 6, i8thwin 6, humanrights 6, hr 6, europe 6, witch 5, shooting 5, redheels 5, purchase 5, piggy 5, foot 5, clip 5, blacknails 5, summer 4, stocks 4, sharepointlms 4, refugeecrisis 4, recruiting 4, merger 4, marine 4, litha 4, iowa 4, elearningforce 4, braemar 4, slovenia 3, sex 3, russia 3, luxembourg 3, leadership 3, job 3, homecare 3, hana 3, candidateexperience 3, art 3, acquisition 3, حملة_جيش_سبام_الدواعش 2, фото 2, природа 2, women 2, westernbalkansroute 2, unga 2, travel 2, slovakia 2, shiite 2, saudiarabia 2, romance 2, risk 2, photo 2, paranormal 2, packaging 2, oman 2, nature 2, muslims 2, music 2, mosul 2, kobani 2, kobane 2, jihad 2, islamicstate 2, isis 2, isil 2, is 2, hizbulla 2, girls 2, color 2, caliphate 2, black 2, berlin 2, beauty 2, beautiful 2, askussegl 2, ashura 2, 태연 1, 拡散 1, オーストリア 1, سوريا 1, ايران 1, اليمن 1, пейзаж 1, музыка 1, макро 1, été 1, yoy 1, you 1, work 1, wiccan 1, whiteink 1, where 1, wapict 1, veterans 1, venäjä 1, unsc 1, ukip 1, twitter 1, turkish 1, turkey 1, toys 1, toronto 1, thisishalloween 1, thewhitehouse 1, teens 1, teen 1, team224 1, tcot 1, tan 1, talent 1, taiwan 1, taipei 1, tagsforl 1, syli 1, surveys 1, sunny 1, sun 1, strategy 1, statfjord 1, startup 1, st 1, spell 1, spain 1, southafrica 1, song 1, soleil 1, slippers 1, skyfishing 1, sinti 1, sings 1, simple 1, shipping 1, share 1, sexy 1, selfie 1, sea 1, scuba 1, sanfrancisco 1, sales 1, saboteur 1, rumania 1, roma 1, riskmanagement 1, rfctournai 1, retailers 1, retailer 1, reseaugaulliste 1, putin 1, pumpkin 1, president 1, portrait 1, poland 1, pig 1, physician 1, photooftheday 1, photoofthed 1, photoo 1, photography 1, phantom 1, perfect 1, people 1, pegida 1, paris 1, organizational_culture 1, opera 1, omanhr2015 1, oilspill 1, offshore 1, ofccp 1, no2rouhani 1, nikon 1, nice 1, nhs 1, news 1, musique 1, msm 1, modelmaking 1, migration 1, migrants 1, migrant 1, metroland 1, massage 1, mascota 1, macro 1, lyrics 1, lunch 1, loveit 1, localgov 1, likeforlike 1, like4like 1, life 1, laurentdepoitre 1, landscape 1, l4l 1, kraft 1, kenzamorsli 1, kabila 1, johnflemingblog 1, jobseekers 1, jobs 1, jeu 1, jasdaq 1, japanese 1, j 1, ipomoea 1, iot 1, investment 1, instavideo 1, innovation 1, indiabombay 1, india 1, igslovenia 1, hungary 1, hoyasintanzania 1, hotgirl 1, hospitality 1, horticulture 1, hmserebus 1, hiring 1, hi 1, hello 1, harvardbiz 1, happyhalloween 1, halloween 1, hair 1, guinée 1, graphicdesign 1, good 1, giant 1, gerardbutler 1, fullmoon 1, fsa 1, free 1, frankfurt 1, flags 1, fail 1, exgf 1, eu 1, diving 1, disruption 1, dinner 1, dieline 1, deutschland 1, day 1, dancer 1, dance 1, damascus 1, dals6 1, dals 1, cute 1, culture 1, cryariver 1, crudeoil 1, crude 1, croatia 1, creativity 1, consumer 1, compliance 1, colors 1, class 1, china 1, candidate 1, bruja 1, bronzage 1, breaking 1, breakfast 1, bratislava 1, bow 1, bounty 1, bogdanov 1, blogs 1, blair 1, bigdata 1, benghazi 1, ballet 1, babes 1, arctic 1, archaeology 1, arabspring 1, anchorage 1, alsaud 1, algeria 1, aleppo 1, adult 1}

```
