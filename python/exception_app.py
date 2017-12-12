'''

@author: xyj
'''

def kaboom(x, y):
    print(x + y) # Trigger TypeError

if __name__ == '__main__':
    try:
        kaboom([0, 1, 2], 'spam')
    except TypeError as e: # Catch and recover here
        print('Hello world!')
    print('resuming here') # Continue here if exception or not
