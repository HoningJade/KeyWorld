from django.urls import path, include, re_path
from django.conf import settings
from . import views

urlpatterns = [
    path('', views.home, name='home'),
    path('keyUpload/', views.keyUpload, name='keyUpload'),
    path('residentUpdate/', views.residentUpdate, name='residentUpdate'),
    path('serviceRequestList/', views.serviceRequestList, name='serviceRequestList'),
    path('liveChat/', views.liveChat, name='liveChat'),
    path('roomServiceRequest/', views.roomServiceRequest, name='roomServiceRequest'),
    re_path(r'^webpush/', include('webpush.urls')),
    path('register/', views.register, name = 'registerNotification'),
    path('keyFetch/', views.keyFetch, name='keyFetch'),
    path('receiveReview/', views.receiveReview, name='receiveReview'),
    path('ratingAndReview/', views.ratingAndReview, name='ratingAndReview'),
]
