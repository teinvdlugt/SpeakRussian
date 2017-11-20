 - Open de app
 - Druk op start
 - Op het scherm verschijnt \'e\'en knop
 - Doe evt. oortjes in: je kunt dan ook het scherm uitdoen en het knopje van je oortjes gebruiken

Fields: n = het aantal keren dat je een woord niet moeilijk vindt voordat hij van de moeilijke
            lijst af gaat. (n = 3 of zo)
        words = <List<Word>> lijst van woorden om te behandelen in deze sessie.
            Word class: fields: RU <str>, EN <str>, prob <int>

Cycle RU-EN:
 - Hij zegt een Russisch woord, Google TTS
 - Hij zegt het Russische woord, WIKT
 - Na een tijdje zegt hij het engelse woord, Google TTS
 Druk op de knop:
   - Als je het woord niet wist of moeilijk vond, druk eenmaal op de knop
     - Voeg het woord toe aan een lijst van moeilijke woorden, en geef hem 'moelijkheid' n
     - Behandel de woorden in de moeilijke lijst vaker, adhv hun 'moeilijkheid'
   - Als je het woord nog een keer wil horen, druk tweemaal op de knop
     - Laat het woord nog een keer horen, en de engele vertaling, en voeg het woord
       toe aan de lijst van moeilijke woorden
   - Als je het woord wel wist, druk niet op de knop
     - Als het woord op de moeilijk lijst stond, verlaag zijn 'moeilijkheid' met 1.
     - Als zijn 'moeilijkheid' nu 0 is, verwijder hem van de moeilijk lijst.

METHODE EEN:
    Elke keer als je het woord moeilijk vindt, voeg toe aan lijst met moeilijke woorden en geef het
    woord 'moeilijkheid' n (= bijv. 3). Deze woorden worden vaker behandeld.

- Je kan aanzetten dat hij op het scherm ook de russische woorden laat zien, en daarna de vertaling