### Frontend 

#### Post Service

The frontend posts service requests selected by the user through `keyFetch` API to the backend. The format of request is:

```kotlin
{	
   "roomid": int (example: 301),	
   "requestdetail": string (example: "Charge Delivery"),
    "timestamp": string
}
```

#### Get Room Information

The frontend receives request response from the backend through `roomServiceRequest` API. 

The request URL has two parameters: `lastname` and `code`.

The format of response should be:

```kotlin
{	
   "room_number": int (example: 301)
    "key": string
    "start_date": Timestamp
    "end_date": Timestamp
}
```
