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

#### Receive Response

The frontend receives request response from the backend through `getmsg` API. The format of response is:

```kotlin
{	
   "code": 200 - Success; 400 - Bad Request
}
```

