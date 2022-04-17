# IAS-interpreter
1-You cannot use address modifiers <br>
2-You should use ( line number - 1 ) instead of address hex while using JUMP and JUMP+ (without L, R. Check the example program)<br>
3-Write JUMP+ M(X) while using conditional jump instead of JUMP + M(X)(don't put space between JUMP and plus sign) <br>
4-Multiplying with negative numbers is problematic. <br>
5-Program.txt should be in the same dir as IAS.java <br>
6-Do not change the name of the Program.txt <br>
<br>
Other than these all instructions are given here:
![instruction_list](https://github.com/alperkaya0/IAS-interpreter/blob/main/unknown.png) <br>
<br>
Usable(like variable) address fields are: <br>
0F9, 0FA, 0FB, 0FC.  (You can add more by changing the code(just add more elements to the usableMemory hashmap))
