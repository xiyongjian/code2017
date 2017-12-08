#!/usr/bin/env python3

import speech_recognition as sr

# get audio from the microphone
r = sr.Recognizer()

with sr.Microphone() as source:
    print("Speak:")
    audio = r.listen(source, 10, 10)

try:
    # setup google API credential
    #r.recognize_google(audio, key="GOOGLE_SPEECH_RECOGNITION_API_KEY")

    print("You said " + r.recognize_google(audio))
except sr.UnknownValueError:
    print("Could not understand audio")
except sr.RequestError as e:
    print("Could not request results; {0}".format(e))
