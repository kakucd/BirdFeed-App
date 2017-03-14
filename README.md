# BiredFeed-App



Local food review app. 

Recommends nearby restaurants based upon what people are saying about it on Twitter and Google Reviews. 

Will use the phones GPS location to determine which restaurants are close. Then queries information from a Firebase database that has recent reviews, information about the restaurant, and eventually will allow users to have personalized settings (i.e food preferences).
Implements Twitter login services that allow users to tweet about food directly from app to their personal Twitter account. Tweets made not through this app are collected through Spark Streaming. All tweets are analyed for sentiment by a customized algorithm and all sentiment scores are stored in the Firebase database. 
