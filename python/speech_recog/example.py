# refer : https://pypi.python.org/pypi/SpeechRecognition/

# for utf-8 multiple language

import sys;
import io;

sys.stdout = io.TextIOWrapper(sys.stdout.buffer,encoding='utf8')

import speech_recognition as sr
for index, name in enumerate(sr.Microphone.list_microphone_names()):
    print("Microphone with name \"{1}\" found for 'Microphone(device_index={0})'".format(index, name))
    
