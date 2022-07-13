from django.urls import path, include
from django.conf import settings
from django.conf.urls.static import static
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
    path('webpush/', include('webpush.urls')),
    path('register/', views.register, name = 'registerNotification'),
    path('getKey/', views.getKey, name='getKey'),
] + static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
