'''

@author: xyj
'''
import json
from datetime import datetime

print(json.dumps(['foo', {'bar': ('baz', None, 1.0, 2)}]))

# cdr = {
#    'DOMAIN' : 'dev' 
#     }
cdr = {}
cdr['DOMAIN'] = 'bluemsp'
cdr['FROM_USER_ID'] = 1234
cdr['DURATION'] = 300
cdr['TO_USERS'] = [{"TO_USER_ID" : 1001}, {"TO_USER_ID" : 1002}]

payld = json.dumps(cdr, indent=4)
print("cdr json payld : " + payld)

start_time = '2017-12-08-13-27-39'
ds = datetime.strptime(start_time, '%Y-%m-%d-%H-%M-%S')
de =  datetime.now();

print("date string : ", start_time)
print("parsed date : ", str(ds))
print("current : ", str(de))
print("diff : ", de - ds)
print("diff (seconds) : ", int((de - ds).total_seconds()))


