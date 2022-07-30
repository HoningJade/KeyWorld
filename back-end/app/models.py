# This is an auto-generated Django model module.
# You'll have to do the following manually to clean this up:
#   * Rearrange models' order
#   * Make sure each model has one field with primary_key=True
#   * Make sure each ForeignKey and OneToOneField has `on_delete` set to the desired behavior
#   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# Feel free to rename the models, but don't rename db_table values or field names.
from django.db import models

class Residents(models.Model):
    username = models.CharField(unique=True, max_length=255, primary_key=True)
    code = models.CharField(max_length=255, blank=True, null=True)
    room_number = models.ForeignKey('rooms',on_delete=models.CASCADE,)
    start_date = models.DateField(blank=True, null=True)
    end_date = models.DateField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'residents'


class Reviews(models.Model):
    id = models.IntegerField(primary_key=True)
    room_number = models.ForeignKey('rooms',on_delete=models.CASCADE,)
    review = models.TextField(blank=True, null=True)
    rating = models.IntegerField(blank=True, null=True)
    class Meta:
        managed = False
        db_table = 'reviews'


class Rooms(models.Model):
    room_number = models.IntegerField(primary_key=True)
    key = models.CharField(max_length=255, blank=True, null=True)
    availability = models.IntegerField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'rooms'


class Services(models.Model):
    id = models.IntegerField(primary_key=True)
    room_number = models.ForeignKey('rooms',on_delete=models.CASCADE,)
    service = models.CharField(max_length=255, blank=True, null=True)
    request_time = models.TimeField(blank=True, null=True)
    status = models.CharField(max_length=255, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'services'
