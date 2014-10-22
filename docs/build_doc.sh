#!/bin/bash
#echo  Building Python Documentation ...
echo  Building Android Documentation 
javasphinx-apidoc -fo source/android_docs/api/ ../android/src/AndroidStreamingLib/src/org/crs4/most/streaming/

#PYTHONPATH=../python/src/ make html
echo  Building Most Streaming Documentation ...
make html