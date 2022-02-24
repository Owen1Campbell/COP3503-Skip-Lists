#!/usr/bin/env bash
javac Hw02.java
if [ $? -ne 0 ]; then 
    echo "Compile of Hw02.java failed"
    echo "Good bye!"
    exit 1 
fi
echo "Compile of Hw02.java succeded."
echo "Begin base test then 2 random seed tests"

echo "->Now processing h2r-in10.txt"
java Hw02 h2r-in10.txt >h2r-10St.txt 2>/dev/null
diff -w h2r-10St.txt h2r-10Base.txt
echo "->Now processing h2r-in10.txt - with r - mismatch expected"
java Hw02 h2r-in10.txt r >h2r-10St.txt 2>/dev/null
diff -w h2r-10St.txt h2r-10Base.txt
echo "->Now processing h2r-in10.txt - with R - mismatch expected"
java Hw02 h2r-in10.txt R >h2r-10St.txt 2>/dev/null
diff -w h2r-10St.txt h2r-10Base.txt
echo "Hw02 simple testing w. R/r seed option test complete"
