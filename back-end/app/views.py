from django.shortcuts import render
from django.http import JsonResponse, HttpResponse
from django.db import connection
from django.views.decorators.csrf import csrf_exempt
import json

# Create your views here.
def home(request):
    return render(request, 'home.html', {})

def keyUpload(request):
    return render(request, 'keyUpload.html', {})

def residentUpdate(request):
    return render(request, 'residentUpdate.html', {})

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

