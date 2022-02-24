#!/usr/bin/env bash
javac Hw02.java
if [ $? -ne 0 ]; then 
    echo "Compile of Hw02.java failed"
    echo "Good bye!"
    exit 1 
fi
echo "Compile of Hw02.java succeded."

echo "->Now processing h2r-in10.txt"
java Hw02 h2r-in10.txt >h2r-10St.txt 2>/dev/null
diff -w h2r-10St.txt h2r-10Base.txt
echo "->Now processing h2r-in20.txt"
java Hw02 h2r-in20.txt >h2r-20St.txt 2>/dev/null
diff -w h2r-20St.txt h2r-20Base.txt
echo "->Now processing h2r-in100.txt"
java Hw02 h2r-in100.txt >h2r-100St.txt 2>/dev/null
diff -w h2r-100St.txt h2r-100Base.txt
echo "->Now processing h2r-in1k.txt"
java Hw02 h2r-in1k.txt >h2r-1kSt.txt 2>/dev/null
diff -w h2r-1kSt.txt h2r-1kBase.txt
echo "->Now processing h2r-in10ka.txt"
java Hw02 h2r-in10ka.txt >h2r-10kaSt.txt 2>/dev/null
diff -w h2r-10kaSt.txt h2r-10kaBase.txt
echo "->Now processing h2r-in10kb.txt"
java Hw02 h2r-in10kb.txt >h2r-10kbSt.txt 2>/dev/null
diff -w h2r-10kbSt.txt h2r-10kbBase.txt
echo "Processing Hw02 input cases complete"
