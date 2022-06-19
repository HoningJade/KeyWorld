from django.shortcuts import render

# Create your views here.
def home(request):
    return render(request, 'home.html', {})

def keyUpload(request):
    return render(request, 'keyUpload.html', {})