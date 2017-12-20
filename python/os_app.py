'''

@author: xyj
'''

import os
import uuid
import tempfile

def testTemp() :
    tmp_dir = tempfile.gettempdir() # prints the current temporary directory
    print("temp folder : ", tmp_dir);
    unique_filename = tmp_dir + os.sep + str(uuid.uuid4()) + ".wav";
    print("temp file : " + unique_filename);

def testRemoveFile() :
    myfile="/tmp/foo.txt"
    print("remove file : ", os.path.abspath(myfile));

    ## if file exists, delete it ##
    if os.path.isfile(myfile):
        os.remove(myfile)
    else:    ## Show an error ##
        print("Error: %s file not found" % myfile)
    

def testRemoveFile2(myfile) :
    print("remove file : ", os.path.abspath(myfile));
    ## try to delete file ##
    try:
        os.remove(myfile)
    except OSError as e:  ## if failed, report it back to the user ##
        print ("Error: %s - %s." % (e.filename,e.strerror))

if __name__ == '__main__':
    testRemoveFile()

    ## get input ##
    #myfile= raw_input("Enter file name to delete : ")
    testRemoveFile2("randomfile");
    testTemp()
