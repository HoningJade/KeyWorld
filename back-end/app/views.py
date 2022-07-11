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

<<<<<<< HEAD
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
=======
def serviceRequestList(request):
    return render(request, 'serviceRequestList.php', {})

def serviceSelect(request):
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

>>>>>>> cbae79f7b975237397262f9758e8cf7e7f474fac
