'''

@author: xyj
'''

if __name__ == '__main__':
    msg = "hello, world, quit!"
    if "quit" in msg :
        print("msg contain quit, so quit")
        
    json = {"hello": "world"}
    print("json string : ", str(json))
    print(dir(json))
    
    print("-- start --")
    msg = ""
    while "quit" not in msg :
        msg = input("give me some message")
        print("I got messasge : " + msg)
    print("-- done --")
    pass