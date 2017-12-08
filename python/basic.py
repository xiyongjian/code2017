'''

@author: xyj
'''

import time;

def printinfo( arg1, *vartuple ):
   "This prints a variable passed arguments"
   print ("Output is: ")
   print (arg1)
   for var in vartuple:
      print (var)
   return

def use_function():
    # Function definition is here
    printinfo("hell", "world", "gosigma", "john", "jessie", "abs")
    
def use_time() :
    ticks = time.time()
    print ("Number of ticks since 12:00am, January 1, 1970:", ticks)
    print (time.localtime());
    localtime = time.asctime( time.localtime(time.time()) )
    print ("Local current time :", localtime)

def use_tuple() :
    print("\n-- using tuple --")
    tuple = ('abcd', 786 , 2.23, 'john', 70.2)
    list = [ 'abcd', 786 , 2.23, 'john', 70.2  ]
    # tuple[2] = 1000  # Invalid syntax with tuple
    list[2] = 1000  # Valid syntax with list
    print(str(tuple))
    print(str(list))
    
    tuple = ('abcd', 786 , 2.23, 'john', 70.2)
    tinytuple = (123, 'john')

    print (tuple)  # Prints complete tuple
    print (tuple[0])  # Prints first element of the tuple
    print (tuple[1:3])  # Prints elements starting from 2nd till 3rd 
    print (tuple[2:])  # Prints elements starting from 3rd element
    print (tinytuple * 2)  # Prints tuple two times
    print (tuple + tinytuple)  # Prints concatenated tuple
    
def use_map() :
    print ("\n-- using map -- ")
    dict = {}
    dict['one'] = "This is one"
    dict[2] = "This is two"

    tinydict = {'name': 'john', 'code':6734, 'dept': 'sales'}

    print (dict['one'])  # Prints value for 'one' key
    print (dict[2])  # Prints value for 2 key
    print (tinydict)  # Prints complete dictionary
    print (tinydict.keys())  # Prints all the keys
    print (tinydict.values())  # Prints all the values
    
    
def use_lambda():
        # Function definition is here
    sum = lambda arg1, arg2: arg1 + arg2


    # Now you can call sum as a function
    print ("Value of total : ", sum( 10, 20 ))
    print ("Value of total : ", sum( 20, 20 ))
    
Money = 2000
def AddMoney():
   # Uncomment the following line to fix the code:
   global Money
   Money = Money + 1

def use_global():
    print ("money before : ",  Money)
    AddMoney()
    print ("money after : ", Money)
    
def use_dir():
    print("\n-- use_dir() --")
    content = dir(time)
    print(content)

def use_file():
    print("\n-- use_file() --")

    print("open file");
    fo = open("foo.txt", "wb")
    print ("Name of the file: ", fo.name)
    print ("Closed or not : ", fo.closed)
    print ("Opening mode : ", fo.mode)
    fo.close()
    
    print("write to file");
    fo = open("foo.txt", "w")
    fo.write( "Python is a great language.\nYeah its great!!\n")
    fo.close()
    
    print("read from file");
    fo = open("foo.txt", "r+")
    str = fo.read(10)
    print ("Read String is : ", str)
    fo.close()
    
if __name__ == '__main__':
    use_tuple()
    use_map()
    use_time()
    use_function()
    use_lambda()
    use_global()
    use_dir()
    use_file();
