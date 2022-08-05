# KeyWorld

| Gradesheet | Team Info | Demo  |  Wiki |  Trello  |
|:----------:|:---------:|:-----:|:-----:|:--------:|
|[<img src="https://eecs441.eecs.umich.edu/img/admin/grades3.png">][grade_sheet]|[<img src="https://eecs441.eecs.umich.edu/img/admin/team.png">][team_contract]|[<img src="https://eecs441.eecs.umich.edu/img/admin/video.png">][demo_page]|[<img src="https://eecs441.eecs.umich.edu/img/admin/wiki.png">][wiki_page]|[<img src="https://eecs441.eecs.umich.edu/img/admin/trello.png">][process_page]|

![Elevator Pitch](/assets/Pitch.jpeg)

[grade_sheet]: https://docs.google.com/spreadsheets/d/1X3MaZ2m3UsdjcJARJQvdTjxElceTxeJx2uY92idq3xk/edit?usp=sharing
[team_contract]: https://docs.google.com/document/d/1YL6lX2RmaLtHDfeRCVXXIFOjAGVMu9O5j3gxOPFHA2Y/edit?usp=sharing
[demo_page]: https://www.youtube.com/watch?v=VIcOGEhsXGM&feature=youtu.be
[wiki_page]: https://github.com/eecs441staff/441template/wiki
[process_page]: https://trello.com/b/MA7frBMt/project-management

## Getting Started
KeyWorld is an app that applies NFC technology to solve the hotel check-in related issues. We help hotels and their guests to easily make the room a home. 

