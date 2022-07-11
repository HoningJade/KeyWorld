from django.urls import path
from . import views

urlpatterns = [
    path('', views.home, name='home'),
    path('keyUpload/', views.keyUpload, name='keyUpload'),
    path('residentUpdate/', views.residentUpdate, name='residentUpdate'),
    path('serviceList/', views.serviceList, name='serviceList'),
    path('liveChat/', views.liveChat, name='liveChat'),
    # path(r'^keyUpload/$', views.keyUpload, name='keyUpload'),
]


