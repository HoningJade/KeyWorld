from django.urls import path
from . import views

urlpatterns = [
    path('', views.home, name='home'),
    path('keyUpload/', views.keyUpload, name='keyUpload'),
    path('residentUpdate/', views.residentUpdate, name='residentUpdate'),
    path('serviceRequestList/', views.serviceRequestList, name='serviceRequestList'),
    path('serviceSelect/', views.serviceSelect, name='serviceSelect'),
]