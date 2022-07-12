from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
import json
from django.db import connection
from django.http import JsonResponse, HttpResponse

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

def serviceRequestList(request):
    "fetch all service request and display"
    cursor = connection.cursor()
    cursor.execute('SELECT * FROM services ORDER BY request_time DESC;')
    serviceList = dictfetchall(cursor)
    return render(request, 'serviceRequestList.html', {'serviceList': serviceList})

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
    cursor.execute('INSERT INTO serviceRequest (room, service, requestTime) VALUES '
                '(%s, %s, %d);', (room, service, requestTime))
    # TODO: notification
    return JsonResponse({})

