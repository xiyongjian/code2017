'''

@author: xyj
'''
def build_func(arg) :
    return 

def simple(*args):
    for arg in args:
        print( arg )

def logit(func):
    def wrapper(*args, **kwargs):
        print( 'function %s called with args %s' % (func, args) )
        func(*args, **kwargs)
    return wrapper

if __name__ == '__main__':
    logged_simple = logit(simple)
    logged_simple('a', 'b', 'c')
    
    def logit2(func):
        def wrapper(*args, **kwargs):
            print( 'function %s called with args %s' % (func, args) )
            func(*args, **kwargs)
        return wrapper

    logged_simple = logit2(simple)
    logged_simple('a', 'b', 'c')

    pass