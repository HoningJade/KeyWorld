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
            if 'Add' in request.POST:
                cursor.execute('SELECT * FROM rooms WHERE room_number=%s', (room_number,))
                if cursor.fetchall():
                    return render(request, 'error.html', {})
                else:
                    cursor.execute('INSERT INTO rooms (room_number, key, availability) VALUES '
                            '(%s, %s, 0);', (room_number, key,))
                    return render(request, 'keyUpload.html', {})

            elif 'Update' in request.POST:
                cursor.execute('SELECT * FROM rooms WHERE room_number=%s', (room_number,))
                if not cursor.fetchall():
                    return render(request, 'error.html', {})
                else:
                    cursor.execute('UPDATE rooms SET key = %s WHERE room_number = %s', (key, room_number,))
                    return render(request, 'keyUpload.html', {})

            else:
                return render(request, 'error.html', {})
                
    return render(request, 'keyUpload.html', {})

def residentUpdate(request):
    if request.method == 'POST':
        if request.POST.get('name') and request.POST.get('code') and request.POST.get('room_number')\
            and request.POST.get('start') and request.POST.get('end'):
            username = request.POST.get('name')
            code = request.POST.get('code')
            room_number = request.POST.get('room_number')
            start_date = request.POST.get('start')
            end_date = request.POST.get('end')
            
            cursor = connection.cursor()
            cursor.execute('SELECT * FROM rooms WHERE room_number=%s', (room_number,))
            if not cursor.fetchall():  # room does not exist
                return render(request, 'error.html', {})
            
            if 'Add' in request.POST:
                cursor.execute('SELECT * FROM residents WHERE room_number=%s AND username=%s', (room_number, username,))
                if cursor.fetchall():
                    return render(request, 'error.html', {})
                else:
                    cursor.execute('INSERT INTO residents (username, code, room_number, start_date, end_date) VALUES '
                            '(%s, %s, %s, %s, %s);', (username, code, room_number, start_date, end_date,))
                    # TODO: set room availability
                    return render(request, 'residentUpdate.html', {})

            elif 'Update' in request.POST: 
                cursor.execute('SELECT * FROM residents WHERE room_number=%s AND username=%s', (room_number, username,))
                if not cursor.fetchall():
                    return render(request, 'error.html', {})
                else:
                    cursor.execute('UPDATE residents SET code=%s, start_date=%s, end_date=%s '
                            'WHERE room_number=%s AND username=%s;', (code, start_date, end_date, room_number, username))
                    # TODO: set room availability
                    return render(request, 'residentUpdate.html', {})

            else:
                return render(request, 'error.html', {})

    return render(request, 'residentUpdate.html', {})

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

@csrf_exempt
def roomServiceRequest(request):
    "insert user's selection of service"
    if request.method != 'POST':
            return HttpResponse(status=404)
    json_data = json.loads(request.body)
    room = json_data['roomid']
    service = json_data['requestdetail']
    requestTime = json_data['timestamp'].replaceAll("[TZ]", " ")
    cursor = connection.cursor()
    cursor.execute('select count(*) from services;')
    count = cursor.fetchone()
    cursor.execute('INSERT INTO services (room_number, service, request_time, status, id) \
                    VALUES (%s, %s, %s, %s, %s);', \
                    (room, service, requestTime, 'pending', count[0]+1,)) #TODO: check if id self increment

    head = 'New Service Request!'
    body = 'Room '+room+' has a new request: '+service
    webNotification("allusers",head,body)
        
    # return JsonResponse(status=200, {"service count": "enter"}) // error
    # return JsonResponse({"msg": count})
    return JsonResponse({})


def webNotification(group_name,head,body):
    payload = {'head': head, 'body': body}
    send_group_notification(group_name=group_name, payload=payload, ttl=1000)


