from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.contrib.auth.models import User, Group
import json
from django.db import connection
from django.http import JsonResponse, HttpResponse
from webpush import send_group_notification

# Create your views here.
def home(request):
    return render(request, 'home.html', {})

def keyUpload(request):
    if request.method == 'POST':
        if request.POST.get('room') and request.POST.get('key'):
            room_number = request.POST.get('room')
            key = request.POST.get('key')
            cursor = connection.cursor()
            cursor.execute('INSERT INTO rooms (room_number, key, availability) VALUES '
                        '(%s, %s, 0);', (room_number, key))
            context = {
                'aaa': '1234',
                'room_number': room_number,
                'nfc_key': key
            }
            return render(request, 'keyUpload.html', {})
        return render(request, 'error.html', {})
    return render(request, 'keyUpload.html', {})

def residentUpdate(request):
    return render(request, 'residentUpdate.html', {})

def serviceList(request):
    return render(request, 'serviceList.html', {})

def liveChat(request):
    return render(request, 'liveChat.html', {})


# @csrf_exempt
# def UploadRoomKey(request):
#     if request.method != 'POST':
#         return HttpResponse(status=404)
#     form = InputForm(request.POST)
#     aaa = '2333'
#     room_num = form['room_number']
#     nfc_key = form['nfc_key']
#     cursor = connection.cursor()
#     cursor.execute('INSERT INTO rooms (room_number, key, availability) VALUES '
#                    '(%s, %s, 0);', (room_num, nfc_key))
#     return render(request, 'keyUpload.html', {})
#     return render(request, 'test.html', {})
def register(request):
    webpush = {"group": 'allusers' } # The group_name should be the name you would define.
    return render(request, 'registerNotification.html', {'webpush':webpush})

    
    
def serviceRequestList(request):
    "fetch all service request and display"
    cursor = connection.cursor()
    cursor.execute('SELECT * FROM services ORDER BY request_time DESC;')
    serviceRequestList = dictfetchall(cursor)
    return render(request, 'serviceRequestList.html', {'serviceRequestList': serviceRequestList})

def dictfetchall(cursor):
    "Return all rows from a cursor as a dict"
    columns = [col[0] for col in cursor.description]
    return [
        dict(zip(columns, row))
        for row in cursor.fetchall()
    ]

def serviceSelect(request):
    "insert user's selection of service"
    if request.method != 'POST':
            return HttpResponse(status=404)
    json_data = json.loads(request.body)
    room = json_data['roomid']
    service = json_data['requestdetail']
    requestTime = json_data['timestamp']
    cursor = connection.cursor()
    cursor.execute('INSERT INTO services (room_number, service, request_time, status) \
                    VALUES (%d, %s, %d, %s);', \
                    (room, service, requestTime, 'pending'))
    # TODO: notification
    payload = {'head': 'New Service Request!', 'body': 'Room '+room+' has a new request: '+service}
    send_group_notification(group_name="my_group", payload=payload, ttl=1000)
        
    return JsonResponse({})


def getKey(request):
    "use name and code to fetch the key"
    if request.method != 'GET':
        return HttpResponse(status=404)
    
    json_data = json.loads(request.body)
    username = json_data['lastname'] 
    code = json_data['code']

    cursor = connection.cursor()
    cursor.execute('SELECT residents.room_number, \
                           rooms.key,\
                           residents.start_date, \
                           rersidents.end_date\
                    FROM residents \
                    JOIN rooms ON residents.room_number = rooms.room_number \
                    WHERE residents.username = %s AND \
                          residents.code = $s;',\
                    (username, code))
    response = dictfetchall(cursor);
    
    if not response:
        return JsonResponse(status=404, data={"message": "wrong code and username"})
    if len(response) > 1:
        return JsonResponse(status=404, data={"message": "something went wrong, please contact the hotel"})
  
    return JsonResponse(response)
