**IP: 18.116.30.203**

To access: https://18.116.30.203/

\*Note: Please limit access to the back-end server to save AWS credits

### Keyworlddb
**Table rooms**
| Column name       | Type | Constraint    |  Note  |
| ---------- | -------- | ------ | ---------------- |
| `room_number` | integer    | Primary key |  |
| `key` | character varying(255) |  |   |
| `availability` | integer |  |  0 represents avaliable while 1 represents being occupied |

**Table residents**
| Column name       | Type | Constraint    |  Note   |
| ---------- | -------- | ------ | ---------------- |
| `username` | character varying(255)    | Primary key |   |
| `code` | character varying(255) |  |   |
| `room_number` | integer | Foreign key references rooms (room_number) on delete cascade  |    |
| `start_date` | date |  |   |
| `end_date` | date |  |    |

**Table services**
| Column name       | Type | Constraint    |  Note   |
| ---------- | -------- | ------ | ---------------- |
| `id` | integer    | Primary key |   |
| `room_number` | integer | Not null & Foreign key references rooms (room_number) on delete cascade |    |
| `service` | character varying(255) |  |   |
| `request_time` | time without time zone |  |    |
| `status` | character varying(255) |  | pending or finished |

**Table reviews**
| Column name       | Type | Constraint    |  Note   |
| ---------- | -------- | ------ | ---------------- |
| `id` | integer    | Primary key |   |
| `room_number` | integer | Not null & Foreign key references rooms (room_number) on delete cascade |    |
| `review` | text |  |   |

**Table liveChats**
| Column name       | Type | Constraint    |  Note   |
| ---------- | -------- | ------ | ---------------- |
| `room_number` | integer | Primary key & Foreign key references rooms (room_number) on delete cascade |    |
| `message_id` | integer | Not null |   |
| `owner` | character varying(255) | Not null |   |
| `message` | text | Not null |   |


P.S.

Yixin Shi's contribution on skeletal project: 

keyupload page, livechat page, part of home page, database implemention

commit on Jul. 11 "Key upload backend updated", 
commit on Jul. 11 "manual merge", 
commit on Jul. 13 "foreign key updated", 
commit on Jul. 13 "add database description".
