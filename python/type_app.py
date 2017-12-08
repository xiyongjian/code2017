'''

@author: xyj
'''

def print_type(s, a):
    print("\n-- print_type() -- : ", s, ", ", str(a))
    print("type : ", type(a));
    print("__dict__ : ", str(type(a).__dict__).replace(",", ",\n"))
    
if __name__ == '__main__':
    print("type demo")
    
    print_type("string/hello, world", "hello, world");
    print_type("tuple/(1,2,'hello', 7.3)", (1,2,'hello', 7.3))
    print_type("list/[1,2,'hello', 7.3]", [1,2,'hello', 7.3])
    print_type("set/{1,2,'hello', 7.3}", {1,2,'hello', 7.3})
    print_type("dict/{1:2,'hello': 7.3}", {1:2,'hello': 7.3})
    