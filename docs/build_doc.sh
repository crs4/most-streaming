#!/bin/bash
echo  Building Most Streaming Documentation ...
#echo  Building Python Documentation ...
#PYTHONPATH=../python/src/ make html
echo  Building Android Documentation 
javasphinx-apidoc -fo android_docs/api/ ../android/src/AndroidStreamingLib/src/org/crs4/most/streaming/