@csrf_exempt
def keyFetch(request):
    "use name and code to fetch the key"
    if request.method != 'GET':
        return HttpResponse(status=404)

    username = request.GET.get('lastname')
    code = request.GET.get('code')
    
    cursor = connection.cursor()
    cursor.execute('SELECT residents.room_number,\
                           rooms.key,\
                           residents.start_date,\
                           residents.end_date\
                    FROM residents\
                    JOIN rooms ON residents.room_number = rooms.room_number\
                    WHERE residents.username = %s AND residents.code = %s;',\
                    (username, code,))
    
    result = dictfetchall(cursor)
    
    if not result:
        return JsonResponse(status=404, data={"msg": "wrong code or username"})
    if len(result) > 1:
        return JsonResponse(status=400, data={"msg": "something went wrong, please contact the hotel"})

    return JsonResponse(status=200, data=result[0])

def ratingAndReview(request):
    if request.method != 'GET':
        return HttpResponse(status=404)

    cursor = connection.cursor()
    cursor.execute('select avg(rating) from reviews;')    
    avg_rating = cursor.fetchone()[0]
    avg_rating = round(avg_rating, 2)
    cursor.execute('select room_number, review from reviews;')
    results = dictfetchall(cursor)
    
    context = {
        'avg_rating': avg_rating,
        'results': results,
    }
    return render(request, 'ratingAndReview.html', context)

@csrf_exempt
def receiveReview(request):
    if request.method != 'POST':
        return HttpResponse(status=404)
    json_data = json.loads(request.body)
    room_number = json_data['room_number']
    rating = json_data['rating']
    review = json_data['review']
    cursor = connection.cursor() 
    cursor.execute('select count(*) from reviews;')
    count = cursor.fetchone()
    cursor.execute('INSERT INTO reviews (room_number, review, rating, id) \
                    VALUES (%s, %s, %s, %s);', \
                    (room_number, review, rating, count[0]+1,))
    return JsonResponse({})


def liveChat(request):
    room_number = 101
    if request.method == 'GET':
        cursor = connection.cursor() 
        cursor.execute('select message_id, owner, message from livechats where \
                        room_number = %s order by message_id asc;', (room_number,))
        messages = dictfetchall(cursor)
        context = {
            'messages': messages,
        }   
         
        return render(request, 'liveChat.html', context)
    elif request.method == 'POST':
        if request.POST.get('message'):
            message = request.POST.get('message')
            cursor = connection.cursor()
            cursor.execute('select count(*) from livechats where room_number = %s;', (room_number,))
            next_message_id = cursor.fetchone()[0] + 1
            cursor.execute('insert into livechats (room_number, message_id, owner, message) \
                    values (%s, %s, %s, %s);', (room_number, next_message_id, 'hotel staff', message,))
            cursor.execute('select message_id, owner, message from livechats where \
                        room_number = %s order by message_id asc;', (room_number,))
            messages = dictfetchall(cursor)
            context = {
                'messages': messages,
            }  
            head = 'New Chat!'
            body = 'You have new messages from' + room_number
            webNotification("allusers",head,body) 
             
            return render(request, 'liveChat.html', context)        
    else:
        return HttpResponse(status=404)

@csrf_exempt
def receiveChat(request):
    if request.method != 'POST':
        return HttpResponse(status=404)
    json_data = json.loads(request.body)
    room_number = json_data['room_number']
    chatts = json_data['chatts'] 
    cursor = connection.cursor() 
    cursor.execute('select count(*) from livechats where room_number = %s;', (room_number,))
    next_message_id = cursor.fetchone()[0] + 1
    cursor.execute('insert into livechats (room_number, message_id, owner, message) \
                    values (%s, %s, %s, %s);', (room_number, next_message_id, 'customer', chatts,))
    head = 'New Chat!'
    body = 'You have new messages from' + room_number
    webNotification("allusers",head,body) 
    
    return JsonResponse({})    

def sendChat(request):
    room_number = 101
    if request.method != 'GET':
        return HttpResponse(status=404)
    cursor = connection.cursor() 
    cursor.execute('select owner, message from livechats where \
                        room_number = %s order by message_id asc;', (room_number,))
    results = cursor.fetchall()
    live_chat = {
        'room_number': room_number,
        'chatts': []
    }
    for result in results:
        usr_msg = [result[0], result[1]]
        live_chat['chatts'].append(usr_msg)
    return JsonResponse(live_chat)    