This app is developed for Android. The languages used for development are mainly Kotlin and Python. The front-end can be directly pulled and built. For more information, see: [Migrate to Android Studio](https://developer.android.com/studio/intro/migrate)

The back-end is built with Django. It relies on the following APIs:
1. django-webpush: https://github.com/safwanrahman/django-webpush

To build the back-end, please first refer to [EECS441 lab1: Chatter Back End](https://eecs441.eecs.umich.edu/ji-asns/lab1-chatter-backend#django-web-framework) to set up a server, PostgreSQL, Django, and prepare server side HTTPS. Notice that there's no need to create the table in the database as this project can apply [django models](https://docs.djangoproject.com/en/4.0/intro/tutorial02/#creating-models) to complete it.

An additional package is needed to be installed to start the project:
```
 pip install django-webpush
```

To enable communication between front-end and back-end, please refer to [EECS441 lab1: Chatter Back End - Preparing self-signed certificate for the front-end](https://eecs441.eecs.umich.edu/ji-asns/lab1-chatter-backend#preparing-self-signed-certificate-for-the-front-end) and [EECS441 lab1: Chatter Front End - Installing your self-signed certificate](https://eecs441.eecs.umich.edu/ji-asns/lab1-kotlinChatter#installing-your-self-signed-certificate)

## Model and Engine
### User Story Map
The following figure shows our user storymap.
![Storymap](/assets/storymap.png)
Users go through four stages with our app, which are: hotel setup, guest door opening, wifi connection, and room service. 
1. Hotel Setup: Hotels can upload room, key, and guest information, coonfigure wifi and instruction tags, and connect to the app to their property management system; 
2. Guest Door Opening: Guests can receive a virtual card from the hotel and open the door with it;
3. Wifi Connection: Guests can automatically connect to wifi through NFC tags
4. Room Service: Guests can request room service and get room instructions.
### Engine Architecture
The following figure shows our engine architecture.
![Engine](/assets/engine.png)

Our engine architecture has three main blocks: resident front-end, hotel front-end, and back-end (including the database). The data flow is presented in the above graph. 

**Virtual key:** 
- In the beginning, the hotel needs to upload the room-key information from the hotel front-end through the key management block to the card module in the back-end, and then the card module will save the corresponding room-key information to the “room” table in our database. Also, before a guest’s virtual check-in, the hotel needs to upload guest information including check-in code and stay period in the hotel web page, which will be stored to the “resident” table.
Then, a guest can request the virtual key with a check-in code and his/her last name. The information is then sent to the backend. If matched, the card module will fetch the key from the database and send it back to the guest's application.
If the guest wants to open the door, he/she can activate the NFC card emulator on the front-end and approach the phone to the NFC reader on the door. After a successful match, the door should be open.

**NFC tag reader**
- When a guest wants to connect to the hotel wifi with the NFC tag, the NFC card reader will be activated. Once the phone catched the NFC signal with the wifi configuration information the wifi connection block will automatically parse the information and connect the phone to the hotel wifi.
- When a guest wants to read the room instruction with the NFC tag, the NFC card reader will be activated. Once the phone catched the NFC signal with the instruction information, the app reads the instructions stored in the NFC tags and displays the instructions to the guest.

**Room service**
- Guests can select room service through the room service request block on the resident front-end, and the request will be sent to the guest service block on the hotel front-end via the service module on the back-end, which stores the new requested service into the “service” table and renders on the corresponding hotel web page. Once the hotel receives the request, it can perform the appropriate service. 
- If guests think they cannot find the service they want in the provided service list, they can have a live chat with the hotel staff. Similarly, chats are sent from the room service request block on the resident front-end to our back-end’s service module, which stores the records into “livechats” table. Also, back-end forwards that change to the guest service section in the hotel web page. When a hotel staff responds, the data flow happens in the reverse order.

**Rating and reviews**
- When checking-out, guests can provide their ratings and write reviews for the hotels. Guests’ rating and review are sent from the rating and review section of the resident front-end to the back-end's feedback module. At the same time, the record is inserted into the “review” table in the database. When the hotel side inquires about customer’s ratings and reviews, the back-end calculates the average rating and collects all reviews, forwarding those to the hotel review page.

## APIs and Controller
### receiveChat

**Request Parameters**

The app frontend sends new user chats to hotel server.

| Key           | Location | Type   | Description                   |
| ------------- | -------- | ------ | ----------------------------- |
| `room_number` | JSON     | Int    | Room ID                       |
| `chatts`      | JSON     | String | Chat message sent by the user |

**Response Codes**

| Code              | Description        |
| ----------------- | ------------------ |
| `200 OK`          | Succeed            |
| `400 Bad Request` | Invalid parameters |

### sendChat

The app fronend receives new hotel chatts along with chat history from backend server.

**Returns**

| Key           | Location | Type                                                         | Description                              |
| ------------- | -------- | ------------------------------------------------------------ | ---------------------------------------- |
| `room_number` | JSON     | Int                                                          | Room ID                                  |
| `chatts`      | JSON     | List of string list: [["username0", "message0"],        ["username1", "message1"], ... ] | New hotel chatts along with chat history |

**Response Codes**

| Code              | Description        |
| ----------------- | ------------------ |
| `200 OK`          | Succeed            |
| `400 Bad Request` | Invalid parameters |

### receiveReview

The app frontend sends user review to hotel backend.

**Request Parameters**

| Key           | Location | Type   | Description            |
| ------------- | -------- | ------ | ---------------------- |
| `room_number` | JSON     | Int    | Room ID                |
| `rating`      | JSON     | Float  | User rating of a hotel |
| `review`      | JSON     | String | User review of a hotel |

**Response Codes**

| Code              | Description        |
| ----------------- | ------------------ |
| `200 OK`          | Succeed            |
| `400 Bad Request` | Invalid parameters |

### RoomUpload

**Request Parameters**

| Key        | Location | Type   | Description      |
| ---------- | -------- | ------ | ---------------- |
| `RoomID` | JSON | Int | Room ID |
| `Key` | JSON | String | The key information of the room |

**Response Codes**

| Code              | Description            |
| ----------------- | ---------------------- |
| `200 OK`          | Succeed |
| `400 Bad Request` | Invalid parameters |

**Returns** 
| Key        | Location       | Type   | Description  |
| ---------- | -------------- | ------ | ------------ |
| `isSuccess` | JSON | Boolean | Whether the guest is found in the database|

### GuestUpload
**Request Parameters**
| Key        | Location | Type   | Description      |
| ---------- | -------- | ------ | ---------------- |
| `RoomID` | JSON | Int | Room ID |
| `GuestFirstName` | JSON | String | Guest's first name |
| `GuestLastName` | JSON | String | Guest's last name |
| `CheckinCode` | JSON | String | Guest's check in code |
| `AvailabilityStartTime` | JSON | Timestamp | When the key starts the availability |
| `AvailabilityEndTime` | JSON | Timestamp | When the key ends the availability |

**Response Codes**
| Code              | Description            |
| ----------------- | ---------------------- |
| `200 OK`          | Succeed |
| `400 Bad Request` | Invalid parameters |

**Returns** 
| Key        | Location       | Type   | Description  |
| ---------- | -------------- | ------ | ------------ |
| `isFound` | JSON | Boolean | Whether the guest is found in the database|

### keyFetch

The frontend receives request response from the backend through `keyFetch` API.

**Request Parameters**
| Key        | Location | Type   | Description      |
| ---------- | -------- | ------ | ---------------- |
| `lastname` | JSON | String | Guest's last name |
| `code` | JSON | String | Guest's check in code |

**Response Codes**

| Code              | Description            |
| ----------------- | ---------------------- |
| `200 OK`          | Succeed |
| `400 Bad Request` | Invalid parameters |
| `404 Not Found`   | name code pair not found |

**Returns**
| Key        | Location       | Type   | Description  |
| ---------- | -------------- | ------ | ------------ |
| `room_number` | JSON | Int | The guest's room number |
| `key` | JSON | String | The virtual key information for the card emulator |
| `start_date` | JSON | Timestamp | When the key starts the availability |
| `end_date` | JSON | Timestamp | When the key ends the availability |

### roomServiceRequest

The frontend posts service requests selected by the user through `roomServiceRequest` API to the backend. 

**Request Parameters**

| Key        | Location | Type   | Description      |
| ---------- | -------- | ------ | ---------------- |
| `roomid` | JSON | Int | Guest's room ID |
| `requestdetail` | JSON | String | The request detail |
| `timestamp` | JSON | Timestamp | The request time |

**Response Codes**
| Code              | Description            |
| ----------------- | ---------------------- |
| `200 OK`          | Succeed |
| `404 Not Found` | Invalid parameters |


### Third-Party SDKs
| SDK        | Description  |
| ---------- | -------------- |
| android.nfc | Support Android basic NFC functions |
| android.nfc.tech | Support Android advanced NFC functions |
| android.nfc.cardemulation |  Support Android NFC card emulation |
| ACTION_WIFI_ADD_NETWORKS | Add WIFI configurations to the saved network or subscription list |
| kotlinx.coroutines | Support live chat |
| django-webpush | Support hotel notification web push |
| OkHttp | Support frontend and backend communication |
| Volley | Support frontend and backend communication |


## View UI/UX
The final UI/UX overview is shown below ([detailed version here](https://www.figma.com/file/GR2Qz36NjUOARcIVuveBgr/UI%2FUX-Flow-Design-(Final)?node-id=0%3A1)).
![Final UI/UX Flow](/assets/UIUX_flow.png)
### UX - NFC key
The user can obtain the key by entering the password (obtained from the hotel) and the last name. 

The key is stored in the phone and the user can press the open button to activate the virtual key. 

The app will give a response based on whether the door has been successfully opened or not.
### UX - NFC WiFi
Users can tap a button to start a WiFi connection.

The app will guide the user to the NFC tag and read the tag.

The app will give feedback based on the success of the connection.
### UX - Hotel Service
The user can select what type of hotel services are available in the app.

If the user chooses to read the instruction tag, the user can follow the prompts given by the app and scan the NFC tag, and then the detailed instruction information will be displayed in a pop-up window.

If the user chooses to send a service request to the hotel, the user can select the service from the established list.

If the user needs some special service not listed, he/she can send it via live chat.
### UX - Checkout
The app confirms that the user wants to exit the current hotel.

The app allows the user to rate the hotel and write a review.
### Mockup Usability Test Results
|                   | Evaluation Metric | KeyWorld |
| :---------------: | :---------------: | :------: |
| **Open the door** |                   |          |
| Get virtual key (hotel just sent you a code) | <= 30 sec | 83.3% |
| Open the room | <= 2 click + move the phone to the door | 100% |
| **Wifi Connection** |                 |          |
| Connect to wifi | <= 15 sec (include finding tag) + move the phone to the tag | 66.7% |
| **Room Instruction** |                |          |
| Seeing tag “room instruction” | <= 2 click + move the phone to the tag | 100% |
| Learn how to use the curtain | <= 2 click + move the phone to the tag | 100% |
| **Room Service** |
| Request cleaning service (to test buttons for service) | <= 40 sec (find service and send request) | 100% |
| Ask hotel to give you slippers (to test live chat) | <= 1 min (get connected with hotel staff via chat) | 100% |
| Send review to the hotel | <= 3 click (find where to enter and send review) | 83.3% |
- The icon with name “Reader” confuses users without technical background.
- Some users tend to find wifi connection in the section of service.
- Our app does not provide enough explanations so that it takes efforts for users to understand and use the app.
- The word “Interrupt” in prompt is confusing.
- Live chat is of the same priority with other services while in reality it should only be chosen when the service in not included in other services.
### Design Justification - Open Key
First, we added corresponding prompts to guide users because two interviewees in our test reported that they are unsure about what to do after clicking the “open” button.
Second, we added room number so that users don’t need to memorize them.
### Design Justification - Connect WiFi
The success rate of wifi connection task is 66.7%. 
Interviewees reported that they have no idea what “reader” is and find it difficult to relate wifi connection with reader.
We thus directly renamed the second section to “wifi”, and moved “instruction” into “service” section to avoid exposing technical terms to users.

### Design Justification - Request Service
The success rate of asking hotel for slippers is only 83.3%.
One interviewee kept looking for slippers in the “select service” section and did not think of using live chat. 
We moved “live chat” to “select service” section so that users can communicate with hotel staff when they cannot find the service they want. 


## Team Roster
| Team member | Contribution |
|:-----------:|:------------:|
|Yuanqi Guo| NFC reader (get wifi info & read instruction tags); implementation and testing of room card; demo of all features from resident side |
|Xinrui Zhao| PM, Back-end APIs(KeyFetch, roomServiceRequest), Hotel side UI, Wifi connection implementation |
|Ruiyu Li| frontend implementation of log-in and service request, server communication protocol design and document, UI/UX of select service |
|Yixin Shi| Hotel side UI design, Room key upload Implementation, Database Design and Management |
|Churong Ji|  Hotel side UI, Database, Back-end APIs (key fetch and service upload), Hotel side key&resident upload|
|Ruge Xu|Resident side UI/UX Design, App UI Implementation, Activity Connections in Kotlin|

### Challenges

- **Ruge Xu**: There are three challenges that I encountered. First, it is a difficult task to design the user interface in a clear and simple way and to implement the designed interface perfectly on the application. I would often go back and forth between several designs, or tweak the parameters repeatedly to achieve a better presentation. Secondly, I spent a lot of time in implementing the navigation bar. On the one hand, the navigation bar switch page needs to be implemented with fragments instead of activities, and I studied for a long time how to connect a mix of fragments and activities; on the other hand, it also took me a lot of time to let the user clearly know which page they are on. Finally, I spent a lot of effort in implementing the live chat feature. I used a recycle view element to present the chat transcript in a loop. To highlight the difference between the hotel side and the user side, I designed two kinds of dialogs, and to select different dialogs for different situations in the recycle view, I went online and consulted a lot of code before I was able to implement it.

- **Yuanqi Guo**: When implementing the NFC-related features, the first challenge was to study the APIs that enable use of the various tag technologies in Android. Since none of us were familiar with NFC operations before, a thorough round of pre-proposal research is quite necessary. The implementation of NFC tag reader is based on a standard called NDEF (NFC Data Exchange Format), while the room key feature is developed using the host-based card emulation (HCE) technique. Although there are plenty of relevant libraries and documentations, few of them can clearly guide us to build such a project step-by-step. After lots of trial and error, I finally learned how to handle the NFC intent filters to get the desired level of priority and how to handle the detected NFC tags using NFCAdapter. Another big challenge was to ensure that different NFC-related features can work in one app without conflict. Since host-based card emulation uses a different permission type than NFC reader, there’s no conflict related to the virtual room key feature. But while reading the two types of tags (Wifi and instruction), I encountered the problem. At first, I implemented activities to handle the Wifi tag and the instruction tag separately, but it turned out that only one of them could be executed, whichever was exported first. This problem led to the same view for the users when reading both types of tags.  Therefore, I implemented extra steps to distinguish between the Wifi and the instruction tags based on the data format stored in them, before I could finally solve the problem.

- **Xinrui Zhao** 1)For the wifi-connection part, the first challenge was to study different kinds of WPA(wifi protection access) and understand what parameters are needed to realize the function. It takes some time to study the APIs, and given that it shall be connected to the NFC, I spent much efforts fixing the NFC parts of the skelatal product structure with Yuanqi and guarantee two parts can work cooperately. 2)For website UI, as our app also needs to develop the website end, which is not covered in the lab, I learned Django tutorials to accomplish several features. For example, though django structure has a built-in structure called models that can automatically generate database and enable developer to fetch data to the web, as we have created our own in the server, I need to figure out a way to re-connect django modules to the existing database. 3)For hotle notification, as web-push is a quite complicated feature that envolves several setup including registering service worker and subscribe users, I have quite a lot background knowledge to learn before implementing it. 4)For the back-end API, I developed the majority parts of KeyFetch, roomServiceRequest. And as all of us are not so familiar with server communication, Churong, Ruiyu and I spent hours to ensure all data types match. 5)As a non-CS student who need to work both on the front-end and back-end in this project, I felt that I learned basically everything from zero, which is definitely a challenge but also an interesting experience.

