'''

@author: xyj
'''

from gosigma import intro
import sample.core
import time
import inspect
import sys

def use_sys() :
    print ("sys.modules : ", str(sys.modules).replace(",", ",\n"))
    print ("sys.path : ", str(sys.path).replace(",", ",\n"))

def use_package() :
    #gosigma.intro();
    intro.inc_intro()
    sample.core.hmm();
    print("type of sample : ", type(sample))
    print(dir(sample))
    print("type of intro : ", type(intro))
    print(dir(intro))
#     sample.hmm()
    
def use_mod():
    print("module type : ", type(time))
    print("module __dict__ : ", str(type(time).__dict__).replace(",", ",\n"))
    print("time dir : ", str(dir(time)).replace(",", ",\n"))
    all_functions = inspect.getmembers(time, inspect.isfunction)
    print("functions of time : ", str(all_functions).replace(",", ",\n"))

if __name__ == '__main__':
#     use_sys();
    use_package()
#     use_mod()
