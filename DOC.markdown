Aplikacja jest częścią projektu akademickiego

Wprowadzenie
============

Aplikacja ta jest klientem na platformę android, korzysta z serwera z repozytorium: https://github.com/piorek/weatherApiService. Klient ten pozwala na wyszukiwanie i prezentowanie wykresów z danymi pogodowymi

Ekrany
======

Aplikacja składa się z 4 ekranów: wyszukiwanie, widok szczegółowy wykresu, lista historii wyszukiwań oraz preferencji.

Po uruchomieniu aplikacji widzimy ekran wyszukiwania na którym można wybrać listę miast, zakres dat, typ danych oraz typ grupowania. Lista miast, typ danych oraz typ grupowania miały pochodzić z wcześniej wspomnianej zewnętrznej usługi, ale z uwagi na brak czasu są wprowadzone na sztywno. Klasa pk.ip.weather.android.api.service.ApiServiceImpl jest fasadą usługi dostarczanej przez w/w serwer. Jedynie metoda getGraphURL rzeczywiście korzysta z tego serwera, reszta metod to zaślepki zwracające dane ustawione na sztywno (listy miast, typów danych i typów grupowań).

Po wciśnięciu przycisku "Show chart" zostaje wysłane żądanie do serwera w celu uzyskania wykresu, następuje przejście do widoku szczegółowego wykresu, a wyszukiwanie zostaje zapamiętane przez obiekt DAO (Data Access Object). Można przejść do listy wyszukiwań klikając przycisk menu -> list. Jest to lista wykonanych wyszukiwań, które są pobrane z obiektu DAO. Po kliknięciu w konkretny element na liście, następuje przejscie do widoku szczegółowego wykresu (ten sam widok do którego prowadzi wyszukiwarka).

Url zewnętrznego serwera można ustawić w preferencjach (menu -> preferences). Implementacja obiektu DAO o którym wcześniej wspominałem, dostarcza zapis/odczyt obiektów do pamięci (klasa pk.ip.weather.android.dao.InMemoryDao), docelowo chciałem zapisywać te dane w bazie danych obsługiwanej przez system android (Sqlite), lecz niestety brakło czasu - ale postaram się to zaimplementować.

Klasa pk.ip.weather.android.service.WeatherServiceImpl to klasa z której inne klasy (androidowe Activity) pobierają listę miast, typów danych oraz typów grupowań. Klasa ta próbuje zwrócić w pierwszej kolejnoście dane z DAO (docelowo z bazy danych, obecnie z pamięci), ale jeśli ich w DAO nie ma, to pobiera dane z zewnętrznego serwera oraz zapisuje je do DAO, aby przy następnym żądaniu już nie wykonywać żądań zdalnych (aczkolwiek tak jak mówiłem, obecnie te wartosci są wpisane na sztywno). Klasa ta jest czymś w rodzaju Proxy, aczkolwiek Dao, WeatherService oraz ApiService mają inne interfejsy, więc cięzko tutaj mówić o wzorcu Proxy.

Instalacja
==========

Aby uruchomić aplikację w emulatorze androida należy pobrać SDK androida, eclipse helios (nie inne, musi być helios) oraz plugin ADT Plugin (opis na: http://developer.android.com/sdk/eclipse-adt.html). Należy utworzyć wirtualne urządzenie android (AVD) klikając w eclipse window -> AVD Manager, a następnie należy przejść krótki (chyba jednoekranowy) wizard. Pozostaje uruchomić aplikację: Run -> Run as -> Android Application.
