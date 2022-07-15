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

This app is developed for Android. The languages used for development are mainly Kotlin and Python. The front-end relies on the following APIs:
1. NFC reader API: https://developer.mozilla.org/en-US/docs/Web/API/Web_NFC_API
2. WiFi information API: https://developer.mozilla.org/en-US/docs/Web/API/Network_Information_API
3. Live Chat API: https://documenter.getpostman.com/view/758169/livechat-rest-api/RVnPL46o

The back-end server includes both a card module and a service module to deal with the customer information verification and service requests respectively. It communicates with a database for customer & room information.

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
Our engine architecture has three main blocks: resident front-end, hotel front-end, and back-end (including the database).

**Virtual key:** 
In the beginning, the hotel needs to upload the room-key information from the hotel front-end through the key managament block to the card module in the back-end, and then the card module will save the corresponding room-key information in the database of room info table.

Then, a guest can request the virtual key with a code and his/her last name. The information is then sent to the backend, and next the card module will fetch the key from the database and send it back to the guest's application.

If the guest wants to open the door, he/she can activate the NFC card emulator on the front-end and approach the phone to the NFC reader on the door. After a successful match, the door should be open.

**NFC tag reader**
When a guest wants to connect to the hotel wifi with the NFC tag, the NFC card reader will be activated. Once the phone catched the NFC signal with the wifi configuration information the wifi conection block will automatically parse the information and connect the phone to the hotel wifi.

When a guest wants to read the room instruction with the NFC tag, the NFC card reader will be activated. Once the phone catched the NFC signal with the instruction information, the app will send a request to the service module in the back-end, which will then fetch the detailed inistruction and send it back to the guest's application.

**Room service**
Guests can request room service through the room service request block on the resident front-end, and the request will be sent to the customer service block on the hotel front-end via the service module on the back-end. Once the hotel receives the request, it can perform the appropriate service. After the service, guests can write review for the service and send feedback to the hotel. Besides, guests can also chat with the hotel.

## APIs and Controller
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

**Example**

### keyFetch
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

### InstructionFetch
**Request Parameters**
| Key        | Location | Type   | Description      |
| ---------- | -------- | ------ | ---------------- |
| `InstructionID` | JSON | Int | instruction ID |

**Response Codes**
| Code              | Description            |
| ----------------- | ---------------------- |
| `200 OK`          | Succeed |
| `400 Bad Request` | Invalid parameters |

**Returns** 
| Key        | Location       | Type   | Description  |
| ---------- | -------------- | ------ | ------------ |
| `InstructionDetail` | JSON | String | The detailed instruction|


**Example**

### roomServiceRequest
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


**Example**


### Third-Party SDKs
| SDK        | Description  |
| ---------- | -------------- | 
| android.nfc | Support Android basic NFC functions |
| android.nfc.tech | Support Android advanced NFC functions |
| android.nfc.cardemulation |  Support Android NFC card emulation |
| android.net.wifi | Support Android wifi suggestions and connection |
| LiveChat API | Live Chat |
| django-webpush | hotel notification web push|


## View UI/UX
The final UI/UX overview is shown below ([detailed version here](https://www.figma.com/file/GR2Qz36NjUOARcIVuveBgr/UI%2FUX-Flow-Design-(Final)?node-id=0%3A1)).
![Final UI/UX Flow](/assets/UIUX_flow.png)
### UX - NFC key
- Enter code (obtained from hotel) and last name to get the key.
- The key is stored in the phone, press open to activate the virtual key.
- There will be response of whether the door is successfully opened.
### UX - NFC WiFi
- Click the button to start WiFi connection.
- Guide the user to find the NFC tag and read the tag.
- Display connection feedback.
### UX - Hotel Service
- Select what type of hotel service.
- If the user chooses to read the instruction tag, the detailed information will be displayed in the window.
- If the user chooses to send a service request, user can select service from a list.
- If the user needs some special service which is not listed, he/she can send through live chat.
### UX - Checkout
- Confirm that the user is exiting the current hotel.
- Allow users to rate and write reviews for the hotel.
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
- We added corresponding prompts to guide users because two interviewees in our test reported that they are unsure about what to do after clicking the “open” button.
- We added room number so that users don’t need to memorize them.
### Design Justification - Connect WiFi
- The success rate of wifi connection task is 66.7%. 
- Interviewees reported that they have no idea what “reader” is and find it difficult to relate wifi connection with reader.
- We thus directly renamed the second section to “wifi”, and moved “instruction” into “service” section to avoid exposing technical terms to users.
### Design Justification - Request Service
- The success rate of asking hotel for slippers is only 83.3%.
- One interviewee kept looking for slippers in the “select service” section and did not think of using live chat. 
- We moved “live chat” to “select service” section so that users can communicate with hotel staff when they cannot find the service they want. 


## Team Roster
| Team member | Contribution |
|:-----------:|:------------:|
|Yuanqi Guo|Implementation of "Read Instruction from NFC tags", implementation of "Get WiFi Info from NFC tags"|
|Xinrui Zhao| Back-end for the KeyFetch and roomServiceRequest, hotel side pending service page |
|Ruiyu Li||
|Yixin Shi||
|Churong Ji||
|Ruge Xu|UI/UX Design and Implementation, Activity Connections in Kotlin|
