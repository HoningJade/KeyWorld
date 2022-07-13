from django.urls import path
from . import views

urlpatterns = [
    path('', views.home, name='home'),
    path('keyUpload/', views.keyUpload, name='keyUpload'),
    path('residentUpdate/', views.residentUpdate, name='residentUpdate'),
    path('serviceList/', views.serviceRequestList, name='serviceList'),
    path('liveChat/', views.liveChat, name='liveChat'),
    # path(r'^keyUpload/$', views.keyUpload, name='keyUpload'),
    # path('serviceRequestList/', views.serviceRequestList, name='serviceRequestList'),
    path('roomServiceRequest/', views.roomServiceRequest, name='roomServiceRequest'),
    path('getKey/', views.getKey, name='getKey'),
]
