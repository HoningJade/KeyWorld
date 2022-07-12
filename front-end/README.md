### Frontend 

#### Post Service

The frontend posts service requests selected by the user through `postmsg` API to the backend. The format of request is:

```kotlin
{	
   "roomid": int (example: 301),	
   "requestdetail": string (example: "Charge Delivery"),
    "timestamp": string
}
```

<<<<<<< HEAD
#### Get Room Information

The frontend receives request response from the backend through `getmsg` API. 

The request URL has two parameters: `lastname` and `code`.

The format of response should be:

```kotlin
{	
   "roomid": int (example: 301)
    "VirtualKey": string
    "AvailabilityStartTime": Timestamp
    "AvailabilityEndTime": Timestamp
=======
#### Receive Response

The frontend receives request response from the backend through `getmsg` API. The format of response is:

```kotlin
{	
   "code": 200 - Success; 400 - Bad Request
>>>>>>> 032ce7c65708ccdcb3f4b346098a4b7a9234c362
}
```
