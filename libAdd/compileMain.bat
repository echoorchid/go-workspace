set TMP_PATH=%PATH%
set PATH=D:\Program Files (x86)\Microsoft Visual Studio 10.0\VC\bin\amd64\;%PATH%

call vcvars64.bat

nmake -f Makefile

set PATH=%TMP_PATH%
