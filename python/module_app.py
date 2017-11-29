'''

@author: xyj
'''

from gosigma import *
import sample
import time
import inspect

if __name__ == '__main__':
    #gosigma.intro();
    intro.inc_intro()
    sample.core.hmm();
#     sample.hmm()

    print("module type : ", type(time))
    print("module __dict__ : ", str(type(time).__dict__).replace(",", ",\n"))
    print("time dir : ", str(dir(time)).replace(",", ",\n"))
    all_functions = inspect.getmembers(time, inspect.isfunction)
    print("functions of time : ", str(all_functions).replace(",", ",\n"))

