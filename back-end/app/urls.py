from django.urls import path, include, re_path
from django.conf import settings
from . import views

urlpatterns = [
    path('', views.home, name='home'),
    path('keyUpload/', views.keyUpload, name='keyUpload'),
    path('residentUpdate/', views.residentUpdate, name='residentUpdate'),
    path('serviceList/', views.serviceRequestList, name='serviceList'),
    path('liveChat/', views.liveChat, name='liveChat'),
    # path(r'^keyUpload/$', views.keyUpload, name='keyUpload'),
    # path('serviceRequestList/', views.serviceRequestList, name='serviceRequestList'),
    path('serviceSelect/', views.serviceSelect, name='serviceSelect'),
    re_path(r'^webpush/', include('webpush.urls')),
    path('register/', views.register, name = 'registerNotification'),
    path('getKey/', views.getKey, name='getKey'),
] 
