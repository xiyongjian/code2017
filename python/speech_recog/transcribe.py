#!/usr/bin/env python3

# from https://pythonprogramminglanguage.com/speech-recognition/

import speech_recognition as sr
import sys
from os import path

    
def do_gsr(AUDIO_FILE):
    print ('recog audio file : ', AUDIO_FILE)

    # use audio file as input
    r = sr.Recognizer()
    with sr.AudioFile(AUDIO_FILE) as source:
        audio = r.record(source)  # read the entire audio file

    # recognize speech using Google Speech Recognition
    try:
        # result = r.recognize_google(audio, key="8b15aac9b5ad5463a741a70a049d4ed7cc211dc8"))
        # print("Google Speech Recognition thinks you said " + r.recognize_google(audio))
        # print("Google Speech Recognition thinks you said " + r.recognize_google(audio, key="8b15aac9b5ad5463a741a70a049d4ed7cc211dc8xx"))
        result = r.recognize_google(audio)
        print("Google Speech Recognition thinks you said " + str(result));
    except sr.UnknownValueError:
        print("Google Speech Recognition could not understand audio")
    except sr.RequestError as e:
        print("Could not request results from Google Speech Recognition service; {0}".format(e))


if __name__ == '__main__':
    if len(sys.argv) <= 1 :
        # load audio file from same directory
        file = path.join(path.dirname(path.realpath(__file__)), "english.wav")
        # AUDIO_FILE = path.join(path.dirname(path.realpath(__file__)), "xyj01x.wav")
    else :
        file = sys.argv[1]
    # file = "e:\\tmp\\IR_1004555945F0171201.wav.flac";
    # file = "e:\\tmp\\IR_1004555945F0171201.mp3"
    # file = "e:\\tmp\\IR_1004555945F0171201.mp3.flac"
    # file = "english.wav"
    file = "e:\\tmp\\IR_1004555945F0171201.mp3.wav"
    do_gsr(file)
